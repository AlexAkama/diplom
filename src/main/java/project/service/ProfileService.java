package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.profile.ProfileRequest;
import project.dto.profile.ProfileResponse;
import project.exception.*;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {

    ResponseEntity<ProfileResponse> updateProfile(ProfileRequest request, HttpServletRequest httpServletRequest)
            throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException, BadRequestException;

    String confirm(String code) throws BadRequestException, NotFoundException;
}
