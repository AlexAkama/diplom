package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.post.VoteCounterView;
import project.dto.post.PostDto;
import project.dto.post.PostListDto;
import project.dto.statistic.PostStatisticView;
import project.dto.statistic.StatisticDto;
import project.exception.*;

public interface PostService {

  ResponseEntity<PostDto> getPost(long postId) throws DocumentNotFoundException;

  ResponseEntity<PostListDto> getAnnounceList(int offset, int limit, String mode);

  ResponseEntity<PostListDto> getAnnounceListToModeration(int offset, int limit, String status) throws UserNotFoundException, UnauthorizedException;

  ResponseEntity<PostListDto> getAnnounceListByAuthUser(int offset, int limit, String status) throws UserNotFoundException, UnauthorizedException;

  ResponseEntity<PostListDto> getAnnounceListByTag(int offset, int limit, String tag);

  ResponseEntity<PostListDto> getAnnounceListByDate(int offset, int limit, String date);

  ResponseEntity<PostListDto> getAnnounceListBySearch(int offset, int limit, String search);

  PostStatisticView getAllPostStatistic();

  PostStatisticView getUserPostStatistic(long userId);

  VoteCounterView getAllVote();

  VoteCounterView getUserVote(long userId);

  StatisticDto getAllStatistic();

  StatisticDto getUserStatistic(long userId);

}
