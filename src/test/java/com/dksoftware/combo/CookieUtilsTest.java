/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CookieUtilsTest {

	@Mock
	private Cookie cookie;
	@Mock
	private Cookie goodCookie;
	@Mock
	private Cookie goodCookieWithIncorrectHost;
	@Mock
	private HttpServletRequest request;

	@Before
	public void setUp() throws ServletException {
		mockCookie("test", "test", "test", cookie);
		mockCookie("name", "value", TestUtils.LOCALHOST, goodCookie);
		mockCookie("name", "value", "localhost_1", goodCookieWithIncorrectHost);
		// order is important for the test
		Mockito.when(request.getCookies()).thenReturn(new Cookie[] { cookie, goodCookieWithIncorrectHost, goodCookie });
	}

	private void mockCookie(final String name, final String value, final String domain, Cookie cookie) {
		Mockito.when(cookie.getName()).thenReturn(name);
		Mockito.when(cookie.getValue()).thenReturn(value);
		Mockito.when(cookie.getDomain()).thenReturn(domain);
	}

	@Test
	public void testGetCookieReturnsNullForEmptyRequest() {
		Cookie cookie = CookieUtils.getCookie(null, "test");
		Assert.assertNull(cookie);
	}

	@Test
	public void testGetCookieReturnsNullIfRequestGetCookiesReturnsNull() {
		Mockito.when(request.getCookies()).thenReturn(null);
		Cookie cookie = CookieUtils.getCookie(request, "test");
		Assert.assertNull(cookie);
	}

	@Test
	public void testGetCookieReturnsNullIfNoCookieWithRequiredNameFound() {
		Cookie cookie = CookieUtils.getCookie(request, "cookie_doesnt_exists");
		Mockito.verify(request, never()).getServerName();
		Assert.assertNull(cookie);
	}

	@Test
	public void testGetCookieReturnsCookieIfExist() {
		Cookie cookie = CookieUtils.getCookie(request, "name");
		Mockito.verify(request, atLeastOnce()).getServerName();
		Assert.assertNotNull(cookie);
	}

	@Test
	public void testGetCookieReturnsCorrectCookieIfExist() {
		Cookie cookie = CookieUtils.getCookie(request, "name");
		Mockito.verify(request, atLeastOnce()).getServerName();
		Assert.assertTrue(cookie.getName().equals("name"));
		Assert.assertTrue(cookie.getDomain().equals(TestUtils.LOCALHOST));
		Assert.assertFalse(cookie.getDomain().equals("localhost_1"));
	}

	@Test(expected = RuntimeException.class)
	public void testGetCookieReturnsNullIfException() {
		Mockito.doThrow(new RuntimeException()).when(request).getCookies();
		CookieUtils.getCookie(request, "test");
	}

}
