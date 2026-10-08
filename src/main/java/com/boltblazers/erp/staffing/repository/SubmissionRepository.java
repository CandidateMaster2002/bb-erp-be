package com.boltblazers.erp.staffing.repository;

import com.boltblazers.erp.staffing.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByRequirementId(Long requirementId);
    List<Submission> findByCandidateId(Long candidateId);
}
