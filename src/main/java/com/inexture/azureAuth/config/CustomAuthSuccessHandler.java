package com.inexture.azureAuth.config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
	
	@Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    private String tenant; 

    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    private String clientId;

    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    private String clientSecret; 
    
    @Value("${redirect_uri}")
    private String redirectUri;
    
    @Value("${scopes}")
    private String scope;

    private String authorizationCode = "";

    

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

        // Check if the authentication was successful
        if (authentication != null && authentication.isAuthenticated()) {
        	System.out.println("user:"+authentication.getPrincipal());
        	
        
        	String code = request.getParameter("code");
        	if(code != null && !code.isEmpty()) {
        		authorizationCode = code;
        		System.out.println("authorizationCode: " + authorizationCode);
        	}
        	
        	        	
        	try {
    			String token = obtainTokenFromAzureAD();
    			System.out.println("token: " + token);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
        	
        }
        
        response.sendRedirect("/group1"); 

	}
	
	private String obtainTokenFromAzureAD() throws URISyntaxException, IOException, InterruptedException {
    	
   	 // Set the token endpoint URL
       String tokenEndpoint = "https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token";

       // Create the request body
       String requestBody = "grant_type=authorization_code" +
               "&client_id=" + clientId +
               "&client_secret=" + clientSecret +
               "&code=" + authorizationCode +
               "&redirect_uri=" + redirectUri +
               "&scope="+scope;
       
       // Create the HTTP client and request
       HttpClient httpClient = HttpClient.newBuilder().build();
       HttpRequest request = HttpRequest.newBuilder()
               .uri(new URI(tokenEndpoint.replace("{tenant}", tenant)))
               .header("Content-Type", "application/x-www-form-urlencoded")
               .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
               .build();

       // Send the request and retrieve the response
       HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

       // Print the response status code and body
       System.out.println("Response status code: " + response.statusCode());
       System.out.println("Response body: " + response.body());

       return "";//token;
   }

}
