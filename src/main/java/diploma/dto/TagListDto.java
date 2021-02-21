package diploma.dto;

import java.util.List;

public class TagListDto {
    List<TagDto> tags;

    public TagListDto(List<TagDto> tags) {
        this.tags = tags;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }
}
