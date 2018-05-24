Feature: Using the registration plate search page


  Scenario: Navigating to search page from given URL
    Given I want to reach the search page from the provided URL
    When I click the search button
    Then I reach the search page

	Scenario: Searching various number plates
		Given I want to look up various number plates
		When I enter these number plates
		Then I find the correct information


  #Scenario Outline: Searching various number plates
    #Given I want to write a step with <name>
    #When I check for the <value> in step
    #Then I verify the <status> in step
#
    #Examples: 
      #| name  | value | status  |
      #| name1 |     5 | success |
      #| name2 |     7 | Fail    |
