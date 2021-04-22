package project.dto.post;

import java.util.List;

public class PostListDto {

    private final long count;

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
