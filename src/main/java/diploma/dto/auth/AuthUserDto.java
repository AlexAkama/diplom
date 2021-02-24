package diploma.dto.auth;

import diploma.config.Connection;
import diploma.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AuthUserDto {
    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation = false;
    private long moderationCount = 0;
    private boolean settings = false;

    public AuthUserDto(User user) {
        id = user.getId();
        name = user.getName();
        photo = user.getPhoto();
        email = user.getEmail();
        if (user.isModerator()) {
            moderation = true;
            settings = true;
            // FIXME вынести в DAO
            try (Session session = Connection.getSession()) {
                Transaction transaction = session.beginTransaction();

                String hql = "select count(*) from Post p where p.moderationStatus = 'NEW'";
                moderationCount = (long) session.createQuery(hql).uniqueResult();

                transaction.commit();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isModeration() {
        return moderation;
    }

    public void setModeration(boolean moderation) {
        this.moderation = moderation;
    }

    public long getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(long moderationCount) {
        this.moderationCount = moderationCount;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }
}
