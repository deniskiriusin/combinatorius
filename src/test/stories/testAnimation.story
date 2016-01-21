Narrative: 

As a user I want to ensure JavaScript animation works as expected

Scenario: Checking F5 JavaScript animation

Given I am on main page
When I press 'f5_js' button
Then I should see '/combinatorius/combo/&type=js&resources=extra_js/extra1.js&v=14' text in 'div#js_url' section
And I should see '304 Not Modified' text in 'div#js_url' section

Scenario: Checking F5 CSS animation

Given I am on main page
When I press 'f5_css' button
Then I should see '/combinatorius/combo/&type=css&resources=extra_css/extra1.css&theme=blue&v=14' text in 'div#css_url' section
And I should see '304 Not Modified' text in 'div#css_url' section

Scenario: Checking Ctrl+F5 JavaScript animation

Given I am on main page
When I press 'ctrl_f5_js' button
Then I should see '/combinatorius/combo/&type=js&resources=extra_js/extra1.js&v=14' text in 'div#js_url' section
And I should see '200 OK' text in 'div#js_url' section

Scenario: Checking Ctrl+F5 CSS animation

Given I am on main page
When I press 'ctrl_f5_css' button
Then I should see '/combinatorius/combo/&type=css&resources=extra_css/extra1.css&theme=blue&v=14' text in 'div#css_url' section
And I should see '200 OK' text in 'div#css_url' section

Scenario: Checking Add file JavaScript button

Given I am on main page
When I press 'add_file_js' button
Then I should see '/combinatorius/combo/&type=js&resources=extra_js/extra1.js,extra_js/extra2.js&v=14' text in 'div#js_url' section
And I should see '200 OK' text in 'div#js_url' section
And I should see 'extra2.js' text in 'div#directory_tree' section
And I verify style 'background-color' of the element 'div#directory_tree a.highlighted' is 'rgba(101, 92, 119, 1)'
And I verify style 'background-color' of the element 'div#js_url i.highlighted' is 'rgba(101, 92, 119, 1)'

Scenario: Checking Delete file JavaScript button

Given I am on main page
When I press 'add_file_js' button
Then I wait for '3' seconds
When I press 'add_file_js' button
Then I should see '/combinatorius/combo/&type=js&resources=extra_js/extra1.js&v=14' text in 'div#js_url' section
And I should see '200 OK' text in 'div#js_url' section
Then I wait for '3' seconds
And I should NOT see 'extra2.js' text in 'div#directory_tree' section
And I verify style 'background-color' of the element 'div#js_url i.highlighted' is 'rgba(101, 92, 119, 1)'

Scenario: Checking Add file CSS button

Given I am on main page
When I press 'add_file_css' button
Then I should see '/combinatorius/combo/&type=css&resources=extra_css/extra1.css,extra_css/extra2.css&theme=blue&v=14' text in 'div#css_url' section
And I should see '200 OK' text in 'div#css_url' section
And I should see 'extra2.css' text in 'div#directory_tree' section
And I verify style 'background-color' of the element 'div#directory_tree a.highlighted' is 'rgba(101, 92, 119, 1)'
And I verify style 'background-color' of the element 'div#css_url i.highlighted' is 'rgba(101, 92, 119, 1)'

Scenario: Checking Delete file CSS button

Given I am on main page
When I press 'add_file_css' button
Then I wait for '3' seconds
When I press 'add_file_css' button
Then I should see '/combinatorius/combo/&type=css&resources=extra_css/extra1.css&theme=blue&v=14' text in 'div#css_url' section
And I should see '200 OK' text in 'div#css_url' section
Then I wait for '3' seconds
And I should NOT see 'extra2.css' text in 'div#directory_tree' section
And I verify style 'background-color' of the element 'div#css_url i.highlighted' is 'rgba(101, 92, 119, 1)'

Scenario: Checking Modify file CSS button

Given I am on main page
When I press 'touch_file_css' button
Then I should see '/combinatorius/combo/&type=css&resources=extra_css/extra1.css&theme=blue&v=14' text in 'div#css_url' section
And I should see '200 OK' text in 'div#css_url' section
Then I wait for '3' seconds
And I should NOT see 'extra2.css' text in 'div#directory_tree' section
And I verify style 'background-color' of the element 'div#css_url i.highlighted' is 'rgba(101, 92, 119, 1)'

Scenario: Checking Modify file JavaScript button

Given I am on main page
When I press 'touch_file_js' button
Then I should see '/combinatorius/combo/&type=js&resources=extra_js/extra1.js&v=14' text in 'div#js_url' section
And I should see '200 OK' text in 'div#js_url' section
Then I wait for '3' seconds
And I should NOT see 'extra2.js' text in 'div#directory_tree' section
And I verify style 'background-color' of the element 'div#js_url i.highlighted' is 'rgba(101, 92, 119, 1)'
