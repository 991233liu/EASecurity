package com.easecurity.core.audit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;

/**
 * 审计日志，记录返回值
 *
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuditGlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final static Logger audit = LoggerFactory.getLogger("audit.WebSecurity.Response");

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        if (!audit.isTraceEnabled())
            return body;

        Integer status = null;
        String uri = request.getURI().getRawPath();
        if (response instanceof ServletServerHttpResponse) {
            ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
            javax.servlet.http.HttpServletResponse httpServletResponse = servletResponse.getServletResponse();
            status = httpServletResponse.getStatus();
        }
        List<String> contentType = response.getHeaders().get("Content-Type");
        boolean isJson = false;
        if (contentType != null && contentType.size() > 0) {
            for (String string : contentType) {
                if (string != null && MediaType.APPLICATION_JSON.includes(MediaType.valueOf(string))) {
                    isJson = true;
                    break;
                }
            }
        }
        if (isJson) {
            if (selectedConverterType == StringHttpMessageConverter.class) {
                audit.trace("URI: {}\n    ServerHttpResponse.StatusCode: {}\n    ServerHttpResponse.ContentType: {}\n    ServerHttpResponse.Body: {}", uri, status, contentType,
                        body);
            } else {
                audit.trace("URI: {}\n    ServerHttpResponse.StatusCode: {}\n    ServerHttpResponse.ContentType: {}\n    ServerHttpResponse.Body: {}", uri, status, contentType,
                        JSON.toJSONString(body));
            }
        } else if (MediaType.APPLICATION_JSON.includes(selectedContentType)) {
            if (selectedConverterType == StringHttpMessageConverter.class) {
                audit.trace("URI: {}\n    ServerHttpResponse.StatusCode: {}\n    ServerHttpResponse.ContentType: {}\n    ServerHttpResponse.Body: {}", uri, status,
                        selectedContentType, body);
            } else {
                audit.trace("URI: {}\n    ServerHttpResponse.StatusCode: {}\n    ServerHttpResponse.ContentType: {}\n    ServerHttpResponse.Body: {}", uri, status,
                        selectedContentType, JSON.toJSONString(body));
            }
        } else {
            audit.trace("URI: {}\n    ServerHttpResponse.StatusCode: {}\n    ServerHttpResponse.ContentType: {}", uri, status, selectedContentType);
        }
        return body;
    }
}
