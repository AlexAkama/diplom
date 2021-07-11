package project.dto.profile;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class ProfileRequestWithoutPhoto {

    private String name;
    private String email;
    private String password;
    private int removePhoto;
}
