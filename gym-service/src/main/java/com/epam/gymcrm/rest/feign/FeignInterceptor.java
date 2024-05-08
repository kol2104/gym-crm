package com.epam.gymcrm.rest.feign;

import com.epam.gymcrm.common.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = request.getHeader(Constants.TRACE_HEADER.getName());
        if (traceId != null) {
            requestTemplate.header(Constants.TRACE_HEADER.getName(), traceId);
        } else if (request.getAttribute(Constants.TRACE_HEADER.getName()) instanceof String requestAttribute) {
            requestTemplate.header(Constants.TRACE_HEADER.getName(), requestAttribute);
        }
        String token = request.getHeader(Constants.AUTH_TOKEN.getName());
        if (token != null) {
            requestTemplate.header(Constants.AUTH_TOKEN.getName(), token);
        }
    }
}
