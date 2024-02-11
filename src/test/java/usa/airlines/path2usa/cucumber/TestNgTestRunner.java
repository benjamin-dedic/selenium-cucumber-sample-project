package usa.airlines.path2usa.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/cucumber",
        glue = "usa.airlines.path2usa.cucumber.travelcalendar",
        monochrome = true,
        plugin = {"json:reports/cucumber/departureCalendarDatePicker.json", "html:reports/cucumber/departureCalendarDatePicker.html"}
)
public class TestNgTestRunner extends AbstractTestNGCucumberTests {

}
