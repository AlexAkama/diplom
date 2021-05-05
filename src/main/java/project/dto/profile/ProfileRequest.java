package project.dto.profile;

import org.springframework.web.multipart.MultipartFile;

public class ProfileRequest extends ProfileRequestWithoutPhoto {

    private MultipartFile photo;

    public ProfileRequest(String name, String email, String password, int removePhoto, MultipartFile photo) {
        super(name, email, password, removePhoto);
        this.photo = photo;
    }

    public ProfileRequest(ProfileRequestWithoutPhoto request) {
        super(request.getName(), request.getEmail(), request.getPassword(), request.getRemovePhoto());
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

}
