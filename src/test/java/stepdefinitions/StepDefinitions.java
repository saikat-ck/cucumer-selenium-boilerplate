package stepdefinitions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobjects.AutomationStore;
import pageobjects.Homepage;
import pageobjects.Searchresults;
import utils.FileUtils;
import utils.Validations;
import utils.Waits;

public class StepDefinitions {

    private WebDriver driver;
    int currentStepIndex = 0;
    Properties prop;

    public void startDriver(String url) {

        driver = new ChromeDriver();
        driver.get(url);
        driver.manage().window().maximize();

    }

    @Before
    public void initializeConfiguration() throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream(new File("src/test/resources/userdata/config.properties")));
    }

    @AfterStep
    public void aterStepActions(Scenario scenario) throws IOException,
            NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        FileUtils fileUtils = new FileUtils();
        fileUtils.addScreenshot(currentStepIndex, scenario, driver);
        currentStepIndex += 1;
    }

    @After
    public void tearDown(Scenario scenario) {
        // if (scenario.isFailed()) {
        // FileUtils fileUtils = new FileUtils();
        // fileUtils.addScreenshot(currentStepIndex, scenario, driver);
        // }

        driver.quit();

    }

    @Given("^the site \"(.+)\" is open$")
    public void theSiteDuckDuckGoIsOpen(String site) {
        String url = "";

        switch (site.toLowerCase()) {
            case "duckduckgo":
                url = "https://duckduckgo.com/";
                break;
            case "automation practise store":
                url = "http://www.automationpractice.pl/index.php";
                break;
            case "<parameter1>":
                if (prop.getProperty("parameter1").equalsIgnoreCase("duckduckgo")) {
                    url = "https://duckduckgo.com/";
                }
                break;
            default:
                Assert.fail(
                        "Something is wrong. The website '" + site + "' you are trying to open in not recognised. ");
        }

        startDriver(url);
        Waits waits = new Waits(driver);

        switch (site.toLowerCase()) {
            case "duckduckgo":
                Homepage homepage = new Homepage(driver);
                waits.waitForElement(homepage.logoHomepage);
                break;
            case "automation practise store":
                AutomationStore automationStore = new AutomationStore(driver);
                waits.waitForElement(automationStore.logoStore);
                break;
            default:
                System.out.println(
                        "Something is wrong. The website '" + site + "' you are trying to open in not recognised. ");
        }
    }

    @Then("the searchbar is enabled")
    public void theSearchbarIsEnabled() {

        Waits waits = new Waits(driver);
        Homepage homepage = new Homepage(driver);

        waits.waitForElement(homepage.searchBar);
        Assert.assertTrue(homepage.searchBar.isEnabled());

    }

    @When("^I search for \"(.+)\"$")
    public void iSearchFor(String searchObject) {

        Homepage homepage = new Homepage(driver);
        if (searchObject.matches("<parameter2>")) {
            searchObject = prop.getProperty("parameter2");
        }
        homepage.searchBar.sendKeys(searchObject);
        homepage.searchButton.click();

    }

    @Then("^I can see the search results for \"(.+)\"$")
    public void iCanSeeTheSearchResultsFor(String searchObject) {

        Waits waits = new Waits(driver);
        Searchresults searchresults = new Searchresults(driver);

        waits.waitForElement(searchresults.sidebarTitle);
        if (searchObject.matches("<parameter2>")) {
            searchObject = prop.getProperty("parameter2");
        }
        Assert.assertEquals(searchObject, searchresults.sidebarTitle.getText().toLowerCase());

    }

    @When("I open the {string} menu")
    public void iOpenTheMenu(String menu) {
        Waits waits = new Waits(driver);
        AutomationStore automationStore = new AutomationStore(driver);

        waits.waitForElement(automationStore.menuCategoryWomen);
        Actions hoover = new Actions(driver);
        hoover.moveToElement(automationStore.menuCategoryWomen).build().perform();
    }

    @Then("I can see the menu items:")
    public void iCanSeeTheMenuItems(List<String> lijstExpected) {
        Waits waits = new Waits(driver);
        Validations validations = new Validations(driver);
        AutomationStore automationStore = new AutomationStore(driver);

        waits.waitForElements(automationStore.dropdownItemsCategoryWomen);
        validations.compareLists(automationStore.dropdownItemsCategoryWomen, lijstExpected);

    }
}
