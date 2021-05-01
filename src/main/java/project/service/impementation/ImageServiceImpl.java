package project.service.impementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageErrorMap;
import project.dto.image.ImageResponse;
import project.exception.*;
import project.service.ImageService;
import project.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import static project.config.AppConstant.byteToMb;

@Service
public class ImageServiceImpl implements ImageService {

    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.maxSize}")
    private long fileMaxSize;
    @Value("${upload.expansion}")
    private String expansions;
    @Value("${upload.randomPart.value}")
    private int randomPathPart;
    @Value("${upload.randomPart.length}")
    private int randomPathPartLength;

    public ImageServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<ImageResponse> save(MultipartFile file)
            throws BadRequestException, UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException {
//        User user = userService.checkUser();
        ImageErrorMap errors = checkFile(file);
        if (errors.getErrors().isEmpty()) {
            String relativePath = buildPath();
            String filename = file.getOriginalFilename();
            String relativeFile = relativePath + File.separator + filename;
            String resultFile = uploadPath + relativeFile;
            createDirIfNotExist(resultFile);
            try {
                file.transferTo(new File(resultFile));
            } catch (IOException e) {
                throw new InternalServerException(String.format("Неудалось сохранить файл %s", filename));
            }
            relativeFile = File.separator + "images" + relativeFile;
            throw new ImageSuccess(relativeFile);
        } else {
            ImageResponse response = new ImageResponse();
            response.setErrors(errors.getErrors());
            return ResponseEntity.ok(response);
        }
    }

    private ImageErrorMap checkFile(MultipartFile file) {
        ImageErrorMap errors = new ImageErrorMap();
        if (file == null || file.getOriginalFilename() == null) {
            errors.addNotFoundError();
        } else {
            String expansion = getExpansionFromFileName(file.getOriginalFilename());
            Set<String> expansions = Set.of(this.expansions.split(", "));
            if (!expansions.contains(expansion)) {
                errors.addFormatError(this.expansions);
                //FIXME
                // org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException:
                // The field image exceeds its maximum permitted size of 1048576 bytes
            } else if (file.getSize() > fileMaxSize)
                errors.addSizeError(byteToMb(file.getSize()), byteToMb(fileMaxSize));
        }
        return errors;
    }

    private String getExpansionFromFileName(String filename) {
        return (filename != null && filename.lastIndexOf(".") > 0)
                ? filename.substring(filename.lastIndexOf(".") + 1)
                : "";
    }

    private String[] randomPathParts() {
        char[] chars = "abcdefhijkprstuvwx".toCharArray();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < randomPathPart * randomPathPartLength; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString().split("(?<=\\G.{" + randomPathPartLength + "})");
    }

    private String buildPath() {
        String[] randomPart = randomPathParts();
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : randomPart) {
            stringBuilder.append(File.separator);
            stringBuilder.append(element);
        }
        return stringBuilder.toString();
    }

    private void createDirIfNotExist(String path) throws InternalServerException {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs())
            throw new InternalServerException(String.format("Не удалось создать папку для хранения [%s]", path));
    }

}
