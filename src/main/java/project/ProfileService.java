package project;

import org.springframework.http.ResponseEntity;
import project.exception.*;

public interface ProfileService {

    ResponseEntity<ProfileResponse> updateProfile(ProfileRequest request)
            throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException;

    void confirm(String code);
}
