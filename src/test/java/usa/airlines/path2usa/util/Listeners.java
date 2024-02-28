package usa.airlines.path2usa.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import java.io.IOException;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import usa.airlines.path2usa.BaseTest;

@Slf4j
public class Listeners extends BaseTest implements ITestListener {

    private WebDriver driver;
    ExtentTest test;
    ExtentReports extentReports = ExtentReportNG.getReportObject();
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        test = extentReports.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());
        String filePath = null;
        try {
            Field driverField
                    = result.getTestClass().getRealClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            driver= (WebDriver)driverField.get(result.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (driver != null) {
                filePath = getScreenshot(result.getMethod().getMethodName(), driver);
            } else {
                log.info("Driver is null. Cannot capture screenshot.");
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        test.addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
    }
}
