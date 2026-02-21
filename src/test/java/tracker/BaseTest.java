package tracker;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.PropertyReader;
import utils.Screenshot;


public class BaseTest {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    // ---------------- Browser Setup ----------------

    public WebDriver initializeBrowser() throws IOException {

        String browser = PropertyReader.getGlobalProperty("browser");

        switch (browser.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            default:
                throw new RuntimeException("Invalid Browser");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(PropertyReader.getGlobalProperty("inergurl"));

        return driver;
    }

    // ---------------- Report Setup ----------------

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeClass
    public void setUp() throws IOException {
        driver = initializeBrowser();
    }

    @BeforeMethod
    public void createTest(Method method) {
        test = extent.createTest(method.getName());
    }

    // ---------------- Screenshot + Logging ----------------

    @AfterMethod
    public void captureResult(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {
            String path = Screenshot.captureScreenshot(driver, result.getName());
            test.fail(result.getThrowable());
            test.addScreenCaptureFromPath(path);
        }

        if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void flushReport() {
        extent.flush();
    }
}