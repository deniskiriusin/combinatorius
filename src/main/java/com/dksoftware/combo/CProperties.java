/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

/**
 * The names of the properties defined in <code>combinatorius.properties</code> file.
 */
enum CProperties {

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
	IS_COMPRESSION_ENABLED("prop.isCompressionEnabled");

	private final String name;

	CProperties(final String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}

}
