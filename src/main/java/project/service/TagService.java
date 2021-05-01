package project.service;

import project.exception.NotFoundException;
import project.model.Post;

public interface TagService {

    void addTagsToPost(String[] tags, Post post) throws NotFoundException;

}
