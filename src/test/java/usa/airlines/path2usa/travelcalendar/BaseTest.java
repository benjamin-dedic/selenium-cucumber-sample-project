package usa.airlines.path2usa.travelcalendar;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.emulation.Emulation;

@RequiredArgsConstructor
public class BaseTest {

    public final static String BASE_URL = "https://www.path2usa.com/travel-companion/";
    public static final String BROWSER_CHROME = "chrome";
    public static final String HEADLESS = "headless";

    public static WebDriver createWebDriver(String browser, String baseURL, int implicitWaitTimeout, boolean isMobile, boolean isHeadless) {
        WebDriver driver = null;
        if (isMobile) {
            if (browser.equalsIgnoreCase(BROWSER_CHROME)) {
                driver = initializeMobileChromeDriver(baseURL, implicitWaitTimeout);
            } else {
                throw new IllegalArgumentException("Mobile browser type not supported: " + browser);
            }
        } else {
            if (browser.equalsIgnoreCase(BROWSER_CHROME)) {
                driver = initializeChromeDriver(baseURL, implicitWaitTimeout, isHeadless);
            }
        }
        return driver;
    }

    private static WebDriver initializeChromeDriver(String baseURL, int implicitWaitTimeout, boolean isHeadless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        if (isHeadless) {
            chromeOptions.addArguments(HEADLESS);
        }
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(baseURL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitTimeout));
        driver.manage().window().maximize();
        return driver;
    }

    private static WebDriver initializeMobileChromeDriver(String baseURL, int implicitWaitTimeout) {
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        devTools.send(
                Emulation.setDeviceMetricsOverride(400, 800, 50, true, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                        Optional.empty()));

        driver.get(baseURL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitTimeout));
        return driver;
    }

    public static String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
        FileUtils.copyFile(source, file);
        return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";

    }
}
