package usa.airlines.path2usa.travelcalendar.page;

import java.time.Duration;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import usa.airlines.path2usa.util.AbstractComponent;

@Slf4j
public class HomePageCalendarTravelDate extends AbstractComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;
    @FindBy(css = "div[class='flatpickr-current-month'] span[class='cur-month']")
    private WebElement currentMonthElement;
    @FindBy(css = "span.flatpickr-next-month")
    private WebElement nextMonthArrowElement;
    @FindBy(css = "span.flatpickr-prev-month")
    private WebElement previousMonthArrowElement;
    @FindBy(css = "input[class*='cur-year']")
    private WebElement currentYearElement;
    @FindBy(css = "span.arrowUp")
    private WebElement nextYearArrowElement;
    @FindBy(css = "span.arrowDown")
    private WebElement previousYearArrowElement;
    @FindBy(css = "span.flatpickr-day:not(.prevMonthDay):not(.nextMonthDay)")
    private List<WebElement> daysOfTheMonth;
    @Getter
    @FindBy(id = "form-field-travel_comp_date")
    private WebElement travelDateCalendarInput;
    @Getter
    @FindBy(css = ".flatpickr-calendar")
    private WebElement calendar;
    @FindBy(css = ".flatpickr-day.today")
    private WebElement todayDate;
    @FindAll({
            @FindBy(css = "div[class='dayContainer'] span")
    })
    private List<WebElement> listOfDays;


    public HomePageCalendarTravelDate(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void chooseMonthOfTheTravel(String month) {
        Month desiredMonth = Month.valueOf(month.toUpperCase());

        boolean monthFound = false;
        while (!monthFound) {
            wait.until(ExpectedConditions.visibilityOf(currentMonthElement));
            var currentMonth = Month.valueOf(currentMonthElement.getText().toUpperCase());

            if (currentMonth == desiredMonth) {
                monthFound = true;
            } else {
                int numberOfClicks;
                WebElement arrowSelector;

                if (currentMonth.getValue() < desiredMonth.getValue()) {
                    numberOfClicks = desiredMonth.getValue() - currentMonth.getValue();
                    arrowSelector = nextMonthArrowElement;
                } else {
                    numberOfClicks = currentMonth.getValue() - desiredMonth.getValue();
                    arrowSelector = previousMonthArrowElement;
                }

                for (int i = 0; i < numberOfClicks; i++) {
                    arrowSelector.click();
                }
            }

        }
    }

    public void chooseYearOfTheTravel(int year) {
        Year desiredYear = Year.of(year);

        boolean yearFound = false;
        while (!yearFound) {
            wait.until(ExpectedConditions.visibilityOf(currentYearElement));
            JavascriptExecutor yearField = (JavascriptExecutor) driver;
            String yearValue = (String) yearField.executeScript("return arguments[0].value;", currentYearElement);

            int currentYear = Integer.parseInt(yearValue);

            if (currentYear == desiredYear.getValue()) {
                yearFound = true;
            } else {
                int numberOfClicks;
                WebElement arrowSelector;

                if (currentYear < desiredYear.getValue()) {
                    numberOfClicks = desiredYear.getValue() - currentYear;
                    arrowSelector = nextYearArrowElement;
                } else {
                    numberOfClicks = currentYear - desiredYear.getValue();
                    arrowSelector = previousYearArrowElement;
                }

                for (int i = 0; i < numberOfClicks; i++) {
                    arrowSelector.click();
                }
            }
        }
    }

    public void chooseDayOfTheTravel(int desiredDay) {
        Optional<WebElement> dayToBeSelected = daysOfTheMonth.stream().filter(day -> Integer.parseInt(day.getText()) == desiredDay).findFirst();

        dayToBeSelected.ifPresent(WebElement::click);
    }

    public WebElement getTravelDateCalendarInput() {
        return travelDateCalendarInput;
    }

    public WebElement getCalendar() {
        return calendar;
    }

    public String getHighlightedCurrentDate() {
        return todayDate.getText();
    }

    public List<WebElement> getListOfDatesBeforeTodayDate() {
        return listOfDays.stream()
                .filter(date -> date.getAttribute("class").contains("prevMonthDay"))
                .toList();
    }

    public void scrollToTheCalendarInput() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", getTravelDateCalendarInput());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}