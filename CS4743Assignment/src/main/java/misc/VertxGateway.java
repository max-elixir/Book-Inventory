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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertxGateway {
	private static Logger logger = LogManager.getLogger();
	
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
			return split2[1].replace('}', '\0');
		}
	} 
	
	public static void vertxReport(String bearer) throws URISyntaxException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		URI uri = new URIBuilder().setScheme("http")
				.setHost("localhost")
				.setPath("/reports/bookdetail")
				.setPort(8888)
				.build();
		
		HttpUriRequest request = RequestBuilder.get()
				.setUri(uri)
				.addHeader("Authorization", bearer)
				//.addHeader("Authorization", "Bearer " + "xxx")
				.build();
		
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(request);
			
			System.out.println("Status code is " + response.getStatusLine().getStatusCode());
			
			System.out.println(EntityUtils.toString(response.getEntity()));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
