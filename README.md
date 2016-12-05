<h1>Combinatorius &mdash; CSS and JavaScript concatenation tool</h1><a href="https://travis-ci.org/deniskiriusin/combinatorius"><img src="https://travis-ci.org/deniskiriusin/combinatorius.svg?branch=master"/></a>

<h2>Demo: <a href="http://combinatorius.dkiriusin.com" target="_blank">http://combinatorius.dkiriusin.com</a></h2>

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

[User's Manual](https://github.com/deniskiriusin/combinatorius/wiki/How-to-use)
