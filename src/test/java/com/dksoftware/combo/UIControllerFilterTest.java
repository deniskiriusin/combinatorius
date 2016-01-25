/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dksoftware.combo.UIControllerFilter.AddFileStrategy;
import com.dksoftware.combo.UIControllerFilter.ModifyFileStrategy;
import com.dksoftware.combo.UIControllerFilter.RemoveFileStrategy;
import com.dksoftware.combo.UIControllerFilter.UIEvent;
import com.dksoftware.combo.UIControllerFilter.UIEventType;

@RunWith(MockitoJUnitRunner.class)
public class UIControllerFilterTest {
	
	@InjectMocks
	private UIControllerFilter filter = new UIControllerFilter();
	
	@Mock
	private Cookie cookie;
	@Mock
	private Properties properties;
	@Mock
	private UIEvent event;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterConfig filterConfig;
	@Mock
	private FilterChain chain;
	@Mock
	private ServletContext servletContext;
	@Mock
	private AddFileStrategy addFileStrategy;
	@Mock
	private RemoveFileStrategy removeFileStrategy;
	@Mock
	private ModifyFileStrategy modifyFileStrategy;
	
	UIEvent e1 = null;
	UIEvent e2 = null;

	@Before
	public void setUp() throws FileNotFoundException, URISyntaxException, IOException {
		Mockito.when(filterConfig.getServletContext()).thenReturn(servletContext);
		Mockito.when(properties.getProperty(Property.CSS_DIR.getName())).thenReturn("src/main/webapp/css");
		Mockito.when(properties.getProperty(Property.JS_DIR.getName())).thenReturn("src/main/webapp/js");
		Mockito.when(cookie.getName()).thenReturn("combinatorius.event");
		Mockito.when(request.getCookies()).thenReturn(new Cookie[] { cookie });
		e1 = new UIEvent();
		e2 = new UIEvent();
		e1.setName(UIEventType.ADD_FILE);
		e1.setType("testType");
		e2.setName(UIEventType.ADD_FILE);
		e2.setType("testType");
	}
	
	@Test
	public void testModifyFile() throws IOException, ServletException, URISyntaxException {
		Mockito.when(cookie.getValue()).thenReturn(URLEncoder.encode("{\"name\":\"MODIFY_FILE\",\"type\":\"css\"}", "UTF-8"));
		filter.doFilter(request, response, chain);
		Mockito.verify(modifyFileStrategy).handleEvent(Mockito.anyString());
		Mockito.verifyZeroInteractions(addFileStrategy, removeFileStrategy);
	}
	
	@Test
	public void testAddFile() throws IOException, ServletException, URISyntaxException {
		Mockito.when(cookie.getValue()).thenReturn(URLEncoder.encode("{\"name\":\"ADD_FILE\",\"type\":\"css\"}", "UTF-8"));
		filter.doFilter(request, response, chain);
		Mockito.verify(addFileStrategy).handleEvent(Mockito.anyString());
		Mockito.verifyZeroInteractions(modifyFileStrategy, removeFileStrategy);
	}
	
	@Test
	public void testRemoveFile() throws IOException, ServletException, URISyntaxException {
		Mockito.when(cookie.getValue()).thenReturn(URLEncoder.encode("{\"name\":\"REMOVE_FILE\",\"type\":\"css\"}", "UTF-8"));
		filter.doFilter(request, response, chain);
		Mockito.verify(removeFileStrategy).handleEvent(Mockito.anyString());
		Mockito.verifyZeroInteractions(modifyFileStrategy, addFileStrategy);
	}
	
	@Test
	public void testEquals() {
		Assert.assertTrue(e1.equals(e2));
	}

	@Test
	public void testToString() {
		Assert.assertTrue(e1.toString().equals("UIEvent [name=ADD_FILE, type=testType]"));
	}
	
	@Test
	public void testMimeTypeEnumValuesAreCorrect() {
		Assert.assertTrue(UIEventType.ADD_FILE != null);
		Assert.assertTrue(UIEventType.MODIFY_FILE != null);
		Assert.assertTrue(UIEventType.REMOVE_FILE != null);
		Assert.assertEquals(UIEventType.values().length, 3);
	}

}
