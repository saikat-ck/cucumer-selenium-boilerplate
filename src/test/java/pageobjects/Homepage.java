package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Homepage {

    public Homepage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div.is-not-mobile-device>div:nth-of-type(2) [title='Learn about DuckDuckGo']")
    public WebElement logoHomepage;

    @FindBy(css = "#searchbox_input")
    public WebElement searchBar;

    @FindBy(css = "[aria-label='Search']")
    public WebElement searchButton;

}
