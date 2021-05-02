package project.service.implementation;

import org.springframework.stereotype.Service;
import project.exception.NotFoundException;
import project.model.*;
import project.repository.TagRepository;
import project.repository.TagToPostRepository;
import project.service.TagService;

import java.util.Locale;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagToPostRepository tagToPostRepository;

    public TagServiceImpl(TagRepository tagRepository,
                          TagToPostRepository tagToPostRepository) {
        this.tagRepository = tagRepository;
        this.tagToPostRepository = tagToPostRepository;
    }

    @Override
    public void addTags(String[] tags) {
        for (String tag : tags) {
            addTagIfNotExist(tag);
        }
    }

    @Override
    public void addTagIfNotExist(String tagName) {
        tagName = tagName.toLowerCase(Locale.ROOT);
        if (!tagRepository.existsByName(tagName)) {
            Tag tag = new Tag(tagName);
            tagRepository.save(tag);
        }
    }

    @Override
    public void addTagsToPost(String[] tags, Post post) throws NotFoundException {
        addTags(tags);
        for (String tagName : tags) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new NotFoundException(String.format("Tag %s not found", tagName)));
            if (!tagToPostRepository.existsByTagAndPost(tag, post)) {
                TagToPost tagToPost = new TagToPost(tag, post);
                tagToPostRepository.save(tagToPost);
            }
        }
    }

}
