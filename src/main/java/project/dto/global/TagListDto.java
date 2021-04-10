package project.dto.global;

import java.util.List;

public class TagListDto {

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
