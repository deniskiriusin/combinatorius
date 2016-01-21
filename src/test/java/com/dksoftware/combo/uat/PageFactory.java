/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo.uat;

import java.io.IOException;
import java.util.Properties;

import org.jbehave.web.selenium.WebDriverProvider;

public class PageFactory {

    private final WebDriverProvider webDriverProvider;
    
    static Properties properties = new Properties();
	static String mainUrl = "";

    public PageFactory(WebDriverProvider webDriverProvider) {
        this.webDriverProvider = webDriverProvider;
        try {
			properties = PageFactory.loadPropertiesFromClasspath("jetty.properties");
			mainUrl += properties.getProperty("jetty.schema") + "://" + properties.getProperty("jetty.host") 
				+ ":" + properties.getProperty("jetty.port") + "/combinatorius/index.jsp";
		} catch (IOException e) {
			throw new RuntimeException("Error loading [jetty.properties]: " + e);
		}
    }

    public HomePage newHomePage() {
        return new HomePage(webDriverProvider);
    }
    
    static final Properties loadPropertiesFromClasspath(final String filename) throws IOException {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));
		return properties;
	}

	static String getMainUrl() {
		return mainUrl;
	}
}