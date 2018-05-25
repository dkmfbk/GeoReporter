$(function() {	
	$("#summaryPanel").on("click",function() {
		//var queryType = "curatorsources";
		
		var springlesrepositoryID= $("#repoChoice").val();
		var url_=restURL+"summary";
        
	

		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL ;
		
	   
		$("#request").empty();
		$("#result").empty();
		
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "json",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			$("#result").empty();
            var result = "";
            
			result+="<table class='table table-striped' style='font-size:0.9em;' ><tbody>";
            result+="<tr><th>ID:</th><td>"+data["ID"]+"</td></tr>";
            result+="<tr><th>Title:</th><td>"+data["Title"]+"</td></tr>";
            result+="<tr><th>Location:</th><td>"+data["Location"]+"</td></tr>";
            result+="<tr><th>Server:</th><td>"+data["Server"]+"</td></tr>";
            result+="<tr><th>Total statements:</th><td>"+data["Total statements"]+"</td></tr>";
            result+="<tr><th>Explicit statements:</th><td>"+data["Explicit statements"]+"</td></tr>";
            result+="<tr><th>Inferred statements:</th><td>"+data["Inferred statements"]+"</td></tr>";
            result+="<tr><th>Closure status:</th><td>"+data["Closure status"]+"</td></tr>";
            result+="<tr><th>Last Inferencer:</th><td>"+data["Last Inferencer"]+"</td></tr>";
            result+="<tr><th>Last Ruleset:</th><td>"+data["Last Ruleset"]+"</td></tr>";
            result+="<tr><th>Inferred Context Prefix:</th><td>"+data["Inferred context prefix"]+"</td></tr>";
            result+="</tbody></table>";
            $('#result').html(result);
            getClosureStatus();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});
