$(function() {	
	$("#form19").submit(function(e) {
		//var queryType = "curatorsources"; 
		var oElements = {};
		$('#form19 [id]').each(function(){
		    oElements[this.id] = this.value;
		});

         var documentData = new FormData();
        
		var url_=restURL+"create_ruleset";
        var ruleset = oElements['file_ruleset'];
        
        documentData.append("newfilename",ruleset.substr(ruleset.lastIndexOf('\\')+1));
        documentData.append("filetoupload",$('#file_ruleset')[0].files[0]);
        documentData.append("inferencer",$("#collapse19 #inferencer").val());
        
		
		e.preventDefault();
		$("#request").empty();
		$("#result").empty();

		$.ajax({
			 crossOrigin: true,
			 url : url_ , 
            data : documentData,    
            contentType : false,
            processData : false,
            cache: false,
            dataType: "html",
			type : "POST"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#result').html(data);
            load_rulesets();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
	});

});