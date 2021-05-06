package project.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dto.global.MapDto;
import project.model.TagToPost;

import java.util.List;

@Repository
public interface TagToPostRepository extends JpaRepository<TagToPost, Long> {

    @Query("SELECT tp.tag.name AS key, COUNT (tp.tag) AS value FROM TagToPost tp GROUP BY tp.tag")
    List<MapDto> getTagCounterList();

    @Query("SELECT t.name FROM TagToPost tp JOIN Tag t ON tp.tag.id = t.id WHERE tp.post.id = ?1")
    List<String> getTagList(long postId);

    @Transactional
    @Modifying
    void deleteAllByPostId(long postId);

}