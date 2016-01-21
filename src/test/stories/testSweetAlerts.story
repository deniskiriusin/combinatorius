Narrative: 

As a user I want to ensure Sweet Alerts handle incorrect Ajax requests as expected

Scenario: Checking Sweet Alerts

Given I am on main page
When I set 'combinatorius.theme'='theme_not_exists' cookie
And I press 'ctrl_f5_css' button
Then I should see 'Something went wrong!' text

Scenario: Back cookie to defaults

Given I am on main page
When I set 'combinatorius.theme'='blue' cookie
And I press 'ctrl_f5_css' button
Then I should NOT see 'Something went wrong!' text
