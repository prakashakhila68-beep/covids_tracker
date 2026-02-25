package utils;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropdownUtil {

    public static void selectByVisibleText(WebElement element, String text) {
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    public static List<WebElement> getAllOptions(WebElement element) {
        Select select = new Select(element);
        return select.getOptions();
    }

    public static String getSelectedOption(WebElement element) {
        Select select = new Select(element);
         System.out.println("Hi"); 
        
        return select.getFirstSelectedOption().getText();
    }
}