package project.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import project.dto.main.AppResponseWithErrors;

@Getter
@Setter
public class CommentResponse extends AppResponseWithErrors {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;

}
