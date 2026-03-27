package io.flowledger.core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Authentication entry point that returns a JSON response for unauthorized requests.
 */
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final String FIELD_TIMESTAMP = "timestamp";
  private static final String FIELD_STATUS = "status";
  private static final String FIELD_ERROR = "error";
  private static final String FIELD_MESSAGE = "message";
  private static final String FIELD_PATH = "path";
  private static final String ERROR_UNAUTHORIZED = "Unauthorized";

  private final ObjectMapper objectMapper;

  /**
   * Handles authentication failures by returning a structured JSON payload.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param authException the authentication exception
   * @throws IOException when the response cannot be written
   * @throws ServletException when the servlet container cannot handle the request
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getOutputStream(), buildPayload(request, authException.getMessage()));
  }

  /**
   * Builds a JSON response payload for authentication failures.
   *
   * @param request the HTTP request
   * @param message the failure message
   * @return the response payload
   */
  private Map<String, Object> buildPayload(HttpServletRequest request, String message) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(FIELD_TIMESTAMP, Instant.now().toString());
    payload.put(FIELD_STATUS, HttpServletResponse.SC_UNAUTHORIZED);
    payload.put(FIELD_ERROR, ERROR_UNAUTHORIZED);
    payload.put(FIELD_MESSAGE, message);
    payload.put(FIELD_PATH, request.getRequestURI());
    return payload;
  }
}
