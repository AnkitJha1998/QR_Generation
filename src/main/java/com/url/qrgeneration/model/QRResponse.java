package com.url.qrgeneration.model;

import com.google.zxing.BinaryBitmap;

public class QRResponse {

    private String url;
    private String urlShort;
    private String pic;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlShort() {
        return urlShort;
    }

    public void setUrlShort(String urlShort) {
        this.urlShort = urlShort;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
