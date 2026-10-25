package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Tag;
import com.boltblazers.erp.leads.TagRepository;
import com.boltblazers.erp.leads.dto.TagResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagResponse> getAllTags() {
        return tagRepository.findAllWithCount().stream()
                .map(tc -> new TagResponse(tc.getId(), tc.getName(), tc.getLeadCount()))
                .collect(Collectors.toList());
    }

    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow();
        Long count = tagRepository.findAllWithCount().stream()
                .filter(tc -> tc.getId().equals(id))
                .map(TagRepository.TagWithCount::getLeadCount)
                .findFirst().orElse(0L);
        return new TagResponse(tag.getId(), tag.getName(), count);
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

    public TagResponse createTag(String name) {
        Tag tag = getOrCreateTag(name);
        return new TagResponse(tag.getId(), tag.getName(), 0L);
    }

    public TagResponse updateTag(Long id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }
        Tag tag = tagRepository.findById(id).orElseThrow();
        tag.setName(newName.trim());
        tagRepository.save(tag);
        return getTagById(id);
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
