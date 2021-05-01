package project.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import project.dto.main.AppResponseWithErrors;

public class CommentResponse extends AppResponseWithErrors {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
