package project.dto.global;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TagListDto {

    @JsonProperty("tags")
    List<TagDto> tagList;

    public TagListDto(List<TagDto> tagList) {
        this.tagList = tagList;
    }

    public List<TagDto> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDto> tagList) {
        this.tagList = tagList;
    }

}
