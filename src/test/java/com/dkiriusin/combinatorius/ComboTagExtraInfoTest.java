/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dkiriusin.combinatorius.ComboTagExtraInfo;

@RunWith(MockitoJUnitRunner.class)
public class ComboTagExtraInfoTest {

	@InjectMocks
	private ComboTagExtraInfo comboTagExtraInfo = new ComboTagExtraInfo();
	@Mock
	private TagData tagData;

	@Before
	public void setUp() throws Exception {
		Mockito.when(tagData.getAttributeString("type")).thenReturn("css");
		Mockito.when(tagData.getAttributeString("path")).thenReturn("/combo/");
		Mockito.when(tagData.getAttributeString("theme")).thenReturn("test-theme");
		Mockito.when(tagData.getAttributeString("csv_resources")).thenReturn(TestUtils.URL_RESOURCES);
	}

	@After
	public void tearDown() throws Exception {
		Mockito.when(tagData.getAttributeString("type")).thenReturn(null);
		Mockito.when(tagData.getAttributeString("path")).thenReturn(null);
		Mockito.when(tagData.getAttributeString("theme")).thenReturn(null);
		Mockito.when(tagData.getAttributeString("csv_resources")).thenReturn(null);
	}

	@Test
	public void testValidateTypeCSSorJS() {
		Mockito.when(tagData.getAttributeString("type")).thenReturn("cssXX");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertTrue("Type must be either 'css' or 'js'",
				validationMessages[0].getMessage().equals("Type must be either 'css' or 'js'"));
	}

	@Test
	public void testValidateIncorrectPath() {
		Mockito.when(tagData.getAttribute("path")).thenReturn("path");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertTrue("Path must match '/path/' or '${path}' pattern",
				validationMessages[0].getMessage().equals("Path must match '/path/' or '${path}' pattern"));
	}
	
	@Test
	public void testValidateELPath() {
		Mockito.when(tagData.getAttribute("path")).thenReturn("${path}");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertNull(validationMessages);
	}

	@Test
	public void testValidatePath() {
		Mockito.when(tagData.getAttribute("path")).thenReturn("/path/");//.thenReturn("${path}");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertNull(validationMessages);
	}

	@Test
	public void testValidateResourcesHasNoSpaces() {
		Mockito.when(tagData.getAttribute("csv_resources")).thenReturn("test.css,test test.css");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertTrue("Resources must not contain white spaces",
				validationMessages[0].getMessage().equals("Resources must not contain white spaces"));
	}

	@Test
	public void testValidateThemeHasNoSpaces() {
		Mockito.when(tagData.getAttribute("theme")).thenReturn("test theme");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertTrue("Theme must not contain white spaces",
				validationMessages[0].getMessage().equals("Theme must not contain white spaces"));
	}

	@Test
	public void testValidateCSSreourcesForCSStype() {
		Mockito.when(tagData.getAttribute("csv_resources")).thenReturn("test.css,test.js");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertTrue("Resources must contain .css files only",
				validationMessages[0].getMessage().equals("Resources must contain .css files only"));
	}

	@Test
	public void testValidateJSreourcesForJStype() {
		Mockito.when(tagData.getAttributeString("type")).thenReturn("js");
		Mockito.when(tagData.getAttribute("csv_resources")).thenReturn("test.js,test.css");
		final ValidationMessage[] validationMessages = comboTagExtraInfo.validate(tagData);
		Assert.assertTrue("Resources must contain .js files only",
				validationMessages[0].getMessage().equals("Resources must contain .js files only"));
	}

}
