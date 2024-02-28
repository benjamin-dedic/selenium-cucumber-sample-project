package usa.airlines.path2usa;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import usa.airlines.path2usa.factories.ChromeDriverFactory;
import usa.airlines.path2usa.factories.EdgeDriverFactory;

@RequiredArgsConstructor
public class BaseTest {

    public final static String BASE_URL = "https://www.path2usa.com/travel-companion/";
    public final static int IMPLICIT_WAIT_TIMEOUT = 10;
    public static final String BROWSER_CHROME = "chrome";
    public static final String BROWSER_EDGE = "edge";

    public static WebDriver createWebDriver(String browser, boolean isMobile, boolean filterNetwork) {
        if (isMobile) {
            if (browser.equalsIgnoreCase(BaseTest.BROWSER_CHROME)) {
                return ChromeDriverFactory.createMobileWebDriver();
            } else {
                throw new IllegalArgumentException("Unsupported mobile browser: " + browser);
            }
        } else {
            if (browser.equalsIgnoreCase(BaseTest.BROWSER_CHROME)) {
                if (filterNetwork) {
                    return ChromeDriverFactory.createWebDriverWithDevTools();
                } else {
                    return ChromeDriverFactory.createWebDriver();
                }
            } else if (browser.equalsIgnoreCase(BaseTest.BROWSER_EDGE)) {
                if (filterNetwork) {
                    throw new IllegalArgumentException("Filtering network unsupported for browser: " + browser);
                }
                return EdgeDriverFactory.createWebDriver();
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }
    }

    public static String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
        FileUtils.copyFile(source, file);
        return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";

    }
}
