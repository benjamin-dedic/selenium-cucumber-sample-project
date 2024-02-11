Feature: Calendar Date Picker for Departure Flight

  Background:
    Given I am on the Path2Usa page

  Scenario: Highlight Current Date in Calendar
    Given I see the travel calendar
    When I open the calendar
    Then Today's date should be highlighted

  Scenario: Disable Dates Before Today in Calendar
    Given I see the travel calendar
    When I open the calendar
    Then Dates before today should be disabled

  Scenario Outline: Select Travel Date from Calendar
    Given I see the travel calendar
    When I open the calendar
    And I select the travel date <day> <month> <year>
    Then The calendar should close
    And The selected travel date <day> <month> <year> should be displayed

    Examples:
      | day | month  | year |
      | 14  | June   | 2025 |
      | 26  | April  | 2029 |
