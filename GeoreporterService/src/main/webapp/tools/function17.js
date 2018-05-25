$(function() {	
	$("#form17").submit(function(e) {
		//var queryType = "curatorsources";
		var oElements = {};
		 var filetoupload="";
		$('#form17 [id]').each(function(){
		    oElements[this.id] = this.value;    
		});
		var springlesrepositoryID=$("#repoChoice").val();
		var url_=restURL+"contexts";
        var includeinferred = $("#form17 #includeinferred:checked").length;

	
	    var documentData = new FormData();
		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL +"&includeinferred="+ includeinferred  ;
		
	   e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url_ + "</span><br />" + library.json.prettyPrint(dataSend));
		dataSend.fieldValueJsonString = JSON.stringify(oElements);
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "json",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			$("#result").empty();
			$('#result').append("<table class='table table-striped'><thead><tr><th>Graphs</th><tr></thead><tbody>");
            for(var i=0;i<data["graphs"].length ;i++)
                $('#result').append("<tr><td class='graph'>"+data["graphs"][i]["graph_name"]+ "</td></tr>");
            $('#result').append("</tbody></table>");
            defineClickListerner();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});