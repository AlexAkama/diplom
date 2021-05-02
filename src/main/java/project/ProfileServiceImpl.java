package project;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.service.UserService;

@Service
public class ProfileServiceImpl implements ProfileService{

    private final UserService userService;

    public ProfileServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public ResponseEntity<ProfileResponse> updateProfile(ProfileRequest request)
            throws UnauthorizedException, NotFoundException {
//        userService.checkUser();
        System.out.println(request.getName());
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request.getRemovePhoto());
        System.out.println(request.getPhoto());
        return null;
    }

}
