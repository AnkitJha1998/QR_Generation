package com.url.qrgeneration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.url.qrgeneration.controller.QRController;
import com.url.qrgeneration.model.QRRequest;
import com.url.qrgeneration.model.QRResponse;
import com.url.qrgeneration.model.UrlObject;
import com.url.qrgeneration.service.UrlObjectService;
import com.url.qrgeneration.util.QRUtility;

public class QRControllerTest {

	@InjectMocks
	QRController controller;

	@Mock
	UrlObjectService service;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetApiStatus() {
		ResponseEntity<String> response = controller.getApiStatus();
		assertEquals(response.getBody(), "API is running");

	}

	@Test
	public void testGetQRandDetailSuccessScenario() throws WriterException, IOException {
		UrlObject object = new UrlObject();
		object.setUrlId(1L);
		object.setUrl("https://www.linkedin.com/in/ankit-jha-2008/");
		object.setUrlShortId("d6b25059");
		ArrayList<UrlObject> returnObj = new ArrayList<>();
		returnObj.add(object);
		Mockito.when(service.findByUrlShortId(object.getUrlShortId())).thenReturn(returnObj);

		QRRequest invokeRequest = new QRRequest();
		invokeRequest.setRequestURL("https://www.linkedin.com/in/ankit-jha-2008/");
		ResponseEntity<QRResponse> entity = controller.getQRandDetail(invokeRequest);

		assertEquals(entity.getStatusCode(), HttpStatus.OK);
		QRResponse response = entity.getBody();
		assertEquals(object.getUrlShortId(), response.getUrlShort());
		assertEquals("https://www.linkedin.com/in/ankit-jha-2008/", response.getUrl());
		assertEquals(QRUtility.generateQRCode("https://www.linkedin.com/in/ankit-jha-2008/"), response.getPic());
	}

	@Test
	public void testGetQRandDetailWithNoURLSavedBefore() throws WriterException, IOException {
		ArrayList<UrlObject> returnObj = new ArrayList<>();
		Mockito.when(service.findByUrlShortId("d6b25059")).thenReturn(returnObj);
		QRRequest invokeRequest = new QRRequest();
		invokeRequest.setRequestURL("https://www.linkedin.com/in/ankit-jha-2008/");
		UrlObject object = new UrlObject();
		object.setUrl("https://www.linkedin.com/in/ankit-jha-2008/");
		object.setUrlShortId("d6b25059");
		Mockito.when(service.save(object)).thenReturn(object);		
		ResponseEntity<QRResponse> entity = controller.getQRandDetail(invokeRequest);
		assertEquals(200, entity.getStatusCodeValue());
		QRResponse response = entity.getBody();
		assertEquals(object.getUrl(), response.getUrl());
		assertEquals(object.getUrlShortId(), response.getUrlShort());
		assertEquals(QRUtility.generateQRCode("https://www.linkedin.com/in/ankit-jha-2008/"), response.getPic());
	}

	@Test
	public void testGetQrandDetailWhenURLIsWrong() {
		QRRequest invokeRequest = new QRRequest();
		invokeRequest.setRequestURL("faultyURL");
		ResponseEntity<QRResponse> response = controller.getQRandDetail(invokeRequest);

		assertEquals(400, response.getStatusCodeValue());
	}
	
	@Test
	public void testGetUrlByShortIdSuccess()
	{
		UrlObject object = new UrlObject();
		object.setUrlId(1L);
		object.setUrl("https://www.linkedin.com/in/ankit-jha-2008/");
		object.setUrlShortId("d6b25059");
		ArrayList<UrlObject> fetchedList = new ArrayList<>();
		fetchedList.add(object);
		Mockito.when(service.findByUrlShortId("d6b25059")).thenReturn(fetchedList);
		ResponseEntity<String> response = controller.findByUrlShortId("d6b25059");
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(object.getUrl(),response.getBody());
	}
	
	@Test
	public void testGetUrlByShortIdNotFound()
	{
		ArrayList<UrlObject> fetchedList = new ArrayList<>();
		Mockito.when(service.findByUrlShortId("d6b25059")).thenReturn(fetchedList);
		ResponseEntity<String> response = controller.findByUrlShortId("d6b25059");
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	public void testGetQRImageSuccess() throws WriterException, IOException
	{
		UrlObject object = new UrlObject();
		object.setUrlId(1L);
		object.setUrl("https://www.linkedin.com/in/ankit-jha-2008/");
		object.setUrlShortId("d6b25059");
		ArrayList<UrlObject> fetchedList = new ArrayList<>();
		fetchedList.add(object);
		Mockito.when(service.findByUrlShortId("d6b25059")).thenReturn(fetchedList);
		ResponseEntity<byte[]> response = controller.getQRImage("d6b25059");
		assertEquals(200, response.getStatusCodeValue());
		byte[] expected = QRUtility.getQRByteArray(object.getUrl());
		byte[] actual = response.getBody();
		for (int i = 0; i<expected.length; i++)
		{
			assertEquals(expected[i] , actual[i]);
		}
	}
	
	@Test
	public void testGetQRImageFail()
	{
		ArrayList<UrlObject> fetchedList = new ArrayList<>();
		Mockito.when(service.findByUrlShortId("d6b25059")).thenReturn(fetchedList);
		ResponseEntity<byte[]> response = controller.getQRImage("d6b25059");
		assertEquals(404, response.getStatusCodeValue());
		
	}

}
