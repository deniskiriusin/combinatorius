# Combinatorius &mdash; CSS and JavaScript concatenation tool 

<a href="https://travis-ci.org/deniskiriusin/combinatorius"><img src="https://travis-ci.org/deniskiriusin/combinatorius.svg?branch=master"/></a>

### Demo: <a href="http://combinatorius.dkiriusin.com" target="_blank">http://combinatorius.dkiriusin.com</a>

## Table of contents

- [Key features](#key-features)
- [Quick start](#quick-start)
- [Contributing](#contributing)

## Key features
* Combined files to reduce the number of HTTP requests by combining all scripts into a single script, and similarly combining all CSS into a single stylesheet.
* Local caching of the combined files for even better response times.
* Appropriate `Expires` and `Cache-Control` headers to help browser with conditional requests.
* `ETag` support to determine whether the component in the browser's cache matches the one on the origin server.
* Gzip compression to reduce response times by reducing the size of the HTTP response.
* YUI Compressor support
* Fingerprinting a.k.a static web resources versioning.
* Themes support (set via URL parameter or cookies).
* Flexible directory structure.
* Simple configuration.

## Quick start

The library is available from the Central Repository and easy to setup.

##### 1. Add dependencied

```xml
<dependency>
    <groupId>com.dkiriusin</groupId>
    <artifactId>combinatorius</artifactId>
    <version>1.0.60</version>
</dependency>
```
You have to add YUI Compressor as well in case you want to take advantage of YUI minification.  
```xml
<dependency>
    <groupId>com.yahoo.platform.yui</groupId>
    <artifactId>yuicompressor</artifactId>
    <version>2.4.8</version>
</dependency>
```

##### 2. Register new servlet

```xml
<servlet>
    <servlet-name>Combinatorius</servlet-name>
    <servlet-class>com.dkiriusin.combinatorius.CombinatoriusServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>Combinatorius</servlet-name>
    <url-pattern>/combo/*</url-pattern>
</servlet-mapping>
```

##### 3. Create `combinatorius.properties` file and place it in the Classpath

```
#---------------------#
# required properties #
#---------------------#

# root CSS directory
prop.css.dir = /var/lib/tomcat7/webapps/my_project/css
# cached CSS directory
prop.css.cache.dir = /var/lib/tomcat7/webapps/my_project/css_cache
# root JS directory
prop.js.dir = /var/lib/tomcat7/webapps/my_project/js
# cached JS directory
prop.js.cache.dir = /var/lib/tomcat7/webapps/my_project/js_cache

#---------------------#
# optional properties #
#---------------------#

# themes root directory
prop.themes.dir = /var/lib/tomcat7/webapps/my_project/themes
# Cache-Control: s-maxage directive (31536000 by default)
prop.s-maxage = 31536000
# Cache-Control: max-age directive (31536000 by default)
prop.max-age = 31536000
# Enables gzip compression (true by default)
prop.isCompressionEnabled = true
# Enables YUI compressor (true by default)
prop.isYUICompressorEnabled = false
# Insert line breaks in output after the specified column number (-1 by default)
prop.YUI.CSSCompressor.linebreakpos = -1
# Splits long lines after a specific column (100 by default)
prop.YUI.JavaScriptCompressor.linebreak = 100
# Minify only, do not obfuscate (false by default)
prop.YUI.JavaScriptCompressor.nomunge = false
# verbose output (false by default)
prop.YUI.JavaScriptCompressor.verbose = false
# Preserve unnecessary semicolons (such as right before a '}') (false by default)
prop.YUI.JavaScriptCompressor.preserveAllSemiColons = true
# Disable all the built-in micro optimizations (true by default)
prop.YUI.JavaScriptCompressor.disableOptimisations = true
# Define files to be omitted of minification ('.*\.min\.(js|css)$' by default)
prop.YUI.OmitFilesFromMinificationRegEx = .*\.min\.(js|css)$
```
Combinatorius reads all CSS and JavaScript resources from `prop.css.dir` and `prop.js.dir` directories recursively in alphabetic order and caches them in `prop.css.cache.dir` and `prop.js.cache.dir` directories respectively.

```
dir/
├── css/
│   ├── main.css
│   ├── layout.css
├── css_cache/
│   ├── 5f87023f44e39b9e735111b02bd7a40e.css.cmb.gzip
├── js/
│   ├── jquery-1.11.3.min.js
│   └── jquery-ui.js
└── js_cache/
    └── e04df45335c9227366384ba3994663ec.js.cmb.gzip
```

Minified and compressed content sent back to client with all necessary HTTP headers set. Resources corresponding the regular expression `prop.YUI.OmitFilesFromMinificationRegEx` are not minified by YUI Compressor.

### CSS themes support

CSS theme represents `prop.themes.dir` sub-directory with one or more CSS resources. For example `prop.themes.dir/green/theme.css`. The name of the theme must match the name of the sub-directory and can be set via theme URL parameter or `combinatorius.theme` Cookies value.

`/combo/&type=css&theme=green`

### Additional resources

It might be useful for testing purposes or with rarely used resources that should not be included in to main assembly by default.

`/combo/&type=js&resources=extra_js/extra1.js,extra_js/extra2.js`

### JSP tag

It is recommended to use JSP tag in order to generate a valid Combinatorius URL. One tag for CSS and one for JavaScript respectively. The only mandatory attributes are `type` and `path`.
```xml
<%@ taglib uri="https://github.com/deniskiriusin/combinatorius" prefix="cb" %>

<cb:combo type="css" path="/combo">
    <jsp:attribute name="theme">blue</jsp:attribute>
    <jsp:attribute name="csv_resources">extra_css/extra1.css,extra_css/extra2.css</jsp:attribute>
</cb:combo>

<cb:combo type="js" path="${path}"></cb:combo>
```
Using JSP tag has some additional benefits like static web resources versioning for aggressive caching (cache busting).

`/combo/&type=js&v=1465737376000`

## Contributing

Pull requests are welcomed.
