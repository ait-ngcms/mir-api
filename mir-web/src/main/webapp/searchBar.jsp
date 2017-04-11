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

	
	function showMirRecord(mirRecord) {

		var rows = [];
		
		var cells = [];
	    cells.push('<th class=\"elem-header\">' + mirRecord.sdocTitle + '</th>');
        rows.push('<tr>' + cells.join('') + '</tr>');
	    
        cells = [];
	    cells.push('<td>' + mirRecord.sdocId + '</td>');
	    rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');

	    cells = [];
	    cells.push('<td>' + mirRecord.sdocScore + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    
	    cells = [];
	    cells.push('<td>' + mirRecord.sdocLicense + '</td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
	    
        cells = [];
	    cells.push('<td><a href=\"' + mirRecord.metadata.guid + '\">guid</a></td>');
        rows.push('<tr class=\"elem-row\">' + cells.join('') + '</tr>');
        
	    cells = [];
		var res = "<div><table class=\"grid-elem\">" +  rows.join('') + "</div></table>"
	    return res;
	}
	
	
	function createGrid(data) {
		
	    var rows = [];
	    var cells = [];
	   
	    var i = 0;
        for (var key in data) {
	        var mirRecord = data[i];
	        //alert("obj: " + obj);
	        var row = [];
            row.push(showMirRecord(data[key]));
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
	        
	        var queryText = document.getElementById("mirRecord").value;
	        
	        $(document).ready(function () {
            	$.ajax({
                    url: '/mir/search',
                    data: {'text':queryText, 'rows':maxCells, 'profile':"FULL"},
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
	    }); // end of click function
	});		


</script>

<!--
<iframe src="" frameborder="0" width="0" height="0" id="setFrame" name="setFrame"></iframe>
 
 	 <table>
			 <tr id="comboInfo"  style="display: none;">
				<td align="left" style="padding-right: 5px;" title="Bag of Features"><input type="checkbox" style="margin-right: 1px;" name="bof" id="bof" value="BOF" title="use BoF" checked onclick="window.frames[0].location.href='SetAdvancedOptions?feature=BOF';  setFeatures();">objects</input></td>
				<td style="border: solid 1px #CCCC77;" title="MPEG-7">
				<table><tr>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" name="mp7SC" id="mp7SC" value="MP7SC" title="use SC"  onclick="document.getElementById('setFrame').src='SetAdvancedOptions?feature=MP7SC';  setFeatures();">global color</input></td>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" style="margin-right: 1px;" name="mp7CS" id="mp7CS" value="MP7CS" title="use CS"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7CS';  setFeatures();">color structure</input></td>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" style="margin-right: 1px;" name="mp7CL" id="mp7CL" value="MP7CL" title="use CL"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7CL';  setFeatures();">color layout</input></td>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" style="margin-right: 1px;" name="mp7EH" id="mp7EH" value="MP7EH" title="use EH"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7EH';  setFeatures();">edges</input></td>
				<td align="left"><input type="checkbox" style="margin-right: 1px;" name="mp7HT" id="mp7HT" value="MP7HT" title="use HT"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7HT';  setFeatures();">texture</input></td>
</tr></table></td>
			</tr>
		</table>
 -->		
		
		<table>
			<tr>
				<td>
				<div style="display: none;" id="advSearch" align="center">
				<img id="queryImage"
					src="queryImg" alt="" height="64" align="middle">
				<!-- 
				<img src="images/close.png"  onclick="changeImage('', '');" border="0" align="top" title="remove the image"> 
				 -->
					
					</div>
					
				</td>
				<td colspan="3">
				<form id="searchbar" method="GET" enctype="" name="form1" action="./Search" onsubmit="setFeatures();">
					<input type="hidden" value="form" name="src">
				
				    <table cellspacing="1" cellpadding="1" border="0">                          
					     <tr>                   
					    <td valign="top">
						<input type="hidden" value="" name="" id="objId">
						<input type="hidden" value="" name="features" id="features">
						<!-- 
						<input type="text" name="value0" id="keywords" value="" size="50">
						<input type="hidden" value="text" name="field0">
						 -->
						<input type="text" name="mirRecord" id="mirRecord" value="" size="50">
						
						</td>                   
					    <td valign="top">
							<!-- <input type="submit" value="Search" title="search" name="sumbit"> -->	
							<input id="btn" type="button" value="Search" />									
						</td>
						
		                <div class="x_content">
							<table>
							  <tbody id="mirTable">
							  </tbody>
							</table>		                
<!--  		                    <table id="datatable-responsive" class="table table-striped table-bordered dt-responsive nowrap jambo_table bulk_action top_search" cellspacing="0" width="100%">
		                    </table>
-->		                    
						</div>
						
					    <!-- 
					     	<td style="display: none; padding-left: 10px;" id="moreSearchOptionsButton">
					     	<a href="Show more search options" title="Show more search options" onclick="showMoreSearchOptions(); return false;">
					     	<img id="expandSearch" src="images/downarrow.png" border="0"></a>
					     	
					    </td>
					     --> 
					</tr>
					<!-- 
					<tr>
						<td colspan="2">
									<div id="moreSearchOptions" style="">
										 <h5>upload image</h5>
									<input id="imageToUpload" name="fileName" size="38" onclick="changeImage('', '');" type="file">
									</div>
						</td>
					 
					</tr>
					 -->
				   </table>
				   
				</form>
				</td>
			</tr>
		</table>