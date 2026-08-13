package com.interviewprep.interview_prep.repository;

import com.interviewprep.interview_prep.model.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {
    List<InterviewSession> findByJobRoleOrderByCreatedAtDesc(String jobRole);
}
