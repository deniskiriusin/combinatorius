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
	public void testCSSCommentsRemoval() {
		final String COMMENT_REGEX = "(/\\*(?!(!))[^*]*\\*+([^/][^*]*\\*+)*/)";
		StringBuilder sb = new StringBuilder("// comment1");
		sb.append("\n\n");
		sb.append("/* comment2 */");
		sb.append(".className {");
		sb.append("\n");
		sb.append("background-color: #fff;");
		sb.append("\n");
		sb.append("//width: 100px;");
		sb.append("\n");
		sb.append("}");
		final String text = sb.toString();
		Pattern COMMENTS_PATTERN = Pattern.compile(COMMENT_REGEX, Pattern.DOTALL);
		String compressed = COMMENTS_PATTERN.matcher(text).replaceAll("");
		StringBuilder sb2 = new StringBuilder("// comment1");
		sb2.append("\n\n");
		sb2.append(".className {");
		sb2.append("\n");
		sb2.append("background-color: #fff;");
		sb2.append("\n");
		sb2.append("//width: 100px;");
		sb2.append("\n");
		sb2.append("}");
		Assert.assertTrue(compressed.equalsIgnoreCase(sb2.toString()));
	}
	
	
}
