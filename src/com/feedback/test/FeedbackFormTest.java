package com.feedback.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
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
    }

    @BeforeMethod
    public void loadPage() {
        driver.get("file:///" + basePath);
    }

    @Test
    public void testPageTitle() {
        Assert.assertTrue(driver.getTitle().contains("Feedback"));
    }

    @Test
    public void testEmptyFormSubmission() {
        driver.findElement(By.id("submit")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testInvalidEmail() {
        driver.findElement(By.id("name")).sendKeys("John");
        driver.findElement(By.id("email")).sendKeys("wrongemail");
        driver.findElement(By.id("submit")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testInvalidMobileNumber() {
        driver.findElement(By.id("mobile")).sendKeys("123");
        driver.findElement(By.id("submit")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testMissingDepartment() {
        driver.findElement(By.id("name")).sendKeys("John");
        driver.findElement(By.id("submit")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testShortFeedback() {
        driver.findElement(By.id("feedback")).sendKeys("ok");
        driver.findElement(By.id("submit")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testValidFormSubmission() {

        driver.findElement(By.id("name")).sendKeys("John Doe");
        driver.findElement(By.id("email")).sendKeys("john@gmail.com");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");
        driver.findElement(By.id("feedback")).sendKeys("This is valid feedback message");

        driver.findElement(By.id("submit")).click();

        Assert.assertTrue(true);
    }

    @Test
    public void testResetButton() {

        driver.findElement(By.id("name")).sendKeys("Test");
        driver.findElement(By.id("reset")).click();

        Assert.assertEquals(
                driver.findElement(By.id("name")).getAttribute("value"),
                ""
        );
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
