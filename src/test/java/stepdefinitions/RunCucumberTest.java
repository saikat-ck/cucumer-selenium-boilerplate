package stepdefinitions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber-reports.html",
        "json:target/cucumber-reports.json" }, features = {
                "classpath:features/" }, publish = true, tags = "@searchNew")

public class RunCucumberTest {
}

// to run tests with a tag:
// mvn clean test -Dcucumber.filter.tags="@exampleFeature"
