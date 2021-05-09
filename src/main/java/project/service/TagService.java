package project.service;

import project.exception.NotFoundException;
import project.model.Post;

public interface TagService {

    void addTags(String[] tags);

    void addTagIfNotExist(String tag);

    void addTagsToPost(String[] tags, Post post) throws NotFoundException;

    void activateTags(Post post) throws NotFoundException;

    void hideTag(String tagName) throws NotFoundException;

}
