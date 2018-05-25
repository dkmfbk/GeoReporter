var closureStatus  = "STALE";

function showPanel(index){
    $(index).show();
}

function defineClickListerner(s,p,o){
        $("."+s).css("cursor","pointer");
        $("."+o).css("cursor","pointer");
        $("."+p).css("cursor","pointer");
        $(".graph").click(function(){
                   var context = $(this).text();
                    var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o FROM <"+ context + "> WHERE { ?s ?p ?o }";
                    var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",springlesserverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
		
                    //dataSend2 = dataSend2.replace("#","%23");
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2, 
                     contentType : false,
                  processData : false,
                  cache: false,
                      dataType: "json",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                        
                            var result ="";
                            result+="<table class='table table-striped'>";
                            result+="<thead><tr><th>"+context+"</th></tr>";
                            result+="<tr>";
                            if(data["res"].length > 0){
                                result+="<th>"+data["res"][0]["s"]+"</th>";
                                result+="<th>"+data["res"][0]["p"]+"</th>";
                                result+="<th>"+data["res"][0]["o"]+"</th>";
                            }
                             result+="</tr></thead><tbody>";
                            for(var i=0;data["res"].length > 0 && i<data["res"].length;i++)
                            {
                                result+="<tr class='active'>";
                                result+="<td class='s'>"+data["res"][i]["s_val"]+"</td>"; 
                                result+="<td class='p'>"+data["res"][i]["p_val"]+"</td>"; 
                                result+="<td class='o'>"+data["res"][i]["o_val"]+"</td>"; 
                                result+="</tr>";
                            }
                            result+="</tbody></table>";
                            $("#result").empty();
                            $("#result").html(result);
                            if(data["res"].length > 0)
                                defineClickListerner(data["res"][0]["s"],data["res"][0]["p"],data["res"][0]["o"]);
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
    
        $(".s").click(function(){
                   var subject = $(this).text();
                     var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o WHERE { <"+subject+"> ?p ?o}";
                    var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",springlesserverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2,contentType : false,
                      processData : false,
                      cache: false,
                      dataType: "json",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                            var result ="";
                            result+="<table class='table table-striped'>";
                            result+="<thead><tr><th>"+subject+"</th></tr>";
                            result+="<tr>";
                            if(data["res"].length > 0){
                                result+="<th>"+data["res"][0]["s"]+"</th>";
                                result+="<th>"+data["res"][0]["p"]+"</th>";
                                result+="<th>"+data["res"][0]["o"]+"</th>";
                            }
                             result+="</tr></thead><tbody>";
                            for(var i=0;data["res"].length > 0 && i<data["res"].length;i++)
                            {
                                result+="<tr class='active'>";
                                result+="<td class='s'>"+data["res"][i]["s_val"]+"</td>"; 
                                result+="<td class='p'>"+data["res"][i]["p_val"]+"</td>"; 
                                result+="<td class='o'>"+data["res"][i]["o_val"]+"</td>"; 
                                result+="</tr>";
                            }
                            result+="</tbody></table>";
                            $("#result").empty();
                            $("#result").html(result);
                            if(data["res"].length > 0)
                                defineClickListerner(data["res"][0]["s"],data["res"][0]["p"],data["res"][0]["o"]);
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
        $(".o").click(function(){
                   var obj = $(this).text();
                    var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o WHERE { ?s ?p <"+obj+"> }";
                   var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",serverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2, 
                      contentType : false,
                  processData : false,
                  cache: false,
                      dataType: "json",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                            var result ="";
                            result+="<table class='table table-striped'>";
                            result+="<thead><tr><th>"+obj+"</th></tr>";
                            result+="<tr>";
                            if(data["res"].length > 0){
                                result+="<th>"+data["res"][0]["s"]+"</th>";
                                result+="<th>"+data["res"][0]["p"]+"</th>";
                                result+="<th>"+data["res"][0]["o"]+"</th>";
                            }
                             result+="</tr></thead><tbody>";
                            for(var i=0;data["res"].length > 0 && i<data["res"].length;i++)
                            {
                                result+="<tr class='active'>";
                                result+="<td class='s'>"+data["res"][i]["s_val"]+"</td>"; 
                                result+="<td class='p'>"+data["res"][i]["p_val"]+"</td>"; 
                                result+="<td class='o'>"+data["res"][i]["o_val"]+"</td>"; 
                                result+="</tr>";
                            }
                            result+="</tbody></table>";
                            $("#result").empty();
                            $("#result").html(result);
                            if(data["res"].length > 0)
                                defineClickListerner(data["res"][0]["s"],data["res"][0]["p"],data["res"][0]["o"]);
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });
    $(".p").click(function(){
                   var pred = $(this).text();
                    var url2_=restURL+"querysparql";
                    var q = "SELECT ?s ?p ?o WHERE { ?s <"+pred+"> ?o }";
                   var dataSend2 = new FormData();
                    dataSend2.append("repositoryID",$("#repoChoice").val());
                    dataSend2.append("serverURL",springlesserverURL);
                    dataSend2.append("querySPARQL",q);
                    dataSend2.append("includeinferred","1" );
                    $.ajax({
                    url : url2_ , 
                    data : dataSend2, contentType : false,
      processData : false,
      cache: false,
                      dataType: "json",
                    type : "POST"

                    }).done(function(data, textStatus, jqXHR) {	
                        
                            var result ="";
                            result+="<table class='table table-striped'>";
                            result+="<thead><tr><th>"+pred+"</th></tr>";
                            result+="<tr>";
                            if(data["res"].length > 0){
                                result+="<th>"+data["res"][0]["s"]+"</th>";
                                result+="<th>"+data["res"][0]["p"]+"</th>";
                                result+="<th>"+data["res"][0]["o"]+"</th>";
                            }
                             result+="</tr></thead><tbody>";
                            for(var i=0;data["res"].length > 0 && i<data["res"].length;i++)
                            {
                                result+="<tr class='active'>";
                                result+="<td class='s'>"+data["res"][i]["s_val"]+"</td>"; 
                                result+="<td class='p'>"+data["res"][i]["p_val"]+"</td>"; 
                                result+="<td class='o'>"+data["res"][i]["o_val"]+"</td>"; 
                                result+="</tr>";
                            }
                            result+="</tbody></table>";
                            $("#result").empty();
                            $("#result").html(result);
                            defineClickListerner();
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
                    
            });

}

function getClosureStatus(){
        var dataSend = "serverURL="+springlesserverURL+"&repositoryID="+$("#repoChoice").val();
            $.ajax({
                    url : restURL + "getClosureStatus" , 
                    data : dataSend, 
                      contentType: "application/json; charset=utf-8",
                      dataType: "html",
                    type : "GET"

                    }).done(function(data, textStatus, jqXHR) {	
                        closureStatus = data;
                    }).fail(function(jqXHR, textStatus, errorThrown) { 
                        $("#result").empty();
                        $('#result').html("<span> " + jqXHR.status + " " + jqXHR.responseText + "</span><br />");
                    });
}

$("#collapse19 #inferencer").on("change",function(){
    load_rulesets();
});