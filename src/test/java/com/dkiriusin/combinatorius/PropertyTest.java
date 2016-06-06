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
		Assert.assertEquals(Property.IS_YUI_COMPRESSOR_ENABLED.getName(), "prop.isYUICompressorEnabled");
		Assert.assertEquals(Property.YUI_CSSCOMPRESSOR_LINEBREAKPOS.getName(), "prop.YUI.CSSCompressor.linebreakpos");
		Assert.assertEquals(Property.YUI_JAVASCRIPT_COMPRESSOR_LINEBREAK.getName(), "prop.YUI.JavaScriptCompressor.linebreak");
		Assert.assertEquals(Property.YUI_JAVASCRIPT_COMPRESSOR_MUNGE.getName(), "prop.YUI.JavaScriptCompressor.munge");
		Assert.assertEquals(Property.YUI_JAVASCRIPT_COMPRESSOR_VERBOSE.getName(), "prop.YUI.JavaScriptCompressor.verbose");
		Assert.assertEquals(Property.YUI_JAVASCRIPT_COMPRESSOR_PRESERVEALLSEMICOLONS.getName(), "prop.YUI.JavaScriptCompressor.preserveAllSemiColons");
		Assert.assertEquals(Property.YUI_JAVASCRIPT_COMPRESSOR_DISABLEOPTIMISATIONS.getName(), "prop.YUI.JavaScriptCompressor.disableOptimisations");
		Assert.assertEquals(Property.YUI_OMIT_FILES_FROM_MINIFICATION_REGEX.getName(), "prop.YUI.OmitFilesFromMinificationRegEx");
		Assert.assertEquals(Property.values().length, 16);
	}
}
