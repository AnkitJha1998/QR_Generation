package com.url.qrgeneration.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRUtility {
		
	public static String generateQRCode(String url) throws WriterException, IOException {
        
        String base64Img = Base64.getEncoder().encodeToString(getQRByteArray(url));
        return base64Img;
    }
	
	public static byte[] getQRByteArray(String url) throws WriterException, IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE,200,200);
        MatrixToImageWriter.writeToStream(matrix,"png", stream);
        return stream.toByteArray();
	}

}
