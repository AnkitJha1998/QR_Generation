var shortURLBox, picEle,inputUrl;
var url_base = "http://localhost:8080/"
function createCORSRequest(method,url)
{
    var xhr = new XMLHttpRequest;
    if("withCredentials" in xhr){
        xhr.open(method,url,true);
    }
    else if(typeof XDomainRequest != "undefined")
    {
        xhr = new XDomainRequest();
        xhr.open(method,url);
    }
    else{
        xhr=null;
    }
    return xhr;
}

function _arrayBufferToBase64( buffer ) {
    var binary = '';
    var bytes = new Uint8Array( buffer );
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
        binary += String.fromCharCode( bytes[ i ] );
    }
    return window.btoa( binary );
}

window.onload = function()
{
    shortURLBox = document.getElementById("url_short")
    picEle = document.getElementById("qr_pic")
    inputUrl = document.getElementById("url_input")

}

function invokePic(urlShort1)
{
    var xmlObj=createCORSRequest("GET",url_base+"qr/"+urlShort1);
    xmlObj.open("GET",url_base+"qr/"+urlShort1,true);
    xmlObj.onload = function()
    {
        if(this.readyState == 4)
        {
            if(this.status = 200)
            {
                var base64str = _arrayBufferToBase64(this.responseText);
                console.log("array: "+this.response);
                picEle.src = "data:image/png;base64,"+this.response;
            }
        }
    };
    xmlObj.send()

}

function loadDetails()
{
    if(this.readyState = 4)
    {
        if(this.status = 200)
        {
            var getJsonData=JSON.parse(this.responseText);
            if(getJsonData==null)
            {
                shortURLBox.innerHTML="Not parsible";
                return;
            }
            if(getJsonData.length==0)
            {
                shortURLBox.innerHTML="Not parsible";
                return;
            }
            shortURLBox.innerHTML = getJsonData.urlShort;
            var picStr = getJsonData.pic;
            console.log("array: "+picStr);
            picEle.src = "data:image/png;base64,"+picStr;
        }
    }
}


function loadQrAndShortURL()
{
    var url = inputUrl.value;
    if(url.length==0){
        window.alert("URL is empty")
        return;
    }
    var jsonReq = JSON.stringify({
        "requestURL" : url
    })
    var xmlObj=createCORSRequest("POST",url_base+"qr");
    xmlObj.setRequestHeader("Content-type","application/json");
    xmlObj.onload = loadDetails
    xmlObj.onerror=function()
    {
        window.alert("Server Offline");
    }
    xmlObj.send(jsonReq);
}

function openInAnotherTab()
{
    var tabURL = url_base+"qr/"+shortURLBox.innerHTML;
    window.open(tabURL,'_blank').focus;
}