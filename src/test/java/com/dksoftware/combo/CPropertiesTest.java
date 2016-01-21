/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import org.junit.Assert;
import org.junit.Test;

public class CPropertiesTest {

	@Test
	public void testPropertiesEnumValuesAreCorrect() {
		Assert.assertEquals(CProperties.CSS_DIR.getName(), "prop.css.dir");
		Assert.assertEquals(CProperties.CSS_CACHE_DIR.getName(), "prop.css.cache.dir");
		Assert.assertEquals(CProperties.JS_DIR.getName(), "prop.js.dir");
		Assert.assertEquals(CProperties.JS_CACHE_DIR.getName(), "prop.js.cache.dir");
		Assert.assertEquals(CProperties.THEMES_DIR.getName(), "prop.themes.dir");
		Assert.assertEquals(CProperties.S_MAXAGE.getName(), "prop.s-maxage");
		Assert.assertEquals(CProperties.MAX_AGE.getName(), "prop.max-age");
		Assert.assertEquals(CProperties.IS_COMPRESSION_ENABLED.getName(), "prop.isCompressionEnabled");
		Assert.assertEquals(CProperties.values().length, 8);
	}
}
