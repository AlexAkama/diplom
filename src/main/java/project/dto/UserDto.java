package project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserDto {
    private int id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String photo;

    public UserDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDto(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
