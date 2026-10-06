package com.boltblazers.erp.links.service;

import com.boltblazers.erp.links.GlobalLink;
import com.boltblazers.erp.links.GlobalLinkRepository;
import com.boltblazers.erp.links.dto.GlobalLinkRequest;
import com.boltblazers.erp.links.dto.GlobalLinkResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GlobalLinkService {

    private final GlobalLinkRepository linkRepository;

    public GlobalLinkService(GlobalLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public GlobalLinkResponse addLink(GlobalLinkRequest request) {
        GlobalLink link = new GlobalLink();
        link.setTitle(request.getTitle());
        link.setUrl(request.getUrl());
        link.setDescription(request.getDescription());
        linkRepository.save(link);
        return toResponse(link);
    }

    public List<GlobalLinkResponse> getAllLinks() {
        return linkRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public GlobalLinkResponse updateLink(Long id, GlobalLinkRequest request) {
        GlobalLink link = linkRepository.findById(id).orElseThrow();
        if (request.getTitle() != null) link.setTitle(request.getTitle());
        if (request.getUrl() != null) link.setUrl(request.getUrl());
        if (request.getDescription() != null) link.setDescription(request.getDescription());
        linkRepository.save(link);
        return toResponse(link);
    }

    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }

    private GlobalLinkResponse toResponse(GlobalLink link) {
        GlobalLinkResponse res = new GlobalLinkResponse();
        res.setId(link.getId());
        res.setTitle(link.getTitle());
        res.setUrl(link.getUrl());
        res.setDescription(link.getDescription());
        if (link.getCreatedAt() != null) res.setCreatedAt(link.getCreatedAt().toString());
        if (link.getUpdatedAt() != null) res.setUpdatedAt(link.getUpdatedAt().toString());
        return res;
    }
}
