package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.UserDto;
import project.dto.comment.CommentDto;
import project.dto.main.AppResponseWithErrors;
import project.dto.post.*;
import project.dto.statistic.PostStatisticView;
import project.dto.statistic.StatisticDto;
import project.dto.vote.VoteCounterView;
import project.exception.*;
import project.model.Post;
import project.model.PostComment;
import project.model.enums.*;
import project.repository.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static project.model.enums.ModerationStatus.*;
import static project.model.enums.PostDtoStatus.ANNOUNCE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final TagToPostRepository tagToPostRepository;
    private final VoteRepository voteRepository;
    private final UserService userService;
    private final TagService tagService;

    @Value("${config.post.minlength.title}")
    private int minTitleLength;
    @Value("${config.post.minlength.text}")
    private int minTextLength;


    public ResponseEntity<AppResponseWithErrors> addPost(PostRequest request)
            throws NotFoundException, UnauthorizedException, ForbiddenException {
        return updatePost(-1, request);
    }

    public ResponseEntity<AppResponseWithErrors> updatePost(long postId, PostRequest request)
            throws UnauthorizedException, NotFoundException, ForbiddenException {
        var user = userService.checkUser();
        var response = new AppResponseWithErrors();
        var errors = checkPostUpdateRequest(request);
        if (errors.isEmpty()) {
            Post post;
            ModerationStatus status;
            if (postId == -1) {
                post = new Post();
                status = NEW;
            } else {
                post = getPost(postId);
                if (post.getUser().getId() == user.getId()) {
                    status = NEW;
                } else if (user.isModerator()) {
                    status = post.getModerationStatus();
                } else {
                    throw new ForbiddenException("Нет прав для изменения поста");
                }
            }
            var date = checkDate(request.getTimestamp());
            var dto = new PostRequestDto(
                    request.isActive(),
                    request.getTitle(),
                    request.getText(),
                    date,
                    user,
                    status,
                    request.getTagArray()
            );
            updatePostAndSave(post, dto);
        } else {
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

    public Post getPost(long postId) throws NotFoundException {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new NotFoundException(String.format("Пост id:%d не найден", postId)));
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public PostComment getComment(long commentId) throws NotFoundException {
        return postCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Комментарий id:%d не найден", commentId)));
    }

    public void save(PostComment comment) {
        postCommentRepository.save(comment);
    }

    public PostComment saveAndFlush(PostComment comment) {
        return postCommentRepository.saveAndFlush(comment);
    }

    public ResponseEntity<PostDto> getPostForResponse(long postId) throws NotFoundException {
        var post = getPost(postId);
        var increment = true;
        try {
            var user = userService.checkUser();
            if (user.isModerator() || user.getId() == post.getUser().getId()) increment = false;
        } catch (UnauthorizedException ignored) {
            // если пользователь не авторизован - ничего не делаем (игнорируем)
        }
        if (increment) post.setViewCounter(post.getViewCounter() + 1);
        postRepository.save(post);
        return ResponseEntity.ok(createPostDto(post));
    }

    /**
     * Получение списка постов
     *
     * @param offset сдвиг для постраничного вывода
     * @param limit  кол-во запрашиваемых постов
     * @param mode   режим вывода (сортировка)
     * @return {@link PostListDto объект с данными постов}
     */
    public ResponseEntity<PostListDto> getAnnounceList(int offset, int limit, String mode) {
        var postMode = PostViewMode.valueOf(mode.toUpperCase());
        int pageNumber = offset / limit;
        Sort sort;
        switch (postMode) {
            case POPULAR:
                sort = Sort.by(Sort.Order.desc("commentCounter"), Sort.Order.desc("time"));
                break;
            case BEST:
                sort = Sort.by(Sort.Order.desc("likeCounter"), Sort.Order.asc("dislikeCounter"), Sort.Order.desc("time"));
                break;
            case EARLY:
                sort = Sort.by(Sort.Direction.ASC, "time");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC, "time");
        }
        Pageable pageable = PageRequest.of(pageNumber, limit, sort);
        Page<Post> page = postRepository.findAllWithBaseCondition(pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    public ResponseEntity<PostListDto> getAnnounceListToModeration(int offset, int limit, String status)
            throws UnauthorizedException {
        var user = userService.checkUser();
        var moderationStatus = valueOf(status.toUpperCase());
        long moderatorId = (moderationStatus == NEW)
                ? 0
                : user.getId();
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page;
        switch (moderationStatus) {
            case ACCEPTED:
                page = getModerationPostPage(ACCEPTED, moderatorId, pageable);
                break;
            case DECLINED:
                page = getModerationPostPage(DECLINED, moderatorId, pageable);
                break;
            default:
                page = getModerationPostPage(pageable);
        }
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    public ResponseEntity<PostListDto> getAnnounceListByAuthUser(int offset, int limit, String status)
            throws UnauthorizedException {
        long userId = userService.checkUser().getId();
        var postState = PostState.valueOf(status.toUpperCase());
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = getUserPostPage(userId, postState, pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    public ResponseEntity<PostListDto> getAnnounceListByTag(int offset, int limit, String tag)
            throws NotFoundException {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = postRepository.findPostByTagWithBaseCondition(tag, pageable);
        List<PostDto> list = createPostListFromPage(page);
        if (list.isEmpty()) {
            tagService.hideTag(tag);
            throw new NotFoundException(String.format("Нет постов по тегу %s", tag));
        }
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    public ResponseEntity<PostListDto> getAnnounceListByDate(int offset, int limit, String date) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = postRepository.findPostByDateWithBaseCondition(date, pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    public ResponseEntity<PostListDto> getAnnounceListBySearch(int offset, int limit, String search) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = postRepository.findPostBySearchWithBaseCondition("%" + search + "%", pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    public PostStatisticView getAllPostStatistic() {
        return postRepository.getAllStatistic();
    }

    public PostStatisticView getUserPostStatistic(long userId) {
        return postRepository.getUserStatistic(userId);
    }

    public VoteCounterView getAllVote() {
        return voteRepository.getBlogResult();
    }

    public VoteCounterView getUserVote(long userId) {
        return voteRepository.getUserResult(userId);
    }

    public StatisticDto getAllStatistic() {
        PostStatisticView postData = getAllPostStatistic();
        VoteCounterView voteData = getAllVote();
        return createStatisticDto(postData, voteData);
    }

    public StatisticDto getUserStatistic(long userId) {
        PostStatisticView postData = getUserPostStatistic(userId);
        VoteCounterView voteData = getUserVote(userId);
        return createStatisticDto(postData, voteData);
    }

    public void saveAndActivateTags(Post post) throws NotFoundException {
        save(post);
        tagService.activateTags(post);
    }


    private Date checkDate(long timestamp) {
        var date = new Date(timestamp * 1000);
        return (date.before(new Date())) ? new Date() : date;
    }

    private PostErrorMap checkPostUpdateRequest(PostRequest request) {
        String title = request.getTitle();
        String text = request.getText();
        var errors = new PostErrorMap();
        if (title.length() <= minTitleLength) errors.addTitleError();
        if (text.length() <= minTextLength) errors.addTextError();
        return errors;
    }

    private void updatePostAndSave(Post post, PostRequestDto dto) throws NotFoundException {
        post.setActive(dto.isActive());
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setTime(dto.getTime());
        post.setUser(dto.getUser());
        post.setModerationStatus(dto.getStatus());
        postRepository.save(post);
        tagService.addTagsToPost(dto.getTagArray(), post);
    }

    private PostDto createAnnounce(Post post) {
        return createPostDto(post, ANNOUNCE);
    }

    private PostDto createPostDto(Post post) {
        return createPostDto(post, PostDtoStatus.POST);
    }

    private PostDto createPostDto(Post post, PostDtoStatus status) {
        var postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTimestamp(AppConstant.dateToTimestamp(post.getTime()));
        postDto.setUser(new UserDto(
                post.getUser().getId(),
                post.getUser().getName()));
        postDto.setTitle(post.getTitle());
        if (status == ANNOUNCE) {
            postDto.setAnnounce(post.getText()
                    .substring(0, Math.min(post.getText().length(), 100))
                    .replaceAll("<[^>]*>", "") + "...");
            postDto.setCommentCounter(post.getCommentCounter());
        } else {
            postDto.setActive(post.isActive());
            postDto.setText(post.getText());
            postDto.setTagList(tagToPostRepository.getTagList(post.getId()));
            List<CommentDto> comments = postCommentRepository.findAllByPost(post)
                    .stream()
                    .map(CommentDto::new)
                    .collect(Collectors.toList());
            postDto.setCommentList(comments);
            postDto.setCommentCounter(comments.size());
        }
        postDto.setViewCounter(post.getViewCounter());
        postDto.setLikeCounter(post.getLikeCounter());
        postDto.setDislikeCounter(post.getDislikeCounter());

        return postDto;
    }

    private List<PostDto> createPostListFromPage(Page<Post> page) {
        return page.getContent().stream()
                .map(this::createAnnounce)
                .collect(Collectors.toList());
    }

    private StatisticDto createStatisticDto(PostStatisticView postData, VoteCounterView voteData) {
        return new StatisticDto(
                notNull(postData.getPostCounter()),
                notNull(voteData.getLikeCounter()),
                notNull(voteData.getDislikeCounter()),
                notNull(postData.getViewCounter()),
                AppConstant.dateToTimestamp(postData.getFirstPublication())
        );
    }

    private long notNull(Long aLong) {
        return (aLong == null) ? 0 : aLong;
    }

    private Page<Post> getModerationPostPage(ModerationStatus status, long moderatorId, Pageable pageable) {
        return (moderatorId == 0)
                ? postRepository.findAllByActiveAndModerationStatus(true, status, pageable)
                : postRepository.findAllByActiveAndModerationStatusAndModeratorId(
                true, status, moderatorId, pageable);
    }

    private Page<Post> getModerationPostPage(Pageable pageable) {
        return getModerationPostPage(NEW, 0, pageable);
    }

    private Page<Post> getUserPostPage(long userId, PostState postState, Pageable pageable) {
        boolean active = postState != PostState.INACTIVE;
        ModerationStatus status;
        switch (postState) {
            case INACTIVE:
            case PENDING:
                status = NEW;
                break;
            case DECLINED:
                status = DECLINED;
                break;
            case PUBLISHED:
                status = ACCEPTED;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + postState);
        }
        return postRepository.findAllByActiveAndModerationStatusAndUserId(active, status, userId, pageable);
    }

}
