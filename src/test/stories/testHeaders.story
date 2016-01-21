Narrative: 

As a user I want to ensure the headers are correct

Scenario: Checking default JavaScript headers

Given I am on main page
Then I should see 'Date:' text in 'js_url' section
And I should see 'ETag:' text in 'js_url' section [ci]
And I should see 'Content-Type: text/javascript' text in 'js_url' section
And I should see 'Expires:' text in 'js_url' section
And I should see 'Cache-Control:' text in 'js_url' section
And I should see 'Last-Modified:' text in 'js_url' section
And I should see 'Content-Encoding:' text in 'js_url' section
And I should see 'Vary' text in 'js_url' section
And I should see 'Content-Length:' text in 'js_url' section
And I should see 'Version:' text in 'js_url' section

Scenario: Checking default CSS headers

Given I am on main page
Then I should see 'Date:' text in 'css_url' section
And I should see 'ETag:' text in 'css_url' section [ci]
And I should see 'Content-Type: text/css' text in 'css_url' section
And I should see 'Expires:' text in 'css_url' section
And I should see 'Cache-Control:' text in 'css_url' section
And I should see 'Last-Modified:' text in 'css_url' section
And I should see 'Content-Encoding:' text in 'css_url' section
And I should see 'Vary' text in 'css_url' section
And I should see 'Content-Length:' text in 'css_url' section
And I should see 'Version:' text in 'css_url' section

Scenario: Checking F5 JavaScript headers

Given I am on main page
When I press 'f5_js' button
Then I should NOT see 'Version:' text in 'js_url' section
And I should NOT see 'Cache-Control:' text in 'js_url' section
And I should see 'Last-Modified:' text in 'js_url' section
And I should see 'Content-Length: 0' text in 'js_url' section
And I should see '304 Not Modified' text in 'js_url' section
And I should NOT see '200 OK' text in 'js_url' section
And I should NOT see '200 OK' text in 'js_url' section
And I should NOT see 'Content-Encoding:' text in 'js_url' section
And I should NOT see 'Etag:' text in 'js_url' section [ci]

Scenario: Checking F5 CSS headers

Given I am on main page
When I press 'f5_css' button
Then I should NOT see 'Version:' text in 'css_url' section
And I should NOT see 'Cache-Control:' text in 'css_url' section
And I should see 'Last-Modified:' text in 'css_url' section
And I should see 'Content-Length: 0' text in 'css_url' section
And I should see '304 Not Modified' text in 'css_url' section
And I should NOT see '200 OK' text in 'css_url' section
And I should NOT see 'Content-Encoding:' text in 'css_url' section
And I should NOT see 'Etag:' text in 'css_url' section [ci]

Scenario: Checking Ctrl+F5 JavaScript headers

Given I am on main page
When I press 'ctrl_f5_js' button
Then I should see 'Date:' text in 'js_url' section
And I should see 'ETag:' text in 'js_url' section [ci]
And I should see 'Content-Type: text/javascript' text in 'js_url' section
And I should see 'Expires:' text in 'js_url' section
And I should see 'Cache-Control:' text in 'js_url' section
And I should see 'Last-Modified:' text in 'js_url' section
And I should see 'Content-Encoding:' text in 'js_url' section
And I should see 'Vary' text in 'js_url' section
And I should see 'Content-Length:' text in 'js_url' section
And I should see 'Version:' text in 'js_url' section

Scenario: Checking Ctrl+F5 CSS headers

Given I am on main page
When I press 'ctrl_f5_css' button
Then I should see 'Date:' text in 'css_url' section
And I should see 'ETag:' text in 'css_url' section [ci]
And I should see 'Content-Type: text/css' text in 'css_url' section
And I should see 'Expires:' text in 'css_url' section
And I should see 'Cache-Control:' text in 'css_url' section
And I should see 'Last-Modified:' text in 'css_url' section
And I should see 'Content-Encoding:' text in 'css_url' section
And I should see 'Vary' text in 'css_url' section
And I should see 'Content-Length:' text in 'css_url' section
And I should see 'Version:' text in 'css_url' section

Scenario: Checking Add File JavaScript headers

Given I am on main page
When I press 'add_file_js' button
Then I should see 'Date:' text in 'js_url' section
And I should see 'ETag:' text in 'js_url' section [ci]
And I should see 'Content-Type: text/javascript' text in 'js_url' section
And I should see 'Expires:' text in 'js_url' section
And I should see 'Cache-Control:' text in 'js_url' section
And I should see 'Last-Modified:' text in 'js_url' section
And I should see 'Content-Encoding:' text in 'js_url' section
And I should see 'Vary' text in 'js_url' section
And I should see 'Content-Length:' text in 'js_url' section
And I should see 'Version:' text in 'js_url' section

Scenario: Checking Add File CSS headers

Given I am on main page
When I press 'add_file_css' button
Then I should see 'Date:' text in 'css_url' section
And I should see 'ETag:' text in 'css_url' section [ci]
And I should see 'Content-Type: text/css' text in 'css_url' section
And I should see 'Expires:' text in 'css_url' section
And I should see 'Cache-Control:' text in 'css_url' section
And I should see 'Last-Modified:' text in 'css_url' section
And I should see 'Content-Encoding:' text in 'css_url' section
And I should see 'Vary' text in 'css_url' section
And I should see 'Content-Length:' text in 'css_url' section
And I should see 'Version:' text in 'css_url' section

Scenario: Checking Modify File JavaScript headers

Given I am on main page
When I press 'touch_file_js' button
Then I should see 'Date:' text in 'js_url' section
And I should see 'ETag:' text in 'js_url' section [ci]
And I should see 'Content-Type: text/javascript' text in 'js_url' section
And I should see 'Expires:' text in 'js_url' section
And I should see 'Cache-Control:' text in 'js_url' section
And I should see 'Last-Modified:' text in 'js_url' section
And I should see 'Content-Encoding:' text in 'js_url' section
And I should see 'Vary' text in 'js_url' section
And I should see 'Content-Length:' text in 'js_url' section
And I should see 'Version:' text in 'js_url' section

Scenario: Checking Modify File CSS headers

Given I am on main page
When I press 'touch_file_css' button
Then I should see 'Date:' text in 'css_url' section
And I should see 'ETag:' text in 'css_url' section [ci]
And I should see 'Content-Type: text/css' text in 'css_url' section
And I should see 'Expires:' text in 'css_url' section
And I should see 'Cache-Control:' text in 'css_url' section
And I should see 'Last-Modified:' text in 'css_url' section
And I should see 'Content-Encoding:' text in 'css_url' section
And I should see 'Vary' text in 'css_url' section
And I should see 'Content-Length:' text in 'css_url' section
And I should see 'Version:' text in 'css_url' section
