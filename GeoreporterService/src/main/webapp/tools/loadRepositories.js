var repoID;

var currenturl=window.location.origin;
//var restURL = "http://localhost:8080/SpringlesREST/rest/rest/";
var restURL = currenturl+"/SpringlesREST/rest/rest/";
var serverURL = "";
$(function() {	
	$(document).on("ready",function() {
		loadRepo();
	});

});

function loadRepo(){
    var oElements = {};
		 var filetoupload="";
		
		var url_=restURL+"getRepositories";

	
	    var documentData = new FormData();
	    documentData.append("springlesserverURL",springlesserverURL);
		var dataSend ="springlesserverURL="+springlesserverURL ;

		$("#result").empty();

		$.ajax({
			url : url_ , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
			$('#repoChoice').html(data);
            $(".springlesrepositoryID").each(function(){
                $(this).html(data);
            });
            $(".hide-repo").each(function(){
                    $(this).show();
                });
            $("#summaryPanel").click();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}