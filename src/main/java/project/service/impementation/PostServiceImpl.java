package project.service.impementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.CommentDto;
import project.dto.UserDto;
import project.dto.post.*;
import project.dto.statistic.PostStatisticView;
import project.dto.statistic.StatisticDto;
import project.dto.vote.VoteCounterView;
import project.exception.*;
import project.model.Post;
import project.model.User;
import project.model.emun.*;
import project.repository.*;
import project.service.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static project.model.emun.ModerationStatus.*;
import static project.model.emun.PostDtoStatus.ANNOUNCE;

@Service
public class PostServiceImpl implements PostService {

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

    public PostServiceImpl(PostRepository postRepository,
                           PostCommentRepository postCommentRepository,
                           TagToPostRepository tagToPostRepository,
                           VoteRepository voteRepository,
                           UserService userService,
                           TagService tagService) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.tagToPostRepository = tagToPostRepository;
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public ResponseEntity<PostResponse> addPost(PostAddRequest request)
            throws UserNotFoundException, UnauthorizedException, ObjectNotFoundException {
        User user = userService.checkUser();
        PostResponse response = new PostResponse();

        String title = request.getTitle();
        String text = request.getText();

        if (title.length() > minTitleLength && text.length() > minTextLength) {
            Date date = new Date(request.getTimestamp() * 1000);
            date = (date.before(new Date())) ? new Date() : date;
            Post post = new Post();
            post.setActive(request.isActive());
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            post.setTime(date);
            post.setUser(user);
            post.setModerationStatus(NEW);
            postRepository.save(post);
            tagService.addTagsToPost(request.getTagArray(), post);
        } else {
            PostErrorMap errors = new PostErrorMap();
            if (!(title.length() > minTitleLength)) errors.addTitleError();
            if (!(text.length() > minTextLength)) errors.addTextError();
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public Post getPost(long postId) throws ObjectNotFoundException {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пост id:%d не найден", postId)));
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public ResponseEntity<PostDto> getPostResponse(long postId) throws ObjectNotFoundException {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пост id:%d не найден", postId)));
        return ResponseEntity.ok(createPostDto(post));
    }

    @Override
    public ResponseEntity<PostListDto> getAnnounceList(int offset, int limit, String mode) {
        PostViewMode postMode = PostViewMode.valueOf(mode.toUpperCase());
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

    @Override
    public ResponseEntity<PostListDto> getAnnounceListToModeration(int offset, int limit, String status)
            throws UserNotFoundException, UnauthorizedException {
        User user = userService.checkUser();
        ModerationStatus moderationStatus = valueOf(status.toUpperCase());
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

    @Override
    public ResponseEntity<PostListDto> getAnnounceListByAuthUser(int offset, int limit, String status)
            throws UserNotFoundException, UnauthorizedException {
        long userId = userService.checkUser().getId();
        PostState postState = PostState.valueOf(status.toUpperCase());
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = getUserPostPage(userId, postState, pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    @Override
    public ResponseEntity<PostListDto> getAnnounceListByTag(int offset, int limit, String tag) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = postRepository.findPostByTagWithBaseCondition(tag, pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    @Override
    public ResponseEntity<PostListDto> getAnnounceListByDate(int offset, int limit, String date) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = postRepository.findPostByDateWithBaseCondition(date, pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    @Override
    public ResponseEntity<PostListDto> getAnnounceListBySearch(int offset, int limit, String search) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);
        Page<Post> page = postRepository.findPostBySearchWithBaseCondition("%" + search + "%", pageable);
        List<PostDto> list = createPostListFromPage(page);
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    @Override
    public PostStatisticView getAllPostStatistic() {
        return postRepository.getAllStatistic();
    }

    @Override
    public PostStatisticView getUserPostStatistic(long userId) {
        return postRepository.getUserStatistic(userId);
    }

    @Override
    public VoteCounterView getAllVote() {
        return voteRepository.getBlogResult();
    }

    @Override
    public VoteCounterView getUserVote(long userId) {
        return voteRepository.getUserResult(userId);
    }

    @Override
    public StatisticDto getAllStatistic() {
        PostStatisticView postData = getAllPostStatistic();
        VoteCounterView voteData = getAllVote();
        return createStatisticDto(postData, voteData);
    }

    @Override
    public StatisticDto getUserStatistic(long userId) {
        PostStatisticView postData = getUserPostStatistic(userId);
        VoteCounterView voteData = getUserVote(userId);
        return createStatisticDto(postData, voteData);
    }


    private PostDto createAnnounce(Post post) {
        return createPostDto(post, ANNOUNCE);
    }

    private PostDto createPostDto(Post post) {
        return createPostDto(post, PostDtoStatus.POST);
    }

    private PostDto createPostDto(Post post, PostDtoStatus status) {
        PostDto postDto = new PostDto();
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
                postData.getPostCounter(),
                voteData.getLikeCounter(),
                voteData.getDislikeCounter(),
                postData.getViewCounter(),
                AppConstant.dateToTimestamp(postData.getFirstPublication())
        );
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
