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

import java.util.List;
import java.util.Optional;

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

    @Query("FROM Post p WHERE " + AppConstant.HQL_BASIC_SEARCH_CONDITION)
    Page<Post> findAllWithBaseCondition(Pageable pageable);

    @Query("FROM Post p WHERE p.id = ?1 AND " + AppConstant.HQL_BASIC_SEARCH_CONDITION)
    Optional<Post> findPostWithBaseCondition(long id);
}
