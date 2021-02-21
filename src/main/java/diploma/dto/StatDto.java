package diploma.dto;

import diploma.config.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;

public class StatDto {
    private long postsCount;
    private long viewsCount;
    private long firstPublication;


    public StatDto getBlogResult() {
        return getResult(0);
    }

    public StatDto getUserResult(int id) {
        return getResult(id);
    }

    private StatDto getResult(int userId) {
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();

            String whereByUserId = (userId != 0) ? " where user.id=" + userId : "";
            String hql = "select count(*), sum(viewCount), min(time) from Post" + whereByUserId;
            Object[] row = (Object[]) session.createQuery(hql).uniqueResult();
            postsCount = (long) row[0];
            viewsCount = (long) row[1];
            firstPublication = ((Timestamp) row[2]).getTime() / 1000;

            transaction.commit();
        }
        return this;
    }

    public long getPostsCount() {
        return postsCount;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public long getFirstPublication() {
        return firstPublication;
    }
}
