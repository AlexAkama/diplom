package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dto.vote.VoteCounterView;
import project.model.PostVote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<PostVote, Long> {

    String baseSelect = "SELECT" +
            " COUNT(CASE WHEN pv.value=1 THEN 1 ELSE NULL END) AS likeCounter, " +
            " COUNT(CASE WHEN pv.value=-1 THEN 1 ELSE NULL END) AS dislikeCounter" +
            " FROM PostVote pv";
    String baseSelectByUserId = baseSelect + " WHERE pv.user.id = ?1";
    String baseSelectByPostId = baseSelect + " WHERE pv.post.id = ?1";

    @Query(baseSelect)
    VoteCounterView getBlogResult();

    @Query(baseSelectByUserId)
    VoteCounterView getUserResult(long userId);

    @Query(baseSelectByPostId)
    VoteCounterView getPostResult(long postId);

    Optional<PostVote> findByPostIdAndUserId(long postId, long userId);

}
