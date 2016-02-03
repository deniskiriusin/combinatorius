/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

/**
 * MIME types the servlet can handle: <br>
 * 
 * <pre>
 * 1) css("text/css")
 * 2) js("text/javascript")
 * </pre>
 */
enum MimeType {

	/**
	 * The property defines <code>text/css</code> MIME type
	 */
	css("text/css"),

	/**
	 * The property defines <code>text/javascript</code> MIME type
	 */
	js("text/javascript");

	private final String name;

	MimeType(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}
}
