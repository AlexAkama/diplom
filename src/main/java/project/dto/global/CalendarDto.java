package project.dto.global;

import java.util.List;
import java.util.Map;

/**
 * <h3>Данные для отображения списка</h3>
 * <tr><td>{@link CalendarDto#years}</td><td>Спсисок годов</td></tr>
 * <tr><td>{@link CalendarDto#posts}</td><td>Карта кол-ва публикаций</td></tr>
 */
public class CalendarDto {

    /**
     * Список годов, в которых были произведены публикации
     */
    private final List<String> years;

    /**
     * Карта кол-ва публикаций: дата - кол-во постов
     */
    private final Map<String, Long> posts;


    public CalendarDto(List<String> years, Map<String, Long> posts) {
        this.years = years;
        this.posts = posts;
    }

    public List<String> getYears() {
        return years;
    }

    public Map<String, Long> getPosts() {
        return posts;
    }

}
