package project.service.impementation;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.Connection;
import project.dto.*;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;
import project.model.Post;
import project.model.PostComment;
import project.model.emun.*;
import project.repository.PostRepository;
import project.repository.VoteRepository;
import project.service.AppService;
import project.service.PostService;

import java.util.*;
import java.util.stream.Collectors;

import static project.model.emun.PostDtoStatus.ANNOUNCE;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AppService appService;

    public PostServiceImpl(PostRepository postRepository,
                           VoteRepository voteRepository,
                           AppService appService) {
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
        this.appService = appService;
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
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "time");
        Page<Post> postPage = findAllWithBaseConditional(pageable);
        List<Post> postList = postPage.getContent();
        List<PostDto> postDtoList = postList.stream()
                .map(this::createAnnounce)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PostListDto(postPage.getTotalElements(), postDtoList));
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
        VoteCounterDto voteCounterDto = voteRepository.getPostResult(post.getId());

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTimestamp(appService.dateToTimestamp(post.getTime()));
        postDto.setUser(new UserDto(
                post.getUser().getId(),
                post.getUser().getName()));
        postDto.setTitle(post.getTitle());
        if (status == ANNOUNCE) {
            postDto.setAnnounce(post.getText()
                    .substring(0, Math.min(post.getText().length(), 100))
                    .replaceAll("<[^>]*>", "") + "...");
        } else {
            postDto.setActive(post.isActive());
            postDto.setText(post.getText());
            postDto.setTagList(getTagsList(post.getId()));
            postDto.setCommentList(getCommentsList(post.getId()));
            postDto.setCommentCounter(postDto.getCommentList().size());
        }
        postDto.setViewCounter(post.getViewCount());
        postDto.setLikeCounter(voteCounterDto.getLikeCounter());
        postDto.setDislikeCounter(voteCounterDto.getDislikeCounter());

        return postDto;
    }

    private static List<String> getTagsList(long postId) {
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

    private static List<CommentDto> getCommentsList(long postId) {
        List<CommentDto> comments = new ArrayList<>();
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String hql = "from PostComment where post.id=:id";
            List<PostComment> resultList = session.createQuery(hql).setParameter("id", postId).getResultList();

            for (PostComment comment : resultList) {
                comments.add(new CommentDto().createFrom(comment));
            }

            transaction.commit();
        }
        return comments;
    }



}
