package project.service.impementation;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.CommentDto;
import project.dto.UserDto;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;
import project.exception.DocumentNotFoundException;
import project.model.Post;
import project.model.emun.PostDtoStatus;
import project.model.emun.PostViewMode;
import project.repository.*;
import project.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

import static project.model.emun.PostDtoStatus.ANNOUNCE;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final TagToPostRepository tagToPostRepository;

    public PostServiceImpl(PostRepository postRepository,
                           PostCommentRepository postCommentRepository,
                           TagToPostRepository tagToPostRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.tagToPostRepository = tagToPostRepository;
    }

    @Override
    public ResponseEntity<PostDto> getPost(long postId) {
        Post post = postRepository.findPostWithBaseCondition(postId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("Пост id:%d не найден", postId)));
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

}
