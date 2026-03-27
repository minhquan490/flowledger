package io.flowledger.core.security;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Access denied handler that returns a JSON response for forbidden requests.
 */
@RequiredArgsConstructor
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

  private static final String FIELD_TIMESTAMP = "timestamp";
  private static final String FIELD_STATUS = "status";
  private static final String FIELD_ERROR = "error";
  private static final String FIELD_MESSAGE = "message";
  private static final String FIELD_PATH = "path";
  private static final String ERROR_FORBIDDEN = "Forbidden";

  private final ObjectMapper objectMapper;

  /**
   * Handles access denied failures by returning a structured JSON payload.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param accessDeniedException the access denied exception
   * @throws IOException when the response cannot be written
   * @throws ServletException when the servlet container cannot handle the request
   */
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getOutputStream(), buildPayload(request, accessDeniedException.getMessage()));
  }

  /**
   * Builds a JSON response payload for access denied failures.
   *
   * @param request the HTTP request
   * @param message the failure message
   * @return the response payload
   */
  private Map<String, Object> buildPayload(HttpServletRequest request, String message) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(FIELD_TIMESTAMP, Instant.now().toString());
    payload.put(FIELD_STATUS, HttpServletResponse.SC_FORBIDDEN);
    payload.put(FIELD_ERROR, ERROR_FORBIDDEN);
    payload.put(FIELD_MESSAGE, message);
    payload.put(FIELD_PATH, request.getRequestURI());
    return payload;
  }
}
