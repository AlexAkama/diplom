package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dto.vote.VoteCounterView;
import project.model.PostVote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<PostVote, Long> {

    @Query("SELECT" +
            " SUM(CASE WHEN pv.value = 1 THEN 1 ELSE 0 END) AS likeCounter, " +
            " SUM(CASE WHEN pv.value = -1 THEN 1 ELSE 0 END) AS dislikeCounter" +
            " FROM PostVote pv"
    )
    VoteCounterView getBlogResult();

    @Query("SELECT" +
            " SUM(CASE WHEN pv.value = 1 THEN 1 ELSE 0 END) AS likeCounter, " +
            " SUM(CASE WHEN pv.value = -1 THEN 1 ELSE 0 END) AS dislikeCounter" +
            " FROM PostVote pv" +
            " WHERE pv.user.id = ?1"
    )
    VoteCounterView getUserResult(long userId);

    @Query("SELECT" +
            " SUM(CASE WHEN pv.value =1 THEN 1 ELSE 0 END) AS likeCounter, " +
            " SUM(CASE WHEN pv.value =-1 THEN 1 ELSE 0 END) AS dislikeCounter" +
            " FROM PostVote pv" +
            " WHERE pv.post.id = ?1"
    )
    VoteCounterView getPostResult(long postId);

    Optional<PostVote> findByPostIdAndUserId(long postId, long userId);

}
