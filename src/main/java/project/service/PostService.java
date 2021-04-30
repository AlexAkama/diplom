package project.service;

import org.springframework.http.ResponseEntity;
import project.dto.post.*;
import project.dto.statistic.PostStatisticView;
import project.dto.statistic.StatisticDto;
import project.dto.vote.VoteCounterView;
import project.exception.*;
import project.model.Post;

public interface PostService {

  ResponseEntity<PostResponse> addPost(PostAddRequest request) throws UserNotFoundException, UnauthorizedException, ObjectNotFoundException;

  Post getPost(long postId) throws ObjectNotFoundException;

  void save(Post post);

  ResponseEntity<PostDto> getPostResponse(long postId) throws ObjectNotFoundException;

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
