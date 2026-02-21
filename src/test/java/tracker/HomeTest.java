package tracker;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;

import java.time.Duration;
import java.util.List;

public class HomeTest extends BaseTest {

    @Test
    public void validateAllStatesData() {

        HomePage page = new HomePage(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> options = page.getAllStateOptions();

        for (WebElement option : options) {

            String stateName = option.getText();

            if (!stateName.equals("Select a State")) {

                page.selectState(stateName);
               
                wait.until(d -> page.getSelectedState().equals(stateName));


                String selectedValue = page.getSelectedState();

                Assert.assertEquals(selectedValue, stateName);
            }
        }
    }

    @Test
    public void validateChartWithCardValues() {

        HomePage page = new HomePage(driver);

        page.selectState("Bihar");

        String cardValue = page.getTotalCasesValue();

        List<WebElement> points = page.getLineChartPoints();
        Actions actions = new Actions(driver);

        String lastChartValue = "";

        for (WebElement point : points) {

            actions.moveToElement(point).perform();

            lastChartValue = page.getTooltipValue();
        }

        Assert.assertEquals(lastChartValue, cardValue);
    }
}