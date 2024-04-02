package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.testng.CucumberOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelUtils;

import java.io.IOException;

@CucumberOptions(features = {"src/test/resources/features"}, glue = {"steps"})
public class StepDefinitions {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(StepDefinitions.class);
    private String username;
    private String password;

    @Before
    public void setUp() {
        // Set up WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Given("I open the browser")
    public void iOpenTheBrowser() throws IOException {
        logger.info("Navigating to the login page...");
        String filePath = "src/test/resources/Tdata.xlsx";
        String sheetName = "Sheet1";
        Object[][] userData = ExcelUtils.readUserData(filePath, sheetName);
        username = (String) userData[0][0];
        password = (String) userData[0][1];
        driver.get("https://www.amazon.in/");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        Actions actions = new Actions(driver);
        WebElement continuebutton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='nav-line-2 ' and text()='Account & Lists']")));
        continuebutton.click();
        driver.findElement(By.xpath("//input[@id='ap_email']")).sendKeys(username);
        logger.info("Passed userid..");
        driver.findElement(By.xpath("//span[@class='a-button-inner']")).click();
        logger.info("cliked on sign in button ..");
        driver.findElement(By.xpath("//input[@id='ap_password']")).sendKeys(password);
        logger.info("entered password..");
        WebElement SignInbutton= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='signInSubmit']")));
        SignInbutton.click();
        logger.info(" sign in successful ..");
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        driver.get(url);
    }

    @Then("I verify the page title")
    public void iVerifyThePageTitle() {
        String expectedTitle = "Amazon Sign In";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle);
        logger.info(" expected and actual domain titles matched  ..");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
