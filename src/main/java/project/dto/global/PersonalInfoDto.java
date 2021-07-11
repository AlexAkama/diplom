package project.dto.global;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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

}
