package com.feedback.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.time.Duration;

/**
 * Selenium Test Suite for Student Feedback Registration Form
 * 
 * Setup in Eclipse:
 * 1. Right-click project → Configure → Convert to Maven Project
 * 2. Maven → Update Project (Alt+F5)
 * 3. Right-click test class → Run As → TestNG Test
 */
public class FeedbackFormtest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String formUrl;

    @BeforeClass
    public void setUp() {
        // Automatic driver management - no need to manually download ChromeDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Path to your index.html file
        String filePath = "c:/Users/whag2/Downloads/CA2_devops/CA2_devops/index.html";
        File file = new File(filePath);
        formUrl = "file:///" + file.getAbsolutePath().replace("\\", "/");

        System.out.println("✅ Browser launched successfully");
        System.out.println("📄 Form URL: " + formUrl);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed successfully");
        }
    }

    private void loadForm() {
        driver.get(formUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("feedbackForm")));
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 1: Page Loads Successfully
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 1, description = "Verify page loads with correct title")
    public void testPageTitle() {
        loadForm();
        Assert.assertTrue(
            driver.getTitle().contains("Student Feedback"),
            "Page title should contain 'Student Feedback'"
        );
        System.out.println("✅ TEST 1 PASSED: Page title verified");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 2: Empty Form Submission Shows Validation Errors
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 2, description = "Empty form submission shows validation errors")
    public void testEmptyFormSubmission() {
        loadForm();
        driver.findElement(By.id("submitBtn")).click();

        String nameErr = driver.findElement(By.id("nameError")).getText();
        Assert.assertTrue(
            nameErr.contains("cannot be empty") || nameErr.length() > 0,
            "Name error should be displayed"
        );

        String emailErr = driver.findElement(By.id("emailError")).getText();
        Assert.assertTrue(emailErr.length() > 0, "Email error should be displayed");

        System.out.println("✅ TEST 2 PASSED: Validation errors displayed");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 3: Invalid Email Format
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 3, description = "Invalid email format triggers error")
    public void testInvalidEmail() {
        loadForm();

        driver.findElement(By.id("studentName")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys("notanemail");
        driver.findElement(By.id("submitBtn")).click();

        String emailErr = driver.findElement(By.id("emailError")).getText();
        Assert.assertTrue(
            emailErr.length() > 0,
            "Email validation error should appear"
        );
        System.out.println("✅ TEST 3 PASSED: Invalid email rejected");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 4: Invalid Mobile Number (< 10 digits)
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 4, description = "Mobile number validation")
    public void testInvalidMobileNumber() {
        loadForm();

        driver.findElement(By.id("studentName")).sendKeys("Jane Smith");
        driver.findElement(By.id("email")).sendKeys("jane@example.com");
        driver.findElement(By.id("mobile")).sendKeys("12345");  // Only 5 digits
        driver.findElement(By.id("submitBtn")).click();

        String mobileErr = driver.findElement(By.id("mobileError")).getText();
        Assert.assertTrue(
            mobileErr.contains("10 digits") || mobileErr.length() > 0,
            "Mobile number validation error should appear"
        );
        System.out.println("✅ TEST 4 PASSED: Invalid mobile rejected");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 5: Missing Department Selection
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 5, description = "Department selection is required")
    public void testMissingDepartment() {
        loadForm();

        driver.findElement(By.id("studentName")).sendKeys("Alex Kumar");
        driver.findElement(By.id("email")).sendKeys("alex@example.com");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");
        // Skip department selection
        driver.findElement(By.id("genderMale")).click();
        driver.findElement(By.id("feedback")).sendKeys("This is excellent feedback with more than 10 words required");
        driver.findElement(By.id("submitBtn")).click();

        String deptErr = driver.findElement(By.id("deptError")).getText();
        Assert.assertTrue(
            deptErr.length() > 0,
            "Department error should be displayed"
        );
        System.out.println("✅ TEST 5 PASSED: Department validation enforced");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 6: Feedback Word Count Validation
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 6, description = "Feedback must contain at least 10 words")
    public void testShortFeedback() {
        loadForm();

        driver.findElement(By.id("studentName")).sendKeys("Priya Sharma");
        driver.findElement(By.id("email")).sendKeys("priya@example.com");
        driver.findElement(By.id("mobile")).sendKeys("9123456789");
        Select deptDropdown = new Select(driver.findElement(By.id("department")));
        deptDropdown.selectByValue("cs");
        driver.findElement(By.id("genderFemale")).click();
        driver.findElement(By.id("feedback")).sendKeys("Too short");  // Less than 10 words
        driver.findElement(By.id("submitBtn")).click();

        String feedbackErr = driver.findElement(By.id("feedbackError")).getText();
        Assert.assertTrue(
            feedbackErr.contains("10 words") || feedbackErr.length() > 0,
            "Feedback word count error should appear"
        );
        System.out.println("✅ TEST 6 PASSED: Feedback word count validation works");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 7: Valid Form Submission
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 7, description = "Valid form submission succeeds")
    public void testValidFormSubmission() {
        loadForm();

        // Fill all fields correctly
        driver.findElement(By.id("studentName")).sendKeys("Rahul Patel");
        driver.findElement(By.id("email")).sendKeys("rahul.patel@example.com");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");

        Select deptDropdown = new Select(driver.findElement(By.id("department")));
        deptDropdown.selectByValue("it");

        driver.findElement(By.id("genderMale")).click();

        driver.findElement(By.id("feedback")).sendKeys(
            "This course was very informative and the faculty explained every concept clearly and thoroughly throughout the semester."
        );

        driver.findElement(By.id("submitBtn")).click();

        // Verify no error messages
        String nameErr = driver.findElement(By.id("nameError")).getText();
        String emailErr = driver.findElement(By.id("emailError")).getText();
        String mobileErr = driver.findElement(By.id("mobileError")).getText();

        Assert.assertTrue(nameErr.isEmpty(), "Name error should be empty");
        Assert.assertTrue(emailErr.isEmpty(), "Email error should be empty");
        Assert.assertTrue(mobileErr.isEmpty(), "Mobile error should be empty");

        System.out.println("✅ TEST 7 PASSED: Valid form submission succeeded");
    }

    // ─────────────────────────────────────────────────────────────
    // TEST 8: Reset Button Functionality
    // ─────────────────────────────────────────────────────────────
    @Test(priority = 8, description = "Reset button clears all fields")
    public void testResetButton() {
        loadForm();

        // Fill form
        driver.findElement(By.id("studentName")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");

        // Click Reset
        driver.findElement(By.id("resetBtn")).click();

        // Verify fields are cleared
        String nameValue = driver.findElement(By.id("studentName")).getAttribute("value");
        String emailValue = driver.findElement(By.id("email")).getAttribute("value");
        String mobileValue = driver.findElement(By.id("mobile")).getAttribute("value");

        Assert.assertTrue(nameValue.isEmpty(), "Name field should be cleared");
        Assert.assertTrue(emailValue.isEmpty(), "Email field should be cleared");
        Assert.assertTrue(mobileValue.isEmpty(), "Mobile field should be cleared");

        System.out.println("✅ TEST 8 PASSED: Reset button works correctly");
    }
}
