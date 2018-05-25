$(function() {	
	$("#form3").submit(function(e) {
		var server=$( "#serverChoice option:selected" ).get(0).value;
		var url_=server+url2+"bgknowledgedomain";
		$("#request").empty();
		$('#request').html("<span>GET " + url +"</span><br />" );
		$.ajax({
			url : url_,
		
			type : "GET"
			
		}).done(function(data, textStatus, jqXHR) {
			//var print = eval("(" + data + ')');
			$("#result").empty();
			$('#result').html("<span>" + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + library.json.prettyPrint(data));
		}).fail(function(jqXHR, textStatus, errorThrown) {
			$("#result").empty();
			$('#result').html("<span>" + jqXHR.status + " " + jqXHR.statusText + "</span><br />");
		});
		return false;
	});
});
