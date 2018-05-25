$(function() {	
	$("#form10").submit(function(e) {
		//var queryType = "curatorsources";
		var oElements = {};
		$('#form10 [id]').each(function(){
		    oElements[this.id] = this.value;
		});
		var springlesrepositoryID=$("#repoChoice").val();
		var url_=restURL+"querysparql";
	   var querySPARQL= yasqe.getValue();
        var includeinferred = $("#form10 #includeinferred:checked").length;
        var dataSend = new FormData();
		dataSend.append("repositoryID",springlesrepositoryID);
        dataSend.append("serverURL",springlesserverURL);
        dataSend.append("querySPARQL",querySPARQL);
        dataSend.append("includeinferred",includeinferred );
		
		
		$.ajax({
			url : url_ , 
			data : dataSend, 
            contentType : false,
      processData : false,
      cache: false,
            dataType:"json",
			type : "POST"
		
		}).done(function(data, textStatus, jqXHR) {			
			$("#result").empty();
            var result="";
			result+="<table class='table table-striped'>";
            result+="<thead><tr>";
            if(data["res"].length > 0){
                result+="<th>"+data["res"][0]["s"]+"</th>";
                result+="<th>"+data["res"][0]["p"]+"</th>";
                result+="<th>"+data["res"][0]["o"]+"</th>";
            }
             result+="</tr></thead><tbody>";
            for(var i=0;data["res"].length > 0 && i<data["res"].length;i++)
            {
                result+="<tr class='active'>";
                result+="<td class='s'>"+data["res"][i]["s_val"]+"</td>"; 
                result+="<td class='o'>"+data["res"][i]["p_val"]+"</td>"; 
                result+="<td class='p'>"+data["res"][i]["o_val"]+"</td>"; 
                result+="</tr>";
            }
            result+="</tbody></table>";
            $("#result").html(result);
            
            if(data["res"].length > 0)
                defineClickListerner(data["res"][0]["s"],data["res"][0]["p"],data["res"][0]["o"]);
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});