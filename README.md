# 🧠 AI-Powered Interview Preparation and Resume Analyser

A full-stack web application built with Java Spring Boot that helps students and job seekers prepare for placements using AI. Upload your resume, match it with job descriptions, generate role-specific interview questions, and get your answers evaluated — all powered by Groq AI (LLaMA 3.3 70B).

---

## ✨ Features

### 📄 Resume Analyser
- Upload your resume as a PDF
- AI analyses strengths, weaknesses, missing skills
- Gives an overall score out of 10
- Uses **RAG (Retrieval Augmented Generation)** prompting

### 🎯 JD Matcher
- Upload resume + paste a job description
- AI compares and finds matching and missing skills
- Gives a match percentage out of 100
- Uses **Chain of Thought** prompting

### ❓ Interview Questions Generator
- Enter any job role
- AI generates 10 realistic interview questions
- Includes technical, behavioural, situational and HR questions
- Uses **Zero-shot** prompting

### ✍️ Answer Evaluator
- Enter a question and your answer
- AI scores it out of 10
- Tells you what was good, what was missing, and gives an ideal answer summary
- Session saved to database for history tracking

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot 3.5.3 | Backend framework |
| Groq API (gpt-oss-120b) | AI model |
| Apache PDFBox 3.0.1 | PDF text extraction |
| MySQL | Database |
| Spring Data JPA | ORM |
| HTML / CSS / JavaScript | Frontend UI |

---

## 🚀 How to Run Locally

### Prerequisites
- Java 21
- Maven
- MySQL
- Groq API key (free at https://console.groq.com)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/Manvitha-kondreddyy/interview-prep.git
cd interview-prep
```

**2. Create MySQL database**
```sql
CREATE DATABASE interviewprep;
```

**3. Create `application.properties`**

Create this file at `src/main/resources/application.properties`:

```properties
spring.application.name=interview-prep

spring.datasource.url=jdbc:mysql://localhost:3306/interviewprep
spring.datasource.username=root
spring.datasource.password=your-mysql-password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.defer-datasource-initialization=true

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

groq.api.key=your-groq-api-key-here
```

**4. Run the application**
```bash
mvn spring-boot:run
```

**5. Open in browser**
http://localhost:8080
---

## 🏗️ Project Structure

src/main/java/com/interviewprep/interview_prep/
├── controller/
│   └── ResumeController.java
├── service/
│   ├── ResumeService.java
│   └── LangChainService.java
├── model/
│   ├── Resume.java
│   └── InterviewSession.java
├── repository/
│   ├── ResumeRepository.java
│   └── InterviewSessionRepository.java
└── InterviewPrepApplication.java

---

## 🤖 AI Prompting Techniques Used

- **RAG (Retrieval Augmented Generation)** — Resume text injected as context into the AI prompt
- **Zero-shot Prompting** — Direct instruction to AI with no examples
- **Chain of Thought Prompting** — AI thinks step by step before giving answer

---

## 👩‍💻 Built By

**Manvitha Kondreddy**  
Integrated M.Tech Software Engineering — VIT-AP University  
[GitHub](https://github.com/Manvitha-kondreddyy)
