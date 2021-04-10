package project.dto.global;

import java.util.List;
import java.util.Map;

public class CalendarDto {

    private final List<String> years;
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
