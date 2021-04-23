package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.Post;
import project.model.PostComment;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findAllByPost(Post post);
}
