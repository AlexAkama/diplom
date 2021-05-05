package project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageErrorMap;
import project.exception.*;
import project.model.RestoreCode;
import project.model.User;
import project.repository.RestoreCodeRepository;
import project.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

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

    public ProfileServiceImpl(UserService userService,
                              AuthService authService,
                              ImageService imageService,
                              EmailService emailService,
                              RestoreCodeRepository restoreCodeRepository) {
        this.userService = userService;
        this.authService = authService;
        this.imageService = imageService;
        this.emailService = emailService;
        this.restoreCodeRepository = restoreCodeRepository;
    }


    @Override
    public ResponseEntity<ProfileResponse> updateProfile(ProfileRequest request, HttpServletRequest httpServletRequest)
            throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException {
        User user = userService.checkUser();
        printRequest(request);

        ProfileErrorMap errors = new ProfileErrorMap();

        String email = request.getEmail();
        boolean emailChange = false;
        if (email != null && !email.equals(user.getEmail())) {
            if (authService.emailIsCorrect(email)) {
                emailChange = true;
            } else {
                errors.addEmailError(email);
            }
        }

        String name = request.getName();
        if (name != null && !name.equals(user.getName())) {
            if (authService.nameIsCorrect(name)) {
                user.setName(name);
            } else {
                errors.addNameError();
            }
        }

        String password = request.getPassword();
        if (password != null) {
            if (authService.passwordIsCorrect(password)) {
                user.setPassword(bCryptPasswordEncoder.encode(password));
            } else {
                errors.addPasswordError();
            }
        }

        int removePhoto = request.getRemovePhoto();
        MultipartFile file = request.getPhoto();
        boolean photoChange = false;
        if (removePhoto == 1) {
            user.setPhoto(defaultAvatar);
        } else if (file != null) {
            ImageErrorMap imageErrors = imageService.checkFile(file);
            if (imageErrors.isEmpty()) {
                photoChange = true;
            } else {
                errors.addPhotoError(imageErrors);
            }
        }

        ProfileResponse response = new ProfileResponse();
        if (errors.isEmpty()) {
            if (photoChange) {
                String fileName = imageService.saveAvatar(file);
                user.setPhoto(fileName);
            }
            if (emailChange) {
                String code = UUID.randomUUID().toString().replace("-", "");
                RestoreCode restoreCode = new RestoreCode(code);
                restoreCode.setEmail(email);
                restoreCodeRepository.save(restoreCode);
                user.setCode(code);
                String link = emailService.getHostAndPort(httpServletRequest) + "/confirm/" + code;
                emailService.sendConfirmEmail(request.getEmail(), link);
            }
            userService.save(user);
        } else {
            response.setErrors(errors.getErrors());
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public String confirm(String code) throws BadRequestException, NotFoundException {
        restoreCodeRepository.deleteExpiredCode(restoreTimeout);
        Optional<RestoreCode> optionalRestoreCode = restoreCodeRepository.findByCode(code);
        if (optionalRestoreCode.isEmpty()) {
            throw new BadRequestException("Код устарел");
        } else {
            RestoreCode restoreCode = optionalRestoreCode.get();
            User user = userService.findByCode(code);
            user.setEmail(restoreCode.getEmail());
            user.setCode(null);
            userService.save(user);
            restoreCodeRepository.delete(restoreCode);
            SecurityContextHolder.clearContext();
        }
        return "index";
    }


    private void printRequest(ProfileRequest request) {
        System.out.println(request.getName());
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request.getRemovePhoto());
        System.out.println(request.getPhoto());
    }

}
