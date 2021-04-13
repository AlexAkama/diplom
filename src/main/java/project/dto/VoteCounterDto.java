package project.dto;

public interface VoteCounterDto {

    long getLikeCounter();

    long getDislikeCounter();

}

//public class VoteCounterDto {
//
//    private long likeCounter;
//    private long dislikeCounter;
//
//    public VoteCounterDto getBlogResult() {
//        return getResult(0, 0);
//    }
//
//    public VoteCounterDto getUserResult(int id) {
//        return getResult(0, id);
//    }
//
//    public VoteCounterDto getPostResult(int id) {
//        return getResult(id, 0);
//    }
//
//    private VoteCounterDto getResult(int postId, int userId) {
//        try (Session session = Connection.getSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            String whereByPostId = (postId != 0 && userId == 0) ? " where post.id=" + postId : "";
//            String whereByUserId = (userId != 0 && postId == 0) ? " where user.id=" + userId : "";
//            String hql = " select" +
//                    " count(case when value=1 then 1 else null end) as likeCount, " +
//                    " count(case when value=-1 then 1 else null end) as dislikeCount" +
//                    " from PostVote" + whereByPostId + whereByUserId;
//            Object[] row = (Object[]) session.createQuery(hql).uniqueResult();
//            likeCounter = (long) row[0];
//            dislikeCounter = (long) row[1];
//
//            transaction.commit();
//        }
//        return this;
//
//    }
//
//    public VoteCounterDto() {
//        likeCounter = 0;
//        dislikeCounter = 0;
//    }
//
//    public long getLikeCounter() {
//        return likeCounter;
//    }
//
//    public long getDislikeCounter() {
//        return dislikeCounter;
//    }
//
//}
