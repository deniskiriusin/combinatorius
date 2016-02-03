/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dkiriusin.combinatorius.ComboTag;

@RunWith(MockitoJUnitRunner.class)
public class ComboTagTest {

	@InjectMocks
	private ComboTag tag = new ComboTag();
	@Mock
	private JspContext jspContext;
	@Mock
	private PageContext pageContext;
	@Mock
	private ServletContext servletContext;
	@Mock
	private SimpleTagSupport simpleTagSupport;
	@Mock
	private JspWriter jspWriter;

	@Before
	public void setUp() throws Exception {
		tag.setJspContext(pageContext);
		Mockito.when(pageContext.getServletContext()).thenReturn(servletContext);
		Mockito.when(servletContext.getAttribute("com.dksoftware.combo.v_css")).thenReturn(11201122321L);
		Mockito.when(servletContext.getAttribute("com.dksoftware.combo.v_js")).thenReturn(11201122321L);

	}

	@Test
	public void testConstructURLAttributes() throws UnsupportedEncodingException {
		tag.setCsv_resources(TestUtils.URL_RESOURCES);
		tag.setTheme("test-theme");
		tag.setType("css");
		final String url = tag.constructURLAttributes();
		Assert.assertTrue(url.equals(
				"&type=css&resources=extra_css/extra1.css,extra_css/extra2.css&theme=test-theme&v=11201122321\""));
	}

	@Test
	public void testConstructHTMLTag_JavaScript() throws JspException, IOException {
		tag.setType("js");
		tag.setPath("/combo/");
		final String tagString = tag.constructHTMLTag();
		Assert.assertTrue(
				tagString.equals("<script type=\"text/javascript\" src=\"/combo/&type=js&v=11201122321\"></script>"));
	}

	@Test
	public void testConstructHTMLTag_CSS() throws JspException, IOException {
		tag.setType("css");
		tag.setPath("/combo/");
		tag.setCsv_resources(TestUtils.URL_RESOURCES);
		tag.setTheme("test-theme");
		final String tagString = tag.constructHTMLTag();
		Assert.assertTrue(tagString.equals(
				"<link href=\"/combo/&type=css&resources=extra_css/extra1.css,extra_css/extra2.css&theme=test-theme&v=11201122321\" rel=\"stylesheet\" type=\"text/css\" />"));
	}

}
