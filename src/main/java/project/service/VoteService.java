package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.main.AppResponse;
import project.dto.vote.VoteCounterView;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.model.Post;
import project.model.PostVote;
import project.model.enums.Vote;
import project.repository.VoteRepository;

import java.util.Date;
import java.util.Optional;

import static project.model.enums.Vote.DISLIKE;
import static project.model.enums.Vote.LIKE;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final UserService userService;
    private final PostService postService;
    private final VoteRepository voteRepository;


    public ResponseEntity<AppResponse> setLike(long postId)
            throws NotFoundException, UnauthorizedException {
        return setVote(LIKE, postId);
    }

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
