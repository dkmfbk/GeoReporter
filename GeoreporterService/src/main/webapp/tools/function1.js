$(function() {	
	$("#formfabbricati").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formfabbricati input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formfabbricati input#filemappings')[0].files[0]);
		documentData.append("fileheader",$('#formfabbricati input#fileheader')[0].files[0]);
		documentData.append("tipodati",$('#formfabbricati input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});

$(function() {	
	$("#formsoggettifabbricati").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formsoggettifabbricati input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formsoggettifabbricati input#filemappings')[0].files[0]);
		documentData.append("fileheader",$('#formsoggettifabbricati input#fileheader')[0].files[0]);
		documentData.append("tipodati",$('#formsoggettifabbricati input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
           //  $("#collapse2").hide();
          //  $("#summaryPanel").click(); 
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});



$(function() {	
	$("#formtitolaritafabbricati").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formtitolaritafabbricati input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formtitolaritafabbricati input#filemappings')[0].files[0]);
		documentData.append("fileheader",$('#formtitolaritafabbricati input#fileheader')[0].files[0]);
		documentData.append("tipodati",$('#formtitolaritafabbricati input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});



$(function() {	
	$("#formparticellefondiarie").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formparticellefondiarie input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formparticellefondiarie input#filemappings')[0].files[0]);
		documentData.append("fileheader",$('#formparticellefondiarie input#fileheader')[0].files[0]);
		documentData.append("tipodati",$('#formparticellefondiarie input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});




$(function() {	
	$("#formsoggettiparticelle").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formsoggettiparticelle input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formsoggettiparticelle input#filemappings')[0].files[0]);
		documentData.append("fileheader",$('#formsoggettiparticelle input#fileheader')[0].files[0]);
		documentData.append("tipodati",$('#formsoggettiparticelle input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});





$(function() {	
	$("#formtitolaritaparticelle").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formtitolaritaparticelle input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formtitolaritaparticelle input#filemappings')[0].files[0]);
		documentData.append("fileheader",$('#formtitolaritaparticelle input#fileheader')[0].files[0]);
		documentData.append("tipodati",$('#formtitolaritaparticelle input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});





$(function() {	
	$("#formanagraficacomunale").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formanagraficacomunale input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formanagraficacomunale input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formanagraficacomunale input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();

            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});



$(function() {	
	$("#formanagraficafamiglie").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formanagraficafamiglie input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formanagraficafamiglie input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formanagraficafamiglie input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();

            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});






$(function() {	
	$("#formfornitureelettriche").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formfornitureelettriche input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formfornitureelettriche input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formfornitureelettriche input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});





$(function() {	
	$("#formfornituregas").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formfornituregas input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formfornituregas input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formfornituregas input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});





$(function() {	
	$("#formcontrattilocazione").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formcontrattilocazione input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formcontrattilocazione input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formcontrattilocazione input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});



$(function() {	
	$("#formutenzerifiuti").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formutenzerifiuti input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formutenzerifiuti input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formutenzerifiuti input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});


$(function() {	
	$("#formutenzeacqua").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formutenzeacqua input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formutenzeacqua input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formutenzeacqua input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});




$(function() {	
	$("#formlettureacqua").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formlettureacqua input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formlettureacqua input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formlettureacqua input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});


$(function() {	
	$("#formiciimuabitazioneprincipale").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formiciimuabitazioneprincipale input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formiciimuabitazioneprincipale input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formiciimuabitazioneprincipale input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});


$(function() {	
	$("#formiciimunudaproprieta").submit(function(e) {
		
	
		var url_=url+"importadati";
	    var documentData = new FormData();
	  
		documentData.append("filedati",$('#formiciimunudaproprieta input#filedati')[0].files[0]);
		documentData.append("filemappings",$('#formiciimunudaproprieta input#filemappings')[0].files[0]);
		documentData.append("tipodati",$('#formiciimunudaproprieta input#tipodati')[0].value);

		$("#request").empty();
		$("#result").empty();
		for (var [key, value] of documentData.entries()) { 
			console.log(key, value);
			
        }
		
		//$('#request').html("<span>GET " + url + "</span><br />" +key +"="+ value);
 
		$.ajax({
			url : url_ , 
			data : documentData, 
      contentType : false,
      processData : false,
      cache: false,
			type : 'POST'
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(library.json.prettyPrint(data));
         /*    $("#collapse1").hide();
            $("#summaryPanel").click(); */
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});



$(function() {	
	$("#summaryPanel").on("click",function() {
		//var queryType = "curatorsources";
		var springlesserverURL = "http://localhost:8080/openrdf-sesame";
		var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
		var springlesrepositoryID= "georeporter";
		var url_=restURL+"summary";
        
	

		var dataSend ="springlesrepositoryID=" +springlesrepositoryID+"&springlesserverURL="+springlesserverURL ;
		
	   
		$("#summaryresponse").empty();
	
		
		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "json",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			$("#summaryresponse").empty();
            var result = "";
            
			result+="<table class='table table-striped' style='font-size:0.9em;' ><tbody>";
            result+="<tr><th>ID:</th><td>"+data["ID"]+"</td></tr>";
            result+="<tr><th>Title:</th><td>"+data["Title"]+"</td></tr>";
            result+="<tr><th>Location:</th><td>"+data["Location"]+"</td></tr>";
            result+="<tr><th>Server:</th><td>"+data["Server"]+"</td></tr>";
            result+="<tr><th>Total statements:</th><td>"+data["Total statements"]+"</td></tr>";
          
          
            result+="</tbody></table>";
            $('#summaryresponse').html(result);
       
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#summaryresponse").empty();
			$('#summaryresponse').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});




