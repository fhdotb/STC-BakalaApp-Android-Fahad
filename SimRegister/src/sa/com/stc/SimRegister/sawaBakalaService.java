package sa.com.stc.SimRegister;

import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * Created by Fahad Alotaibi on 09/04/14.
 *
 */
public class sawaBakalaService {

    private static String HEADER_USER = "x-stcws-user"; // + user:password
    private static String HEADER_AUTH = "Authorization"; // +signature
    private static String HEADER_DATE = "Date"; // + date in format: Thu, 12 Jan 2012 16:12:00 GMT
    private static String HEADER_AGENT = "User-Agent";
    private static String USERKEY = "eSalesApp";
    private static String CONETN_TYPE = "Content-Type";
    private static String KEY = "Wempfu69";
    private JSONObject response;


    private static String FILE = "E:/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private static File outPDFfile;





    public JSONObject setHeadrs(File imageId,String MSISDN , String simNumber , String iDNumber, String idType, String DOB)
           throws Exception{

        String url = "https://stc.com.sa/stcesb/stcrs2/public/sawaBakala" ;
        HttpPost request = new HttpPost(url);

        ////////////////////////////////////////////////////////////////////
        String headerUser = "Basic "+new String(Base64.encodeBase64(("eSalesApp:Wempfu69").getBytes()));
        String headerAuth = "STCWS "+USERKEY+":"+Utils.computeSignature(KEY);
        String headerDate = ""+Utils.getDate();
        String headerAgent = "eSalesApp;2.3 client;1.0";
        String headerContentType = "multipart/form-data;";
        //////////////////////////////////////////////////////////////////////

        request.addHeader(HEADER_USER, headerUser);
        request.addHeader(HEADER_AUTH, headerAuth);
        request.addHeader(HEADER_DATE, headerDate);
        request.addHeader(HEADER_AGENT, headerAgent);
        request.addHeader(CONETN_TYPE, headerContentType);


        // read image from file
       // Image image = ImageIO.read(imageId);

        /*
        Document document = new Document();

        try {

            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

            String phoneNumber = "899660107001305680";
            String photoId = "id.jpg";

          //  String jsonString = "{\"phoneNumber\":\""+phoneNumber+"\",\"msisdn\":\""+MSISDN+"\",\"imsi\":\""+simNumber+"\",\"idNumber\":\""+iDNumber+"\",\"idType\":\""+idType+"\",\"DOB\":\""+DOB+"\"}";
            String jsonString = "{\"phoneNumber\":\""+phoneNumber+"\",\"msisdn\":\""+MSISDN+"\",\"imsi\":\""+simNumber+"\",\"idNumber\":\""+iDNumber+"\",\"idType\":\""+idType+"\",\"DOB\":\""+DOB+"\",\"filename\":\""+photoId+"\"}";
            Log.d("sawaBakala URL", jsonString);

/*
        try {
           // FileBody fileContent= new FileBody(new File("test.pdf"));

            System.out.println("imageId:="+ imageId);
            MultipartEntity entity = new MultipartEntity(
            HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("id", new StringBody(jsonString));

           // entity.addPart("idPhoto", new FileBody(imageId));
           // entity.addPart("file", document);
            request.addHeader(entity.getContentType());
            request.setEntity(entity);
            Log.d("sawaBakala URL", entity.toString());

        } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
 */

           try {
                System.out.println("imageId:="+ imageId);
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


        System.out.println("Beffffffffffffffffffffffffffffffffor");
        JSONObject body = executeRequest(request, url);
        System.out.println("Affffffffffffffffffffffffffter");


        return body;
    }


    public JSONObject setHeadrsForGet(String registrationId ) throws Exception {


        String url = "https://stc.com.sa/stcesb/stcrs2/public/sawaBakalaRegistrationStatus?registrationId=" + registrationId;

        ////////////////////////////////////////////////////////////////////
        String headerUser = "Basic " + new String(Base64.encodeBase64(("eSalesApp:Wempfu69").getBytes()));
        String headerAuth = "STCWS " + USERKEY + ":" + Utils.computeSignature(KEY);
        String headerDate = "" + Utils.getDate();
        String headerAgent = "eSalesApp;2.3 client;1.0";
        // String headerContentType = "multipart/form-data;";
        //////////////////////////////////////////////////////////////////////

        HttpGet request = new HttpGet(url);
        request.addHeader(HEADER_USER, headerUser);
        request.addHeader(HEADER_AUTH, headerAuth);
        request.addHeader(HEADER_DATE, headerDate);
        request.addHeader(HEADER_AGENT, headerAgent);
        // request.addHeader(CONETN_TYPE, headerContentType);


        JSONObject body = executeRequest(request, url);

        System.out.println("body = " + body);
        if (body == null)
        {
            try {
                JSONObject obj = new JSONObject();
                obj.put("status", "null");
                obj.put("productName", "null");
                obj.put("errorDescription", "null");

                System.out.println("body = null : try case to send Json object error");
                System.out.println("Obj = "+obj);
                return obj;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                System.out.println("body = null : catch case to send Json object error");
                e.printStackTrace();
            }
        }
        System.out.println(body);
        return body;
    }


    public JSONObject executeRequest(HttpUriRequest getRequest,String url) throws IllegalStateException, IOException {


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

        DefaultHttpClient client = new DefaultHttpClient(cm,params);

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
            Log.e("log_tag", "Error parsing data " + e.toString());
        }


        return jArray;
    }





    public static boolean convertToPdf(String jpgFilePath, String outputPdfPath)
    {
        try
        {
            // Check if Jpg file exists or not
            File inputFile = new File(jpgFilePath);

            if (!inputFile.exists())
                throw new Exception("File '" + jpgFilePath + "' doesn't exist.");

            // Create output file if needed
            File outputFile = new File(outputPdfPath);
            if (!outputFile.exists())
                outputFile.createNewFile();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();
            Image image = Image.getInstance(jpgFilePath);
            document.add(image);
            document.close();

            outPDFfile = outputFile;

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }



}


