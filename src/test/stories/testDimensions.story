Narrative: 

As a user I want to ensure UI dimensions are correct

Scenario: Checking UI dimensions  

Given I am on main page
Then I verify 'div#wrapper' width is '1200'
And I verify 'div#directory_tree' height is '525'
And I verify 'div.outer' width is '585'
And I verify 'div.inner' height is '250'
