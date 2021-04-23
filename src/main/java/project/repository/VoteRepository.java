package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dto.post.VoteCounterView;
import project.model.PostVote;

public interface VoteRepository extends JpaRepository<PostVote, Long> {

    String baseSelect = "SELECT" +
            " COUNT(CASE WHEN pv.value=1 THEN 1 ELSE NULL END) AS likeCounter, " +
            " COUNT(CASE WHEN pv.value=-1 THEN 1 ELSE NULL END) AS dislikeCounter" +
            " FROM PostVote pv";

    @Query(baseSelect)
    VoteCounterView getBlogResult();

    @Query(baseSelect + " WHERE pv.user.id = ?1")
    VoteCounterView getUserResult(long userId);

    @Query(baseSelect + " WHERE pv.post.id = ?1")
    VoteCounterView getPostResult(long postId);

}
