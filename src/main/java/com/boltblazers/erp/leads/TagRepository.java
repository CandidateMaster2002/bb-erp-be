package com.boltblazers.erp.leads;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameIgnoreCase(String name);
    List<Tag> findAllByOrderByNameAsc();

    @Query(value = "SELECT t.id AS id, t.name AS name, COUNT(lt.lead_id) AS leadCount " +
                   "FROM tags t " +
                   "LEFT JOIN lead_tags lt ON t.id = lt.tag_id " +
                   "GROUP BY t.id, t.name " +
                   "ORDER BY t.name ASC", nativeQuery = true)
    List<TagWithCount> findAllWithCount();

    interface TagWithCount {
        Long getId();
        String getName();
        Long getLeadCount();
    }
}
