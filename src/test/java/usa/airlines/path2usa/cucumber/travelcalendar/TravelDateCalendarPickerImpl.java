package usa.airlines.path2usa.cucumber.travelcalendar;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import usa.airlines.path2usa.BaseTest;
import usa.airlines.path2usa.travelcalendar.page.HomePageCalendarTravelDate;

public class TravelDateCalendarPickerImpl extends BaseTest {

    private WebDriver driver;
    @After
    public void afterScenario() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I am on the Path2Usa page")
    public void i_landed_on_path2usa_page() {
        var browser = System.getProperty("BROWSER", BROWSER_CHROME);
        var isMobile = System.getProperty("IS_MOBILE", "false");
        var isFilterNetworkEnabled = System.getProperty("FILTER_NETWORK", "false");

        driver = createWebDriver(browser,Boolean.parseBoolean(isMobile), Boolean.parseBoolean(isFilterNetworkEnabled));
    }

    @Given("I see the travel calendar")
    public void spotted_travel_calendar() throws InterruptedException {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        calendarTravelDate.scrollToTheCalendarInput();
        Thread.sleep(1000);
        calendarTravelDate.waitForElementToBeClickable(calendarTravelDate.getTravelDateCalendarInput());
    }

    @When("I open the calendar")
    public void i_click_on_calendar() {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        calendarTravelDate.getTravelDateCalendarInput().click();
        calendarTravelDate.waitForElementToAppear(calendarTravelDate.getCalendar());
    }

    @And("^I select the travel date (\\d+) (.+) (\\d+)$")
    public void i_select_date_of_travel(int day, String month, int year) {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        selectDateOfTravelInCalendar(day, month, year, calendarTravelDate);
        calendarTravelDate.waitForElementToDisappear(calendarTravelDate.getCalendar());
    }

    @Then("The calendar should close")
    public void calendar_is_closed() {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        Assert.assertFalse(calendarTravelDate.getCalendar().isDisplayed(), "Calendar is still visible after selecting a date.");
    }

    @Then("Today's date should be highlighted")
    public void current_date_is_highlighted() {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);

        int todayInt = 0;
        try {
            todayInt = Integer.parseInt(calendarTravelDate.getHighlightedCurrentDate());
        } catch (NumberFormatException e) {
            Assert.fail("Error: The date is not a valid integer.");
        }

        Assert.assertEquals(todayInt, LocalDate.now().getDayOfMonth(), "Today's date is not highlighted in the calendar.");
    }

    @Then("Dates before today should be disabled")
    public void dates_before_today_are_disabled() {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);

        List<WebElement> listOfDatesBeforeTodayDate =
                calendarTravelDate.getListOfDatesBeforeTodayDate();

        boolean datesThatShouldBeDisabled = listOfDatesBeforeTodayDate.stream()
                .allMatch(date -> date.getAttribute("class").contains("disabled"));

        Assert.assertTrue(datesThatShouldBeDisabled, "Return dates before selected departure date are not disabled.");
    }

    @And("^The selected travel date (\\d+) (.+) (\\d+) should be displayed$")
    public void chosen_date_of_travel_is_displayed(int day, String month, int year) {
        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);

        String expectedDate = String.format("%d %s %d", day, month, year);
        String selectedDate = calendarTravelDate.getTravelDateCalendarInput().getAttribute("value");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate expectedLocalDate = LocalDate.parse(expectedDate, DateTimeFormatter.ofPattern("dd MMMM uuuu"));
        LocalDate selectedLocalDate = LocalDate.parse(selectedDate, formatter);

        Assert.assertEquals(
                selectedLocalDate,
                expectedLocalDate,
                "Selected date is not as expected."
        );
    }

    private void selectDateOfTravelInCalendar(int day, String month, int year, HomePageCalendarTravelDate calendarTravelDate) {
        calendarTravelDate.chooseYearOfTheTravel(year);
        calendarTravelDate.chooseMonthOfTheTravel(month);
        calendarTravelDate.chooseDayOfTheTravel(day);
    }
}