package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto._post.PostYearDto;
import project.dto.global.MapDto;
import project.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    String year = "function('DATE_FORMAT', p.time, '%Y')";
    String date = "function('DATE_FORMAT', p.time, '%Y-%m-%d')";

    @Query("SELECT " + year + " AS year FROM Post p GROUP BY year ORDER BY " + year + " DESC")
    List<PostYearDto> getYearList();

    @Query("SELECT " + date + " AS key, COUNT(p) AS value FROM Post p"
            + " WHERE function('YEAR', p.time) = ?1 AND p.isActive=true AND p.moderationStatus='ACCEPTED'"
            + " AND p.time < function('NOW')"
            + " GROUP BY key ORDER BY " + date + " DESC")
    List<MapDto> getPostCounterList(int year);

}
