package project.dto.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatisticDto {

    @JsonProperty("postsCount")
    private final long postCounter;
    @JsonProperty("likesCount")
    private final long likeCounter;
    @JsonProperty("dislikesCount")
    private final long dislikeCounter;
    @JsonProperty("viewsCount")
    private final long viewCounter;
    private final long firstPublication;

}
