package sa.com.stc.SimRegister;


import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SawaZiyaraService {
	
	 private static String HEADER_USER = "x-stcws-user"; // + user:password
     private static String HEADER_AUTH = "Authorization"; // +signature
     private static String HEADER_DATE = "Date"; // + date in format: Thu, 12 Jan 2012 16:12:00 GMT
     private static String HEADER_AGENT = "User-Agent";
     private static String USERKEY = "stcbakala";
     private static String CONETN_TYPE = "Content-Type";
     private static String KEY = "iywtruta570";
	private JSONObject response;

	
	//(f, MSISDN, simNumber, iDNumber,customerFName,customerLName,country);
	public JSONObject setHeadrs(File imageId,String MSISDN , String simNumber , String iDNumber, String customerFName, String customerLName, 
			String country, String custmerTypeNmae, String destMSISDN ) throws Exception{
		
		/*
		System.out.println("MSISDN:="+ MSISDN);
		System.out.println("simNumber:="+ simNumber);
		System.out.println("iDNumber:="+ iDNumber);
		System.out.println("customerFName:="+ customerFName);
		System.out.println("customerLName:="+ customerLName);
		System.out.println("country:="+ country);
		System.out.println("custmerTypeNmae:="+ custmerTypeNmae);
		System.out.println("destMSISDN:="+ destMSISDN);*/
		
	   //String url = "http://172.20.51.206:17001/stcrs2/public/email/mime";
		//String url = "http://10.33.18.5:71001/stcrs2/public/email/mime" ;
		//String url = "http://preprod.stc.com.sa/stcesb/stcrs2/public/phoneNumbers/"+MSISDN+"/sawaZiyara" ;   
		String url = "https://stc.com.sa/stcesb/stcrs2/public/phoneNumbers/"+MSISDN+"/sawaZiyara" ;
           HttpPost request = new HttpPost(url);
           String headerUser = "Basic "+new String(Base64.encodeBase64(("stcbakala:iywtruta570").getBytes()));
           String headerAuth = "STCWS "+USERKEY+":"+Utils.computeSignature(KEY);
           String headerDate = ""+Utils.getDate();
           String headerAgent = "SawaZiyara;2.3 client;1.0";
           String headerContentType = "multipart/form-data;";
           
           
        //   System.out.println("URL:="+ url);
           
        //request.addHeader(HEADER_USER, headerUser);
		request.addHeader(HEADER_AUTH, headerAuth);
		request.addHeader(HEADER_DATE, headerDate);
		request.addHeader(HEADER_AGENT, headerAgent);
		request.addHeader(CONETN_TYPE, headerContentType);
		
		String IdType =  custmerTypeNmae.split("-")[0].trim();
		String nationality = country;
		String photoId = "id.jpg";
		
		//String jsonString = "{subject:'"+MSISDN+"',message:'MSISDN: "+ MSISDN+" \\n IMSI: "+IMSI+" \\n ID Number: "+ID+"',email:'sawaid@stc.com.sa',filename:'id.jpg'}";
		//String jsonString = "{sourceMsisdn:'"+MSISDN+"',custMsisdn:'"+MSISDN+"',custSimNumber:'"+simNumber+"',custId:'"+iDNumber+"',custIdType:'"+"S"+"',custFirstName:'"+customerFName+"',custFamilyName:'"+customerLName+"',custNationality:'"+"
		
		String jsonString = "{\"sourceMsisdn\":\""+MSISDN+"\",\"custMsisdn\":\""+destMSISDN+"\",\"custSimNumber\":\""+simNumber+"\",\"custId\":\""+iDNumber+"\",\"custIdType\":\""+IdType+"\",\"custFirstName\":\""+customerFName+"\",\"custFamilyName\":\""+customerLName+"\",\"custNationality\":\""+nationality+"\",\"filename\":\""+photoId+"\"}";

		//System.out.println("jsonString:="+ jsonString);
		
		try {
			//System.out.println("imageId:="+ imageId);
			MultipartEntity entity = new MultipartEntity(
		            HttpMultipartMode.BROWSER_COMPATIBLE);
			  entity.addPart("id", new StringBody(jsonString));
			  entity.addPart("idPhoto", new FileBody(imageId));
			  request.addHeader(entity.getContentType());
		request.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.print("URL:="+ url);
		JSONObject body = executeRequest(request, url);
		
		return body;
	}
	
	//----------------------------------------------------------------------------------------------------------//
	
	public JSONObject setHeadrsForGet() throws Exception{
		

		String url = "https://stc.com.sa/stcesb/stcrs2/public/content/nationalities" ;
		   
           HttpGet request = new HttpGet(url);
           String headerUser = "Basic "+new String(Base64.encodeBase64(("stcbakala:iywtruta570").getBytes()));
           String headerAuth = "STCWS "+USERKEY+":"+Utils.computeSignature(KEY);
           String headerDate = ""+Utils.getDate();
           String headerAgent = "SawaZiyara;2.3 client;1.0";
           String headerContentType = "multipart/form-data;";
           
        //request.addHeader(HEADER_USER, headerUser);
		request.addHeader(HEADER_AUTH, headerAuth);
		request.addHeader(HEADER_DATE, headerDate);
		request.addHeader(HEADER_AGENT, headerAgent);
		request.addHeader(CONETN_TYPE, headerContentType);

//		String IdType =  custmerTypeNmae.split("-")[0].trim();
//		System.out.println("99999999  ---->"+ IdType);
//		String nationality = "Afghanistan";
//		String photoId = "id.jpg";
//
		//String jsonString = "{subject:'"+MSISDN+"',message:'MSISDN: "+ MSISDN+" \\n IMSI: "+IMSI+" \\n ID Number: "+ID+"',email:'sawaid@stc.com.sa',filename:'id.jpg'}";
		//String jsonString = "{sourceMsisdn:'"+MSISDN+"',custMsisdn:'"+MSISDN+"',custSimNumber:'"+simNumber+"',custId:'"+iDNumber+"',custIdType:'"+"S"+"',custFirstName:'"+customerFName+"',custFamilyName:'"+customerLName+"',custNationality:'"+"

		//String jsonString = "{\"sourceMsisdn\":\""+MSISDN+"\",\"custMsisdn\":\""+destMSISDN+"\",\"custSimNumber\":\""+simNumber+"\",\"custId\":\""+iDNumber+"\",\"custIdType\":\""+IdType+"\",\"custFirstName\":\""+customerFName+"\",\"custFamilyName\":\""+customerLName+"\",\"custNationality\":\""+nationality+"\",\"filename\":\""+photoId+"\"}";



		//request.setEntity(new StringEntity(jsonString, HTTP.UTF_8));


		//System.out.println("JSON STR:="+ jsonString);

	//	String jsonString = "{"sourceMsisdn":"831040010017","custMsisdn":"831040010017","custSimNumber":"899660178001625170","custId":"1092039765","custIdType":"S","custFirstName":"wddfsdf","custFamilyName":"zdfdf","custNationality":"222"}";

//		try {
//			System.out.println("imageId:="+ imageId);
//			MultipartEntity entity = new MultipartEntity(
//		            HttpMultipartMode.BROWSER_COMPATIBLE);
//			  entity.addPart("id", new StringBody(jsonString));
//			  entity.addPart("idPhoto", new FileBody(imageId));
//			  request.addHeader(entity.getContentType());
//		request.setEntity(entity);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//System.out.print("URL:="+ url);
		JSONObject body = executeRequest(request, url);
		//System.out.println("body :="+ body);
		
		return body;
	}
	
	
	
	
	
	//----------------------------------------------------------------------------------------------------------//

public JSONObject executeRequest(HttpUriRequest getRequest,String url) throws IllegalStateException, IOException  {
		
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

		HttpParams params = new BasicHttpParams();
		int timeoutConnection = 60000;
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		int timeoutSocket = 60000;
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

		DefaultHttpClient 	client = new DefaultHttpClient(cm,params);

		//HttpGet getRequest = new HttpGet(url);
		int statusCode = 0;
		
			//System.out.println("GET CLIENT:="+ client);
			//System.out.println("GET REQUEST:="+ getRequest);
			HttpResponse getResponse = client.execute(getRequest);
			
			
			statusCode = getResponse.getStatusLine().getStatusCode();
			
			
			System.out.println("status CODE:="+ statusCode);

			if (statusCode != HttpStatus.SC_OK && statusCode != 404 && statusCode != 400 && statusCode != 401 && statusCode != 204 && statusCode != 502 && statusCode != 304) {
				HttpEntity getResponseEntity = getResponse.getEntity();
				response = convertStreamToString(getResponseEntity.getContent());
			}

			if(statusCode != 204 && statusCode != 304) {
				HttpEntity getResponseEntity = getResponse.getEntity();
				response = convertStreamToString(getResponseEntity.getContent());
			} else {
				//response = "no content";
			}
			
			
			
			
			return response;
			
			
			
		} 
		/*
		catch (NetworkingException e) {
			getRequest.abort();
			Log.d("DEBUG", "Networking Error for URL " + url, e);
			e.printStackTrace();
			throw e;
		}*/
		
	
	private static JSONObject convertStreamToString(InputStream is) {
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
	
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		//Object ojb = JSONValue.parse(sb.toString());
		JSONObject jArray = null;
		
		try
		{
			jArray = new JSONObject(sb.toString());
			
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		
		
		return jArray;
	}
	

}
