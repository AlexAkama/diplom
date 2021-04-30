package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.main.*;
import project.dto.vote.VoteCounterView;
import project.exception.*;
import project.model.*;
import project.model.emun.Vote;
import project.repository.VoteRepository;
import project.service.*;

import java.util.Date;
import java.util.Optional;

import static project.model.emun.Vote.DISLIKE;
import static project.model.emun.Vote.LIKE;

@Service
public class VoteServiceImpl implements VoteService {

    private final UserService userService;
    private final PostService postService;
    private final VoteRepository voteRepository;

    public VoteServiceImpl(UserService userService,
                           PostService postService,
                           VoteRepository voteRepository) {
        this.userService = userService;
        this.postService = postService;
        this.voteRepository = voteRepository;
    }

    @Override
    public ResponseEntity<? extends AppResponse> setLike(long postId)
            throws UserNotFoundException, UnauthorizedException, ObjectNotFoundException {
        return setVote(LIKE, postId);
    }

    @Override
    public ResponseEntity<? extends AppResponse> setDislike(long postId)
            throws UserNotFoundException, UnauthorizedException, ObjectNotFoundException {
        return setVote(DISLIKE, postId);
    }

    private ResponseEntity<? extends AppResponse> setVote(Vote vote, long postId)
            throws UserNotFoundException, UnauthorizedException, ObjectNotFoundException {
        User user = userService.checkUser();
        Optional<PostVote> optionalPostVote = voteRepository.findByPostIdAndUserId(postId, user.getId());
        if (optionalPostVote.isEmpty()) {
            PostVote postVote = new PostVote();
            Post post = postService.getPost(postId);
            postVote.setPost(post);
            postVote.setUser(user);
            postVote.setTime(new Date());
            postVote.setValue(vote.getValue());
            voteRepository.save(postVote);
            updatePost(post);
            return ResponseEntity.ok(new OkResponse());
        } else if (optionalPostVote.get().getValue() != vote.getValue()) {
            PostVote postVote = optionalPostVote.get();
            postVote.setValue(-postVote.getValue());
            voteRepository.save(postVote);
            updatePost(postVote.getPost());
            return ResponseEntity.ok(new OkResponse());
        } else {
            return ResponseEntity.ok(new BadResponse());
        }
    }

    private void updatePost(Post post) {
        VoteCounterView view = voteRepository.getPostResult(post.getId());
        post.setLikeCounter(view.getLikeCounter());
        post.setDislikeCounter(view.getDislikeCounter());
        postService.save(post);
    }

}
