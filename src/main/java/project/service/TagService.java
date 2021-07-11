package project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.exception.NotFoundException;
import project.model.*;
import project.repository.TagRepository;
import project.repository.TagToPostRepository;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagToPostRepository tagToPostRepository;


    public void addTags(String[] tags) {
        for (String tag : tags) {
            addTagIfNotExist(tag);
        }
    }

    public void addTagIfNotExist(String tagName) {
        tagName = tagName.toLowerCase(Locale.ROOT);
        if (!tagRepository.existsByName(tagName)) {
            var tag = new Tag(tagName);
            tagRepository.save(tag);
        }
    }

    public void addTagsToPost(String[] tags, Post post) throws NotFoundException {
        addTags(tags);
        tagToPostRepository.deleteAllByPostId(post.getId());
        for (String tagName : tags) {
            var tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new NotFoundException(String.format("Tag %s not found", tagName)));
            var tagToPost = new TagToPost(tag, post);
            tagToPostRepository.save(tagToPost);
        }
    }

    public void activateTags(Post post) throws NotFoundException {
        List<String> tagNameList = tagToPostRepository.getTagList(post.getId());
        for (String tagName : tagNameList) {
            setActive(tagName, true);
        }
    }

    public void hideTag(String tagName) throws NotFoundException {
        setActive(tagName, false);
    }


    private void setActive(String tagName, boolean status) throws NotFoundException {
        var tag = findTagByName(tagName);
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
