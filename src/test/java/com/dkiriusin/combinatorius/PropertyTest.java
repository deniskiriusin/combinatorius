/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import org.junit.Assert;
import org.junit.Test;

import com.dkiriusin.combinatorius.Property;

public class PropertyTest {

	@Test
	public void testPropertiesEnumValuesAreCorrect() {
		Assert.assertEquals(Property.CSS_DIR.getName(), "prop.css.dir");
		Assert.assertEquals(Property.CSS_CACHE_DIR.getName(), "prop.css.cache.dir");
		Assert.assertEquals(Property.JS_DIR.getName(), "prop.js.dir");
		Assert.assertEquals(Property.JS_CACHE_DIR.getName(), "prop.js.cache.dir");
		Assert.assertEquals(Property.THEMES_DIR.getName(), "prop.themes.dir");
		Assert.assertEquals(Property.S_MAXAGE.getName(), "prop.s-maxage");
		Assert.assertEquals(Property.MAX_AGE.getName(), "prop.max-age");
		Assert.assertEquals(Property.IS_COMPRESSION_ENABLED.getName(), "prop.isCompressionEnabled");
		Assert.assertEquals(Property.values().length, 8);
	}
}
