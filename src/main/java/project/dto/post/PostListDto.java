package project.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * <h3>Объект со списком данных постов</h3>
 */
@AllArgsConstructor
@Getter
public class PostListDto {

    /**
     * Общее кол-во постов, доступное по запросу
     */
    private final long count;

    /**
     * Список с данными постов
     */
    private final List<PostDto> posts;

}
