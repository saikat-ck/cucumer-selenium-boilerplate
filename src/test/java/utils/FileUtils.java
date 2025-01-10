package utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCase;

public class FileUtils {

    public void addScreenshot(int currentStepIndex, Scenario scenario, WebDriver driver)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
            IOException {
        Field f1 = null;
        f1 = scenario.getClass().getDeclaredField("delegate");
        f1.setAccessible(true);
        TestCaseState tcState = null;
        tcState = (TestCaseState) f1.get(scenario);
        f1.setAccessible(true);

        Field f2 = null;
        f2 = tcState.getClass().getDeclaredField("testCase");
        f2.setAccessible(true);

        TestCase r = null;
        r = (TestCase) f2.get(tcState);

        List<PickleStepTestStep> stepDefs = r.getTestSteps()
                .stream()
                .filter(PickleStepTestStep.class::isInstance)
                .map(PickleStepTestStep.class::cast)
                .toList();

        PickleStepTestStep currentStepDef = stepDefs.get(currentStepIndex);
        String testCase = currentStepDef.getStep().getText();

        String nameScreenshot = testCase;
        String targetPath = new File("boilerplate/output/" + nameScreenshot + ".png").getCanonicalPath();

        // If scenario fails, add a screenshot to the report
        try {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", nameScreenshot); // Updated method call
            try {
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                org.apache.commons.io.FileUtils.copyFile(screenshotFile, new File(targetPath));
            } catch (IOException e) {
                System.out.println("Error Error Error");
            }
        } catch (WebDriverException somePlatformsDontSupportScreenshots) {
            System.err.println(somePlatformsDontSupportScreenshots.getMessage());
        }
    }

    public String createScreenshotFileName() {

        final StringBuilder sb = new StringBuilder();
        final DateFormat dateformat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        final java.lang.String timestamp = dateformat.format(new Date());
        sb.append(timestamp).append(".png");
        return sb.toString();

    }
}
