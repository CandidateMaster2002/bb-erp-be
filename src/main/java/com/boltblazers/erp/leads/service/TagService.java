package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Tag;
import com.boltblazers.erp.leads.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAllByOrderByNameAsc();
    }

    public Tag getOrCreateTag(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }
        String cleanName = name.trim();
        return tagRepository.findByNameIgnoreCase(cleanName)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(cleanName);
                    return tagRepository.save(tag);
                });
    }

    public Tag createTag(String name) {
        return getOrCreateTag(name);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public List<Tag> resolveTags(List<Long> tagIds, List<String> tagNames) {
        List<Tag> result = new ArrayList<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            result.addAll(tagRepository.findAllById(tagIds));
        }
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String name : tagNames) {
                if (name != null && !name.trim().isEmpty()) {
                    Tag tag = getOrCreateTag(name);
                    if (!result.contains(tag)) {
                        result.add(tag);
                    }
                }
            }
        }
        return result;
    }
}
