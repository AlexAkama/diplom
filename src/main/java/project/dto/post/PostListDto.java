package project.dto.post;

import java.util.List;

/**
 * <h3>Объект со списком данных постов</h3>
 */
public class PostListDto {

    /**
     * Общее кол-во постов, доступное по запросу
     */
    private final long count;

    /**
     * Список с данными постов
     */
    private final List<PostDto> posts;

    public PostListDto(long count, List<PostDto> posts) {
        this.count = count;
        this.posts = posts;
    }

    public long getCount() {
        return count;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

}
