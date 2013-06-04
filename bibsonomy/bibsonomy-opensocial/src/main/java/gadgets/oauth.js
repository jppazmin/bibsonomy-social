var dateFormatString = "MMM dd, yyyy, hh:mm a"; //Dec 10, 1815, 12:00 AM

function $(x) {
    return document.getElementById(x);
}

function showOneSection(toshow) {
    var sections = [ 'main', 'approval', 'waiting' ];
    for (var i=0; i < sections.length; ++i) {
	var s = sections[i];
	var el = $(s);
	if (s === toshow) {
	    el.style.display = "block";
	} else {
	    el.style.display = "none";
	}
    }
}

// parse and format a date
function parsePostDate(dateString) {
    if (!dateString) {
	dateString = "0";
    }
    // parseDate doesn't know milliseconds
    dateString = dateString.substring(0,19);
    var testDate = new Date(getDateFromFormat(dateString, 'yyyy-MM-ddTHH:mm:ss'));
    return formatDate(testDate, dateFormatString);
}

// does some data manipulation which is not possible using templates
// as the template library does not support function calls in the 
// library's expression language
function preparePosts(posts) {
    // beautify dates
    for (i=0; i<posts.length; i++) {
	posts[i].changedate  = parsePostDate(posts[i].changedate);
	posts[i].postingdate = parsePostDate(posts[i].postingdate);
    }
}

function fetchData(url) {
    os.Loader.loadUrl('http://folke.mitzlaff.org/os/opensociallib.xml?rnd='+Math.random(), function(){});
    os.Loader.loadUrl('http://folke.mitzlaff.org/os/bibsonomylib.xml?rnd='+Math.random(), function(){});

    if (url==null) {
	url = "http://www.bibsonomy.org/api/users/folke/posts?resourcetype=bibtex&format=json&start=0&end=4";
    } else {
	url = "http://www.bibsonomy.org/api"+url;
    }
    //"http://folke.biblicious.org/social/rest/people/@me/@friends";
    //"http://folke.biblicious.org/api/users/folke/posts?resourcetype=bibtex&format=json";
    var params = {};
    params[gadgets.io.RequestParameters.CONTENT_TYPE]       = gadgets.io.ContentType.JSON;
    params[gadgets.io.RequestParameters.AUTHORIZATION]      = gadgets.io.AuthorizationType.OAUTH;
    params[gadgets.io.RequestParameters.METHOD]             = gadgets.io.MethodType.GET;
    params[gadgets.io.RequestParameters.OAUTH_SERVICE_NAME] = "BibSonomy";

    gadgets.io.makeRequest(url, function (response) {
	    if (response.oauthApprovalUrl) { 
		var onOpen = function() {showOneSection('waiting'); };
          				
		var onClose = function() { fetchData(); };
            				
		var popup = new gadgets.oauth.Popup(response.oauthApprovalUrl,null, onOpen, onClose);
            				
		$('personalize').onclick = popup.createOpenerOnClick();
		$('approvaldone').onclick = popup.createApprovedOnClick();
		showOneSection('approval');
	    } else if (response.data) {
		// handle data
		
		// data = new Object();
		// data.friends = response.data.entry;
		// os.Container.processDocument(data);

		preparePosts(response.data.posts.post);

		//os.Container.processDocument(response.data);
		var template = os.getTemplate('bibtexList');
		template.renderInto(document.getElementById('gadgetContent'), response.data);
				
		// show data
		showOneSection('main');
		gadgets.window.adjustHeight();
	    } else {
		var whoops = document.createTextNode('OAuth error: ' + response.oauthError + ': ' +response.oauthErrorText);
		$('main').appendChild(whoops);
		showOneSection('main');
	    }
	}, params);
}

function clearResult() {
    // delte old dom tree if exists
    var oldView = $('gadgetContent');
    if (oldView && oldView.hasChildNodes) {
	while (oldView.childNodes.length >= 1) {
	    oldView.removeChild( oldView.firstChild );       
	} 
    }
}	
