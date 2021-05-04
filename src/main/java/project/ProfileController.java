package project;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.exception.*;

@Controller
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(value = "/my", consumes = {"multipart/form-data"})
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "removePhoto", required = false, defaultValue = "0") int removePhoto,
            @RequestParam(name = "photo", required = false) MultipartFile photo
    ) throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException {
        return profileService.updateProfile(new ProfileRequest(name, email, password, removePhoto, photo));
    }

    @PostMapping("/my")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody ProfileRequestWithoutPhoto request
    ) throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException {
        return profileService.updateProfile(new ProfileRequest(request));
    }

    @GetMapping("/confirm/{code}")
    public void confirmEmail(@PathVariable String code) {
        profileService.confirm(code);
    }

}
