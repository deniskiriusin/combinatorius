/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo.uat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.WebDriverException;

import com.dksoftware.combo.ComboServlet;
import com.dksoftware.combo.UIControllerFilter;

public class LifecycleSteps extends PerStoriesWebDriverSteps {

    private final WebDriverProvider webDriverProvider;

    public LifecycleSteps(WebDriverProvider webDriverProvider) {
    	super(webDriverProvider);
        this.webDriverProvider = webDriverProvider;
    }

    @BeforeScenario
    public void deleteCookies() {
        try {
        	System.out.println("Deleting cookies");
            webDriverProvider.get().manage().deleteCookieNamed(ComboServlet.combinatoriusTheme);
            webDriverProvider.get().manage().deleteCookieNamed(UIControllerFilter.combinatoriusEvent);
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeScenario
    public void maximizeWindow() {
        try {
            //webDriverProvider.get().manage().window().maximize();
        } catch (WebDriverException e) {
            e.printStackTrace();
        }
    }
    
    @AfterStories
    public void clearCache() throws IOException {
    	File cssCacheDir = new File("src/main/webapp/css_cache");
    	File jsCacheDir = new File("src/main/webapp/js_cache");
    	FileUtils.cleanDirectory(cssCacheDir);
    	FileUtils.cleanDirectory(jsCacheDir);
    }
}
