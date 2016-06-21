<h1>Combinatorius &mdash; CSS and JavaScript concatenation tool</h1><a href="https://travis-ci.org/deniskiriusin/combinatorius"><img src="https://travis-ci.org/deniskiriusin/combinatorius.svg?branch=master"/></a>

<h2>Key features:</h2>
* Combined files to reduce the number of HTTP requests by combining all scripts into a single script, and similarly combining all CSS into a single stylesheet.
* Local caching of the combined files for even better response times.
* Appropriate <code>Expires</code> and <code>Cache-Control</code> headers to help browser with conditional requests.
* <code>ETag</code> support to determine whether the component in the browser's cache matches the one on the origin server.
* Gzip compression to reduce response times by reducing the size of the HTTP response.
* YUI Compressor support
* Fingerprinting a.k.a static web resources versioning.
* Themes support (set via URL parameter or cookies).
* Flexible directory structure support.
* Simple configuration, usage and deployment.

<h2>JSP tag:</h2>
<pre>
&lt;%@ taglib uri="https://github.com/deniskiriusin/combinatorius" prefix="cb" %&gt;

&lt;cb:combo type=&quot;css&quot; path=&quot;${path}&quot;&gt;
&nbsp;&nbsp;&lt;jsp:attribute name=&quot;theme&quot;&gt;blue&lt;/jsp:attribute&gt;
&nbsp;&nbsp;&lt;jsp:attribute name=&quot;csv_resources&quot;&gt;extra_css/extra1.css,extra_css/extra2.css&lt;/jsp:attribute&gt;
&lt;/cb:combo&gt;

&lt;cb:combo type="js" path="${path}"&gt;&lt;/cb:combo&gt;
</pre>

<h2>Maven dependency:</h2>
<pre>
&lt;dependency&gt;
    &lt;groupId&gt;com.dkiriusin&lt;/groupId&gt;
    &lt;artifactId&gt;combinatorius&lt;/artifactId&gt;
    &lt;version&gt;1.0.56&lt;/version&gt;
&lt;/dependency&gt;
</pre>
