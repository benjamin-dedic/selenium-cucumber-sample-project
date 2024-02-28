package usa.airlines.path2usa.travelcalendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import usa.airlines.path2usa.BaseTest;
import usa.airlines.path2usa.travelcalendar.page.HomePageCalendarTravelDate;
import usa.airlines.path2usa.util.Retry;
import usa.airlines.path2usa.util.TestDataLoader;

@Slf4j
public class CalendarTravelDatePickerTest extends BaseTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        var browser = System.getProperty("BROWSER", BROWSER_CHROME);
        var isMobile = System.getProperty("IS_MOBILE", "false");
        var isFilterNetworkEnabled = System.getProperty("FILTER_NETWORK", "false");

        driver = createWebDriver(browser, Boolean.parseBoolean(isMobile), Boolean.parseBoolean(isFilterNetworkEnabled));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void acceptTerms() {
        try {
            HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
            calendarTravelDate.waitForElementToAppear(By.className("fc-consent-root"));

            WebElement consentTitle = driver.findElement(By.className("fc-dialog-headline"));
            Assert.assertTrue(consentTitle.isDisplayed(), "Consent message is not displayed.");
            driver.findElement(By.cssSelector("button[class*='fc-button']")).click();
        } catch (
                TimeoutException e) {
            log.error("Consent message did not appear within the specified time. Skipping the test.");
            throw new SkipException("Skipping test due to missing consent message");
        }
    }

    @Test
    public void verifyTodayDateIsHighlightedInCalendar() throws InterruptedException {

        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        calendarTravelDate.scrollToTheCalendarInput();
        Thread.sleep(1000);
        calendarTravelDate.waitForElementToBeClickable(calendarTravelDate.getTravelDateCalendarInput());
        calendarTravelDate.getTravelDateCalendarInput().click();

        int todayInt = 0;
        try {
            todayInt = Integer.parseInt(calendarTravelDate.getHighlightedCurrentDate());
        } catch (NumberFormatException e) {
            Assert.fail("Error: The date is not a valid integer.");
        }

        Assert.assertEquals(todayInt, LocalDate.now().getDayOfMonth(), "Today's date is not highlighted in the calendar.");
    }

    @Test(retryAnalyzer = Retry.class)
    public void verifyDepartureDatesAreDisabledBeforeTodayDate() throws InterruptedException {

        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        calendarTravelDate.scrollToTheCalendarInput();
        Thread.sleep(1000);
        calendarTravelDate.waitForElementToBeClickable(calendarTravelDate.getTravelDateCalendarInput());
        calendarTravelDate.getTravelDateCalendarInput().click();
        calendarTravelDate.waitForElementToAppear(calendarTravelDate.getCalendar());

        List<WebElement> listOfDatesBeforeTodayDate =
                calendarTravelDate.getListOfDatesBeforeTodayDate();

        boolean datesThatShouldBeDisabled = listOfDatesBeforeTodayDate.stream()
                .allMatch(date -> date.getAttribute("class").contains("disabled"));

        Assert.assertTrue(datesThatShouldBeDisabled, "Return dates before selected departure date are not disabled.");
    }

    @Test(dataProvider = "dateProvider")
    public void selectDateOfTravel(int day, String month, int year) throws InterruptedException {
        log.info("Desired date: {}-{}-{}", day, month, year);

        HomePageCalendarTravelDate calendarTravelDate = new HomePageCalendarTravelDate(driver);
        calendarTravelDate.scrollToTheCalendarInput();
        Thread.sleep(1000);
        calendarTravelDate.waitForElementToBeClickable(calendarTravelDate.getTravelDateCalendarInput());
        calendarTravelDate.getTravelDateCalendarInput().click();
        calendarTravelDate.waitForElementToAppear(calendarTravelDate.getCalendar());

        selectDateOfTravelInCalendar(day, month, year, calendarTravelDate);

        calendarTravelDate.waitForElementToDisappear(calendarTravelDate.getCalendar());
        Assert.assertFalse(calendarTravelDate.getCalendar().isDisplayed(), "Calendar is still visible after selecting a date.");

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

    private void selectDateOfTravelInCalendar(int day, String month, int year, HomePageCalendarTravelDate calendarTravelDate){
        calendarTravelDate.chooseYearOfTheTravel(year);
        calendarTravelDate.chooseMonthOfTheTravel(month);
        calendarTravelDate.chooseDayOfTheTravel(day);
    }

    @DataProvider(name = "dateProvider")
    public Object[][] getTravelDatesData() {
        return TestDataLoader.getTestData();
    }
}
