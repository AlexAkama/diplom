package project.service.implementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.main.*;
import project.dto.vote.VoteCounterView;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.model.*;
import project.model.enums.Vote;
import project.repository.VoteRepository;
import project.service.*;

import java.util.Date;
import java.util.Optional;

import static project.model.enums.Vote.DISLIKE;
import static project.model.enums.Vote.LIKE;

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
    public ResponseEntity<AppResponse> setLike(long postId)
            throws NotFoundException, UnauthorizedException {
        return setVote(LIKE, postId);
    }

    @Override
    public ResponseEntity<AppResponse> setDislike(long postId)
            throws NotFoundException, UnauthorizedException {
        return setVote(DISLIKE, postId);
    }

    private ResponseEntity<AppResponse> setVote(Vote vote, long postId)
            throws NotFoundException, UnauthorizedException {
        var user = userService.checkUser();
        Optional<PostVote> optionalPostVote = voteRepository.findByPostIdAndUserId(postId, user.getId());
        if (optionalPostVote.isEmpty()) {
            var postVote = new PostVote();
            var post = postService.getPost(postId);
            postVote.setPost(post);
            postVote.setUser(user);
            postVote.setTime(new Date());
            postVote.setValue(vote.getValue());
            voteRepository.save(postVote);
            updatePost(post);
            return ResponseEntity.ok(new AppResponse().ok());
        } else if (optionalPostVote.get().getValue() != vote.getValue()) {
            var postVote = optionalPostVote.get();
            postVote.setValue(-postVote.getValue());
            voteRepository.save(postVote);
            updatePost(postVote.getPost());
            return ResponseEntity.ok(new AppResponse().ok());
        } else {
            return ResponseEntity.ok(new AppResponse().bad());
        }
    }

    private void updatePost(Post post) {
        VoteCounterView view = voteRepository.getPostResult(post.getId());
        post.setLikeCounter(view.getLikeCounter());
        post.setDislikeCounter(view.getDislikeCounter());
        postService.save(post);
    }

}
