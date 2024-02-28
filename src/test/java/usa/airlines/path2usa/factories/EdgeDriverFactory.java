package usa.airlines.path2usa.factories;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import usa.airlines.path2usa.BaseTest;

import java.time.Duration;

public class EdgeDriverFactory {

    public static WebDriver createWebDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions edgeOptions = new EdgeOptions();
        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.get(BaseTest.BASE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(BaseTest.IMPLICIT_WAIT_TIMEOUT));
        driver.manage().window().maximize();
        return driver;
    }
}