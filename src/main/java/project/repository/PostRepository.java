package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.post.PostCounter;
import project.dto.post.PostYear;
import project.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select date_format(p.time, '%Y') as year from Post p group by year order by year desc")
    List<PostYear> getYearList();

//    List<PostCounter>

}
