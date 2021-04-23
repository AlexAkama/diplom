package project.dto.statistic;


import java.util.Date;

public interface PostStatisticView {

    long getPostCounter();

    long getViewCounter();

    Date getFirstPublication();

}
