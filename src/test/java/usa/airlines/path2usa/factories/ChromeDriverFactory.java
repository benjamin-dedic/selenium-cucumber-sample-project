package usa.airlines.path2usa.factories;

import com.google.common.collect.ImmutableList;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v117.network.Network;
import org.openqa.selenium.devtools.v85.emulation.Emulation;
import usa.airlines.path2usa.BaseTest;

import java.time.Duration;
import java.util.Optional;

public class ChromeDriverFactory {

    public static WebDriver createWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(BaseTest.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(BaseTest.IMPLICIT_WAIT_TIMEOUT));
        driver.manage().window().maximize();
        return driver;
    }

    public static WebDriver createMobileWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(
                Emulation.setDeviceMetricsOverride(400, 800, 50, true, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                        Optional.empty()));
        driver.get(BaseTest.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(BaseTest.IMPLICIT_WAIT_TIMEOUT));
        return driver;
    }

    public static WebDriver createWebDriverWithDevTools() {
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*.jpg", "*.svg", "*.png")));
        driver.get(BaseTest.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(BaseTest.IMPLICIT_WAIT_TIMEOUT));
        driver.manage().window().maximize();
        return driver;
    }
}