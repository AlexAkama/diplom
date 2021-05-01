package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.comment.CommentRequest;
import project.dto.comment.CommentResponse;
import project.dto.main.OkResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.model.*;
import project.model.emun.ModerationDecision;
import project.model.emun.ModerationStatus;
import project.service.*;

import static project.model.emun.ModerationDecision.ACCEPT;
import static project.model.emun.ModerationDecision.valueOf;
import static project.model.emun.ModerationStatus.ACCEPTED;
import static project.model.emun.ModerationStatus.DECLINED;

@Service
public class SubPostServiceImpl implements SubPostService {

    private final UserService userService;
    private final PostService postService;

    public SubPostServiceImpl(UserService userService,
                              PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public ResponseEntity<OkResponse> setModerationDecision(ModerationRequest request)
            throws NotFoundException, UnauthorizedException {
        User user = userService.checkUser();
        if (user.isModerator()) {
            ModerationDecision decision = valueOf(request.getDecision().toUpperCase());
            Post post = postService.getPost(request.getPostId());
            ModerationStatus status = (decision == ACCEPT) ? ACCEPTED : DECLINED;
            post.setModerationStatus(status);
            post.setModerator(user);
            postService.save(post);
            return ResponseEntity.ok(new OkResponse());
        } else // TODO Передалать на 403
            throw new UnauthorizedException("Нет прав для модерации");
    }

    @Override
    public ResponseEntity<CommentResponse> addComment(CommentRequest request)
            throws NotFoundException, UnauthorizedException {
        User user = userService.checkUser();
        PostComment comment = new PostComment();
        return null;
    }

}
