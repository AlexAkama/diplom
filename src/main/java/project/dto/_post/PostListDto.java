package project.dto._post;

import java.util.ArrayList;
import java.util.List;

public class PostListDto {

    private long count = 0;

    private List<PostDto> posts = new ArrayList<>();


    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }
}
