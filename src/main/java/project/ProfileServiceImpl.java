package project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageErrorMap;
import project.exception.*;
import project.model.User;
import project.service.*;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;
    private final AuthService authService;
    private final ImageService imageService;

    @Value("${avatar.default}")
    private String defaultAvatar;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public ProfileServiceImpl(UserService userService,
                              AuthService authService,
                              ImageService imageService) {
        this.userService = userService;
        this.authService = authService;
        this.imageService = imageService;
    }


    @Override
    public ResponseEntity<ProfileResponse> updateProfile(ProfileRequest request)
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
                // окончательная смена после подтверждения
            }
            userService.save(user);
        } else {
            response.setErrors(errors.getErrors());
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public void confirm(String code) {

    }


    private void printRequest(ProfileRequest request) {
        System.out.println(request.getName());
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request.getRemovePhoto());
        System.out.println(request.getPhoto());
    }

}
