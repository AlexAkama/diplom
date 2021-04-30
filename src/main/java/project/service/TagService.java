package project.service;

import project.exception.ObjectNotFoundException;
import project.model.Post;

public interface TagService {

    void addTags(String[] tags);

    void addTagIfNotExist(String tag);

    void addTagsToPost(String[] tags, Post post) throws ObjectNotFoundException;

}
