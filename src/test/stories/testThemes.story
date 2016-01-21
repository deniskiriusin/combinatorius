Narrative: 

As a user I want to ensure theme colours are correct

Scenario: Checking UI colours

Given I am on main page
Then I verify style 'background-color' of the element 'body' is 'rgba(101, 92, 119, 1)'
And I verify style 'color' of the element 'body' is 'rgba(174, 203, 207, 1)'
And I verify style 'background-color' of the element 'div.outer.left' is 'rgba(44, 36, 55, 1)'
And I verify style 'background-color' of the element 'div#directory_tree' is 'rgba(44, 36, 55, 1)'
And I verify style 'background-color' of the element 'div#get' is 'rgba(79, 71, 91, 1)'
And I verify style 'font-family' of the element 'div.inner pre' is 'Consolas, monospace'

Scenario: Checking green theme

Given I am on main page
When I click on 'li#green_theme' element
Then I verify style 'background-color' of the element 'body' is 'rgba(74, 136, 79, 1)'
And I verify style 'color' of the element 'body' is 'rgba(181, 221, 184, 1)'
And I verify style 'background-color' of the element 'div.outer.left' is 'rgba(36, 73, 37, 1)'
And I verify style 'background-color' of the element 'div#directory_tree' is 'rgba(36, 73, 37, 1)'
And I verify style 'background-color' of the element 'div#get' is 'rgba(59, 116, 61, 1)'
And I verify style 'font-family' of the element 'div.inner pre' is 'Consolas, monospace'

Scenario: Back to blue theme

Given I am on main page
When I click on 'li#blue_theme' element
