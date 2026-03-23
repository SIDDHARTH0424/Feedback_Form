# Eclipse Selenium Test Setup Guide

## Project Structure Created
```
CA2_devops/
├── pom.xml                                    (Maven configuration)
├── testng.xml                                 (TestNG configuration)
├── src/
│   └── com/feedback/test/
│       └── FeedbackFormtest.java             (Test class with 8 test cases)
├── index.html, home.html, login.html         (Your forms)
├── firebase-config.js, auth.js, etc.         (Your app files)
└── requirements.txt, Jenkinsfile             (Other configs)
```

---

## Step 1: Install Required Software

### Install Eclipse IDE
- Download: https://www.eclipse.org/downloads/
- Choose **Eclipse IDE for Enterprise Java Developers**
- Install Java JDK 11 or higher

### Install Maven (Optional - Eclipse includes it)
- Download: https://maven.apache.org/download.cgi
- Extract and add to PATH environment variable

---

## Step 2: Import Project into Eclipse

1. **Open Eclipse**
2. **File → Import**
3. **Maven → Existing Maven Projects**
4. **Browse** to your `CA2_devops` folder
5. **Finish**
6. Eclipse will download all dependencies automatically

---

## Step 3: Configure Eclipse for TestNG

1. **Help → Eclipse Marketplace**
2. Search for **TestNG** (by Cedric Beust)
3. **Install** and restart Eclipse
4. **Window → Preferences → TestNG → Check "Use project TestNG jar"**

---

## Step 4: Right-Click & Run Tests

### Option A: Run Single Test
1. Right-click `FeedbackFormtest.java`
2. **Run As → TestNG Test**

### Option B: Run Test Suite
1. Right-click `testng.xml`
2. **Run As → TestNG Suite**

### Option C: Run with Maven (Terminal)
```powershell
# Navigate to project folder
cd c:\Users\whag2\Downloads\CA2_devops

# Run all tests
mvn clean test

# Run single test class
mvn test -Dtest=FeedbackFormtest
```

---

## Test Cases Included (8 Tests)

| # | Test Name | Description |
|---|-----------|-------------|
| 1 | `testPageTitle` | Verifies page loads with correct title |
| 2 | `testEmptyFormSubmission` | Empty form shows validation errors |
| 3 | `testInvalidEmail` | Invalid email triggers error |
| 4 | `testInvalidMobileNumber` | Mobile < 10 digits rejected |
| 5 | `testMissingDepartment` | Department selection required |
| 6 | `testShortFeedback` | Feedback < 10 words rejected |
| 7 | `testValidFormSubmission` | Valid form submits successfully |
| 8 | `testResetButton` | Reset clears all fields |

---

## Dependencies Automatically Installed

```xml
✅ Selenium 4.18.1
✅ WebDriver Manager (auto driver management)
✅ TestNG (test framework)
✅ JUnit (alternative test framework)
✅ SLF4J + Logback (logging)
```

**No need to manually download ChromeDriver!** WebDriver Manager handles it automatically.

---

## Common Issues & Solutions

### Issue 1: "Chrome driver not found"
**Solution:** WebDriver Manager will auto-download. If it fails:
```java
// In setUp() method - already included!
WebDriverManager.chromedriver().setup();
```

### Issue 2: "TestNG not found"
**Solution:** 
1. Right-click project → **Maven → Update Project** (Alt+F5)
2. Restart Eclipse

### Issue 3: "Form not loading"
**Solution:** Verify the file path in `FeedbackFormtest.java`:
```java
String filePath = "c:/Users/whag2/Downloads/CA2_devops/CA2_devops/index.html";
```

### Issue 4: Tests timeout
**Solution:** Increase wait time in setUp():
```java
wait = new WebDriverWait(driver, Duration.ofSeconds(15));  // Increase from 10
```

---

## Next Steps

1. ✅ Import project into Eclipse
2. ✅ Create `src` folder structure if needed  
3. ✅ Run `testPageTitle` first to verify setup
4. ✅ Run all tests: **Right-click testng.xml → Run As → TestNG Suite**
5. ✅ View Test Results in **TestNG Results** tab

---

## Terminal Commands Reference

```powershell
# Clean and rebuild
mvn clean compile

# Run all tests
mvn clean test

# Run specific test
mvn test -Dtest=FeedbackFormtest#testValidFormSubmission

# Generate test report
mvn clean test -Dtest=FeedbackFormtest -Dsurefire.reports.directory=target/test-reports

# Skip tests during build
mvn clean compile -DskipTests
```

---

**Ready to go!** 🚀 Let me know if you need any adjustments.
