package project.dto.auth.registration;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class CaptchaDto {

    private String secret;
    private String image;

}
