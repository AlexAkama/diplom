package project.service.implementation;

import org.springframework.stereotype.Service;
import project.exception.NotFoundException;
import project.model.*;
import project.repository.TagRepository;
import project.repository.TagToPostRepository;
import project.service.TagService;

import java.util.List;
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
        tagToPostRepository.deleteAllByPostId(post.getId());
        for (String tagName : tags) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new NotFoundException(String.format("Tag %s not found", tagName)));
            TagToPost tagToPost = new TagToPost(tag, post);
            tagToPostRepository.save(tagToPost);
        }
    }

    @Override
    public void activateTags(Post post) throws NotFoundException {
        List<String> tagNameList = tagToPostRepository.getTagList(post.getId());
        for (String tagName : tagNameList) {
            setActive(tagName, true);
        }
    }

    @Override
    public void hideTag(String tagName) throws NotFoundException {
        setActive(tagName, false);
    }


    private void setActive(String tagName, boolean status) throws NotFoundException {
        Tag tag = findTagByName(tagName);
        if (tag.isActive() != status) {
            tag.setActive(status);
            tagRepository.save(tag);
        }
    }

    private Tag findTagByName(String tagName) throws NotFoundException {
        return tagRepository.findByName(tagName)
                .orElseThrow(() -> new NotFoundException(String.format("Тэг %s не найден", tagName)));
    }

}
