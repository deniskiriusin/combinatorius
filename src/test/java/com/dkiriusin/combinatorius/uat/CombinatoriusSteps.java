/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius.uat;

import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

public class CombinatoriusSteps {
    
    private HomePage home;

    public CombinatoriusSteps(PageFactory pageFactory){
        home = pageFactory.newHomePage();
    }

    @Given("I am on main page")
    public void homepage() {
        home.go();
    }
    
    @When("I set $attribute attribute of the $element element to $result")
    public void homepageIncorrect(String attribute, String element, String result) {
        home.setElementsAttribute(attribute, element, result);
    }
    
    @When("I press $button button")
    public void whenPressedButton(String identifier) {
    	home.pressButton(identifier);
    }
    
    @When("I remove $cookie cookie")
    public void deleteCookie(String cookie) {
    	home.deleteCookie(cookie);
    }
    
    @When("I set $name=$value cookie")
    public void deleteCookie(String name, String value) {
    	home.setCookie(name, value);
    }
    
    @Then("I should see $text text")
    public void textShouldBeDisplayed(String text) {
    	Assert.assertTrue(home.bodyContainsText(text));
    }
    
    @Then("I should NOT see $text text")
    public void textShouldNotBeDisplayed(String text) {
    	Assert.assertFalse(home.bodyContainsText(text));
    }
    
    @Then("I should see $text text in $section section")
    public void textShouldBeInSection(String text, String section) {
    	Assert.assertTrue(home.sectionContainsText(section, text));
    }
    
    @Then("I should see $text text in $section section [ci]")
    public void textShouldBeInSectionCaseInsensitive(String text, String section) {
    	Assert.assertTrue(home.sectionContainsTextCaseInsensitive(section, text));
    }
    
    @Then("I should NOT see $text text in $section section")
    public void textShouldNotBeInSection(String text, String section) {
    	Assert.assertTrue(home.sectionContainsNoText(section, text));
    }
    
    @Then("I should NOT see $text text in $section section [ci]")
    public void textShouldNotBeInSectionCaseInsensitive(String text, String section) {
    	Assert.assertTrue(home.sectionContainsNoTextCaseInsensitive(section, text));
    }
    
    @Then("I verify style $style of the element $element is $result")
    public void verifyStyleInSectionEquals(String style, String element, String result) {
    	Assert.assertTrue(home.verifyStyleOfTheElement(style, element, result));
    }
    
    @Then("I verify element $element disappeared")
    public void verifyElementDisappeared(String element) {
    	Assert.assertTrue(home.verifyElementDisappeared(element));
    }
    
    @Then("I verify element $element exists")
    public void verifyElementExists(String identifier) {
    	Assert.assertTrue(home.verifyElementExists(identifier));
    }
    
    @Then("I wait for $seconds seconds")
    @Alias("I wait for $seconds second")
    public void waitForSeconds(String seconds) {
    	home.wait(seconds);
    }
    
    @Then("I verify '$element' width is '$width'")
    public void checkElementsWidth(String identifier, String width) {
    	Assert.assertTrue(home.verifyElementsWidth(identifier, width));
    }
    
    @Then("I verify '$element' height is '$height'")
    public void checkElementsHeight(String identifier, String height) {
    	Assert.assertTrue(home.verifyElementsHeight(identifier, height));
    }
    
    @When("I click on '$element' element")
    public void clickOn(String identifier) {
    	home.clickOnElement(identifier);
    }
}
