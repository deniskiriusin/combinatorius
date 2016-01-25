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

	static Properties properties;
	
	private ModifyFileStrategy modifyFileStrategy;
	private AddFileStrategy addFileStrategy;
	private RemoveFileStrategy removeFileStrategy;
	
	// no synchronisation required
	private static String cssDirPath = null;
	private static String jsDirPath = null;
	
	/**
	 * UI event type: <br>
	 * 
	 * <pre>
	 * 1) ADD_FILE
	 * 2) MODIFY_FILE
	 * 2) REMOVE_FILE
	 * </pre> 
	 */
	enum UIEventType {
		ADD_FILE,
		MODIFY_FILE,
		REMOVE_FILE;
	}

	@Override
	public void init(FilterConfig filterConfig) {
		try {
			properties = CIOUtils.loadPropertiesFromClasspath(ComboServlet.propertiesFileName);
			cssDirPath = properties.getProperty(Property.CSS_DIR.getName());
			jsDirPath = properties.getProperty(Property.JS_DIR.getName());
			modifyFileStrategy = new ModifyFileStrategy();
			addFileStrategy = new AddFileStrategy();
			removeFileStrategy = new RemoveFileStrategy();
		} catch (IOException e) {
			throw new RuntimeException("Initialization failed: ", e);
		}
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
				UIEvent event = getEventFromJSON(event_json);
				EventHandlerStrategy strategy = getSelectedStrategy(event);
				if (strategy != null) {
					strategy.handleEvent(event.getType());
				}
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
	
	UIEvent getEventFromJSON(String event_json) throws URISyntaxException {
		Gson gson = new Gson();
		event_json = new java.net.URI(event_json).getPath();
		return gson.fromJson(event_json, UIEvent.class);
	}

	EventHandlerStrategy getSelectedStrategy(UIEvent event) {
		EventHandlerStrategy strategy = null;
		if (event.getName() == UIEventType.ADD_FILE) {
			strategy = addFileStrategy;
		} else if (event.getName() == UIEventType.REMOVE_FILE) {
			strategy = removeFileStrategy;
		} else if (event.getName() == UIEventType.MODIFY_FILE) {
			strategy = modifyFileStrategy;
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
	
	/**
	 * Represents Ajax request generated UI event. The event created by reading JSON
	 * based <tt>combinatorius.event</tt> cookie value.
	 */
	static class UIEvent {
		private UIEventType name = null;
		private String type = null;
		private volatile Object lock1 = new Object();
		private volatile Object lock2 = new Object();

		public UIEventType getName() {
			synchronized (lock1) {
				return name;
			}
		}

		public void setName(UIEventType name) {
			synchronized (lock1) {
				this.name = name;
			}
		}

		public String getType() {
			synchronized (lock2) {
				return type;
			}
		}

		public void setType(String eventType) {
			synchronized (lock2) {
				type = eventType;
			}
		}

		@Override
		public String toString() {
			return "UIEvent [name=" + name + ", type=" + type + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UIEvent other = (UIEvent) obj;
			if (name != other.name)
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
	}
	
	@Override
	public void destroy() {
		System.out.println("UIControllerFilter is being taken out of service");
	}
}
