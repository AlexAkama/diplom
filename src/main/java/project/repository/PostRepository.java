package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.global.MapDto;
import project.dto.post.PostYearDto;
import project.dto.statistic.PostStatisticView;
import project.model.Post;
import project.model.enums.ModerationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT function('DATE_FORMAT', p.time, '%Y') AS year FROM Post p WHERE p.moderationStatus = 'ACCEPTED'" +
            "GROUP BY year ORDER BY function('DATE_FORMAT', p.time, '%Y') DESC"
    )
    List<PostYearDto> getYearList();

    @Query("SELECT function('DATE_FORMAT', p.time, '%Y-%m-%d') AS key, COUNT(p) AS value FROM Post p" +
            " WHERE function('YEAR', p.time) = ?1" +
            " AND p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')" +
            " GROUP BY key ORDER BY function('DATE_FORMAT', p.time, '%Y-%m-%d') DESC"
    )
    List<MapDto> getPostCounterList(int year);

    Page<Post> findAll(Pageable pageable);

    @Query("FROM Post p WHERE p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')"
    )
    Page<Post> findAllWithBaseCondition(Pageable pageable);

    Optional<Post> findPostById(long id);

    @Query("FROM Post p WHERE p.id = ?1" +
            " AND p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')"
    )
    Optional<Post> findPostWithBaseCondition(long id);

    @Query("FROM Post p WHERE p.id IN " +
            "(SELECT ttp.post.id FROM TagToPost ttp WHERE ttp.tag.id = " +
            "(SELECT t.id FROM Tag t WHERE t.name = ?1))" +
            " AND p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')"
    )
    Page<Post> findPostByTagWithBaseCondition(String tag, Pageable pageable);

    @Query("FROM Post p WHERE function('DATE_FORMAT', p.time, '%Y-%m-%d') = ?1" +
            " AND p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')"
    )
    Page<Post> findPostByDateWithBaseCondition(String date, Pageable pageable);

    @Query("FROM Post p WHERE" +
            " (p.text LIKE ?1" +
            " OR p.title LIKE ?1" +
            " OR p.id IN (SELECT pc.post.id FROM PostComment pc WHERE pc.text LIKE ?1)" +
            " OR p.user.id IN (SELECT u.id FROM User u WHERE u.name LIKE ?1)" +
            " OR p.id IN (SELECT ttp.post.id FROM TagToPost ttp WHERE ttp.tag.id IN (SELECT t.id FROM Tag t WHERE t.name LIKE ?1)))" +
            " AND p.active = true AND p.moderationStatus = 'ACCEPTED' AND p.time < function('NOW')"
    )
    Page<Post> findPostBySearchWithBaseCondition(String search, Pageable pageable);

    @Query("SELECT" +
            " COUNT(p) AS postCounter," +
            " COALESCE(SUM(p.viewCounter),0) AS viewCounter," +
            " MIN(p.time) AS firstPublication" +
            " FROM Post p"
    )
    PostStatisticView getAllStatistic();

    @Query("SELECT" +
            " COUNT(p) AS postCounter," +
            " COALESCE(SUM(p.viewCounter),0) AS viewCounter," +
            " MIN(p.time) AS firstPublication" +
            " FROM Post p" +
            " WHERE p.user.id = ?1"
    )
    PostStatisticView getUserStatistic(long userId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.moderationStatus = 'NEW'"
    )
    long getPostToModerationCounter();

    long countAllByActiveAndModerationStatus(boolean active, ModerationStatus status);

    Page<Post> findAllByActiveAndModerationStatusAndModeratorId
            (boolean active, ModerationStatus status, long moderatorId, Pageable pageable);

    Page<Post> findAllByActiveAndModerationStatus(boolean active, ModerationStatus status, Pageable pageable);

    Page<Post> findAllByActiveAndModerationStatusAndUserId
            (boolean active, ModerationStatus status, long userId, Pageable pageable);

}
