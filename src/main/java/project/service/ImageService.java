package project.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageErrorMap;
import project.dto.image.ImageResponse;
import project.exception.*;

import java.awt.image.BufferedImage;

public interface ImageService {

    BufferedImage resizeCaptchaImage(BufferedImage originalImage);

    ResponseEntity<ImageResponse> saveImage(MultipartFile file)
            throws BadRequestException, UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException;

    String saveAvatar(MultipartFile file)
            throws BadRequestException, UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException;

    ImageErrorMap checkFile(MultipartFile file);

}
