package utils;
import java.io.File;
import utils.Screenshot;
	import java.io.IOException;
	import java.text.SimpleDateFormat;
	import java.util.Date;

	import org.apache.commons.io.FileUtils;
	import org.openqa.selenium.OutputType;
	import org.openqa.selenium.TakesScreenshot;
	import org.openqa.selenium.WebDriver;

	public class Screenshot {
	    public static String captureScreenshot(WebDriver driver, String testName) {

	        // Create timestamp
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

	        // Screenshot file name
	        String fileName = testName + "_" + timeStamp + ".png";

	        // Path
	        String path = System.getProperty("user.dir") + "/screenshots/" + fileName;

	        // Take screenshot
	        TakesScreenshot ts = (TakesScreenshot) driver;
	        File source = ts.getScreenshotAs(OutputType.FILE);

	        try {
	            FileUtils.copyFile(source, new File(path));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return path;
	    }
	}

