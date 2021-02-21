package diploma.dto;

import diploma.config.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LikesDto {
    private long likeCount;
    private long dislikeCount;

    public LikesDto getBlogResult() {
        return getResult(0, 0);
    }

    public LikesDto getUserResult(int id) {
        return getResult(0, id);
    }

    public LikesDto getPostResult(int id) {
        return getResult(id, 0);
    }

    private LikesDto getResult(int postId, int userId) {
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String whereByPostId = (postId != 0 && userId == 0) ? " where post.id=" + postId : "";
            String whereByUserId = (userId != 0 && postId == 0) ? " where user.id=" + userId : "";
            String hql = " select" +
                    " count(case when value=1 then 1 else null end) as likeCount, " +
                    " count(case when value=-1 then 1 else null end) as dislikeCount" +
                    " from PostVote" + whereByPostId + whereByUserId;
            Object[] row = (Object[]) session.createQuery(hql).uniqueResult();
            likeCount = (long) row[0];
            dislikeCount = (long) row[1];

            transaction.commit();
        }
        return this;

    }

    public LikesDto() {
        likeCount = 0;
        dislikeCount = 0;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getDislikeCount() {
        return dislikeCount;
    }
}
