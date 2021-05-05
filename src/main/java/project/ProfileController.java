package project;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.exception.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "/api/profile/my", consumes = {"multipart/form-data"})
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "removePhoto", required = false, defaultValue = "0") int removePhoto,
            @RequestParam(name = "photo", required = false) MultipartFile photo,
            HttpServletRequest httpServletRequest
    ) throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException {
        return profileService.updateProfile(new ProfileRequest(name, email, password, removePhoto, photo), httpServletRequest);
    }

    @PostMapping("/api/profile/my")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody ProfileRequestWithoutPhoto request,
            HttpServletRequest httpServletRequest
    ) throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException {
        return profileService.updateProfile(new ProfileRequest(request), httpServletRequest);
    }

    @GetMapping("/confirm/{code}")
    public String confirmEmail(@PathVariable String code) throws BadRequestException, NotFoundException {
        return profileService.confirm(code);
    }

}
