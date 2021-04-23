package project.service.impementation;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.config.Connection;
import project.dto.CommentDto;
import project.dto.UserDto;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;
import project.model.Post;
import project.model.emun.*;
import project.repository.*;
import project.service.PostService;

import java.util.*;
import java.util.stream.Collectors;

import static project.model.emun.PostDtoStatus.ANNOUNCE;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final PostCommentRepository postCommentRepository;

    public PostServiceImpl(PostRepository postRepository,
                           VoteRepository voteRepository,
                           PostCommentRepository postCommentRepository) {
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
        this.postCommentRepository = postCommentRepository;
    }

//    public PostDto make(Post post) {
//        return makeDto(post, false);
//    }

//    public PostDto makeAnnounce(Post post) {
//        return makeDto(post, true);
//    }

//    public PostListDto makeAnnounces(int offset, int limit, PostViewMode sort) {
//        return getResult(true, "", offset, limit, sort);
//    }

//    public PostListDto makeAnnounces(String conditions, int offset, int limit) {
//        return getResult(true, conditions, offset, limit, PostViewMode.NONE);
//    }

//    public PostListDto makeAnnounces(String conditions, int offset, int limit, PostViewMode sort) {
//        return getResult(true, conditions, offset, limit, sort);
//    }

//    private PostListDto getResult(Boolean useBaseCondition, String conditions, int offset, int limit, PostViewMode mode) {
//
//        //FIXME облагородить HQL
//        String hql = "from Post p where ";
//        String countHql = "select count(*) from Post p where ";
//        if (useBaseCondition) {
//            hql += Dto.baseCondition;
//            countHql += Dto.baseCondition;
//        }
//        if (!conditions.isEmpty()) {
//            hql += " and (" + conditions + ")";
//            countHql += " and (" + conditions + ")";
//        }
//
//        String orderByTime = "";
//        switch (mode) {
//            case EARLY:
//                orderByTime = " ORDER BY p.time";
//                break;
//            case RECENT:
//                orderByTime = " ORDER BY p.time DESC";
//                break;
//        }
//
//        List<Post> postList;
//        try (Session session = Connection.getSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            count = (long) session.createQuery(countHql).uniqueResult();
//
//            hql += orderByTime;
//            postList = session.createQuery(hql)
//                    .setFirstResult(offset)
//                    .setMaxResults(limit)
//                    .getResultList();
//
//            for (Post post : postList) {
//                posts.add(new PostDto().makeAnnounce(post));
//            }
//
//            transaction.commit();
//        }
//
//        switch (mode) {
//            case POPULAR:
//                posts.sort(Comparator.comparing(PostDto::getCommentCounter)
//                        .reversed());
//                break;
//            case BEST:
//                posts.sort(Comparator.comparing(PostDto::getLikeCounter)
//                        .thenComparing((o1, o2) -> Long.compare(o2.getDislikeCounter(), o1.getDislikeCounter()))
//                        .reversed());
//                break;
//        }
//
//        return this;
//    }


    @Override
    public ResponseEntity<PostListDto> getAnnounceList(int offset, int limit, String mode) {
        PostViewMode postMode = PostViewMode.valueOf(mode.toUpperCase());
        int pageNumber = offset / limit;
        Sort sort;
        if (postMode == PostViewMode.EARLY) {
            sort = Sort.by(Sort.Direction.ASC, "comments.size()");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "time");
        }
        Pageable pageable = PageRequest.of(pageNumber, limit, sort);
        Page<Post> page = findAllWithBaseConditional(pageable);
        List<PostDto> list = page.getContent().stream()
                .map(this::createAnnounce)
                .collect(Collectors.toList());
        switch (postMode) {
            case POPULAR:
                list.sort(Comparator.comparing(PostDto::getCommentCounter));
                break;
            case BEST:
                list.sort(Comparator.comparing(PostDto::getLikeCounter)
                        .thenComparing((o1, o2) -> Long.compare(o2.getDislikeCounter(), o1.getDislikeCounter()))
                        .reversed());
                break;

        }
        return ResponseEntity.ok(new PostListDto(page.getTotalElements(), list));
    }

    private Page<Post> findAllWithBaseConditional(Pageable pageable) {
        return postRepository.findAllByActiveAndModerationStatusAndTimeBefore(
                true, ModerationStatus.ACCEPTED, new Date(), pageable);
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
            postDto.setCommentCounter(postDto.getCommentCounter());
        } else {
            postDto.setActive(post.isActive());
            postDto.setText(post.getText());
            postDto.setTagList(getTagsList(post.getId()));
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

    private List<String> getTagsList(long postId) {
        List<String> tags = new ArrayList<>();
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "select t.name from TagToPost tp"
                    + " join Tag t on tp.tag.id = t.id"
                    + " where tp.post.id=:id";
            tags = session.createQuery(hql).setParameter("id", postId).getResultList();

            transaction.commit();
        }
        return tags;
    }

//    private  List<CommentDto> getCommentList(long postId) {
//        List<CommentDto> comments = new ArrayList<>();
//        try (Session session = Connection.getSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            String hql = "from PostComment where post.id=:id";
//            List<PostComment> resultList = session.createQuery(hql).setParameter("id", postId).getResultList();
//
//            for (PostComment comment : resultList) {
//                comments.add(new CommentDto().createFrom(comment));
//            }
//
//            transaction.commit();
//        }
//        return comments;
//    }


}
