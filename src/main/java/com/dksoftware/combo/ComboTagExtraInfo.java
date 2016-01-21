/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

public class ComboTagExtraInfo extends TagExtraInfo {

	@Override
	public ValidationMessage[] validate(TagData data) {
		final String type = data.getAttributeString("type");
		final Object pathObject = data.getAttribute("path");
		final Object themeObject = data.getAttribute("theme");
		final Object resourcesObject = data.getAttribute("csv_resources");

		// validate path
		if (pathObject != null && TagData.REQUEST_TIME_VALUE != pathObject) {
			final String path = pathObject.toString().toLowerCase();
			if ((path.startsWith("/") && path.endsWith("/")) || (path.startsWith("${") && path.endsWith("}"))) {
				return null;
			} else {
				return new ValidationMessage[] { new ValidationMessage(null, "Path must match '/path/' or '${path}' pattern") };
			}
		}

		// validate type
		if (!type.equalsIgnoreCase("css") && !type.equalsIgnoreCase("js")) {
			return new ValidationMessage[] { new ValidationMessage(null, "Type must be either 'css' or 'js'") };
		}

		// validate that resources correspond to type
		if (resourcesObject != null && TagData.REQUEST_TIME_VALUE != resourcesObject) {
			final String resources = resourcesObject.toString().toLowerCase();
			final String[] filePaths = resources.split(",");
			for (String fp : filePaths) {
				if (type.equalsIgnoreCase("css") && !fp.endsWith(".css")) {
					return new ValidationMessage[] {
							new ValidationMessage(null, "Resources must contain .css files only") };
				} else if (type.equalsIgnoreCase("js") && !fp.endsWith(".js")) {
					return new ValidationMessage[] {
							new ValidationMessage(null, "Resources must contain .js files only") };
				}
			}
			// validate that resources attribute's value has no spaces
			if (resources != null && resources.contains(" ")) {
				return new ValidationMessage[] { new ValidationMessage(null, "Resources must not contain white spaces") };
			}
		}

		// validate that theme attribute's value has no spaces
		if (themeObject != null && TagData.REQUEST_TIME_VALUE != themeObject) {
			final String theme = themeObject.toString().toLowerCase();
			if (theme != null && theme.contains(" ")) {
				return new ValidationMessage[] { new ValidationMessage(null, "Theme must not contain white spaces") };
			}
		}

		return null;
	}
}
