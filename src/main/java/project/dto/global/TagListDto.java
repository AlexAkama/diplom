package project.dto.global;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * <h3>Список тегов</h3>
 * {@link TagListDto#tagList} Список тегов
 */
@AllArgsConstructor
@Getter
public class TagListDto {

    /**
     * Список тегов
     */
    @JsonProperty("tags")
    private final List<TagDto> tagList;

}
