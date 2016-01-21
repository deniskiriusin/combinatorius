/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo.uat;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.WebDriverException;

public class LifecycleSteps extends PerStoriesWebDriverSteps {

    private final WebDriverProvider webDriverProvider;

    public LifecycleSteps(WebDriverProvider webDriverProvider) {
    	super(webDriverProvider);
        this.webDriverProvider = webDriverProvider;
    }

    @BeforeScenario
    public void emptyCart() {
        try {
        	System.out.println("Deleting cookies");
            webDriverProvider.get().manage().deleteCookieNamed("combinatorius.theme");
            webDriverProvider.get().manage().deleteCookieNamed("combinatorius.event");
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
    }
}
