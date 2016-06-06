/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

/**
 * The names of the properties defined in <code>combinatorius.properties</code> file.
 */
enum Property {

	/**
	 * The property defines main CSS directory
	 */
	CSS_DIR("prop.css.dir"),

	/**
	 * The property defines directory to store cached CSS style sheets
	 */
	CSS_CACHE_DIR("prop.css.cache.dir"),

	/**
	 * The property defines main JavaScript directory
	 */
	JS_DIR("prop.js.dir"),

	/**
	 * The property defines directory to store cached JavaScript files
	 */
	JS_CACHE_DIR("prop.js.cache.dir"),

	/**
	 * The property defines directory to store themes
	 */
	THEMES_DIR("prop.themes.dir"),

	/**
	 * The property defines <code>s-maxage</code> HTTP header's value
	 */
	S_MAXAGE("prop.s-maxage"),

	/**
	 * The property defines <code>max-age</code> HTTP header's value
	 */
	MAX_AGE("prop.max-age"),

	/**
	 * The property defines if JavaScript and CSS compression enabled
	 */
	IS_COMPRESSION_ENABLED("prop.isCompressionEnabled"),

	/**
	 * The property defines if YUI compressor enabled
	 */
	IS_YUI_COMPRESSOR_ENABLED("prop.isYUICompressorEnabled"),

	/**
	 * The property defines the maximum length of CSS line after YUI
	 * minification
	 */
	YUI_CSSCOMPRESSOR_LINEBREAKPOS("prop.YUI.CSSCompressor.linebreakpos"),

	/**
	 * The property defines the maximum length of JavaScript line after YUI
	 * minification
	 */
	YUI_JAVASCRIPT_COMPRESSOR_LINEBREAK("prop.YUI.JavaScriptCompressor.linebreak"),

	/**
	 * The property defines if obfuscation will apply
	 */
	YUI_JAVASCRIPT_COMPRESSOR_MUNGE("prop.YUI.JavaScriptCompressor.munge"),

	/**
	 * The property defines the verboseness of YUI output
	 */
	YUI_JAVASCRIPT_COMPRESSOR_VERBOSE("prop.YUI.JavaScriptCompressor.verbose"),

	/**
	 * The property defines if all semicolons will be preserved
	 */
	YUI_JAVASCRIPT_COMPRESSOR_PRESERVEALLSEMICOLONS("prop.YUI.JavaScriptCompressor.preserveAllSemiColons"),

	/**
	 * The property defines if extra micro optimisation will take place during
	 * minification process
	 */
	YUI_JAVASCRIPT_COMPRESSOR_DISABLEOPTIMISATIONS("prop.YUI.JavaScriptCompressor.disableOptimisations"),

	/**
	 * The property defines a regular expression for the files to be omitted from minification
	 */
	YUI_OMIT_FILES_FROM_MINIFICATION_REGEX("prop.YUI.OmitFilesFromMinificationRegEx");

	private final String name;

	Property(final String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}

}
