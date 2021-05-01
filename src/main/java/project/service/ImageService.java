package project.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageResponse;
import project.exception.*;

public interface ImageService {

    ResponseEntity<ImageResponse> save(MultipartFile file) throws BadRequestException, UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException;

}
