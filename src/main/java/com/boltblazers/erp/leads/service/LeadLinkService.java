package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.Lead;
import com.boltblazers.erp.leads.LeadLink;
import com.boltblazers.erp.leads.LeadLinkRepository;
import com.boltblazers.erp.leads.LeadRepository;
import com.boltblazers.erp.leads.dto.LeadLinkRequest;
import com.boltblazers.erp.leads.dto.LeadLinkResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeadLinkService {

    private final LeadLinkRepository linkRepository;
    private final LeadRepository leadRepository;

    public LeadLinkService(LeadLinkRepository linkRepository, LeadRepository leadRepository) {
        this.linkRepository = linkRepository;
        this.leadRepository = leadRepository;
    }

    public LeadLinkResponse addLink(Long leadId, LeadLinkRequest request) {
        Lead lead = leadRepository.findById(leadId).orElseThrow();
        LeadLink link = new LeadLink();
        link.setLead(lead);
        link.setTitle(request.getTitle());
        link.setUrl(request.getUrl());
        link.setDescription(request.getDescription());
        linkRepository.save(link);
        return toResponse(link);
    }

    public List<LeadLinkResponse> getLinksForLead(Long leadId) {
        return linkRepository.findByLeadIdOrderByCreatedAtDesc(leadId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public LeadLinkResponse updateLink(Long linkId, LeadLinkRequest request) {
        LeadLink link = linkRepository.findById(linkId).orElseThrow();
        if (request.getTitle() != null) link.setTitle(request.getTitle());
        if (request.getUrl() != null) link.setUrl(request.getUrl());
        if (request.getDescription() != null) link.setDescription(request.getDescription());
        linkRepository.save(link);
        return toResponse(link);
    }

    public void deleteLink(Long linkId) {
        linkRepository.deleteById(linkId);
    }

    private LeadLinkResponse toResponse(LeadLink link) {
        LeadLinkResponse res = new LeadLinkResponse();
        res.setId(link.getId());
        res.setLeadId(link.getLead().getId());
        res.setTitle(link.getTitle());
        res.setUrl(link.getUrl());
        res.setDescription(link.getDescription());
        if (link.getCreatedAt() != null) res.setCreatedAt(link.getCreatedAt().toString());
        if (link.getUpdatedAt() != null) res.setUpdatedAt(link.getUpdatedAt().toString());
        return res;
    }
}
