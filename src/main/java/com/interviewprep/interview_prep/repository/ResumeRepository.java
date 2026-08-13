package com.interviewprep.interview_prep.repository;

import com.interviewprep.interview_prep.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}