package project.kakao;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class kakao {
	
	
	private final static String K_CLIENT_ID = "369b216cafcc89156aa8d4dd04ab8675";
    private final static String K_REDIRECT_URI = "http://localhost/join3/kakao_login_ok.do";

    public String getAuthorizationUrl(HttpSession session) {
		System.out.println("kakao - getAuthorizationUrl");

      String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?"
          + "client_id=" + K_CLIENT_ID + "&redirect_uri="
          + K_REDIRECT_URI + "&response_type=code";
      return kakaoUrl;
    }
    
	
	public static JsonNode getAccessToken(String code) {
		System.out.println("kakao - getAccessToken");
		 
	    final String RequestUrl = "https://kauth.kakao.com/oauth/token";
	    final List<NameValuePair> postParams = new ArrayList<NameValuePair>();

	    postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
	    // 자신의RESTAPIKEY
	    // REST API KEY
	    postParams.add(new BasicNameValuePair("client_id", K_CLIENT_ID));
	    // http://아이피/최상위경로/aa
	    // 리다이렉트 URI
	    postParams.add(new BasicNameValuePair("redirect_uri", K_REDIRECT_URI)); 
	    // 로그인 과정 중 얻은 code 값
	    postParams.add(new BasicNameValuePair("code", code));

	    System.out.println("getAccessToken - code - " + code);

	    final HttpClient client = HttpClientBuilder.create().build();
	    final HttpPost post = new HttpPost(RequestUrl);
	    JsonNode returnNode = null;
	    

	    try {

	        post.setEntity(new UrlEncodedFormEntity(postParams));
	        final HttpResponse response = client.execute(post);
	        final int responseCode = response.getStatusLine().getStatusCode();
	        
		    System.out.println("RequestUrl - " + RequestUrl);
		    System.out.println("Post parameters - " + postParams);
		    System.out.println("Response Code - " + responseCode);

	        
	        // JSON 형태 반환값 처리
	        ObjectMapper mapper = new ObjectMapper();
	        returnNode = mapper.readTree(response.getEntity().getContent());

	    } catch (UnsupportedEncodingException e) {

	        e.printStackTrace();

	    } catch (ClientProtocolException e) {

	        e.printStackTrace();

	    } catch (IOException e) {

	        e.printStackTrace();

	    } finally {
		        // clear resources
		}
	    
	    return returnNode;
	    
		// return returnNode.get("access_token").toString();

	}
	
	
	



    public static JsonNode getKakaoUserInfo(String autorize_code) {
    	System.out.println("kakao - getKakaoUserInfo");
    	
      final String RequestUrl = "https://kapi.kakao.com/v1/user/me";
      String CLIENT_ID = K_CLIENT_ID; // REST API KEY
      String REDIRECT_URI = K_REDIRECT_URI; // 리다이렉트 URI
      String code = autorize_code; // 로그인 과정중 얻은 토큰 값

      System.out.println("autorize_code - " + autorize_code);

	  final HttpClient client = HttpClientBuilder.create().build();
	  final HttpPost post = new HttpPost(RequestUrl);

	  // add header
	  post.addHeader("Authorization", "Bearer " + autorize_code);
	  JsonNode returnNode = null;


      try {

        final HttpResponse response = client.execute(post);
        final int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
        System.out.println("Response Code : " + responseCode);

        // JSON 형태 반환값 처리
        ObjectMapper mapper = new ObjectMapper();
        returnNode = mapper.readTree(response.getEntity().getContent());
      } catch (UnsupportedEncodingException e) {

        e.printStackTrace();
      } catch (ClientProtocolException e) {

        e.printStackTrace();
      } catch (IOException e) {

        e.printStackTrace();
      } finally {

        // clear resources
      }
      return returnNode;
    }
}
