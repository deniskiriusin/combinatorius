/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

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

import com.dkiriusin.combinatorius.MimeType;
import com.dkiriusin.combinatorius.Property;
import com.dkiriusin.combinatorius.UIControllerFilter;
import com.dkiriusin.combinatorius.UIControllerFilter.AddFileStrategy;
import com.dkiriusin.combinatorius.UIControllerFilter.ModifyFileStrategy;
import com.dkiriusin.combinatorius.UIControllerFilter.RemoveFileStrategy;
import com.dkiriusin.combinatorius.UIControllerFilter.UIEvent;
import com.dkiriusin.combinatorius.UIControllerFilter.UIEventType;

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
		Mockito.when(cookie.getName()).thenReturn(UIControllerFilter.combinatoriusEvent);
		Mockito.when(request.getCookies()).thenReturn(new Cookie[] { cookie });
		e1 = new UIEvent();
		e2 = new UIEvent();
		e1.setEventType(UIEventType.add_file);
		e1.setMimeType(MimeType.css);
		e2.setEventType(UIEventType.add_file);
		e2.setMimeType(MimeType.css);
	}
	
	@Test
	public void testModifyFile() throws IOException, ServletException, URISyntaxException {
		Mockito.when(cookie.getValue()).thenReturn(URLEncoder.encode("{\"eventType\":\"modify_file\",\"mimeType\":\"css\"}", "UTF-8"));
		filter.doFilter(request, response, chain);
		Mockito.verify(modifyFileStrategy).handleEvent(Mockito.any(UIEvent.class));
		Mockito.verifyZeroInteractions(addFileStrategy, removeFileStrategy);
	}
	
	@Test
	public void testAddFile() throws IOException, ServletException, URISyntaxException {
		Mockito.when(cookie.getValue()).thenReturn(URLEncoder.encode("{\"eventType\":\"add_file\",\"mimeType\":\"css\"}", "UTF-8"));
		filter.doFilter(request, response, chain);
		Mockito.verify(addFileStrategy).handleEvent(Mockito.any(UIEvent.class));
		Mockito.verifyZeroInteractions(modifyFileStrategy, removeFileStrategy);
	}
	
	@Test
	public void testRemoveFile() throws IOException, ServletException, URISyntaxException {
		Mockito.when(cookie.getValue()).thenReturn(URLEncoder.encode("{\"eventType\":\"remove_file\",\"mimeType\":\"css\"}", "UTF-8"));
		filter.doFilter(request, response, chain);
		Mockito.verify(removeFileStrategy).handleEvent(Mockito.any(UIEvent.class));
		Mockito.verifyZeroInteractions(modifyFileStrategy, addFileStrategy);
	}
	
	@Test
	public void testEquals() {
		Assert.assertTrue(e1.equals(e2));
	}

	@Test
	public void testToString() {
		Assert.assertTrue(e1.toString().equals("UIEvent [eventType=add_file, mimeType=css]"));
	}
	
	@Test
	public void testMimeTypeEnumValuesAreCorrect() {
		Assert.assertTrue(UIEventType.add_file != null);
		Assert.assertTrue(UIEventType.modify_file != null);
		Assert.assertTrue(UIEventType.remove_file != null);
		Assert.assertEquals(UIEventType.values().length, 3);
	}

}
