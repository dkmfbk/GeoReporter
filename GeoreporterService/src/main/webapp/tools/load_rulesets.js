function load_rulesets(){
    var dataSend ="inferencer="+$("#collapse19 #inferencer").val()+"&serverURL="+springlesserverURL+"&repositoryID="+$("#repoChoice").val();
    $.ajax({
			 crossOrigin: true,
			url : restURL+"list_of_ruleset" , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')');
            var tmp="";
            $("#result").empty();
            $("#list_of_rulesets").empty();
            var table= "";
             table+= "<table class='table table-striped'><thead><tr><th>Name</th><th></th></thead></tr><tbody>";
            for(var i=0;data!= null && i<data.split("\n").length-1;i++)
            {
                tmp = data.split("\n")[i];
               table += "<tr class='info'><td><a id="+tmp.split("&")[0]+" class='ruleset'>"+tmp.split("&")[1]+"</a></td><td><a style='color:red;' class='delete' id='r_"+tmp.split("&")[1]+"'>Delete</a></td></tr>";
            }
            table += "</tbody></table>";
            $("#list_of_rulesets").append(table);                  
            getRuleset($(".ruleset").get(0).id);
            $(".ruleset").click(function(){
                var index=$(".ruleset").index(this);
                getRuleset($(".ruleset").get(index).id);
            });
            $(".delete").click(function(){
                var index=$(".delete").index(this);
                var ruleset = $(".ruleset").get(index).id;
                deleteRuleset(ruleset);
                
            });
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}


function getRuleset(name){
    $.ajax({
            
			 crossOrigin: true,
			url : restURL+"content_of_ruleset" , 
			data : "filename="+name, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            $("#result").append(data);
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}

function deleteRuleset(name){
    $.ajax({
            
			 crossOrigin: true,
			url : restURL+"delete_ruleset" , 
			data : "filename="+name+"&inferencer="+$("#collapse19 #inferencer").val(), 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            $("#result").append(data);
            load_rulesets();
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}

function load_rulesets_closure(){
    var dataSend ="inferencer="+$("#closure_inferencer").val()+"&serverURL="+springlesserverURL+"&repositoryID="+$("#repoChoice").val();
    $.ajax({
			 crossOrigin: true,
			url : restURL+"list_of_ruleset" , 
			data : dataSend, 
      contentType: "application/json; charset=utf-8",
      dataType: "html",
			type : "GET"
		
		}).done(function(data, textStatus, jqXHR) {			
			//var print = eval("(" + data + ')'); 
			$("#result").empty();
            var tmp="";
            $("#closure_ruleset").empty();
            for(var i=0;data!= null && i<data.split("\n").length-1;i++)
            {
                tmp = data.split("\n")[i];
                if($("#closure_inferencer").val() == "NaiveInferencer")
                    $("#closure_ruleset").append("<option value="+tmp.split("&")[1]+" >"+tmp.split("&")[1]+"</option>");
                else
                    $("#closure_ruleset").append("<option value="+tmp.split("&")[0]+" >"+tmp.split("&")[1]+"</option>");
            }
		}).fail(function(jqXHR, textStatus, errorThrown) { 
			$("#result").empty();
			$('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
		});
		return false;
}