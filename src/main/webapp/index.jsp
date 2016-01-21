<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="https://github.com/deniskiriusin/combinatorius" prefix="cb" %>
<%@ page session="false" %>

<html>
<title>&#8721;&nbsp;&ndash;&nbsp;Combinatorius</title>
<head>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">

<c:set var="path" scope="page" value="/combinatorius/combo/"/>
<c:set var="theme" scope="page" value="blue"/>
<c:set var="resources" scope="page" value="extra_css/extra1.css"/>

<link href="extra_css/font-awesome.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="extra_js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="extra_js/jquery.animate-colors-min.js"></script>

<cb:combo type="css" path="${path}">
	<jsp:attribute name="theme">${theme}</jsp:attribute>
	<jsp:attribute name="csv_resources">${resources}</jsp:attribute>
</cb:combo>
<cb:combo type="js" path="${path}">
	<jsp:attribute name="csv_resources">extra_js/extra1.js</jsp:attribute>
</cb:combo>
</head>

<body>

	<div id="wrapper">

		<h1>Combinatorius &mdash; an effective tool for delivering CSS and JavaScript files</h1>
		
		<div id="themes">
			<ul>
				<li id="blue_theme"></li>
				<li id="green_theme"></li>
			</ul>
		</div>

		<div id="get">
		
			<div class="outer left" id="js_url">
				<div class="url_div"><h3>Generated URL:</h3><span><!-- JavaScript URL --></span></div>
				<div class="response_name_div"><h3>Response Headers <span>(JavaScript)</span></h3></div>
				<div class="inner"><!-- dynamic content --></div>
				<div class="controls_div">
					<button type="button" id="f5_js" class="button big">F5 refresh</button>
					<button type="button" id="ctrl_f5_js" class="button big">Ctrl+F5 refresh</button>
					<button type="button" id="add_file_js" class="button big">Add file</button>
					<button type="button" id="touch_file_js" class="button big">Modify file</button>
				</div>
			</div>
			
			<div class="outer right" id="css_url">
				<div class="url_div"><h3>Generated URL:</h3><span><!-- CSS URL --></span></div>
				<div class="response_name_div"><h3>Response Headers <span>(CSS)</span></h3></div>
				<div class="inner"><!-- dynamic content --></div>
				<div class="controls_div">
					<button type="button" id="f5_css" class="button big">F5 refresh</button>
					<button type="button" id="ctrl_f5_css" class="button big">Ctrl+F5 refresh</button>
					<button type="button" id="add_file_css" class="button big">Add file</button>
					<button type="button" id="touch_file_css" class="button big">Modify file</button>
				</div>
			</div>
			
			<div class="outer left" id="help">
				<p><span>F5 refresh</span> &mdash; send generic cachable request.</p>
				<p><span>Ctrl+F5 refresh</span> &mdash; send non-cacheble request.</p>
				<p><span>Add file</span> &mdash; add new JavaScript or CSS file to directory tree.</p>
				<p><span>Delete file</span> &mdash; delete JavaScript or CSS file from directory tree.</p>
				<p><span>Modify file</span> &mdash; modifies <i>"jquery-ui.js"</i> or <i>"layout.css"</i> file respectively.</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p><span class="features">Key features</span></p>
				<ul>
					<li>&mdash;&nbsp;Combined files to reduce the number of HTTP requests by combining all scripts into a single script, and similarly combining all CSS into a single stylesheet.</li>
					<li>&mdash;&nbsp;Local caching of the combined files for even better response times.</li>
					<li>&mdash;&nbsp;Appropriate <code>Expires</code> and <code>Cache-Control</code> headers to help the browser with conditional requests.</li>
					<li>&mdash;&nbsp;<code>ETag</code> support to determine whether the component in the browser's cache matches the one on the origin server.</li>
					<li>&mdash;&nbsp;Gzip compression to reduce response times by reducing the size of the HTTP response.</li>
					<li>&mdash;&nbsp;Fingerprinting a.k.a static web resources versioning.</li>
					<li>&mdash;&nbsp;Flexible directory structure support.</li>
					<li>&mdash;&nbsp;Flexible configuration.</li>
					<li>&mdash;&nbsp;Simple usage and deployment.</li>
				</ul>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p><span class="features">JSP tag</span></p>
				<ul>
					<li>
