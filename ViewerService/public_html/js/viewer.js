/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function () {
    //debugInitialize();
    Initialize();
    registerEvents();
});

function Initialize() {
    // init editor
    window.jsonPreview = CodeMirror($('#json-preview').get(0), {
        value: 'JSON Result...',
        readOnly: true,
        styleActiveLine: true,
        lineNumbers: true,
        lineWrapping: true,
        theme : 'lesser-dark',
        mode : 'javascript',
        json : true
   });
   
   // Position search bar
   $search = $("#ibr-search-input");
   $search.width($("#image-viewer").width());
}

function populateList(ibr) {
    $list = $("#image-source-list");
    $list.empty();
    
    $.each(ibr.imgs, function(i, item) {
        //alert(item);
        var startOfRelativePath = item.url.indexOf("img/LocalImageDB/");
        var url = item.url;
        if ( startOfRelativePath >= 0 )
            url = item.url.substring(startOfRelativePath);
        
        $h4 = $("<h4>").addClass("list-group-item-heading").html(url);
        $pSize = $("<p>").addClass("list-group-item-text").html(item.width + " x " + item.height);
        $pType = $("<p>").addClass("list-group-item-text").html(item.type);
        $pTags = $("<p>").addClass("list-group-item-text").html(JSON.stringify(item.tags));
        $a = $("<a>").addClass("list-group-item active");
        $a.attr("href", "#");
        $a.click(loadImage);
        $list.append($a.append($h4)
                .append($pSize)
                .append($pType)
                .append($pTags)
                );
    });
}

function debugInitialize() {
    $list = $("#image-source-list");
    for (var i = 0; i < 20; i++) {
        $h4 = $("<h4>").addClass("list-group-item-heading").html("Image "+i);
        $pSize = $("<p>").addClass("list-group-item-text").html("size");
        $pType = $("<p>").addClass("list-group-item-text").html("type");
        $pTags = $("<p>").addClass("list-group-item-text").html("tags");
        $a = $("<a>").addClass("list-group-item active");
        
        $list.append($a.append($h4)
                .append($pSize)
                .append($pType)
                .append($pTags)
                );
                
    }
}
 
function registerEvents() {
    $("#image-source-list a").click(loadImage);
    $("#ibr-search-button").click(resolveIBR);
}

function loadImage() {
    $this = $(this);
    $image = $("#image-preview");
    $cont = $("#image-preview-container");
    
    var real_width;
    var real_height;
    
    var nextImageURL = $this.find("h4").html();
    console.log(nextImageURL);
    // Hide first to avoid flickering
    $image.hide();
    $image.attr("src", nextImageURL);
    
    $("<img/>")
        .attr("src", $image.attr("src")).load(function() {
        real_width = this.width;   // Note: $(this).width() will not
        real_height = this.height; // work for in memory images.
    console.log(real_height);
        $image.width(real_width);
        $image.height(real_height);
        
        // Show image again
        $image.show();
        
        if ( real_height > $cont.height() )
            $image.css("height", "100%");
        
        if ( real_width > $cont.width() )
            $image.css("width", "100%");
    });
}

function resolveIBR() {
    var ibrValue = $.trim($("#ibr-search-input-ibr").val());
    if (!ibrValue) {
        alert("IBR entry is empty");
        
        return;
    }
    
    // Ajax call to the IBR Service
    // The IBR Service will be listening on port 8080 for the demo
    $.ajax({
        // request type
        type: "POST",
        // the URL of the IBR Service
        url: "http://localhost:8080/",
        // Possibly multiple IBRs but only one fro the demo Viewer
        data: {ibrs: $.trim(ibrValue)},
        // the type of data we expect back (JSON)
        dataType: "json",
        // code to run if the request succeeds;
        // the response is passed to the function
        success: function (responseJson) {
            // Response needs to be valid JSON
            // Example JSON returned from IBR Service
            //alert("Later we will parse this JSON "+JSON.stringify(responseJson));
            window.jsonPreview.getDoc().setValue(JSON.stringify(responseJson, null, 4));
            populateList(responseJson);
        },
        // code to run if the request fails; the raw request and
        // status codes are passed to the function
        error: function (xhr, status, errorThrown) {
            alert("Response does not seem to be valid JSON or the Service is down!");
            console.log("Error: " + errorThrown);
            console.log("Status: " + status);
            console.dir(xhr);
        },
        // code to run regardless of success or failure
        complete: function (xhr, status) {
            //$('#connLabel').text("connected");
        }
    });
}