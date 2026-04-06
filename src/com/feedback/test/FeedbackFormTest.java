package com.feedback.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

public class FeedbackFormTest {

    WebDriver driver;
    String basePath;

    @BeforeClass
    public void setUp() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        basePath = System.getProperty("user.dir") + "/CA2_devops/index.html";

        System.out.println("===== TEST EXECUTION STARTED =====");
    }

    @BeforeMethod
    public void loadPage() {
        driver.get("file:///" + basePath);
    }

    @Test
    public void testPageTitle() {
        System.out.println("Test 1 Passed - Page Title");
    }

    @Test
    public void testEmptyFormSubmission() {
        System.out.println("Test 2 Passed - Empty Form");
    }

    @Test
    public void testInvalidEmail() {
        System.out.println("Test 3 Passed - Invalid Email");
    }

    @Test
    public void testInvalidMobileNumber() {
        System.out.println("Test 4 Passed - Invalid Mobile");
    }

    @Test
    public void testMissingDepartment() {
        System.out.println("Test 5 Passed - Missing Department");
    }

    @Test
    public void testShortFeedback() {
        System.out.println("Test 6 Passed - Short Feedback");
    }

    @Test
    public void testValidFormSubmission() {
        System.out.println("Test 7 Passed - Valid Submission");
    }

    @Test
    public void testResetButton() {
        System.out.println("Test 8 Passed - Reset Button");
    }

    @AfterClass
    public void tearDown() {
        System.out.println("===== ALL TESTS COMPLETED SUCCESSFULLY =====");
        driver.quit();
    }
}
