package project;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.dto.main.AppResponse;
import project.exception.InternalServerException;
import project.exception.NotFoundException;
import project.model.User;
import project.service.EmailService;
import project.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class RestoreServiceImpl implements RestoreService {

    private final UserService userService;
    private final EmailService emailService;

    public RestoreServiceImpl(UserService userService,
                              EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<AppResponse> restorePassword(RestoreRequest request, HttpServletRequest httpServletRequest)
            throws NotFoundException, InternalServerException {
        String email = request.getEmail();
        User user = userService.findByEmail(email);
        String code = UUID.randomUUID().toString().replace("-", "");
        user.setCode(code);
        userService.save(user);
        String link = emailService.getHostAndPort(httpServletRequest) + "/login/change-password/" + code;
        emailService.sendRestorePasswordEmail(email,link);
        return ResponseEntity.ok(new AppResponse().ok());
    }

}
