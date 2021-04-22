package project.service.impementation;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.config.AppConstant;
import project.dto.UserDto;
import project.dto.VoteCounterDto;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;
import project.model.Post;
import project.model.emun.*;
import project.repository.PostRepository;
import project.repository.VoteRepository;
import project.service.PostService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static project.model.emun.PostDtoStatus.ANNOUNCE;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;

    public PostServiceImpl(PostRepository postRepository,
                           VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
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
//        return null;
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
        postDto.setTimestamp(AppConstant.dateToTimestamp(post.getTime()));
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
            //postDto.setTagList(Dto.getTagsList(id));
            //commentList = Dto.getCommentsList(id);
            //commentCounter = commentList.size();
        }
        postDto.setViewCounter(post.getViewCount());
        postDto.setLikeCounter(voteCounterDto.getLikeCounter());
        postDto.setDislikeCounter(voteCounterDto.getDislikeCounter());

        return postDto;
    }

}
