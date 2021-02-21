package diploma.dao;

public interface UserDao {

    boolean isExistEmail(String email);

    String getSecretCode(String code);

}
