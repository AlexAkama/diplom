package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.global.MapDto;
import project.dto._post.PostYearDto;
import project.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DATE_FORMAT(p.time, '%Y') AS year FROM Post p GROUP BY year ORDER BY year DESC")
    List<PostYearDto> getYearList();

    @Query("SELECT DATE_FORMAT(p.time, '%Y-%m-%d') AS key, COUNT(p) AS value FROM Post p"
            + " WHERE YEAR(p.time) = ?1 AND is_active=1 AND moderation_status='ACCEPTED' AND time<NOW()"
            + " GROUP BY key ORDER BY key DESC")
    List<MapDto> getPostCounterList(int year);

}
