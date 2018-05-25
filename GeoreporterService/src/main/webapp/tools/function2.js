$(function() {
	
	var input = $("<input class='form-control'>");
	var helper = $("<p class='help-block'></p>");
	
	$("#form2").submit(function(e) {
		var queryType = $( "#kbInsertQuery option:selected" ).get(0).value;
		var server=$( "#serverChoice option:selected" ).get(0).value;
		url_=server+url2+"knowledgedomain/"+queryType;
		var dataSend = 
			queryType;
		
	//	e.preventDefault();
		$("#request").empty();
		$("#result").empty();
		$('#request').html("<span>GET " + url + "</span><br />" + library.json.prettyPrint(dataSend));
	//	dataSend.fieldValueJsonString = JSON.stringify(oElements);
  //  alert(url+dataSend.processID);
		$.ajax({
			url : url_ , 
			
   
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
		//	var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html("<span>OK " + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + library.json.prettyPrint(data));
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});
	
	
	

});



