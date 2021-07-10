package project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dto.image.ImageErrorMap;
import project.dto.image.ImageResponse;
import project.exception.*;
import project.model.enums.ImageTarget;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import static project.config.AppConstant.byteToMb;
import static project.model.enums.ImageTarget.AVATAR;
import static project.model.enums.ImageTarget.IMAGE;

@Service
public class ImageService {

    private final Random random = new Random();

    private final UserService userService;

    /**
     * Ширина картинки капчи
     */
    @Value("${config.captcha.image.width}")
    private int captchaWidth;
    /**
     * Высота картинки капчи
     */
    @Value("${config.captcha.image.height}")
    private int captchaHeight;

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

    /**
     * Ширина картинки аватара
     */
    @Value("${avatar.width}")
    private int avatarWidth;
    /**
     * Высота картинкаи аватара
     */
    @Value("${avatar.height}")
    private int avatarHeight;

    public ImageService(UserService userService) {
        this.userService = userService;
    }

    public BufferedImage resizeCaptchaImage(BufferedImage originalImage) {
        return resizeImage(originalImage, captchaWidth, captchaHeight);
    }

    public ResponseEntity<ImageResponse> saveImage(MultipartFile file)
            throws UnauthorizedException, NotFoundException, ImageSuccess, InternalServerException {
        userService.checkUser();
        ImageErrorMap errors = checkFile(file);
        if (errors.getErrors().isEmpty()) {
            String name = save(file, IMAGE);
            throw new ImageSuccess(name);
        } else {
            var response = new ImageResponse();
            response.setErrors(errors.getErrors());
            return ResponseEntity.ok(response);
        }
    }

    public String saveAvatar(MultipartFile file)
            throws UnauthorizedException, InternalServerException {
        return save(file, AVATAR);
    }

    public ImageErrorMap checkFile(MultipartFile file) {
        var errors = new ImageErrorMap();
        if (file == null || file.getOriginalFilename() == null) {
            errors.addNotFoundError();
        } else {
            String expansion = getExtensionFromFileName(file.getOriginalFilename());
            Set<String> expansionSet = Set.of(this.expansions.split(", "));
            if (!expansionSet.contains(expansion.toLowerCase())) {
                errors.addFormatError(this.expansions);
            } else if (file.getSize() > fileMaxSize)
                errors.addSizeError(byteToMb(file.getSize()), byteToMb(fileMaxSize));
        }
        return errors;
    }


    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        var resizedImage = new BufferedImage(width, height, originalImage.getType());
        var graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private String save(MultipartFile file, ImageTarget target) throws InternalServerException, UnauthorizedException {

        String avatar = null;
        if (target == AVATAR) {
            var user = userService.checkUser();
            avatar = user.getPhoto();
        }
        String filename = file.getOriginalFilename();
        String relativePath;
        String relativeFile;
        if (avatar != null) {
            var avatarFile = new File(avatar);
            relativePath = avatarFile.getPath().replace(avatarFile.getName(), "");
            relativeFile = relativePath + filename;
        } else {
            relativePath = File.separator + target.toString().toLowerCase() + buildPath();
            createDirIfNotExist(uploadPath + relativePath);
            relativeFile = relativePath + File.separator + filename;
        }
        String resultFile = uploadPath + relativeFile;
        try {
            if (target == AVATAR) {
                saveFileAsAvatar(file, resultFile);
            } else {
                file.transferTo(new File(resultFile));
            }
        } catch (IOException e) {
            throw new InternalServerException(String.format("Неудалось сохранить файл %s", filename));
        }
        return relativeFile;
    }

    private String getExtensionFromFileName(String filename) {
        return (filename != null && filename.lastIndexOf(".") > 0)
                ? filename.substring(filename.lastIndexOf(".") + 1)
                : "";
    }

    private String[] randomPathParts() {
        char[] chars = "abcdefhijkprstuvwx".toCharArray();
        var stringBuilder = new StringBuilder();
        for (var i = 0; i < randomPathPart * randomPathPartLength; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString().split("(?<=\\G.{" + randomPathPartLength + "})");
    }

    private String buildPath() {
        String[] randomPart = randomPathParts();
        var stringBuilder = new StringBuilder();
        for (String element : randomPart) {
            stringBuilder.append(File.separator);
            stringBuilder.append(element);
        }
        return stringBuilder.toString();
    }

    private void createDirIfNotExist(String path) throws InternalServerException {
        var dir = new File(path);
        if (!dir.exists() && !dir.mkdirs())
            throw new InternalServerException(String.format("Не удалось создать папку для хранения [%s]", path));
    }

    private BufferedImage cropToSquare(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int min = Math.min(width, height);
        int deltaWidth = (width > min) ? (width - min) / 2 : 0;
        int deltaHeight = (height > min) ? (height - min) / 2 : 0;
        return image.getSubimage(deltaWidth, deltaHeight, min, min);
    }

    private void saveFileAsAvatar(MultipartFile file, String relativeFile) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        image = cropToSquare(image);
        image = resizeImage(image, avatarWidth, avatarHeight);
        String extension = getExtensionFromFileName(file.getOriginalFilename());
        ImageIO.write(image, extension, new File(relativeFile));
    }

}