<pre>
&lt;cb:combo type=&quot;css&quot; path=&quot;\${path}&quot;&gt;
&nbsp;&nbsp;&lt;jsp:attribute name=&quot;theme&quot;&gt;blue&lt;/jsp:attribute&gt;
&nbsp;&nbsp;&lt;jsp:attribute name=&quot;csv_resources&quot;&gt;extra_css/extra1.css,extra_css/extra2.css&lt;/jsp:attribute&gt;
&lt;/cb:combo&gt;
</pre>
					</li>
				</ul>
			</div>
			
			<div class="outer right" id="directory_tree">
				<div id="tree">
					<div><p><strong>Directory tree</strong></p></div>
					<div class="tree">
					  <ul>
					    <!-- web-app -->
					    <li><i class="fa fa-folder-open-o"></i><a class="node">WebApp</a>
					      <ul>
					        <!-- css -->
					        <li><i class="fa fa-folder-open-o"></i><a>css</a>
					          <ul>
					            <li><i class="fa fa-folder-o"></i><a>main.css</a></li>
					            <li><i class="fa fa-folder-o"></i><a>buttons.css</a>
					            	<ul>
					            		<li><i class="fa fa-folder-open-o"></i><a class="node">layout</a>
					            			<ul>
					            				<li><i class="fa fa-folder-o"></i><a>layout.css</a></li>
					            				<!-- add CSS file -->
					            			</ul>
					            		</li>
					            	</ul>
					            </li>
					          </ul>
					        </li>
					        <li><i class="fa fa-folder-open-o"></i><a>extra_css</a>
						        <ul>
						        	<li><i class="fa fa-folder-o"></i><a>extra1.css</a></li>
						        </ul>
					        </li>
					        <!-- js -->
					        <li><i class="fa fa-folder-open-o"></i><a>js</a>
					          <ul>
					            <li><i class="fa fa-folder-o"></i><a>jquery-ui.js</a></li>
					            <li><i class="fa fa-folder-o"></i><a>custom.js</a></li>
					            <!-- add JS file -->
					          </ul>
					        </li>
					        <li><i class="fa fa-folder-open-o"></i><a>extra_js</a>
					          <ul>
					            <li><i class="fa fa-folder-o"></i><a>extra1.js</a></li>
					            <!-- add JS file -->
					          </ul>
					        </li>
					        <li><i class="fa fa-folder-open-o"></i><a>themes</a>
						        <ul>
						        	<li><i class="fa fa-folder-open-o"></i><a>blue</a>
						        		<ul>
						        			<li><i class="fa fa-folder-open-o"></i><a>css</a>
						        				<ul>
						        					<li><i class="fa fa-folder-o"></i><a>main.css</a></li>
						        				</ul>
						        			</li>
						        		</ul>
						        	</li>
						        	<li><i class="fa fa-folder-open-o"></i><a>green</a>
						        		<ul>
						        			<li><i class="fa fa-folder-open-o"></i><a>css</a>
						        				<ul>
						        					<li><i class="fa fa-folder-o"></i><a>main.css</a></li>
						        				</ul>
						        			</li>
						        		</ul>
						        	</li>
						        </ul>
					        </li>
					      </ul>
					    </li>
					  </ul>
					</div>
				</div>
			</div>

		</div>
		
		<div id="footer">
			<jsp:useBean id="date" class="java.util.Date" />
			<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
			<p><c:out value="${currentYear}" />&nbsp;&copy;<a href="mailto:deniskir@gmail.com">&nbsp;Denis Kiriusin</a></p>
		</div>

	</div>
	
