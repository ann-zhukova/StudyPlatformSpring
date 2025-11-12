package com.example.studyplatformspring.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class XmlStylesheetResponseFilter extends OncePerRequestFilter {

    @Value("${app.xml.stylesheet-url:/xsl/study.xsl}")
    private String stylesheetUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(request, wrapper);
        } finally {
            try {
                String contentType = wrapper.getContentType();
                byte[] bodyBytes = wrapper.getContentAsByteArray();
                if (bodyBytes != null && bodyBytes.length > 0 && contentType != null && isXmlContentType(contentType)) {
                    Charset charset = getCharsetFromContentType(contentType);
                    String body = new String(bodyBytes, charset);
                    if (!containsStylesheetPi(body)) {
                        String withPi = insertStylesheetPi(body, stylesheetUrl);
                        byte[] modified = withPi.getBytes(StandardCharsets.UTF_8);
                        wrapper.resetBuffer();
                        wrapper.setCharacterEncoding(StandardCharsets.UTF_8.name());
                        wrapper.setContentType("application/xml");
                        wrapper.getOutputStream().write(modified);
                    }
                }
            } finally {
                wrapper.copyBodyToResponse();
            }
        }
    }

    private static boolean isXmlContentType(String contentType) {
        String ct = contentType.toLowerCase();
        return ct.contains("application/xml") || ct.contains("text/xml") || ct.contains("+xml");
    }

    private static Charset getCharsetFromContentType(String contentType) {
        try {
            int i = contentType.toLowerCase().indexOf("charset=");
            if (i > -1) {
                String enc = contentType.substring(i + 8).trim();
                return Charset.forName(enc);
            }
        } catch (Exception ignore) { }
        return StandardCharsets.UTF_8;
    }

    private static boolean containsStylesheetPi(String xml) {
        int idx = xml.indexOf("?>");
        if (idx < 0) return xml.contains("<?xml-stylesheet");
        int start = Math.max(0, idx);
        int end = Math.min(xml.length(), idx + 200);
        return xml.substring(0, end).contains("<?xml-stylesheet");
    }

    private static String insertStylesheetPi(String xml, String href) {
        String pi = "<?xml-stylesheet type=\"text/xsl\" href=\"" + href + "\"?>\n";
        int idx = xml.indexOf("?>");
        if (idx >= 0 && xml.startsWith("<?xml")) {
            return xml.substring(0, idx + 2) + "\n" + pi + xml.substring(idx + 2);
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + pi + xml;
    }
}

