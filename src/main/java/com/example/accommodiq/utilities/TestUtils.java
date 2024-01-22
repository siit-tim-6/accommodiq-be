package com.example.accommodiq.utilities;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.OutputKeys;

public class TestUtils {
    private static String getAuthToken(RestTemplate restTemplate, String email, String password) {
        String loginUrl = "/sessions";

        CredentialsDto credentialsDto = new CredentialsDto(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CredentialsDto> request = new HttpEntity<>(credentialsDto, headers);

        ResponseEntity<LoginResponseDto> response = restTemplate.postForEntity(loginUrl, request, LoginResponseDto.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getJwt();
        } else {
            throw new IllegalStateException("Failed to obtain authentication token");
        }
    }

    public static HttpHeaders createHttpHeaders(RestTemplate restTemplate, String email, String password) {
        String token = getAuthToken(restTemplate, email, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer" + token);
        return headers;
    }
}
