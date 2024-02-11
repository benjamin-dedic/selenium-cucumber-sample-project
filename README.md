# Selenium Cucumber Sample Project

This is a sample Selenium-Cucumber project for automating calendar-related tests on the Path2Usa website.

## Triggering Tests

### Global Parameters

You can customize the test execution by passing global parameters while triggering tests using Maven. Here are the available parameters:

- `BROWSER`: Specifies the browser to use for testing (default: `chrome`).
- `IMPLICIT_WAIT_TIMEOUT`: Specifies the implicit wait timeout in seconds (default: `10`).
- `IS_MOBILE`: Specifies whether to run tests on a mobile device (`true` or `false`, default: `false`).
- `IS_HEADLESS`: Specifies whether to run the browser in headless mode (`true` or `false`, default: `false`).

### Maven Profiles

The following Maven profiles are available for test execution:

- `cucumber-regression-tests`: Executes Cucumber regression tests.
- `selenium-regression-tests`: Executes Selenium regression tests.

### Test Execution in Parallel

Tests under 'selenium-regression-tests' profile are executed in parallel to optimize test execution time.

### Usage

To execute tests with a specific Maven profile and override default parameter values, use the following syntax:

```bash
mvn clean test -Pselenium-regression-tests -DBROWSER=chrome -DIS_MOBILE=false
mvn clean test -Pcucumber-regression-tests
```

## Project Structure

- **src/main**: Contains main source code (not included in this project).
- **src/test**: Contains test source code.
    - **java/usa/airlines/path2usa/travelcalendar**: Java classes related to calendar tests.
    - **java/usa/airlines/path2usa/cucumber/calendar**: Java classes related to Cucumber calendar tests.
    - **java/usa/airlines/path2usa/util**: Utility classes used across the project.
    - **resources**: Contains test resources including feature files and TestNG XML configurations.
        - **cucumber**: Contains feature files for Cucumber tests.
            - **DepartureFlightDatePicker.feature**: Feature file defining scenarios for the departure flight date picker.
        - **testSuites**: Contains TestNG XML configuration files for running test suites.
            - **travel-calendar-date-picker.xml**: TestNG XML configuration file for the travel calendar date picker tests.

## Test Classes

### TestNG Tests (Selenium)

- **CalendarTravelDatePickerTest**: Contains TestNG test methods related to calendar date selection and validation.

### Cucumber Tests

- **TestNgTestRunner**: TestNG runner class for executing Cucumber tests.

#### Step Definitions

- **TravelDateCalendarPickerImpl**: Step definition class for Cucumber scenarios related to calendar date selection.

## Utility Classes

### AbstractComponent

- Contains reusable methods for handling web elements such as waiting for elements to appear, disappear, or become clickable.

### ExtentReportNG

- Provides methods for creating ExtentReports for test reporting.

### Listeners

- Implements TestNG listeners for handling test execution events like test start, test success, test failure, etc. It also captures screenshots on test failure.

### Retry

- Implements TestNG's IRetryAnalyzer interface for retrying failed tests.

### TestDataLoader

- Loads test data from a JSON file for parameterized testing.

## Usage

1. **AbstractComponent**: Extend this class in your page object classes to leverage the provided methods for element interactions.

2. **ExtentReportNG**: Use this class to configure and generate ExtentReports for test reporting.

3. **Listeners**: Ensure that this class is registered as a TestNG listener to capture test execution events and handle test failures.

4. **Retry**: Implement this class in your TestNG test classes to enable test retry functionality.

5. **TestData [calendar-test-data.json](src%2Ftest%2Fresources%2Fcalendar-test-data.json)taLoader**: Use this class to load test data from a JSON file for parameterized testing.

## Author

Benjamin Dedic  
Email: benja.dedic@gmail.com