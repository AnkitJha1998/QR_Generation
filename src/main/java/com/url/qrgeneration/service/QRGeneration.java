package com.url.qrgeneration.service;

import com.url.qrgeneration.model.QRResponse;

import java.net.MalformedURLException;

public interface QRGeneration {

    public QRResponse getQrResponse(String url) throws MalformedURLException;

    public byte[] getQRCode(String url);
    
    public String getQRBase64Str(String url);

}
