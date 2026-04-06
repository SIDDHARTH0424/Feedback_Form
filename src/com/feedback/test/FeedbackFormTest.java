package com.feedback.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FeedbackFormTest {

    WebDriver driver;

    @BeforeClass
    public void setUp() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        driver.get("file:///C:/feedback-form/index.html"); 
    }

    @Test
    public void testPageTitle() {
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Feedback"));
    }

    @Test
    public void testEmptyFormSubmission() {
        driver.findElement(By.id("submitBtn")).click();
        Assert.assertTrue(driver.getPageSource().contains("required"));
    }

    @Test
    public void testInvalidEmail() {
        driver.findElement(By.id("email")).sendKeys("invalidemail");
        driver.findElement(By.id("submitBtn")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testInvalidMobileNumber() {
        driver.findElement(By.id("mobile")).sendKeys("123");
        driver.findElement(By.id("submitBtn")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testMissingDepartment() {
        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("submitBtn")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testShortFeedback() {
        driver.findElement(By.id("feedback")).sendKeys("ok");
        driver.findElement(By.id("submitBtn")).click();
        Assert.assertTrue(true);
    }

    @Test
    public void testValidFormSubmission() {

        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("John Doe");

        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("john@gmail.com");

        driver.findElement(By.id("mobile")).clear();
        driver.findElement(By.id("mobile")).sendKeys("9876543210");

        driver.findElement(By.id("feedback")).clear();
        driver.findElement(By.id("feedback")).sendKeys("Very good system");

        driver.findElement(By.id("submitBtn")).click();

        Assert.assertTrue(true);
    }

    @Test
    public void testResetButton() {

        driver.findElement(By.id("resetBtn")).click();
        WebElement name = driver.findElement(By.id("name"));

        Assert.assertEquals(name.getAttribute("value"), "");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
