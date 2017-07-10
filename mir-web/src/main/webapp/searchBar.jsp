<%@page import="java.util.ArrayList"%>

<script src="js/jquery-3.1.1.js"></script>

<script src="js/vendors/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="js/vendors/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="js/vendors/datatables.net-buttons/js/dataTables.buttons.min.js"></script>
<script src="js/vendors/datatables.net-buttons-bs/js/buttons.bootstrap.min.js"></script>
<script src="js/vendors/datatables.net-buttons/js/buttons.flash.min.js"></script>
<script src="js/vendors/datatables.net-buttons/js/buttons.html5.min.js"></script>
<script src="js/vendors/datatables.net-buttons/js/buttons.print.min.js"></script>
<script src="js/vendors/datatables.net-fixedheader/js/dataTables.fixedHeader.min.js"></script>
<script src="js/vendors/datatables.net-keytable/js/dataTables.keyTable.min.js"></script>
<script src="js/vendors/datatables.net-responsive/js/dataTables.responsive.min.js"></script>
<script src="js/vendors/datatables.net-responsive-bs/js/responsive.bootstrap.js"></script>
<script src="js/vendors/datatables.net-scroller/js/dataTables.scroller.min.js"></script>
<script src="js/vendors/datatables.net-select.js/dataTables.select.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	
	var maxRows = 5;
	var maxColumns = 4;
	var maxCells = maxRows*maxColumns;
	var dataSet = [];

	var MAX_LENGTH = 200;
	
	
	function showMirRecord(mirRecord, i) {

		var rows = [];
		
        // title
		var cells = [];
	    cells.push('<th class=\"elem-header\">' + '<b>' + "Title: " + '</b>' + mirRecord.sdocTitle + '</th>');
        rows.push('<tr>' + cells.join('') + '</tr>');
	    
		// picture
	    cells = [];
	    var imgLink = "";
	    var edmPreview = "";
	    var OLD_SYNTAX = "europeanastatic.eu/api/image?";
	    var NEW_SYNTAX = "europeana.eu/api/v2/thumbnail-by-url.json?size=w400&";
	    if (typeof(mirRecord.metadata.edmPreview) !== 'undefined' && mirRecord.metadata.edmPreview.length > 0) {
	    	edmPreview = mirRecord.metadata.edmPreview[0];
	    	edmPreview = edmPreview.replace(OLD_SYNTAX, NEW_SYNTAX);
	    } else {
	    	edmPreview = "http://www.europeana.eu/api/v2/thumbnail-by-url.json?size=w400&uri=missing&size=LARGE&type=SOUND";
	    }
	    imgLink = edmPreview;
	    console.log("preview: " + edmPreview + ", isShownBy: " + edmIsShownBy);
	    if (imgLink.length > 0) {
	    	cells.push('<td style=\"display:table-cell; vertical-align:middle; text-align:center\"><a href=\"' + imgLink + '\"><img border=\"0\" alt=\"Europeana image\" src=\"' + imgLink + '\" width=\"100\" height=\"100\"></a></td>');
	    } 
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
		
		// audio
		cells = [];
	    var edmIsShownBy = "";
	    if (typeof(mirRecord.metadata.edmIsShownBy) !== 'undefined' && mirRecord.metadata.edmIsShownBy.length > 0) {
	    	edmIsShownBy = mirRecord.metadata.edmIsShownBy[0];
	    }
	    cells.push('<td><audio controls><source src=\"' + edmIsShownBy + '\" type=\"audio/mpeg\" autostart=\"false\"></audio></td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    
        // description
	    cells = [];
	    var description = "";
	    if (typeof(mirRecord.metadata.dcDescription) !== 'undefined' && mirRecord.metadata.dcDescription.length > 0) {
	    	description = mirRecord.metadata.dcDescription[0];
	    }
	    cells.push('<td>' + '<b>' + "Description: " + '</b>' + description.substring(0, MAX_LENGTH) + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    	
        // sdocId
        cells = [];
	    cells.push('<td>' + '<b>' + "Sdoc ID: " + '</b>' + mirRecord.sdocId + '</td>');
	    rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');

        // license
	    cells = [];
	    cells.push('<td>' + '<b>' + "License: " + '</b>' + mirRecord.sdocLicense + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    
	    // score
	    cells = [];
	    cells.push('<td>' + '<b>' + "Score: " + '</b>' + mirRecord.sdocScore + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    
        // Search similar link and Europeana link
        cells = [];        
	    cells.push('<td><input id=\"similarBtn' + i + '\" type=\"button\" value=\"Search similar\" onclick=\"callSearchSimilarFunction(\'' 
	    	+ mirRecord.sdocTitle + '\',\'' + mirRecord.sdocId + '\',\'' + mirRecord.sdocLicense + '\')\" />' + 
	    	'<input class=\"right-side-button\" type=\"button\" onclick=\"window.location.href=\'' + mirRecord.metadata.guid + 
	    	'\';\" value=\"View Europeana\" style=\"float: right;\"/></td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
        
	    cells = [];
		var res = "<div><table class=\"grid-elem\">" +  rows.join('') + "</div></table>"
	    return res;
	}
	
	
	function showSimilarImage(item, i) {

		var rows = [];
		
		var IMAGE_NAME_POS = 1;
		var FEATURES_POS = 2;
		var G_SCORE_POS = 3;
		var TITLE_POS = 8;
		var URL_POS = 9;
		var PATH_POS = 10;
		var IMAGE_IN_PAIR_POS = 1;
		
        // image name
        var imageName = item[IMAGE_NAME_POS].split(' ')[IMAGE_IN_PAIR_POS];
		var cells = [];
	    cells.push('<th class=\"elem-header\">' + '<b>' + "Image: " + '</b>' + imageName + '</th>');
        rows.push('<tr>' + cells.join('') + '</tr>');
	    
		// picture
	    cells = [];
//	    var imgLink = "file://///" + item[PATH_POS].replace(/\\/g, "/") + "/" + imageName;
//	    var imgLink = "file://" + item[PATH_POS].replace(/\\/g, "/") + "/" + imageName;
////	    var imgLink = "testcollection/" + imageName;
	    var imgLink = item[URL_POS];
	    //var imgLink = "file://E:/app-test/testcollection/O_446.jpg";
	    if (imgLink != null && imgLink.length > 0) {
	    	cells.push('<td style=\"display:table-cell; vertical-align:middle; text-align:center\"><a href=\"' +  imgLink + '\"><img border=\"0\" alt=\"Europeana image\" src=\"' 
	    		+ imgLink + '\" width=\"100\" height=\"100\"></a></td>');
	    } 
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
		
        // title
	    cells = [];
	    cells.push('<td>' + '<b>' + "Title: " + '</b>' + item[TITLE_POS] + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    	
        // features
	    cells = [];
	    cells.push('<td>' + '<b>' + "Features: " + '</b>' + item[FEATURES_POS] + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    	
        // g-score
        cells = [];
	    cells.push('<td>' + '<b>' + "G-score: " + '</b>' + item[G_SCORE_POS] + '</td>');
	    rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');

	    var res = "<div><table class=\"grid-elem\">" +  rows.join('') + "</div></table>"
	    return res;
	}
	
	
	function callSearchSimilarFunction(title, europeanaId, license) {

        //alert("Similar button clicked.");
        
        document.getElementById('mirRecord').value = "*";
        document.getElementById('similarTo').value = europeanaId;

        executeSearchQuery();
	}
	
	
	function executeSearchQuery() {
		
        var queryText = document.getElementById("mirRecord").value;
        var similarTo = document.getElementById("similarTo").value;
        var license = document.getElementById("license").value;
        
        $(document).ready(function () {
        	$.ajax({
                url: '/mir/search',
                data: {'text':queryText, 'qdoc_id':similarTo, 'license':license, 'rows':maxCells, 'profile':"FULL"},
                method: 'get'
            }).done(function(data){
		        //alert("Get request done");
		        $("#mirTable").html("");
		        
		        if (data.results != null) {
//    		        if (data.success=='true') {
    		        //alert("Get request success");
    		        //alert(data.results);
					var tbody = document.getElementById('mirTable');
					tbody.insertAdjacentHTML('beforeend', createGrid(data.results));
                } else {
    		        alert("Get request error. " + data.errorMsg);
                }
            }).fail(function(reason){
		        alert("Get request failed");
            });		        
        }); // end of request function	        

	}
        
	
	function executeCdvsSearchQuery() {
		
        var queryText = document.getElementById("mirRecord").value;
        
        $(document).ready(function () {
        	$.ajax({
                url: '/mir/cdvssearch',
                data: {'text':queryText},
                method: 'get'
            }).done(function(data){
		        //alert("Get request done");
		        $("#mirTable").html("");
		        
		        if (data != null) {
//			        if (data.results != null) {
//    		        if (data.success=='true') {
    		        //alert("Get request success");
    		        //alert(data.results);
					var tbody = document.getElementById('mirTable');
					//tbody.insertAdjacentHTML('beforeend', '<h1>Hello</h1>');
//					tbody.insertAdjacentHTML('beforeend', data);
					tbody.insertAdjacentHTML('beforeend', createSimilarityGrid(data));
//					tbody.insertAdjacentHTML('beforeend', createGrid(data.results));
                } else {
    		        alert("Get request error. " + data.errorMsg);
                }
            }).fail(function(reason){
		        alert("Get request failed");
            });		        
        }); // end of Cdvs request function	        
	}
        
	
	function createGrid(data) {
		
	    var rows = [];
	    var cells = [];
	   
	    var i = 0;
        for (var key in data) {
	        var mirRecord = data[i];
	        //alert("obj: " + obj);
	        var row = [];
            row.push(showMirRecord(data[key],i));
	        cells.push('<td>' + row + '</td>');
	        if (i !== 0 && (i + 1) % maxColumns === 0) {
	            rows.push('<tr>' + cells.join('') + '</tr>');
	            cells = [];
	        }
	        i++;
	    }
	    return rows.join('');
	}


	function createSimilarityGrid(data) {
		
	    var rows = [];
	    var cells = [];
	   
	    var i = 0;
        for (var key in data) {
	        //var mirRecord = data[i];
	        //alert("obj: " + obj);
	        var row = [];
            row.push(showSimilarImage(data[key],i));
//	        cells.push('<td>' + data[key] + '</td>'); //
	        cells.push('<td>' + row + '</td>');
	        if (i !== 0 && (i + 1) % maxColumns === 0) {
	            rows.push('<tr>' + cells.join('') + '</tr>');
	            cells = [];
	        }
	        i++;
	    }
	    return rows.join('');
	}


	$(function(){
	    $('#btn').click(function(qdocId) {
	        //alert("Search button clicked.");
            executeSearchQuery();
/*
	        var queryText = document.getElementById("mirRecord").value;
	        var similarTo = document.getElementById("similarTo").value;
	        var license = document.getElementById("license").value;
	        
	        $(document).ready(function () {
            	$.ajax({
                    url: '/mir/search',
                    data: {'text':queryText, 'qdoc_id':similarTo, 'license':license, 'rows':maxCells, 'profile':"FULL"},
                    method: 'get'
                }).done(function(data){
    		        //alert("Get request done");
    		        $("#mirTable").html("");
    		        
    		        if (data.results != null) {
//        		        if (data.success=='true') {
        		        //alert("Get request success");
        		        //alert(data.results);
						var tbody = document.getElementById('mirTable');
						tbody.insertAdjacentHTML('beforeend', createGrid(data.results));
                    } else {
        		        alert("Get request error. " + data.errorMsg);
                    }
                }).fail(function(reason){
    		        alert("Get request failed");
                    });		        
	        }); // end of request function	        
	        */
	    }); // end of click function

	    $('#cdvs-btn').click(function(qdocId) {
	        //alert("Search button clicked.");
            executeCdvsSearchQuery();
	    }); // end of CDVS click function
	    
	});		

</script>

		
<table>
	<tr>
		<td>
			<div style="display: none;" id="advSearch" align="center">
				<img id="queryImage"
					src="queryImg" alt="" height="64" align="middle">	
			</div>					
		</td>
		<td colspan="3">
		<form id="searchbar" method="GET" enctype="" name="form1" action="./Search" onsubmit="setFeatures();">
			<input type="hidden" value="form" name="src">
		
		    <table cellspacing="1" cellpadding="1" border="0" width="100%">                          
			    <tr>    
					<div class="input-fields">
						<td valign="top" class="search-input-block">
							<label class="label-name" for="text">Text:</label>
							<input class="input-field" type="text" name="mirRecord" id="mirRecord" value="" size="50">
							<label class="label-name" for="similarTo">Similar to:</label>
							<input class="input-field" type="text" name="similarTo" id="similarTo" value="" size="50">
							<label class="label-name" for="license">License:</label>
							<input class="input-field" type="text" name="license" id="license" value="" size="50">
							<input id="btn" type="button" value="Search" />									
							<input id="cdvs-btn" type="button" value="CDVS Search" />									
						</td>					 		
					</div>														
				</tr>
				<tr>
	                <div class="x_content">
						<table>
						  <tbody id="mirTable">
						  </tbody>
						</table>		                
					</div>
				</tr>
		   </table>
		   
		</form>
		</td>
	</tr>
</table>