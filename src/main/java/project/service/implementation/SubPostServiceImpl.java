package project.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.comment.*;
import project.dto.main.AppResponse;
import project.dto.moderation.ModerationRequest;
import project.exception.*;
import project.model.*;
import project.model.enums.ModerationDecision;
import project.model.enums.ModerationStatus;
import project.service.*;

import java.util.Date;

import static project.model.enums.ModerationDecision.ACCEPT;
import static project.model.enums.ModerationDecision.valueOf;
import static project.model.enums.ModerationStatus.ACCEPTED;
import static project.model.enums.ModerationStatus.DECLINED;

@Service
public class SubPostServiceImpl implements SubPostService {

    private final UserService userService;
    private final PostService postService;

    @Value("${config.comment.minlength.text}")
    private int minTextLength;

    public SubPostServiceImpl(UserService userService,
                              PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public ResponseEntity<AppResponse> setModerationDecision(ModerationRequest request)
            throws NotFoundException, UnauthorizedException, ForbiddenException {
        var user = userService.checkUser();
        if (user.isModerator()) {
            ModerationDecision decision = valueOf(request.getDecision().toUpperCase());
            var post = postService.getPost(request.getPostId());
            ModerationStatus status = (decision == ACCEPT) ? ACCEPTED : DECLINED;
            post.setModerationStatus(status);
            post.setModerator(user);
            postService.saveAndActivateTags(post);
            return ResponseEntity.ok(new AppResponse().ok());
        } else
            throw new ForbiddenException("Нет прав для модерации");
    }

    @Override
    public ResponseEntity<CommentResponse> addComment(CommentRequest request)
            throws NotFoundException, UnauthorizedException {
        var user = userService.checkUser();
        var response = new CommentResponse();
        String text = request.getText();
        if (text.length() > minTextLength) {
            var post = postService.getPost(request.getPostId());
            var comment = new PostComment();
            comment.setText(text);
            comment.setPost(post);
            comment.setUser(user);
            comment.setTime(new Date());
            long parentId = request.getParentId();
            if (parentId != 0) {
                PostComment parent = postService.getComment(parentId);
                comment.setParent(parent);
            }
            comment = postService.saveAndFlush(comment);
            response.setId(comment.getId());
        } else {
            var errors = new CommentErrorMap();
            errors.addTextError();
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

}
