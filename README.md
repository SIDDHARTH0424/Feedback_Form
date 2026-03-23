# Student Feedback Portal

## Overview
This is a full-stack student feedback portal with:
- User auth (Firebase Email/Password)
- Firestore feedback persistence
- Responsive UI with form validation
- Selenium automation tests in Python and Java
- Jenkins CI pipeline and GitHub repo integration

## Repo Structure
- index.html, home.html, login.html (UI pages)
- style.css (shared styling)
- validation.js (client validation + Firestore save)
- auth.js, firebase-config.js (Firebase auth + config)
- test_feedback_form.py (Python Selenium tests)
- FeedbackFormTest.java (Java Selenium tests)
- pom.xml, testng.xml (Java test build config)
- Jenkinsfile (pipeline workflow)
- requirements.txt (Python deps)
- ECLIPSE_SETUP_GUIDE.md (IDE and run instructions)

## Firebase Setup
1. Create Firebase project.
2. Enable Authentication (Email/Password).
3. Create Firestore `feedback` collection.
4. Update firebase-config.js:
   - `apiKey`
   - `authDomain`
   - `projectId`
   - `storageBucket`
   - `messagingSenderId`
   - `appId`
   - `measurementId`

## Quick Start (Local)
### Python Selenium
```bash
python -m venv venv
.\venv\Scripts\activate
pip install -r requirements.txt
python -m pytest test_feedback_form.py -v --html=reports/test_report.html --self-contained-html --junitxml=reports/test_results.xml
```

### Java Selenium
```bash
mvn clean test
```

### Jenkins
- Use pipeline script at Jenkinsfile
- Set repo: `https://github.com/SIDDHARTH0424/Feedback_Form.git`
- Add credentials + email info for notifications
- Enable triggers as needed

## Tests in this project
- Empty form validation
- Invalid email, mobile, department, gender, short feedback
- Valid full submission
- Reset button behavior
- Optional cross-browser/performance (pipeline flags)

## GitHub Actions / Pipeline
- Checkout
- Python + Java environment setup
- Run Selenium tests (Python and Java)
- Publish reports + artifacts
- Notifications with success/failure email
