package com.url.qrgeneration.controller;


import com.url.qrgeneration.model.QRRequest;
import com.url.qrgeneration.model.QRResponse;
import com.url.qrgeneration.model.UrlObject;
import com.url.qrgeneration.service.QRGeneration;
import com.url.qrgeneration.service.UrlObjectService;
import com.url.qrgeneration.service.impl.QRGenerationImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class QRController {

    QRGeneration service = new QRGenerationImpl();

    @Autowired
    private UrlObjectService serv;

    @GetMapping("/message")
    public ResponseEntity<String> getApiStatus() { return new ResponseEntity<String>("API is running",HttpStatus.OK); }

    @PostMapping("/qr")
    public ResponseEntity<QRResponse> getQRandDetail(@RequestBody QRRequest request)
    {

        QRResponse response = new QRResponse();
        try{
             response = service.getQrResponse(request.getRequestURL());
            UrlObject object = new UrlObject();
            object.setUrl(response.getUrl());
            object.setUrlShortId(response.getUrlShort());

            Iterable<UrlObject> iter = serv.findByUrlShortId(object.getUrlShortId());
            ArrayList<UrlObject> list = new ArrayList<>();
            iter.forEach(list::add);
            if(list.size() == 0) {
                serv.save(object);
            }
        }
        catch (MalformedURLException e)
        {
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<QRResponse>(response,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findByUrlShortId(@PathVariable String id)
    {
        Iterable<UrlObject> iter = serv.findByUrlShortId(id);
        ArrayList<UrlObject> list = new ArrayList<>();
        iter.forEach(list::add);
        if(list.size()==0) {
            return new ResponseEntity<String>("Not Found", HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<String>(list.get(0).getUrl(),HttpStatus.OK);
    }
    
    @GetMapping("/qr/{id}")
    public ResponseEntity<byte[]> getQRImage(@PathVariable String id)
    {
    	Iterable<UrlObject> iter = serv.findByUrlShortId(id);
        ArrayList<UrlObject> list = new ArrayList<>();
        iter.forEach(list::add);
        
        if(list.size()==0) {
        	byte[] empty = new byte[0];
        	return new ResponseEntity<byte[]>(empty,HttpStatus.NOT_FOUND);
        }
        byte[] obj = service.getQRCode(list.get(0).getUrl());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(obj);
    }


}
