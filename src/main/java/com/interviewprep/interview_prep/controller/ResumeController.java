package com.interviewprep.interview_prep.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interviewprep.interview_prep.model.InterviewSession;
import com.interviewprep.interview_prep.model.Resume;
import com.interviewprep.interview_prep.service.LangChainService;
import com.interviewprep.interview_prep.service.ResumeService;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private LangChainService langChainService;

    @PostMapping("/upload")
    public ResponseEntity<Resume> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        Resume savedResume = resumeService.uploadAndParseResume(file);
        return ResponseEntity.ok(savedResume);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.getResumeById(id));
    }

    @PostMapping("/match")
    public ResponseEntity<String> matchWithJD(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription) throws IOException {
        String result = resumeService.matchWithJobDescription(file, jobDescription);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/interview-questions")
    public ResponseEntity<String> generateQuestions(
            @RequestParam("jobRole") String jobRole) {
        // Using LangChain4j for interview questions
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
        String result = langChainService.chat(prompt);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/evaluate-answer")
    public ResponseEntity<String> evaluateAnswer(
            @RequestParam("question") String question,
            @RequestParam("answer") String answer,
            @RequestParam("jobRole") String jobRole) {
        String result = resumeService.evaluateAnswer(question, answer, jobRole);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/{jobRole}")
    public ResponseEntity<List<InterviewSession>> getHistory(@PathVariable String jobRole) {
        return ResponseEntity.ok(resumeService.getSessionsByJobRole(jobRole));
    }
}