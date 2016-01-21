/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class UIControllerFilter implements Filter {

	enum EventHandler {
		ADD_FILE,
		MODIFY_FILE,
		REMOVE_FILE,
		REFRESH;
	}
	
	static Properties properties;
	
	private ModifyFileStrategy modifyFileStrategy;
	private AddFileStrategy addFileStrategy;
	private RemoveFileStrategy removeFileStrategy;
	
	// no synchronisation required
	private static String cssDirPath = null;
	private static String jsDirPath = null;

	@Override
	public void init(FilterConfig filterConfig) {
		try {
			properties = CIOUtils.loadPropertiesFromClasspath(ComboServlet.propertiesFileName);
			cssDirPath = properties.getProperty(CProperties.CSS_DIR.getName());
			jsDirPath = properties.getProperty(CProperties.JS_DIR.getName());
			modifyFileStrategy = new ModifyFileStrategy();
			addFileStrategy = new AddFileStrategy();
			removeFileStrategy = new RemoveFileStrategy();
		} catch (IOException e) {
			throw new RuntimeException("Initialization failed: ", e);
		}
	}
	
	Event getEventFromJSON(String event_json) throws URISyntaxException {
		Gson gson = new Gson();
		event_json = new java.net.URI(event_json).getPath();
		return gson.fromJson(event_json, Event.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		Cookie cookie = CookieUtils.getCookie(httpServletRequest, "combinatorius.event");
		if (cookie != null && cookie.getValue() != null) {
			final String event_json = cookie.getValue();
			try {
				Event event = getEventFromJSON(event_json);
				EventHandlerStrategy strategy = getSelectedStrategy(event);
				strategy.handleEvent(event.getType());
			} catch(Exception e) {
				System.out.println(e);
			}
		}

		chain.doFilter(httpServletRequest, httpServletResponse);

		if (cookie != null) {
			cookie.setMaxAge(0);
			httpServletResponse.addCookie(cookie);
		}
	}

	EventHandlerStrategy getSelectedStrategy(Event event) {
		EventHandlerStrategy strategy = null;
		if (event.getName().equalsIgnoreCase(EventHandler.ADD_FILE.toString())) {
			strategy = addFileStrategy;
		} else if (event.getName().equalsIgnoreCase(EventHandler.REMOVE_FILE.toString())) {
			strategy = removeFileStrategy;
		} else if (event.getName().equalsIgnoreCase(EventHandler.MODIFY_FILE.toString())) {
			strategy = modifyFileStrategy;
		} else if (event.getName().equalsIgnoreCase(EventHandler.REFRESH.toString())) {
			strategy = new EventHandlerStrategy() {
				@Override
				public void handleEvent(String type) throws IOException {
					// dummy default strategy
				}
			};
		} else {
			throw new IllegalArgumentException("No event handler for [" + event.getName() + "] event type exists.");
		}
		return strategy;
	}

	interface EventHandlerStrategy {
		void handleEvent(String type) throws IOException;
	}

	static class AddFileStrategy implements EventHandlerStrategy {
		@Override
		public void handleEvent(String type) throws IOException {
			String path = "";
			if (type.equalsIgnoreCase("css")) {
				path = cssDirPath + (path.endsWith(File.separator) ? "" : File.separator) + "../extra_css" + File.separator + "extra2.css";
			} else if (type.equalsIgnoreCase("js")) {
				path = jsDirPath + (path.endsWith(File.separator) ? "" : File.separator) + "../extra_js" + File.separator + "extra2.js";
			}
			FileUtils.touch(CIOUtils.getLocalFile(path));
		}
	}

	static class RemoveFileStrategy implements EventHandlerStrategy {
		@Override
		public void handleEvent(String type) throws IOException {
			String path = "";
			if (type.equalsIgnoreCase("css")) {
				path = cssDirPath + (path.endsWith(File.separator) ? "" : File.separator) + "../extra_css" + File.separator + "extra1.css";
			} else if (type.equalsIgnoreCase("js")) {
				path = jsDirPath + (path.endsWith(File.separator) ? "" : File.separator) + "../extra_js" + File.separator + "extra1.js";
			}
			FileUtils.touch(CIOUtils.getLocalFile(path));
		}
	}
	
	static class ModifyFileStrategy implements EventHandlerStrategy {
		@Override
		public void handleEvent(String type) throws IOException {
			String path = "";
			if (type.equalsIgnoreCase("css")) {
				path = cssDirPath + (path.endsWith(File.separator) ? "" : File.separator) + "layout" + File.separator + "layout.css";
			} else if (type.equalsIgnoreCase("js")) {
				path = jsDirPath + (path.endsWith(File.separator) ? "" : File.separator) + "jquery-ui.js";
			}
			FileUtils.touch(CIOUtils.getLocalFile(path));
		}
	}
	
	static class Event {
		private String name = null;
		private String type = null;
		
		public String getName() {
			synchronized (name) {
				return name;
			}
		}
		public void setName(String eventName) {
			synchronized (name) {
				name = eventName;
			}
		}
		public String getType() {
			synchronized (type) {
				return type;
			}
		}
		public void setType(String eventType) {
			synchronized (type) {
				type = eventType;
			}
		}
	}

	@Override
	public void destroy() {
		System.out.println("UIControllerFilter is being taken out of service");
	}
}
