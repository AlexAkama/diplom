package project;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;

@Controller
@RequestMapping("/api/profile/my")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "removePhoto", required = false, defaultValue = "0") int removePhoto,
            @RequestParam(name = "photo", required = false) MultipartFile photo
    ) throws UnauthorizedException, NotFoundException {
        return profileService.updateProfile(new ProfileRequest(name, email, password, removePhoto, photo));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody ProfileRequestWithoutPhoto request
    ) throws UnauthorizedException, NotFoundException {
        return profileService.updateProfile(new ProfileRequest(request));
    }

}
