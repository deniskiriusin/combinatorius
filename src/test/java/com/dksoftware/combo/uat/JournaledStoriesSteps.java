/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo.uat;

import org.jbehave.core.annotations.AfterStories;
import org.jbehave.web.selenium.FirefoxWebDriverProvider;
import org.jbehave.web.selenium.WebDriverProvider;

public class JournaledStoriesSteps {

    private static final String JOURNAL_FIREFOX_COMMANDS = System.getProperty("JOURNAL_FIREFOX_COMMANDS", "false");
    private final WebDriverProvider webDriverProvider;

    public JournaledStoriesSteps(WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
    }

    @AfterStories
    public void afterStories() throws Exception {

        if (!JOURNAL_FIREFOX_COMMANDS.equals("false") && webDriverProvider instanceof FirefoxWebDriverProvider) {
            FirefoxWebDriverProvider.WebDriverJournal journal = ((FirefoxWebDriverProvider) webDriverProvider).getJournal();
            System.out.println("Journal of WebDriver Commands:");
            for (Object entry : journal) {
                System.out.println(entry);
            }
            ((FirefoxWebDriverProvider) webDriverProvider).clearJournal();
        }

    }

}
