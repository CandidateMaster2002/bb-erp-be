package com.boltblazers.erp.vault;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaultItemRepository extends JpaRepository<VaultItem, Long> {

    List<VaultItem> findByIsArchivedOrderByCreatedAtDesc(Boolean isArchived);

    List<VaultItem> findByCategoryAndIsArchivedOrderByCreatedAtDesc(String category, Boolean isArchived);

    List<VaultItem> findAllByOrderByCreatedAtDesc();
}
