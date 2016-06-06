/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests regular expressions used within the project.
 */
public class RegExTest {

	@Test
	public void testSectionContainsText() {
		final String text = "This is \t a\n \ta \n Combinatorius \n\n servlet \n";
		Pattern p = Pattern.compile("(?s)(?:.*Combinatorius.*)");
		Assert.assertTrue(p.matcher(text).find());
	}

	@Test
	public void testSectionContainsTextCaseSensitive() {
		final String text = "This is \t a\n \ta \n combinatorius \n\n servlet \n";
		Pattern p = Pattern.compile("(?s)(?:.*Combinatorius.*)");
		Assert.assertFalse(p.matcher(text).find());
	}

	@Test
	public void testSectionContainsTextCaseInsensitive() {
		final String text = "This is \t a\n \ta \n combinatorius \n\n servlet \n";
		Pattern p = Pattern.compile("(?s)(?i:.*Combinatorius.*)");
		Assert.assertTrue(p.matcher(text).find());
	}

	@Test
	public void testPropYUIOmitFilesFromMinificationRegEx() {
		final String s = "jquery-1.9.1.min.js";
		final String regex = ".*\\.min\\.(js|css)$";
		Pattern p = Pattern.compile(regex);
		Assert.assertTrue(p.matcher(s).find());
	}

}
