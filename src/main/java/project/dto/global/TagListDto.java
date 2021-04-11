package project.dto.global;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <h3>Список тегов</h3>
 * {@link TagListDto#tagList} Список тегов
 */
public class TagListDto {

    /**
     * Список тегов
     */
    @JsonProperty("tags")
    private final List<TagDto> tagList;


    public TagListDto(List<TagDto> tagList) {
        this.tagList = tagList;
    }


    public List<TagDto> getTagList() {
        return tagList;
    }

}
