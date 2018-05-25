$(function() {	
	$("#form4").submit(function(e) {
		var server=$( "#serverChoice option:selected" ).get(0).value;
		var subject = $( "#subject" ).val();
		var relation = $( "#relation" ).val(); 
		var object = $( "#object" ).val(); 
		if($.isEmptyObject(subject) || $.isEmptyObject(relation) || $.isEmptyObject(object)){
			alert("Fill all input box.");
			return;
		}
		e.preventDefault();
		
		var dataSend = 
				"subject="+subject+"&relation="+relation+"&object="+object;
			
		
		var url_=server+url2+"insertsemanticrelation";
		$("#request").empty();
		$('#request').html("<span>get " + url_ +"</span><br />" + dataSend );
$.ajax({ 
             type: "GET",
            
             url: url_,
             data: dataSend,
}).done(function(data, textStatus, jqXHR) {
	//var print = eval("(" + data + ')');
	$("#result").empty();
	$('#result').html("<span>" + jqXHR.status + " " + jqXHR.statusText + "</span><br />" + data);
}).fail(function(jqXHR, textStatus, errorThrown) {
	$("#result").empty();
	$('#result').html("<span>" + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
});
        
		return false;
	});
});
