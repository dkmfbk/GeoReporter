$(function() {	
	$("#form8").submit(function(e) {
		var queryType = "curatorsources";
		var oElements = {};
		$('#form8 [id]').each(function(){
		    oElements[this.id] = this.value;
		});
		var server=$( "#serverChoice option:selected" ).get(0).value;
		var url_="http://localhost:8081/SemanticQueryAnswering/trentour/sqa/curatorsources";
		var dataSend ="curatorID="+oElements["curatorID"];
		
		e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url + "</span><br />" + library.json.prettyPrint(dataSend));
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