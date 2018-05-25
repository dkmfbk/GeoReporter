$(function() {	
	$("#form9").submit(function(e) {
		//var queryType = "curatorsources";
		var oElements = {};
		$('#form9 [id]').each(function(){
		    oElements[this.id] = this.value;
		});
		var springleserverURL= serverURL;
		var springlesrepositoryID=oElements["springlesrepositoryID"];
		var url_=restURL+"nstatements";
	   var includeinferred = $("#form9 #includeinferred:checked").length;
		var dataSend ="repositoryID=" +springlesrepositoryID+"&serverURL="+serverURL + "&includeinferred="+includeinferred;
		
		e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url_ + "</span><br />" + library.json.prettyPrint(dataSend));
		dataSend.fieldValueJsonString = JSON.stringify(oElements);
 //  alert(url_+dataSend.processID);
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "json",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html("<span>OK " + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + library.json.prettyPrint(data));
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});