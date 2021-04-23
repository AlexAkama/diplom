package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.config.AppConstant;
import project.dto.global.MapDto;
import project.dto.post.PostYearDto;
import project.model.Post;
import project.model.emun.ModerationStatus;

import java.util.*;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    String year = "function('DATE_FORMAT', p.time, '%Y')";
    String date = "function('DATE_FORMAT', p.time, '%Y-%m-%d')";

    @Query("SELECT " + year + " AS year FROM Post p GROUP BY year ORDER BY " + year + " DESC")
    List<PostYearDto> getYearList();

    @Query("SELECT " + date + " AS key, COUNT(p) AS value FROM Post p"
            + " WHERE function('YEAR', p.time) = ?1"
            + " AND " + AppConstant.HQL_BASIC_SEARCH_CONDITION
            + " GROUP BY key ORDER BY " + date + " DESC")
    List<MapDto> getPostCounterList(int year);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByActiveAndModerationStatusAndTimeBefore(boolean active, ModerationStatus status, Date date, Pageable pageable);

    Optional<Post> findPostByIdAndActiveAndModerationStatusAndTimeBefore(long id, boolean active, ModerationStatus moderationStatus, Date time);
}
