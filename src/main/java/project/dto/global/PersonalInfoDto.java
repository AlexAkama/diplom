package project.dto.global;

/**
 * <h3>Персональные данные владельца блога</h3>
 * <tr>
 * <td>{@link PersonalInfoDto#title}</td>
 * <td>Заголовок блога</td>
 * </tr>
 * <tr>
 * <td>{@link PersonalInfoDto#subtitle}</td>
 * <td>Подзаголовок блога (краткое описание)</td>
 * </tr>
 * <tr>
 * <td>{@link PersonalInfoDto#phone}</td>
 * <td>Контактный телефон</td>
 * </tr>
 * <tr>
 * <td>{@link PersonalInfoDto#email}</td>
 * <td>Электронная почта</td>
 * </tr>
 * <tr>
 * <td>{@link PersonalInfoDto#copyright}</td>
 * <td>Автороское право</td>
 * </tr>
 * <tr>
 * <td>{@link PersonalInfoDto#copyrightFrom}</td>
 * <td>Начало срока действия авторских прав</td>
 * </tr>
 */
public class PersonalInfoDto {

    /**
     * Заголовок блога
     */
    private String title;

    /**
     * Подзаголовок блога (краткое описание)
     */
    private String subtitle;

    /**
     * Контактный телефон
     */
    private String phone;

    /**
     * Электронная почта
     */
    private String email;

    /**
     * Автороское право
     */
    private String copyright;

    /**
     * Начало срока действия авторских прав
     */
    private String copyrightFrom;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }

    public void setCopyrightFrom(String copyrightFrom) {
        this.copyrightFrom = copyrightFrom;
    }

}
