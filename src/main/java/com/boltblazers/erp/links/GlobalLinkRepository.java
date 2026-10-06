package com.boltblazers.erp.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalLinkRepository extends JpaRepository<GlobalLink, Long> {
    List<GlobalLink> findAllByOrderByCreatedAtDesc();
}
