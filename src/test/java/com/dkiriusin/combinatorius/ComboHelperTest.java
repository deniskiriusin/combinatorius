/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dkiriusin.combinatorius.ComboHelper;
import com.dkiriusin.combinatorius.ComboServlet;
import com.dkiriusin.combinatorius.MimeType;
import com.dkiriusin.combinatorius.Property;
import com.dkiriusin.combinatorius.RequestDetails;

@RunWith(MockitoJUnitRunner.class)
public class ComboHelperTest {

	@Rule
	public TemporaryFolder tp = new TemporaryFolder();
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@InjectMocks
	private final ComboServlet servlet = new ComboServlet();
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private RequestDetails requestDetailsObject;
	@Mock
	private ThreadLocal<RequestDetails> requestDetails;
	@Mock
	private Properties properties;
	@Mock
	private ComboHelper helper;

	private final Set<File> mocked_files = new LinkedHashSet<File>();
	private final Set<File> files = new LinkedHashSet<File>();

	byte[] bytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24,
			25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78,
			79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
			35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
			62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88,
			89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
			45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
			72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98,
			99, 100, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27,
			28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54,
			55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
			82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64,
			65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91,
			92, 93, 94, 95, 96, 97, 98, 99, 100, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
			20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
			48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
			75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 0,
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30,
			31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
			58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
			85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100 };

	@Before
	public void setUp() throws URISyntaxException, IOException {
		mocked_files.addAll(TestUtils.getMockedFiles());
		Mockito.when(requestDetailsObject.getMimeType()).thenReturn(MimeType.css);
		Mockito.when(requestDetailsObject.getResources()).thenReturn(TestUtils.URL);
		Mockito.when(requestDetailsObject.getThemeName()).thenReturn("test-theme");
		Mockito.when(requestDetailsObject.getVersion()).thenReturn(0L);
		Mockito.when(requestDetailsObject.getExtension()).thenReturn("css");
		Mockito.when(requestDetails.get()).thenReturn(requestDetailsObject);

		tp.newFolder("css");
		File cssFolder = tp.newFolder("css/extra_css");
		File f1 = cssFolder.toPath().resolve("extra1.css").toFile();
		File f2 = cssFolder.toPath().resolve("extra2.css").toFile();
		TestUtils.writeTo(f1.getPath(), "body {background-color: white}");
		TestUtils.writeTo(f2.getPath(), "body {background-color: black}");
		files.addAll(new LinkedHashSet<File>(Arrays.asList(f1, f2)));
	}

	@After
	public void tearDown() {
		mocked_files.clear();
	}

	@Test
	public void testGetQueryStringParametersAlwaysReturnsMap() {
		Assert.assertTrue("Should always return Map, never null",
				(ComboHelper.getInstance().getQueryStringParameters(null) instanceof Map<?, ?>));
	}

	@Test
	public void testGetQueryStringParameters() {
		Assert.assertTrue("Map should contain [type->css] key-value",
				ComboHelper.getInstance().getQueryStringParameters(TestUtils.URL).get("type").equalsIgnoreCase("css"));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetQueryStringParameters_ArrayIndexOutOfBoundsException() {
		ComboHelper.getInstance().getQueryStringParameters("/incorrect&/URL");
	}

	@Test
	public void testGenerateEtagValue() {
		Assert.assertEquals("Etag should be correct", ComboHelper.getInstance().generateEtagValue(mocked_files, 0L, false),
				"\"" + TestUtils.EXPECTED_ETAG + "\"");
	}

	@Test
	public void testGenerateEtagValueWithCompressionEnabled() {
		Assert.assertFalse("Etag should be different when compression enabled", ComboHelper.getInstance()
				.generateEtagValue(mocked_files, 0L, true).equals("\"" + TestUtils.EXPECTED_ETAG + "\""));
	}

	@Test
	public void testGenerateEtagValueChangesIfLastModified() {
		Assert.assertTrue("Etag should be differend with different last modified values", !ComboHelper.getInstance()
				.generateEtagValue(mocked_files, 1500000000000L, false).equals("\"" + TestUtils.EXPECTED_ETAG + "\""));
	}

	@Test
	public void testGenerateEtagValueChangesIfFilePathsChanges() {
		for (File file : mocked_files) {
			if (file.getName().equals("mocked_file_1")) {
				Mockito.when(file.getAbsolutePath()).thenReturn("mocked_file_1_AbsolutePath__changed");
			}
		}
		Assert.assertTrue("Etag should be differend when file stucture has been changed or one or more files modified",
				!ComboHelper.getInstance().generateEtagValue(mocked_files, 0L, false)
						.equals("\"" + TestUtils.EXPECTED_ETAG + "\""));
	}

	@Test
	public void testParseQueryString() throws IOException {
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(TestUtils.URL));
		Assert.assertTrue("Map should contain [type->css] key-value",
				(ComboHelper.getInstance().parseQueryString(request).get("type").equalsIgnoreCase("css")));
		Assert.assertTrue("Map should contain [theme->blue] key-value",
				(ComboHelper.getInstance().parseQueryString(request).get("theme").equalsIgnoreCase("blue")));
	}

	@Test
	public void testGetRequestDetails() throws IOException {
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(TestUtils.URL));
		RequestDetails requestDetails = ComboHelper.getInstance().getRequestDetails(request);
		Assert.assertTrue("RequestDetails.type should be 'css'", requestDetails.getMimeType() == MimeType.css);
		Assert.assertTrue(requestDetails.getResources().equals(TestUtils.URL_RESOURCES));
		Assert.assertTrue("RequestDetails.theme should be 'blue'", requestDetails.getThemeName().equals("blue"));
		Assert.assertTrue("RequestDetails.version should be 16546544", requestDetails.getVersion() == 16546544L);
	}

	@Test
	public void testGetRequestDetailsWithThemeCookieSet() throws IOException {
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(TestUtils.URL_NO_THEME));
		Cookie cookie = new Cookie(ComboServlet.combinatoriusTheme, "green");
		Mockito.when(request.getCookies()).thenReturn(new Cookie[] { cookie });
		RequestDetails requestDetails = ComboHelper.getInstance().getRequestDetails(request);
		Assert.assertTrue("RequestDetails.type should be 'css'", requestDetails.getMimeType() == MimeType.css);
		Assert.assertTrue(requestDetails.getResources().equals(TestUtils.URL_RESOURCES));
		Assert.assertTrue("RequestDetails.theme should be 'green'", requestDetails.getThemeName().equals("green"));
		Assert.assertTrue("RequestDetails.version should be 16546544", requestDetails.getVersion() == 16546544L);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetRequestDetailsNeverReturnsNull() throws IOException {
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(""));
		ComboHelper.getInstance().getRequestDetails(request);
	}

	@Test(expected = NullPointerException.class)
	public void testGetCombinedFileNameNeverReturnsNull() throws IOException {
		Assert.assertTrue("getCombinedFileName() should never return null",
				ComboHelper.getInstance().getCombinedFileName(null, null) != null);
	}

	@Test
	public void testGetCombinedFileNameCompressionEnabled() throws IOException, URISyntaxException {
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(TestUtils.URL));
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
		RequestDetails requestDetails = ComboHelper.getInstance().getRequestDetails(request);
		Assert.assertTrue("getCombinedFileName() should return 'effce7dc7eabc4dc38928f832ad2d2.css.cmb.gzip'",
				ComboHelper.getInstance().getCombinedFileName(requestDetails, TestUtils.EXPECTED_ETAG)
						.equals("effce7dc7eabc4dc38928f832ad2d2.css.cmb.gzip"));
	}

	@Test
	public void testGetCombinedFileNameCompressionDisabled() throws IOException, URISyntaxException {
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(TestUtils.URL));
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("false");
		RequestDetails requestDetails = ComboHelper.getInstance().getRequestDetails(request);
		Assert.assertTrue("getCombinedFileName() should return 'effce7dc7eabc4dc38928f832ad2d2.css.cmb'",
				ComboHelper.getInstance().getCombinedFileName(requestDetails, TestUtils.EXPECTED_ETAG)
						.equals("effce7dc7eabc4dc38928f832ad2d2.css.cmb"));
	}

	@Test
	public void testCompressConditionallyEncodingFalsePropertyTrue() throws IOException {
		Mockito.when(response.containsHeader("Content-Encoding")).thenReturn(false);
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
		Assert.assertTrue(
				"Compression should be enabled when 'Content-Encoding' response header is not set and "
						+ "prop.isCompressionEnabled property set to true",
				ComboHelper.getInstance().compressConditionally(bytes, response).length < 200);
	}

	@Test
	public void testCompressConditionallyEncodingTruePropertyTrue() throws IOException {
		Mockito.when(response.containsHeader("Content-Encoding")).thenReturn(true);
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
		Assert.assertTrue(
				"Compression should be disabled when 'Content-Encoding' response header is set and "
						+ "prop.isCompressionEnabled property set to true",
				ComboHelper.getInstance().compressConditionally(bytes, response).length == 700);
	}

	@Test
	public void testCompressConditionallyEncodingFalsePropertyFalse() throws IOException {
		Mockito.when(response.containsHeader("Content-Encoding")).thenReturn(false);
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("false");
		Assert.assertTrue(
				"Compression should be disabled when 'Content-Encoding' response header is not set and "
						+ "prop.isCompressionEnabled property set to false",
				ComboHelper.getInstance().compressConditionally(bytes, response).length == 700);
	}

	@Test(expected = NullPointerException.class)
	public void testCompressConditionallyNeverReturnsNull() throws IOException {
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");
		ComboHelper.getInstance().compressConditionally(null, response);
	}

	@Test
	public void testCompressConditionallyEncodingTruePropertyFalse() throws IOException {
		Mockito.when(response.containsHeader("Content-Encoding")).thenReturn(true);
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("false");
		Assert.assertTrue(
				"Compression should be disabled when 'Content-Encoding' response header is set and "
						+ "prop.isCompressionEnabled property set to false",
				ComboHelper.getInstance().compressConditionally(bytes, response).length == 700);
	}

	@Test(expected = IOException.class)
	public void testGetContentNeverReturnsNull() throws IOException {
		ComboHelper.getInstance().getContent(null, null, null, null);
	}

	@Test
	public void testGetContentReturnsActualContent() throws IOException {
		Mockito.when(properties.getProperty(Property.CSS_DIR.getName())).thenReturn("src/test/resources/css");
		Mockito.when(properties.getProperty(Property.CSS_CACHE_DIR.getName()))
				.thenReturn("src/test/resources/css_cache");
		Mockito.when(properties.getProperty(Property.IS_COMPRESSION_ENABLED.getName())).thenReturn("true");

		byte[] content = ComboHelper.getInstance().getContent(requestDetailsObject, files, TestUtils.EXPECTED_ETAG,
				response);
		Assert.assertTrue("The content length returned by the method should be higher than 10", content.length > 10);
	}
	
	@Test
	public void testGetContentIsCachingFilesAsExpected() {
		final String[] extentions = new String[] { "gzip" };
		final Collection<File> files = FileUtils.listFiles(new File("src/test/resources/css_cache"), extentions, true);
		Assert.assertTrue("One cached and gzipped .css file should exist", files.size() == 1);
		// remove generated file from local cache
		for (final File f : files) {
			f.delete();
		}
	}

}
