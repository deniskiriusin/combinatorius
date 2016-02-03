/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius.uat;

import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class HomePage extends SeleniumCommons {

	public HomePage(WebDriverProvider webDriverProvider) {
		super(webDriverProvider);
	}

	public void go() {
		get(PageFactory.getMainUrl());
	}
	
	public void wait(String seconds) {
		waitInSeconds(removeWrappingQuotes(seconds));
	}

	public boolean bodyContainsText(String text) {
		return findElement(By.tagName("body")).getText().contains(removeWrappingQuotes(text));
	}

	public boolean sectionContainsText(String sectionId, String text) {
		return findElement(sectionId).getText().matches("(?s)(?:.*" + removeWrappingQuotes(text) + ".*)");
	}

	public boolean sectionContainsTextCaseInsensitive(String sectionId, String text) {
		return findElement(sectionId).getText().matches("(?s)(?i:.*" + removeWrappingQuotes(text) + ".*)");
	}

	public boolean sectionContainsNoText(String sectionId, String text) {
		return !sectionContainsText(sectionId, text);
	}
	
	public boolean sectionContainsNoTextCaseInsensitive(String sectionId, String text) {
		return !sectionContainsTextCaseInsensitive(sectionId, text);
	}

	public void pressButton(String identifier) {
		WebElement element = findElement(identifier);
		if (element.getTagName().equalsIgnoreCase("button") || element.getTagName().equalsIgnoreCase("submit")) {
			waitForElementToEnable(element);
			element.click();
		}
	}

	public boolean verifyStyleOfTheElement(String style, String identifier, String result) {
		WebElement element = findElement(identifier);
		style = removeWrappingQuotes(style).toLowerCase();
		result = removeWrappingQuotes(result);
		String styleAsString = element.getCssValue(style).toString();
		styleAsString = styleAsString.replaceAll("\\s","");
		result = result.replaceAll("\\s","");
		return styleAsString.equalsIgnoreCase(result);
	}

	public boolean verifyElementDisappeared(String identifier) {
		WebElement element = findElement(identifier);
		return waitForElementToDisappear(element);
	}

	public boolean verifyElementExists(String identifier) {
		return findElement(identifier) != null;
	}
	
	public boolean verifyElementsWidth(String identifier, String width) {
		WebElement element = findElement(identifier);
		width = removeWrappingQuotes(width);
		return getElementWidth(element) == Integer.parseInt(width);
	}
	
	public boolean verifyElementsHeight(String identifier, String height) {
		WebElement element = findElement(identifier);
		height = removeWrappingQuotes(height);
		return getElementHeight(element) == Integer.parseInt(height);
	}
	
	public void clickOnElement(String identifier) {
		findElement(identifier).click();
	}
	
	public void setElementsAttribute(String attribute, String identifier, String result) {
		WebElement element =  findElement(identifier);
		attribute = removeWrappingQuotes(attribute);
		result = removeWrappingQuotes(result);
		JavascriptExecutor js = (JavascriptExecutor) getWebDriverProvider().get();
		js.executeScript("arguments[0].setAttribute('" + attribute + "', '" + result + "')", element);
	}

}
