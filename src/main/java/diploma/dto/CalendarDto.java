package diploma.dto;

import java.util.List;
import java.util.Map;

public class CalendarDto {

    private List<String> years;
    private Map<String, Long> posts;


    public CalendarDto(List<String> years, Map<String, Long> posts) {
        this.years = years;
        this.posts = posts;
    }


    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public Map<String, Long> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Long> posts) {
        this.posts = posts;
    }
}
