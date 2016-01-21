/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dksoftware.combo;

import org.junit.Assert;
import org.junit.Test;

public class MimeTypeTest {

	@Test
	public void testMimeTypeEnumValuesAreCorrect() {
		Assert.assertEquals(MimeType.css.getName(), "text/css");
		Assert.assertEquals(MimeType.js.getName(), "text/javascript");
		Assert.assertEquals(MimeType.values().length, 2);
	}

}
