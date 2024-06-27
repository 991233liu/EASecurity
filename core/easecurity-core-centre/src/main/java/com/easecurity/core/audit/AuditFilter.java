package com.easecurity.core.audit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 审计Filter
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuditFilter implements Filter {
    private final static Logger audit = LoggerFactory.getLogger("audit.WebSecurity.Request");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (audit.isTraceEnabled()) {
            HttpServletRequest req = (HttpServletRequest) request;
            String contentType = req.getContentType();
            // 判断Content-Type是否为JSON
            if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(req);
                audit.trace("URI: {}\n    HttpServletRequest.QueryString: {}\n    HttpServletRequest.Headers: {}\n    HttpServletRequest.JsonBodyParameter: {}",
                        req.getRequestURI(), req.getQueryString(), getJsonHeaders(req), cachedRequest.getBodyString());
                chain.doFilter(cachedRequest, response);
            } else {
                audit.trace("URI: {}\n    HttpServletRequest.QueryString: {}\n    HttpServletRequest.Headers: {}\n    HttpServletRequest.Parameter: {}", req.getRequestURI(),
                        req.getQueryString(), getJsonHeaders(req), JSON.toJSONString(req.getParameterMap()));
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String getJsonHeaders(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        // 获取所有请求头名称
        Enumeration<String> headerNames = request.getHeaderNames();
        // 遍历请求头
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            sb.append("\"");
            sb.append(headerName);
            sb.append("\": \"");
            sb.append(headerValue);
            sb.append("\", ");
        }
        if (sb.length() > 3) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }

    private class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

        private byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            // 读取并缓存请求体
            this.cachedBody = readBytes(request.getInputStream());
        }

        private byte[] readBytes(InputStream inputStream) throws IOException {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            return sb.toString().getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(cachedBody);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }

        // 如果需要的话，也要覆盖getReader方法
        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }

        public String getBodyString() {
            return new String(cachedBody);
        }
    }
}
