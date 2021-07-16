package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageErrorMap;
import project.dto.main.AppResponseWithErrors;
import project.dto.profile.*;
import project.exception.*;
import project.model.RestoreCode;
import project.model.User;
import project.model.enums.ChangeStatus;
import project.repository.RestoreCodeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import static project.model.enums.ChangeStatus.NO_CHANGE;
import static project.model.enums.ChangeStatus.TO_CHANGE;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;
    private final AuthService authService;
    private final ImageService imageService;
    private final EmailService emailService;
    private final RestoreCodeRepository restoreCodeRepository;

    @Value("${avatar.default}")
    private String defaultAvatar;

    /**
     * Срок действия кода востановления пароля в минутах
     */
    @Value("${config.restore.timeout}")
    private int restoreTimeout;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    private void changeNameIfChecked(String name, String userName, User user, ProfileErrorMap errors) {
        if (name != null && !name.equals(userName)) {
            if (authService.nameIsCorrect(name)) {
                user.setName(name);
            } else {
                errors.addNameError();
            }
        }
    }

    private void changePasswordIfChecked(String password, User user, ProfileErrorMap errors) {
        if (password != null) {
            if (authService.passwordIsCorrect(password)) {
                user.setPassword(bCryptPasswordEncoder.encode(password));
            } else {
                errors.addPasswordError();
            }
        }
    }

    private ChangeStatus checkEmailChangeRequest(String email, String userEmail, ProfileErrorMap errors) {
        var check = NO_CHANGE;
        if (email != null && !email.equals(userEmail)) {
            if (authService.emailIsCorrect(email)) {
                check = TO_CHANGE;
            } else {
                errors.addEmailError(email);
            }
        }
        return check;
    }

    private ChangeStatus checkPhotoChangeRequest(int removePhoto, MultipartFile file, User user, ProfileErrorMap errors) {
        var check = NO_CHANGE;
        if (removePhoto == 1) {
            user.setPhoto(defaultAvatar);
        } else if (file != null) {
            ImageErrorMap imageErrors = imageService.checkFile(file);
            if (imageErrors.isEmpty()) {
                check = TO_CHANGE;
            } else {
                errors.addPhotoError(imageErrors);
            }
        }
        return check;
    }

    public ResponseEntity<AppResponseWithErrors> updateProfile(ProfileRequest request, HttpServletRequest httpServletRequest)
            throws UnauthorizedException, InternalServerException {

        var user = userService.checkUser();
        var errors = new ProfileErrorMap();
        var newEmail = request.getEmail();
        var file = request.getPhoto();

        changeNameIfChecked(request.getName(), user.getName(), user, errors);
        changePasswordIfChecked(request.getPassword(), user, errors);
        var emailChange = checkEmailChangeRequest(newEmail, user.getEmail(), errors);
        var photoChange =
                checkPhotoChangeRequest(request.getRemovePhoto(), file, user, errors);

        var response = new AppResponseWithErrors();
        if (errors.isEmpty()) {
            if (photoChange == TO_CHANGE) {
                String fileName = imageService.saveAvatar(file);
                user.setPhoto(fileName);
            }
            if (emailChange == TO_CHANGE) {
                String code = UUID.randomUUID().toString().replace("-", "");
                var restoreCode = new RestoreCode(code);
                restoreCode.setEmail(newEmail);
                restoreCodeRepository.save(restoreCode);
                user.setCode(code);
                String link = emailService.getHostAndPort(httpServletRequest) + "/confirm/" + code;
                emailService.sendConfirmEmail(newEmail, link);
            }
            userService.save(user);
        } else {
            response.setErrors(errors.getErrors());
        }

        return ResponseEntity.ok(response);
    }

    public String confirm(String code) throws BadRequestException, NotFoundException {
        restoreCodeRepository.deleteExpiredCode(restoreTimeout);
        Optional<RestoreCode> optionalRestoreCode = restoreCodeRepository.findByCode(code);
        if (optionalRestoreCode.isEmpty()) {
            throw new BadRequestException("Код устарел");
        } else {
            var restoreCode = optionalRestoreCode.get();
            var user = userService.findByCode(code);
            user.setEmail(restoreCode.getEmail());
            user.setCode(null);
            userService.save(user);
            restoreCodeRepository.delete(restoreCode);
            SecurityContextHolder.clearContext();
        }
        return "index";
    }

}
