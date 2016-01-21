/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo.uat;

import java.util.concurrent.TimeUnit;

import org.jbehave.web.selenium.FluentWebDriverPage;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.seleniumhq.selenium.fluent.FluentBy;

import com.google.common.base.Function;

public class SeleniumCommons extends FluentWebDriverPage {

	private static WebDriverProvider webDriverProvider = null;
	private static FluentWait<WebDriver> wait = null;

	public SeleniumCommons(WebDriverProvider webDriverProvider) {
		super(webDriverProvider);
		SeleniumCommons.webDriverProvider = webDriverProvider;
	}

	private static void initWait() {
		wait = new FluentWait<WebDriver>(webDriverProvider.get()).withTimeout(120, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
	}

	WebElement findElement(String identifier) {
		WebElement element = null;
		NoSuchElementException exception = null;
		try {
			element = findElement(findElementById(identifier));
		} catch (NoSuchElementException nse) {
			exception = nse;
		}
		if (element == null) {
			try {
				element = findElement(findElementByClassName(identifier));
			} catch (NoSuchElementException nse) {
				exception = nse;
			}
		}
		if (element == null) {
			try {
				element = findElement(findElementByCssSelector(identifier));
			} catch (NoSuchElementException nse) {
				exception = nse;
			}
		}
		if (element == null) {
			throw new NoSuchElementException("No element with [id|className|cssSelector] = " + identifier + " found",
					exception);
		}
		return element;
	}

	void waitInMilliseconds(final long milliseconds) {
		final long currentMillis = System.currentTimeMillis();
		initWait();
		synchronized (wait) {
			wait.until(new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver driver) {
					return (System.currentTimeMillis() - currentMillis) > milliseconds;
				}
			});
		}
	}

	void waitInSeconds(final String seconds) {
		long millis = Long.parseLong(seconds) * 1000L;
		waitInMilliseconds(millis);
	}

	void waitForElementToEnable(final WebElement element) {
		final long currentMillis = System.currentTimeMillis();
		initWait();
		synchronized (wait) {
			wait.until(new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver driver) {
					return (element.isEnabled() && !element.getCssValue("disabled").equalsIgnoreCase("disabled")
							&& (System.currentTimeMillis() - currentMillis) > 500L);
				}
			});
		}
	}

	boolean waitForElementToDisappear(final WebElement element) {
		initWait();
		synchronized (wait) {
			return wait.until(new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver driver) {
					return !element.isDisplayed();
				}
			});
		}
	}
	
	int getElementWidth(final WebElement element) {
		return element.getSize().getWidth();
	}
	
	int getElementHeight(final WebElement element) {
		return element.getSize().getHeight();
	}

	private By findElementById(String elementId) {
		elementId = removeWrappingQuotes(elementId);
		return FluentBy.id(elementId);
	}

	private By findElementByClassName(String elementId) {
		elementId = removeWrappingQuotes(elementId);
		return FluentBy.className(elementId);
	}

	private By findElementByCssSelector(String cssSelector) {
		cssSelector = removeWrappingQuotes(cssSelector);
		return FluentBy.cssSelector(cssSelector);
	}

	String removeWrappingQuotes(String text) {
		if (text.startsWith("'") && text.endsWith("'")) {
			return text.substring(1, text.length() - 1);
		}
		return text;
	}
	
	void deleteCookie(String cookieName) {
		cookieName = removeWrappingQuotes(cookieName);
		if (getWebDriverProvider().get().manage().getCookieNamed(cookieName) != null) {
			getWebDriverProvider().get().manage().deleteCookieNamed(cookieName);
		}
	}
	
	void setCookie(String name, String value) {
		name = removeWrappingQuotes(name);
		value = removeWrappingQuotes(value);
		Cookie cookie = new Cookie(name, value);
		getWebDriverProvider().get().manage().addCookie(cookie);
	}

	public static WebDriverProvider getWebDriverProvider() {
		return webDriverProvider;
	}

}
