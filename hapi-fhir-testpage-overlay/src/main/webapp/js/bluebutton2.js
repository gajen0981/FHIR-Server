// Download the JSON
$("#getJson").bind("click",function(){
    console.info("Downloading Data from FHIR API as JSON: " + url);

    // If baseDstu1 then replace it otherwise regular pass through
    var url = $(this).attr('name').replace("baseDstu1", "baseDstu2");

    d3.json(url,function(data){
        console.log(data);
        var href = "data:application/octet-stream," + encodeURIComponent(JSON.stringify(data));
        location.href = href;

        return true;
    });
});

// Download the XML
$("#getXml").bind("click",function(){
    console.info("Downloading Data from FHIR API as XML: " + url);

    // If baseDstu1 then replace it otherwise regular pass through
    var url = $(this).attr('name').replace("baseDstu1", "baseDstu2");

    d3.xml(url, function(data){
        // Need to serialize it before encode it
        var xml = new XMLSerializer().serializeToString(data);
        console.log(xml);

        var href = "data:application/octet-stream," + encodeURIComponent(xml);
        location.href = href;

        return true;
    });
});