package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import utils.DropdownUtil;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ================= CONSTRUCTOR =================

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ================= DROPDOWN =================

    @FindBy(css = "select.data-filter-input")
    private WebElement stateDropdown;

    public void selectState(String stateName) {
        wait.until(ExpectedConditions.visibilityOf(stateDropdown));
        DropdownUtil.selectByVisibleText(stateDropdown, stateName);

        // Wait until cards refresh after selection
        wait.until(ExpectedConditions.visibilityOf(totalCasesCard));
    }

    public List<WebElement> getAllStateOptions() {
        return DropdownUtil.getAllOptions(stateDropdown);
    }

    public String getSelectedState() {
        return DropdownUtil.getSelectedOption(stateDropdown);
    }

    // ================= CARDS =================

    @FindBy(xpath = "//p[strong[normalize-space()='Total Cases :']]")
    private WebElement totalCasesCard;

    @FindBy(xpath = "//p[strong[normalize-space()='Active Cases :']]")
    private WebElement activeCasesCard;

    @FindBy(xpath = "//p[strong[normalize-space()='Recovered :']]")
    private WebElement recoveredCard;

    @FindBy(xpath = "//p[strong[normalize-space()='Deaths :']]")
    private WebElement deathsCard;

    private int extractNumericValue(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(d -> !element.getText().trim().isEmpty());

        String number = element.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(number);
    }

    public int getTotalCases() {
        return extractNumericValue(totalCasesCard);
    }

    public int getActiveCases() {
        return extractNumericValue(activeCasesCard);
    }

    public int getRecoveredCases() {
        return extractNumericValue(recoveredCard);
    }

    public int getDeathCases() {
        return extractNumericValue(deathsCard);
    }

    // ================= LINE CHART (FIXED) =================

    public List<WebElement> getLineChartPoints() {

        // Wait for chart container
        By chartLocator = By.className("js-plotly-plot");

        WebElement chart = wait.until(
                ExpectedConditions.visibilityOfElementLocated(chartLocator)
        );

        // Scroll chart into view
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", chart);

        /*
         FIX:
         Earlier you used only <circle>
         Plotly sometimes renders markers as <path>
         So now we accept BOTH circle and path
        */

        By pointLocator = By.xpath(
                "//*[name()='g' and contains(@class,'scatter')]//*[name()='circle' or name()='path']"
        );

        // Wait until points are present
        wait.until(driver ->
                driver.findElements(pointLocator).size() > 0
        );

        return driver.findElements(pointLocator);
    }

    // ================= TOOLTIP =================

    public int getTooltipValue() {

        WebElement tooltip = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("hovertext"))
        );

        String number = tooltip.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(number);
    }

    // ================= PIE CHART =================

    public int getPieChartSliceCount() {

        By pieChartLocator = By.xpath(
                "(//div[contains(@class,'js-plotly-plot')])[1]"
        );

        WebElement pieChart = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pieChartLocator)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", pieChart);

        By sliceLocator = By.xpath(
                "//*[name()='g' and contains(@class,'slice')]"
        );

        wait.until(driver ->
                driver.findElements(sliceLocator).size() > 0
        );

        return driver.findElements(sliceLocator).size();
    }
}