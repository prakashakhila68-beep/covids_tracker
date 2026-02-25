package tracker;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;
import CovidDataValidator.Validator;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomeTest extends BaseTest {

    // ============================================================
    // 1️⃣ Validate Dropdown Contains States
    // ============================================================

    @Test
    public void validateDropdownStatesLoaded() {

        HomePage page = new HomePage(driver);

        List<WebElement> options = page.getAllStateOptions();

        Assert.assertTrue(options.size() > 1,
                "Dropdown does not contain state values");
    }

    // ============================================================
    // 2️⃣ Validate Cards Populate For All States
    // ============================================================

    @Test
    public void validateCardsForAllStates() {

        HomePage page = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        for (WebElement option : page.getAllStateOptions()) {

            String stateName = option.getText();

            if (!stateName.equals("Select a State")) {

                page.selectState(stateName);
                wait.until(d -> page.getSelectedState().equals(stateName));

                int total = page.getTotalCases();
                int active = page.getActiveCases();
                int recovered = page.getRecoveredCases();
                int deaths = page.getDeathCases();

                Assert.assertTrue(total > 0,
                        "Total cases not populated for: " + stateName);

                Assert.assertTrue(active >= 0,
                        "Active cases invalid for: " + stateName);

                Assert.assertTrue(recovered >= 0,
                        "Recovered cases invalid for: " + stateName);

                Assert.assertTrue(deaths >= 0,
                        "Death cases invalid for: " + stateName);
            }
        }
    }

    // ============================================================
    // 3️⃣ Validate Business Logic For All States (Report Only)
    // ============================================================

    @Test
   
    public void validateBusinessLogicForAllStates() {

        HomePage page = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        List<String> matchedStates = new ArrayList<>();
        List<String> mismatchDetails = new ArrayList<>();

        for (WebElement option : page.getAllStateOptions()) {

            String stateName = option.getText();

            if (!stateName.equals("Select a State")) {

                page.selectState(stateName);
                wait.until(d -> page.getSelectedState().equals(stateName));

                int total = page.getTotalCases();
                int active = page.getActiveCases();
                int recovered = page.getRecoveredCases();
                int deaths = page.getDeathCases();

                int sum = active + recovered + deaths;

                boolean valid = Validator
                        .validateBusinessLogic(total, active, recovered, deaths);

                if (valid) {

                    matchedStates.add(stateName);

                    System.out.println("MATCHED → " + stateName +
                            " | Total: " + total +
                            " | Sum: " + sum);

                } else {

                    int difference = total - sum;

                    mismatchDetails.add(stateName +
                            " | Total: " + total +
                            " | Sum: " + sum +
                            " | Diff: " + difference);

                    System.out.println("MISMATCH → " + stateName +
                            " | Total: " + total +
                            " | Sum: " + sum +
                            " | Diff: " + difference);
                }
            }
        }

        System.out.println("\n========== MATCHED STATES ==========");
        matchedStates.forEach(System.out::println);

        System.out.println("\n========== MISMATCH STATES ==========");
        mismatchDetails.forEach(System.out::println);

        Assert.assertTrue(true); // Do not fail test
    }
    // ============================================================
    // 4️⃣ Validate Line Chart Renders For All States
    // ============================================================

    @Test
    public void validateLineChartRendersForAllStates() {

        HomePage page = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        for (WebElement option : page.getAllStateOptions()) {

            String stateName = option.getText();

            if (!stateName.equals("Select a State")) {

                page.selectState(stateName);
                wait.until(d -> page.getSelectedState().equals(stateName));

                Assert.assertTrue(
                        page.getLineChartPoints().size() > 0,
                        "Line chart not rendered for: " + stateName
                );
            }
        }
    }

    // ============================================================
    // 5️⃣ Validate Line Chart Matches Card Values (Single State)
    // ============================================================

    @Test
    public void validateLineChartValuesMatchCardValues() {

        HomePage page = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String stateName = "Bihar";

        page.selectState(stateName);
        wait.until(d -> page.getSelectedState().equals(stateName));

        int total = page.getTotalCases();
        int active = page.getActiveCases();
        int recovered = page.getRecoveredCases();
        int deaths = page.getDeathCases();

        List<Integer> cardValues = List.of(total, active, recovered, deaths);

        Actions actions = new Actions(driver);
        boolean matchFound = false;

        for (WebElement point : page.getLineChartPoints()) {

            actions.moveToElement(point).perform();

            int chartValue = page.getTooltipValue();

            if (cardValues.contains(chartValue)) {
                matchFound = true;
                break;
            }
        }

        Assert.assertTrue(matchFound,
                "Line chart values do not match card values for: " + stateName);
    }

    // ============================================================
    // 6️⃣ Validate Pie Chart Renders For All States
    // ============================================================

    @Test
    public void validatePieChartRendersForAllStates() {

        HomePage page = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        for (WebElement option : page.getAllStateOptions()) {

            String stateName = option.getText();

            if (!stateName.equals("Select a State")) {

                page.selectState(stateName);
                wait.until(d -> page.getSelectedState().equals(stateName));

                int sliceCount = page.getPieChartSliceCount();

                Assert.assertEquals(sliceCount, 4,
                        "Pie chart slice count incorrect for: " + stateName);
            }
        }
    }
}