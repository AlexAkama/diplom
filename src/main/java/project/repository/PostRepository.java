package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.global.KeyAndLongValueDto;
import project.dto.post.PostYearDto;
import project.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select date_format(p.time, '%Y') as year from Post p group by year order by year desc")
    List<PostYearDto> getYearList();

    @Query(value = "select date_format(p.time, '%Y-%m-%d') as key, count(*) as value from Post p"
            + " where year(p.time) = ?1 and is_active=1 AND moderation_status='ACCEPTED' AND time < NOW() "
            + " group by key order by key desc")
    List<KeyAndLongValueDto> getPostCounterList(int year);

}
