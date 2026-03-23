package com.feedback.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.time.Duration;

public class FeedbackFormTest {

    // ── WebDriver instance ───────────────────────────────────────────
    static WebDriver driver;
    static WebDriverWait wait;

    // ── Absolute path to your index.html ────────────────────────────
    // Update this path if your project folder is in a different location
    static final String FORM_URL = "file:///" +
        new File("c:/Users/User/OneDrive/Documents/CA2_devops/index.html")
            .getAbsolutePath().replace("\\", "/");

    // ── Track test results ───────────────────────────────────────────
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        setUp();

        try {
            // ---------------------------------------------------------
            // TC-01: Page loads with correct title
            // ---------------------------------------------------------
            testPageTitle();

            // ---------------------------------------------------------
            // TC-02: Submit empty form → all error messages appear
            // ---------------------------------------------------------
            testEmptyFormSubmission();

            // ---------------------------------------------------------
            // TC-03: Invalid email format shows email error
            // ---------------------------------------------------------
            testInvalidEmail();

            // ---------------------------------------------------------
            // TC-04: Mobile number with less than 10 digits shows error
            // ---------------------------------------------------------
            testInvalidMobile();

            // ---------------------------------------------------------
            // TC-05: Feedback with fewer than 10 words shows error
            // ---------------------------------------------------------
            testShortFeedback();

            // ---------------------------------------------------------
            // TC-06: Valid form fills all fields without errors
            // ---------------------------------------------------------
            testValidFormFill();

            // ---------------------------------------------------------
            // TC-07: Reset button clears all fields
            // ---------------------------------------------------------
            testResetButton();

        } finally {
            printResults();
            tearDown();
        }
    }

    // ── Setup & Teardown ─────────────────────────────────────────────

    static void setUp() {
        ChromeOptions options = new ChromeOptions();
        // Uncomment below line to run headlessly (no browser window)
        // options.addArguments("--headless");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("✅ Browser launched successfully.\n");
    }

    static void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("\nBrowser closed.");
        }
    }

    static void loadForm() {
        driver.get(FORM_URL);
        // Wait for the form to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("feedbackForm")));
    }

    // ── Helper: assert with logging ───────────────────────────────────

    static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName);
            failed++;
        }
    }

    static void assertContains(String testName, String actual, String expected) {
        assertTrue(testName, actual != null && actual.contains(expected));
    }

    // ── TC-01: Page title ─────────────────────────────────────────────

    static void testPageTitle() {
        System.out.println("TC-01: Verify page title");
        loadForm();
        assertTrue(
            "Title contains 'Student Feedback'",
            driver.getTitle().contains("Student Feedback")
        );
    }

    // ── TC-02: Empty form submission ──────────────────────────────────

    static void testEmptyFormSubmission() {
        System.out.println("\nTC-02: Empty form submission shows validation errors");
        loadForm();

        // Click Submit without filling anything
        driver.findElement(By.id("submitBtn")).click();

        // Name error
        String nameErr = driver.findElement(By.id("nameError")).getText();
        assertContains("Name error shown", nameErr, "cannot be empty");

        // Email error
        String emailErr = driver.findElement(By.id("emailError")).getText();
        assertContains("Email error shown", emailErr, "cannot be empty");

        // Mobile error
        String mobileErr = driver.findElement(By.id("mobileError")).getText();
        assertContains("Mobile error shown", mobileErr, "cannot be empty");

        // Department error
        String deptErr = driver.findElement(By.id("deptError")).getText();
        assertContains("Department error shown", deptErr, "select a department");

        // Gender error
        String genderErr = driver.findElement(By.id("genderError")).getText();
        assertContains("Gender error shown", genderErr, "select a gender");

        // Feedback error
        String feedbackErr = driver.findElement(By.id("feedbackError")).getText();
        assertContains("Feedback error shown", feedbackErr, "cannot be blank");
    }

    // ── TC-03: Invalid email ──────────────────────────────────────────

    static void testInvalidEmail() {
        System.out.println("\nTC-03: Invalid email format triggers error");
        loadForm();

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("notanemail");

        // Trigger blur (click elsewhere) to fire validation
        driver.findElement(By.id("studentName")).click();

        String emailErr = driver.findElement(By.id("emailError")).getText();
        assertContains("Invalid email error shown", emailErr, "valid email address");
    }

    // ── TC-04: Invalid mobile (< 10 digits) ───────────────────────────

    static void testInvalidMobile() {
        System.out.println("\nTC-04: Mobile number with fewer than 10 digits shows error");
        loadForm();

        WebElement mobileField = driver.findElement(By.id("mobile"));
        mobileField.sendKeys("12345");

        // Trigger blur
        driver.findElement(By.id("studentName")).click();

        String mobileErr = driver.findElement(By.id("mobileError")).getText();
        assertContains("Short mobile error shown", mobileErr, "exactly 10 digits");
    }

    // ── TC-05: Short feedback (< 10 words) ───────────────────────────

    static void testShortFeedback() {
        System.out.println("\nTC-05: Feedback with fewer than 10 words shows error");
        loadForm();

        WebElement feedbackField = driver.findElement(By.id("feedback"));
        feedbackField.sendKeys("Too short feedback");

        // Trigger blur
        driver.findElement(By.id("studentName")).click();

        String feedbackErr = driver.findElement(By.id("feedbackError")).getText();
        assertContains("Short feedback error shown", feedbackErr, "at least 10 words");
    }

    // ── TC-06: Valid form fill ────────────────────────────────────────

    static void testValidFormFill() {
        System.out.println("\nTC-06: Valid data fills form without showing any errors");
        loadForm();

        // Student Name
        driver.findElement(By.id("studentName")).sendKeys("John Doe");

        // Email
        driver.findElement(By.id("email")).sendKeys("john.doe@example.com");

        // Mobile
        driver.findElement(By.id("mobile")).sendKeys("9876543210");

        // Department — select "Computer Science"
        Select deptDropdown = new Select(driver.findElement(By.id("department")));
        deptDropdown.selectByValue("cs");

        // Gender — select Male
        driver.findElement(By.id("genderMale")).click();

        // Feedback — at least 10 words
        driver.findElement(By.id("feedback")).sendKeys(
            "This course was very informative and the faculty explained every concept clearly and thoroughly."
        );

        // Trigger blur on name to fire all validators
        driver.findElement(By.id("studentName")).click();

        // Verify no error messages are visible
        assertTrue("Name error is empty",
            driver.findElement(By.id("nameError")).getText().isEmpty());
        assertTrue("Email error is empty",
            driver.findElement(By.id("emailError")).getText().isEmpty());
        assertTrue("Mobile error is empty",
            driver.findElement(By.id("mobileError")).getText().isEmpty());
        assertTrue("Department error is empty",
            driver.findElement(By.id("deptError")).getText().isEmpty());
        assertTrue("Feedback error is empty",
            driver.findElement(By.id("feedbackError")).getText().isEmpty());
    }

    // ── TC-07: Reset button ───────────────────────────────────────────

    static void testResetButton() {
        System.out.println("\nTC-07: Reset button clears all fields and error messages");
        loadForm();

        // Fill some data
        driver.findElement(By.id("studentName")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("test@test.com");

        // Submit to trigger errors on unfilled fields
        driver.findElement(By.id("submitBtn")).click();

        // Click Reset
        driver.findElement(By.id("resetBtn")).click();

        // Verify fields are cleared
        assertTrue("Name field cleared",
            driver.findElement(By.id("studentName")).getAttribute("value").isEmpty());
        assertTrue("Email field cleared",
            driver.findElement(By.id("email")).getAttribute("value").isEmpty());
        assertTrue("Mobile field cleared",
            driver.findElement(By.id("mobile")).getAttribute("value").isEmpty());

        // Verify error messages are cleared
        assertTrue("Name error cleared after reset",
            driver.findElement(By.id("nameError")).getText().isEmpty());
        assertTrue("Email error cleared after reset",
            driver.findElement(By.id("emailError")).getText().isEmpty());
    }

    // ── Print summary ─────────────────────────────────────────────────

    static void printResults() {
        System.out.println("\n========================================");
        System.out.println("  TEST RESULTS SUMMARY");
        System.out.println("========================================");
        System.out.println("  Total  : " + (passed + failed));
        System.out.println("  Passed : " + passed);
        System.out.println("  Failed : " + failed);
        System.out.println("========================================");
    }
}
