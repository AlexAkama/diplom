package diploma.dao;

import diploma.config.Connection;
import diploma.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DaoUserServiceImpl implements DaoUserService {

    @Override
    public boolean isEmailExist(String email) {
        String hql = "select 1 from User where email='" + email + "'";
        return getUniqueResult(hql) != null;
    }

    @Override
    public boolean isCodeCorrect(String code, String secret) {
        String hql = "select c.code from CaptchaCode c where c.secretCode='" + secret + "'";
        Object result = getUniqueResult(hql);
        return result != null && (result.equals(code));
    }

    @Override
    public void saveUser(User user) {
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String hql = "from User u where u.email='" + email + "'";
        return Optional.of((User) getUniqueResult(hql));
    }

    private Object getUniqueResult(String hql) {
        Object result;
        try (Session session = Connection.getSession()) {
            Transaction transaction = session.beginTransaction();
            result = session.createQuery(hql).uniqueResult();
            transaction.commit();
        }
        return result;
    }
}
