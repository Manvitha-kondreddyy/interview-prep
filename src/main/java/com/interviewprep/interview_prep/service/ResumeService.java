package com.interviewprep.interview_prep.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interviewprep.interview_prep.model.InterviewSession;
import com.interviewprep.interview_prep.model.Resume;
import com.interviewprep.interview_prep.repository.InterviewSessionRepository;
import com.interviewprep.interview_prep.repository.ResumeRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private InterviewSessionRepository interviewSessionRepository;

    @Autowired
    private LangChainService langChainService;

    public Resume uploadAndParseResume(MultipartFile file) throws IOException {
        String extractedText;
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            extractedText = stripper.getText(document);
        }

        String prompt = "You are an expert resume reviewer.\n\n" +
                "Analyse this resume and think step by step:\n" +
                "Step 1: Identify the candidate's key skills and experience.\n" +
                "Step 2: Evaluate the strength of their projects.\n" +
                "Step 3: Identify any gaps or missing skills.\n" +
                "Step 4: Give an overall assessment.\n\n" +
                "Now provide detailed feedback on:\n" +
                "1) Strengths\n" +
                "2) Areas of improvement\n" +
                "3) Missing skills\n" +
                "4) Overall score out of 10\n\n" +
                "Resume:\n" + extractedText;

        String aiFeedback = langChainService.chat(prompt);

        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);
        resume.setAiFeedback(aiFeedback);
        resume.setUploadedAt(LocalDateTime.now());

        return resumeRepository.save(resume);
    }

    public String matchWithJobDescription(MultipartFile file, String jobDescription) throws IOException {
        String extractedText;
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            extractedText = stripper.getText(document);
        }

        String prompt = "You are an expert recruiter.\n\n" +
                "Compare this resume with the job description step by step:\n" +
                "Step 1: Identify required skills from the job description.\n" +
                "Step 2: Identify skills present in the resume.\n" +
                "Step 3: Find matching and missing skills.\n" +
                "Step 4: Calculate match percentage.\n\n" +
                "Now provide:\n" +
                "1) Match percentage out of 100\n" +
                "2) Matching skills\n" +
                "3) Missing skills\n" +
                "4) Suggestions to improve resume for this job\n\n" +
                "Resume:\n" + extractedText + "\n\n" +
                "Job Description:\n" + jobDescription;

        return langChainService.chat(prompt);
    }

    public String generateInterviewQuestions(String jobRole) {
        String prompt = "You are an expert technical interviewer.\n\n" +
                "Generate 10 interview questions for the role of " + jobRole + ".\n" +
                "Think step by step:\n" +
                "Step 1: Identify the core technical skills needed for this role.\n" +
                "Step 2: Identify behavioural traits important for this role.\n" +
                "Step 3: Think of real-world scenarios relevant to this role.\n" +
                "Step 4: Now generate the questions based on above thinking.\n\n" +
                "Include:\n" +
                "1) 3 technical questions\n" +
                "2) 3 behavioural questions\n" +
                "3) 2 situational questions\n" +
                "4) 2 HR questions\n\n" +
                "Make them realistic and commonly asked in interviews.";

        return langChainService.chat(prompt);
    }

    public String evaluateAnswer(String question, String answer, String jobRole) {
        String prompt = "You are an expert interview coach evaluating a candidate's answer.\n\n" +
                "Question: " + question + "\n\n" +
                "Candidate's Answer: " + answer + "\n\n" +
                "Think step by step:\n" +
                "Step 1: Understand what the question is really asking.\n" +
                "Step 2: Identify what a strong answer should include.\n" +
                "Step 3: Compare the candidate's answer with the ideal answer.\n" +
                "Step 4: Identify what was good and what was missing.\n" +
                "Step 5: Give a fair score and constructive feedback.\n\n" +
                "Now provide:\n" +
                "1) Score out of 10\n" +
                "2) What was good\n" +
                "3) What was missing\n" +
                "4) Ideal answer summary";

        String feedback = langChainService.chat(prompt);

        InterviewSession session = new InterviewSession();
        session.setJobRole(jobRole);
        session.setQuestion(question);
        session.setUserAnswer(answer);
        session.setAiFeedback(feedback);
        session.setCreatedAt(LocalDateTime.now());
        interviewSessionRepository.save(session);

        return feedback;
    }

    public List<InterviewSession> getSessionsByJobRole(String jobRole) {
        return interviewSessionRepository.findByJobRoleOrderByCreatedAtDesc(jobRole);
    }

    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));
    }
}