package project.dto.statistic;


import java.util.Date;

public interface PostStatisticView {

    Long getPostCounter();

    Long getViewCounter();

    Date getFirstPublication();

}
