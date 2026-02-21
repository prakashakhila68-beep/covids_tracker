package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import utils.DropdownUtil;

public class HomePage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(css = "select.data-filter-input")
    private WebElement stateDropdown;

    @FindBy(xpath = "//g[contains(@class,'scatter')]//g[@class='points']//*[name()='circle']")
    private List<WebElement> linePoints;

    @FindBy(xpath = "//strong[text()='Total Cases :']/parent::p")
    private WebElement totalCasesCard;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Dropdown handled via Util
    public void selectState(String stateName) {
        wait.until(ExpectedConditions.visibilityOf(stateDropdown));
        DropdownUtil.selectByVisibleText(stateDropdown, stateName);
    }

    public List<WebElement> getAllStateOptions() {
        return DropdownUtil.getAllOptions(stateDropdown);
    }

    public String getSelectedState() {
        return DropdownUtil.getSelectedOption(stateDropdown);
    }

    // Card value
    public String getTotalCasesValue() {
        wait.until(ExpectedConditions.visibilityOf(totalCasesCard));
        wait.until(d -> !totalCasesCard.getText().trim().isEmpty());
        return totalCasesCard.getText().replaceAll("[^0-9]", "");
    }

    // Chart
   
    	public List<WebElement> getLineChartPoints() {

    	    // Scroll to chart
    	    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,600)");

    	    By pointsLocator = By.xpath(
    	        "//g[contains(@class,'scatter')]//g[@class='points']//*[name()='circle']"
    	    );

    	    // Wait until at least one circle is present
    	    wait.until(ExpectedConditions.presenceOfElementLocated(pointsLocator));

    	    return driver.findElements(pointsLocator);
    	}


    

    public String getTooltipValue() {
        WebElement tooltip = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("hovertext")));
        return tooltip.getText().replaceAll("[^0-9]", "");
    }
}