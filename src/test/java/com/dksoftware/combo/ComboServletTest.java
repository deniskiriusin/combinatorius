/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ComboServletTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Rule
	public TemporaryFolder tp = new TemporaryFolder();

	@InjectMocks
	private ComboServlet servlet = new ComboServlet();
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private ServletConfig servletConfig;
	@Mock
	private ServletContext servletContext;
	@Mock
	private ThreadLocal<RequestDetails> requestDetails;
	@Mock
	private RequestDetails requestDetailsObject;
	@Mock
	private Cookie cookie;
	@Mock
	private Properties properties;

	@Before
	public void setUp() throws ServletException, IOException, URISyntaxException {
		Mockito.when(servlet.getServletContext()).thenReturn(servletContext);
		Mockito.when(servletContext.getRealPath(Mockito.anyString())).thenReturn("src/test/resources/css/file1.css");
		Mockito.when(requestDetailsObject.getMimeType()).thenReturn(MimeType.css);
		Mockito.when(requestDetailsObject.getResources()).thenReturn(TestUtils.URL);
		Mockito.when(requestDetailsObject.getThemeName()).thenReturn("test-theme");
		Mockito.when(requestDetailsObject.getVersion()).thenReturn(0L);
		Mockito.when(requestDetailsObject.getExtension()).thenReturn("css");
		Mockito.when(requestDetails.get()).thenReturn(requestDetailsObject);
		
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(TestUtils.URL));
		Mockito.when(properties.getProperty(CProperties.CSS_DIR.getName())).thenReturn("src/test/resources/css");
		Mockito.when(properties.getProperty(CProperties.THEMES_DIR.getName())).thenReturn("src/test/resources/themes");
		Mockito.when(properties.getProperty(CProperties.CSS_CACHE_DIR.getName())).thenReturn("css_cache");
		Mockito.when(properties.getProperty(CProperties.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
	}

	@After
	public void tearDown() {
		Mockito.when(servlet.getServletContext()).thenReturn(null);
		requestDetailsObject = null;
	}

	@Test
	public void testGetFilesWithDefaultDirectoriesOnly() {
		Mockito.when(properties.getProperty(CProperties.CSS_DIR.getName())).thenReturn("src/test/resources/css");
		Mockito.when(requestDetailsObject.getThemeName()).thenReturn(null);
		Mockito.when(requestDetailsObject.getResources()).thenReturn(null);

		Collection<File> files = servlet.getFiles(request, requestDetailsObject);

		Assert.assertTrue("Should be at least several CSS files in test folder", files.size() > 0);
	}

	@Test
	public void testGetFilesWithThemes() {
		Mockito.when(properties.getProperty(CProperties.CSS_DIR.getName())).thenReturn("src/test/resources/css");
		Mockito.when(properties.getProperty(CProperties.THEMES_DIR.getName())).thenReturn("src/test/resources/themes");
		Mockito.when(requestDetailsObject.getResources()).thenReturn(null);

		Collection<File> files = servlet.getFiles(request, requestDetailsObject);

		Assert.assertTrue("Should be at least several CSS files in test folders including test themes",
				files.size() > 3);
	}

	@Test
	public void testGetFilesWithWrongTheme() {
		Mockito.when(requestDetailsObject.getThemeName()).thenReturn("wrong-theme");
		Mockito.when(properties.getProperty(CProperties.CSS_DIR.getName())).thenReturn("src/test/resources/css");
		Mockito.when(properties.getProperty(CProperties.THEMES_DIR.getName())).thenReturn("src/test/resources/themes");
		Mockito.when(requestDetailsObject.getResources()).thenReturn(null);

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Error getting 'wrong-theme' theme. Please make sure the theme name is correctly specified via 'theme' "
						+ "URL parameter or as 'combinatorius.theme' cookie value.");

		servlet.getFiles(request, requestDetailsObject);
	}

	@Test
	public void testGetFilesWithIcorrectResourcesDir() {
		Mockito.when(properties.getProperty(CProperties.CSS_DIR.getName())).thenReturn("src/test/resources/wrong_dir");

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(Matchers.containsString("Error getting files from"));

		servlet.getFiles(request, requestDetailsObject);
	}
	
	@Test
	public void testDoGetWithIcorrectResourcesURL() throws ServletException, IOException {
		servlet.doGet(request, response);
		Mockito.verify(response).sendError(Mockito.eq(HttpServletResponse.SC_BAD_REQUEST), 
				Mockito.contains("Error trying to get content:"));
	}
	
	@Test
	public void testDoGetWithNoCacheDir() throws ServletException, IOException {
		Mockito.when(properties.getProperty(CProperties.CSS_CACHE_DIR.getName())).thenReturn(null);
		servlet.doGet(request, response);
		Mockito.verify(response).sendError(Mockito.eq(HttpServletResponse.SC_BAD_REQUEST), 
				Mockito.contains("Error trying to get content:"));
	}
	
	@Test
	public void testDoGetWithNoCssDir() throws ServletException, IOException {
		Mockito.when(properties.getProperty(CProperties.CSS_DIR.getName())).thenReturn(null);

		servlet.doGet(request, response);
		
		Mockito.verify(response).sendError(Mockito.eq(HttpServletResponse.SC_BAD_REQUEST), 
				Mockito.contains("CSS directory not specified"));
	}

	@Test
	public void testGetFilesAlwaysReturnsCollection() throws IOException {
		Collection<File> files = servlet.getFiles(request, null);
		Assert.assertEquals("Should always return collection, never null", files.size(), 0);
	}

	@Test
	public void testSetResponseHeaders() {
		Mockito.when(request.getScheme()).thenReturn("non-https");
		Mockito.when(properties.getProperty(CProperties.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
		Mockito.when(properties.getProperty(CProperties.S_MAXAGE.getName())).thenReturn("31536000");
		Mockito.when(properties.getProperty(CProperties.MAX_AGE.getName())).thenReturn("31536000");

		ComboServlet.setResponseHeaders(request, response, "test_etag", 127151112L, 128);

		Mockito.verify(response).setContentType(requestDetails.get().getMimeType().getName());
		Mockito.verify(response).setHeader("Etag", "test_etag");
		Mockito.verify(response).setCharacterEncoding("UTF-8");
		Mockito.verify(response).setDateHeader(Mockito.eq("Expires"), Mockito.anyLong());
		Mockito.verify(response).setHeader("Cache-Control", "private, max-age=31536000");
		Mockito.verify(response).setDateHeader(Mockito.eq("Last-Modified"), Mockito.anyLong());
		Mockito.verify(response).setContentLength(Mockito.anyInt());
	}

	@Test
	public void testSetResponseHeadersHTTPS() {
		Mockito.when(request.getScheme()).thenReturn("https");
		Mockito.when(properties.getProperty(CProperties.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
		Mockito.when(properties.getProperty(CProperties.S_MAXAGE.getName())).thenReturn("31536000");
		Mockito.when(properties.getProperty(CProperties.MAX_AGE.getName())).thenReturn("31536000");

		ComboServlet.setResponseHeaders(request, response, "test_etag", 127151112L, 128);

		Mockito.verify(response).setContentType(requestDetails.get().getMimeType().getName());
		Mockito.verify(response).setHeader("Etag", "test_etag");
		Mockito.verify(response).setCharacterEncoding("UTF-8");
		Mockito.verify(response).setDateHeader(Mockito.eq("Expires"), Mockito.anyLong());
		Mockito.verify(response).setHeader("Cache-Control", "public, s-maxage=31536000, max-age=31536000");
		Mockito.verify(response).setDateHeader(Mockito.eq("Last-Modified"), Mockito.anyLong());
		Mockito.verify(response).setContentLength(Mockito.anyInt());
	}

	@Test
	public void testSetConditionalResponseHeaders() {
		ComboServlet.setConditionalResponseHeaders(request, response);
		Mockito.verify(response).setStatus(304);
		Mockito.verify(response).setHeader("Content-Length", "0");
		Mockito.verify(response).setHeader("Last-Modified", request.getHeader("If-Modified-Since"));
	}

	@Test
	public void testGetThemeName() {
		Mockito.when(cookie.getName()).thenReturn("combinatorius.theme");
		Mockito.when(cookie.getValue()).thenReturn("green");
		Mockito.when(cookie.getDomain()).thenReturn("localhost");
		Mockito.when(request.getCookies()).thenReturn(new Cookie[] { cookie });
		Mockito.when(request.getServerName()).thenReturn("localhost");

		String themeName = servlet.getThemeName(request, requestDetails.get());

		Assert.assertEquals("green", themeName);
	}
}
