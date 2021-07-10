package project.dto.profile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileRequest extends ProfileRequestWithoutPhoto {

    private MultipartFile photo;

    public ProfileRequest(String name, String email, String password, int removePhoto, MultipartFile photo) {
        super(name, email, password, removePhoto);
        this.photo = photo;
    }

    public ProfileRequest(ProfileRequestWithoutPhoto request) {
        super(request.getName(), request.getEmail(), request.getPassword(), request.getRemovePhoto());
    }

}
