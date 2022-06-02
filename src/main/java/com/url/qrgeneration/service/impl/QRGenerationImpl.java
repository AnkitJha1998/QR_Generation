package com.url.qrgeneration.service.impl;

import com.google.common.hash.Hashing;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.url.qrgeneration.model.QRResponse;
import com.url.qrgeneration.service.QRGeneration;
import java.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class QRGenerationImpl implements QRGeneration {

    private boolean isValidURL(String url)
    {
        try{
            new URL(url).toURI();
            return true;
        }
        catch (MalformedURLException | URISyntaxException e)
        {
            System.out.println("URL is malformed, url : "+url);
        }
        return false;
    }

    private byte[] generateQRCode(String url) throws WriterException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,200,200);
        MatrixToImageWriter.writeToStream(matrix,"png", stream);
        return stream.toByteArray();
    }

    @Override
    public QRResponse getQrResponse(String url) throws MalformedURLException{

        QRResponse response = new QRResponse();
        response.setUrl(url);
        if(!isValidURL(url))
        {
            throw new MalformedURLException();
        }
        response.setPic(getQRBase64Str(url));
        final String urlId = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        response.setUrlShort(urlId);
        return response;
    }

    @Override
    public byte[] getQRCode(String url) {
        byte[] qrResponseString = null;
		try {
			qrResponseString = generateQRCode(url);
		} catch (WriterException | IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
        return qrResponseString;
    }
    
    @Override
    public String getQRBase64Str(String url) {
    	String base64Img = null;
    	try{
            byte[] qrResponseString = generateQRCode(url);
            base64Img = Base64.getEncoder().encodeToString(qrResponseString);
        }
    	catch (WriterException | IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        return base64Img;
    }


}
