package project;

import org.springframework.http.ResponseEntity;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;

public interface ProfileService {

    ResponseEntity<ProfileResponse> updateProfile(ProfileRequest request)
            throws UnauthorizedException, NotFoundException;

}
