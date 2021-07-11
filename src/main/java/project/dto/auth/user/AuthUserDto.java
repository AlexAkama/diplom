package project.dto.auth.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserDto {

    private long id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    @JsonProperty("moderationCount")
    private long moderationCounter;
    private boolean settings;

}
