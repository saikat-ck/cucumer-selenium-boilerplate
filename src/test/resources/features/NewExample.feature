@exampleFeatureNew
Feature: New Example

  @searchNew
  Scenario: New Search with DuckDuckGo
    Given the site "<parameter1>" is open
    When I search for "<parameter2>"
    Then I can see the search results for "<parameter2>"
