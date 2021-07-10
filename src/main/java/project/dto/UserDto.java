package project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String photo;

    public UserDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
