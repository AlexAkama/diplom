package project.dto.statistic;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticDto {

    @JsonProperty("postCount")
    private final long postCounter;
    @JsonProperty("likeCount")
    private final long likeCounter;
    @JsonProperty("dislikeCount")
    private final long dislikeCounter;
    @JsonProperty("viewsCount")
    private final long viewCounter;
    private final long firstPublication;

    public StatisticDto(long postCounter,
                        long likeCounter,
                        long dislikeCounter,
                        long viewCounter,
                        long firstPublication) {
        this.postCounter = postCounter;
        this.likeCounter = likeCounter;
        this.dislikeCounter = dislikeCounter;
        this.viewCounter = viewCounter;
        this.firstPublication = firstPublication;
    }

    public long getPostCounter() {
        return postCounter;
    }

    public long getLikeCounter() {
        return likeCounter;
    }

    public long getDislikeCounter() {
        return dislikeCounter;
    }

    public long getViewCounter() {
        return viewCounter;
    }

    public long getFirstPublication() {
        return firstPublication;
    }

}
