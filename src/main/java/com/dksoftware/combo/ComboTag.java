/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ComboTag extends SimpleTagSupport {

	private String type;
	private String path;
	private String csv_resources;
	private String theme;

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().write(constructHTMLTag());
	}

	/**
	 * Constructs CSS or JavaScript tag string.<br>
	 * 
	 * <pre>
	 *{@code
	 *<link href="/combo/&type=css&resources=optional_css/opt_css_1.css&theme=blue&v=1443201442000" rel="stylesheet" type="text/css" />
	 *}
	 *{@code
	 *<script type="text/javascript" src="/combo/&type=js&v=1443201442000"></script>
	 *}
	 * </pre>
	 * 
	 * @return tag as a String
	 * @throws UnsupportedEncodingException
	 */
	String constructHTMLTag() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		if (getType().equalsIgnoreCase("css")) {
			sb.append("<link href=\"");
			sb.append(getPath());

			final String url = constructURLAttributes();
			sb.append(url);

			sb.append(" rel=\"stylesheet\"");
			sb.append(" type=\"");
			sb.append(MimeType.css.getName());
			sb.append("\" />");
		} else if (getType().equalsIgnoreCase("js")) {
			sb.append("<script type=\"");
			sb.append(MimeType.js.getName());
			sb.append("\"");
			sb.append(" src=\"");
			sb.append(getPath());

			final String url = constructURLAttributes();
			sb.append(url);

			sb.append("></script>");
		}
		return sb.toString();
	}

	/**
	 * Constructs URL parameters in the following format: <br>
	 * <code>resources=file_1.css,embedded/file_2.css&theme=green&v=16546547</code>.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	String constructURLAttributes() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("&type=");
		sb.append(URLEncoder.encode(getType(), "UTF-8"));
		if (getResources() != null && getResources() != "") {
			sb.append("&resources=");
			sb.append(getResources());
		}
		if (getTheme() != null && getTheme() != "") {
			sb.append("&theme=");
			sb.append(URLEncoder.encode(getTheme(), "UTF-8"));
		}
		PageContext pageContext = (PageContext) getJspContext();
		ServletContext servletContext = pageContext.getServletContext();
		Long v = (Long) servletContext.getAttribute("com.dksoftware.combo.v_" + getType());
		if (v == null) {
			v = System.currentTimeMillis();
		}
		sb.append("&v=");
		sb.append(v.toString());
		sb.append("\"");
		return sb.toString();
	}

	// getters and setters

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getResources() {
		return csv_resources;
	}

	public void setCsv_resources(String csv_resources) {
		this.csv_resources = csv_resources;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

}