<script type="text/javascript">
	$(function() {
		
		get_div = $("div#get");
		
		function getUrlWithNoVersion(url) {
			return url.substring(0, url.indexOf('v='));
		}
		
		function getVersionFromURL(url) {
			var index1 = getUrlWithNoVersion(url).length + 2;
			return url.substring(index1, index1 + 13);
		}
		
		function showSatusCode(xhr, event) {
			var h3 = $('div.response_name_div h3', '#' + event.data.obj.type + '_url');
			$('i', h3).remove();
			var el = $("<i></i>").fadeIn(2000);
			if (xhr.status == 200) {
				el.html("200 OK");
			} else if (xhr.status == 304) {
				el.html("304 Not Modified");
			}
			h3.append(el);
		}

		function replaceURL(event) {
			var url = event.data.obj.url;
			var pos = url.indexOf('&theme');
			pos = (pos == -1 ? url.indexOf('&v') : pos); 
			var insert = ',extra_' + event.data.obj.type + '/extra2.' + event.data.obj.type + '&';
			if (url.indexOf('extra2') != -1) {
				url = url.replace(insert, '&');
			} else {
				url = [url.slice(0, pos), insert, url.slice(pos + 1)].join('');
			}
			event.data.obj.url = url;
		}

		function setCookies(event, name) {
			var object = {};
			object['name'] = name;
			object['type'] = event.data.obj.type;
			$.cookie('combinatorius.event', JSON.stringify(object));
		}

		function getDirectoryTreeUL(event) {
			return $('#tree li:has(a:contains(extra1.' + event.data.obj.type + ')) > ul').last();	
		}

		function refreshPage(event) {
			// make request
			setCookies(event, 'refresh');
			makeRequest(event);
		}

		function addFile(event) {
			replaceURL(event);
			// make request
			setCookies(event, 'add_file');
			makeRequest(event);
			// update directory tree
			var ul = getDirectoryTreeUL(event);
			var i = $('<i></i>').addClass('fa fa-folder-o');
			var a = $('<a>extra2.' + event.data.obj.type + '</a>').addClass('highlighted').fadeIn(1500);
			var li = $('<li></li>').fadeIn(1000);
			li.append(i).append(a);
			ul.append(li);
		}

		function removeFile(event) {
			replaceURL(event);
			// make request
			setCookies(event, 'remove_file');
			makeRequest(event);
			// update directory tree
			var ul = getDirectoryTreeUL(event);
			$('li:nth-child(2)', ul).fadeOut(1000, function() { $(this).remove(); });
		}

		function addOrDeleteFile(event) {
			if ($(this).text() === 'Delete file') {
				$(this).timedDisable(1000);
				removeFile(event);
				$(this).text('Add file');
			} else {
				$(this).timedDisable(2000);
				addFile(event);
				$(this).text('Delete file');
			}
		}

		function modifyFile(event) {
			$(this).timedDisable(2000);
			setCookies(event, 'modify_file');
			makeRequest(event);
			var fileName = 'layout.css';
			if (event.data.obj.type === 'js') {
				$('#tree li:has(a:contains(jquery-ui.js)) > a').last().blink();	
			} else {
				$('#tree li:has(a:contains(' + fileName + ')) > a').last().blink();
			}
		}

		function createDiv(xhr, data) {
			var headers = xhr.getAllResponseHeaders().split("\n");
			var v = xhr.getResponseHeader("Version");
			
			if (v == null || v == undefined) {
				v = getVersionFromURL(data.obj.url);
			}

			var inner_div = $('div.inner', '#' + data.obj.type + '_url');
			inner_div.empty();
			
			var url_div = $('div.url_div span', '#' + data.obj.type + '_url');
			url_div.empty();
			
			var el_i = $("<i>" + v + "</i>").fadeIn(2000);
			var p = $("<p>" + getUrlWithNoVersion(data.obj.url) + "v=</p>");
			p.append(el_i);
			if (v > data.obj.v) {
				el_i.addClass("highlighted");
				data.obj.v = v;
			}
			url_div.append(p.fadeIn('slow'));

			$.each(headers, function(key, value) {
				var p = $("<pre>" + value + "</pre>");
				if (!data.cache) {
					p.fadeIn('slow')
				}
	    		p.appendTo(inner_div);
	    	})
		}

		var js_last_modified;
		var js_eTag;
		var css_last_modified;
		var css_eTag;
		
		function makeRequest(event) {
			var xhr = $.ajax({
			    type: 'GET',		
			    cache: false,
			    ifModified: event.data.cache,
			    beforeSend: function(xhr){
			    	if (!event.data.cache) {
				    	xhr.setRequestHeader('Cache-Control', 'no-cache');
				    	xhr.setRequestHeader('Pragma', 'no-cache');
			    	} else {
		    			if (event.data.obj.type === 'js') {
		    				xhr.setRequestHeader('If-Modified-Since', js_last_modified);
		    				xhr.setRequestHeader('If-None-Match', js_eTag);
		    			} else if (event.data.obj.type === 'css') {
		    				xhr.setRequestHeader('If-Modified-Since', css_last_modified);
		    				xhr.setRequestHeader('If-None-Match', css_eTag);
		    			}
			    	}
		    	},
			    url: event.data.obj.url,
			    success: function(response) {
			    	createDiv(xhr, event.data);
			    },
			    complete: function() {
			    	if (event.data.obj.type === 'js') {
			    		js_last_modified = xhr.getResponseHeader("Last-Modified");
			    		if (xhr.getResponseHeader("ETag") != null) {
			    			js_eTag = xhr.getResponseHeader("ETag");
			    		}
	    			} else if (event.data.obj.type === 'css') {
	    				css_last_modified = xhr.getResponseHeader("Last-Modified");
	    				if (xhr.getResponseHeader("ETag") != null) {
			    			css_eTag = xhr.getResponseHeader("ETag");
	    				}
	    			}
			    	showSatusCode(xhr, event);
			    	event.data.refresh ? location.reload() : false;
			    },
			    error: function(e) {
			    	sweetAlert("Oops...", "Something went wrong!", "error");
			    }
			});
		}

		function changeTheme(event) {
		    $.cookie('combinatorius.theme', event.data.theme);
			makeRequest(event);
		}

		var js_url = $("script[src^='${path}']", "head").attr("src");
		var css_url = $("link[href^='${path}']", "head").attr("href");

		var js_version = getVersionFromURL(js_url);
		var css_version = getVersionFromURL(css_url);

		var css_obj = {
				url: css_url, 
				type: "css",
				v: css_version
			};
		
		var js_obj = {
				url: js_url, 
				type: "js", 
				v: js_version
			};
		
		function bindButtons() {
			$('button#f5_js', '#js_url').on('click', {cache: true, obj: js_obj}, refreshPage);
			$('button#ctrl_f5_js', '#js_url').on('click', {cache: false, obj: js_obj}, refreshPage);
			$('button#add_file_js', '#js_url').on('click', {cache: false, type: "js", obj: js_obj}, addOrDeleteFile);
			$('button#touch_file_js', '#js_url').on('click', {cache: false, type: "js", obj: js_obj}, modifyFile);
			
			$('button#f5_css', '#css_url').on('click', {cache: true, obj: css_obj}, refreshPage);
			$('button#ctrl_f5_css', '#css_url').on('click', {cache: false, obj: css_obj}, refreshPage);
			$('button#add_file_css', '#css_url').on('click', {cache: false, type: "css", obj: css_obj}, addOrDeleteFile);
			$('button#touch_file_css', '#css_url').on('click', {cache: false, type: "css", obj: css_obj}, modifyFile);
		}
		
		function bindThemes() {
			$('#blue_theme', '#themes').on('click', {cache: true, theme: "blue", refresh: true, obj: css_obj}, changeTheme);
			$('#green_theme', '#themes').on('click', {cache: true, theme: "green", refresh: true, obj: css_obj}, changeTheme);
		}
		
		bindButtons();
		bindThemes();
		
		// triggering CTRL+F5 buttons for CSS and JavaScript
		$('button#ctrl_f5_js', '#js_url').trigger("click");
		$('button#ctrl_f5_css', '#css_url').trigger("click");
	});
</script>


</body>

</html>