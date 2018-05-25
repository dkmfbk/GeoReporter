$(function() {	
	$("#form11").submit(function(e) {
		//var queryType = "curatorsources"; 
		var oElements = {};
		$('#form11 [id]').each(function(){
		    oElements[this.id] = this.value;
		});
		var springlesrepositoryID=oElements["springlesrepositoryID"];
		var springlesrepositorytitle=oElements["springlesrepositorytitle"];

        
		var url_=restURL+"create";
		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesrepositorytitle="+springlesrepositorytitle+
		"&springlesserverURL="+springlesserverURL;
		
		e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url_ + "</span><br />" + library.json.prettyPrint(dataSend));
		dataSend.fieldValueJsonString = JSON.stringify(oElements);
 //  alert(url_+dataSend.processID);
		$.ajax({
			 crossOrigin: true,
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html("Repository '" +  springlesrepositorytitle + "' creata");
            $("#collapse11").hide();
            loadRepo();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});