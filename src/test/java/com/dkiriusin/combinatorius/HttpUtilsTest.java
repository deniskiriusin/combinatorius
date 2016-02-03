/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dkiriusin.combinatorius.HttpUtils;

@RunWith(MockitoJUnitRunner.class)
public class HttpUtilsTest {

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;

	@Test
	public void testIsRequestModified() {
		final String etag = "098f6bcd4621d373cade4e832627b4f6";
		Mockito.when(request.getHeader("If-None-Match")).thenReturn("098f6bcd4621d373cade4e832627b4f6");
		Assert.assertFalse(HttpUtils.isRequestModified(request, etag));
		Mockito.when(request.getHeader("If-None-Match")).thenReturn("00000000000000000000000000000000");
		Assert.assertTrue(HttpUtils.isRequestModified(request, etag));
		Mockito.when(request.getHeader("If-None-Match")).thenReturn(null);
		Assert.assertTrue(HttpUtils.isRequestModified(request, etag));
	}

	@Test
	public void testIsCompressionAcceptedByDefault() {
		Assert.assertFalse(HttpUtils.isCompressionAccepted(request));
	}

	@Test
	public void testIsCompressionAcceptedWhenAcceptEncodingHeaderIsNull() {
		Mockito.when(request.getHeader("Accept-Encoding")).thenReturn("something");
		Assert.assertFalse(HttpUtils.isCompressionAccepted(request));
	}

	@Test
	public void testIsCompressionAcceptedWhenAcceptEncodingHeaderIsNotGzip() {
		Mockito.when(request.getHeader("Accept-Encoding")).thenReturn("not_zip");
		Assert.assertFalse(HttpUtils.isCompressionAccepted(request));
	}

	@Test
	public void testIsCompressionAcceptedGzip() {
		Mockito.when(request.getHeader("Accept-Encoding")).thenReturn("compress;q=0.5, gzip;q=1.0");
		Assert.assertTrue(HttpUtils.isCompressionAccepted(request));
	}

	@Test
	public void testIsCompressionAcceptedNorton() {
		Mockito.when(request.getHeader("---------------")).thenReturn("not_null");
		Assert.assertTrue(HttpUtils.isCompressionAccepted(request));
	}

	@Test
	public void testIsCompressionAcceptedWithIElessThan70() {
		Mockito.when(request.getHeader("Accept-Encoding")).thenReturn("not_zip");
		Mockito.when(request.getHeader("User-Agent"))
				.thenReturn("Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; sv1;)");
		Assert.assertTrue(HttpUtils.isIE70orLess(request));
		Assert.assertFalse(HttpUtils.isCompressionAccepted(request));
	}

	@Test
	public void testSetCompressionHeaders() {
		HttpUtils.setCompressionHeaders(response);
		Mockito.verify(response).setHeader("Content-Encoding", "gzip");
		Mockito.verify(response).setHeader("Vary", "Accept-Encoding");
		Mockito.verifyNoMoreInteractions(response);
	}

	@Test
	public void testGetServletPath() {
		Mockito.when(request.getServletPath()).thenReturn("/combinator");
		Assert.assertEquals("/combinator", HttpUtils.getServletPath(request));
	}

	@Test
	public void testGetServletPathWithEmptyServletPath() {
		Mockito.when(request.getServletPath()).thenReturn("");
		Mockito.when(request.getPathInfo()).thenReturn("/path");
		HttpUtils.getServletPath(request);
		Mockito.verify(request, Mockito.atLeastOnce()).getPathInfo();
		Assert.assertEquals("/path", HttpUtils.getServletPath(request));
	}

	@Test
	public void testGetServletPathWithRequestURI() {
		Mockito.when(request.getRequestURI()).thenReturn("/combinator/path/main.html");
		Mockito.when(request.getPathInfo()).thenReturn("/path");
		Assert.assertEquals("/combinator", HttpUtils.getServletPath(request));
	}

	@Test
	public void testGetServletPathGetPathInfoCall() {
		Mockito.when(request.getRequestURI()).thenReturn("/combinator/path/main.html");
		HttpUtils.getServletPath(request);
		Mockito.verify(request, Mockito.times(1)).getPathInfo();
	}

	@Test
	public void testIsIE70orLessWithNullUserAgent() {
		Mockito.when(request.getHeader("User-Agent")).thenReturn(null);
		Assert.assertFalse("Should be false when User-Agent header is null", HttpUtils.isIE70orLess(request));
	}

	@Test
	public void testIsIE70orLessWithGeckoUserAgent() {
		Mockito.when(request.getHeader("User-Agent")).thenReturn("msie");
		Assert.assertFalse("Should be false when User-Agent header is gecko", HttpUtils.isIE70orLess(request));
	}

	@Test
	public void testIsIE70orLessWithMSIE61UserAgentAndSP2() {
		Mockito.when(request.getHeader("User-Agent"))
				.thenReturn("Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; sv1;)");
		Assert.assertTrue("Should be true if MSIE 6.1", HttpUtils.isIE70orLess(request));
	}

	@Test
	public void testIsIE70orLessWhithMSIE70UserAgentAndSP2() {
		Mockito.when(request.getHeader("User-Agent")).thenReturn("Mozilla/4.0 (compatible; MSIE 7.0; Windows XP;)");
		Assert.assertFalse("Should be false if MSIE 7.0 or newer", HttpUtils.isIE70orLess(request));
	}
}
