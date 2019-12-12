package misc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class VertxGateway {
	
	public static String vertxLogin(String username, String password) throws URISyntaxException, ParseException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		URI uri = new URIBuilder().setScheme("http")
				.setHost("localhost")
				.setPath("/login")
				.setPort(8888)
				.setParameter("username", username)
				.setParameter("password", password)
				.build();
		
		HttpGet httpget = new HttpGet(uri);
		//System.out.println(uri);
		CloseableHttpResponse response = null;
		int status = 0;
		String session = "";
		try {
			//System.out.println("it worked!");
			response = httpclient.execute(httpget);
			
			status = response.getStatusLine().getStatusCode();
			
			session = EntityUtils.toString(response.getEntity());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (status == 401 || status == 0) {
			return null;
		} else {
			String[] split = session.split(",");
			String[] split2 = split[1].split(":");
			//logger.info( split2[1].replace('}', '\0'));
			return split2[1].replaceAll("}", "");
		}
	} 
	
	public static String vertxReport(String bearer) throws URISyntaxException {
		/*HttpPost httpPost = new HttpPost("http://localhost/reports/bookdetail");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("Authorization", bearer));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		URI uri = new URIBuilder().setScheme("http")
				.setHost("localhost")
				.setPath("/reports/bookdetail")
				.setPort(8888)
				.build();
		
		bearer = bearer.replaceAll("\"", "");
		HttpUriRequest request = RequestBuilder.post()
				.setUri(uri)
				.addHeader("Authorization", bearer)
				.build();
		
		CloseableHttpResponse response = null;
		String report =null;
		
		try {
			response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				report = EntityUtils.toString( response.getEntity());
				/*InputStream is = response.getEntity().getContent();
				report = new File("./tmp.xls");
				FileOutputStream tmpRead = new FileOutputStream(report);
				
				byte[] buffer = new byte[5600];
	            int inByte;
	            while((inByte = is.read(buffer)) > 0)
	                tmpRead.write(buffer,0,inByte);
	            is.close();
	           tmpRead.close();*/
	           EntityUtils.consume(response.getEntity());
				//EntityUtils.toString(response.getEntity());
				//FileOutputStream reportFile = new FileOutputStream(report);
				//response.getEntity().writeTo(reportFile);
				//IOUtils.copy(tmp, tmpRead);
				 //response.getEntity().writeTo();;
			} 
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return report;
	}
}
