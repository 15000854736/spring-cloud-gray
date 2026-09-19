package cn.springcloud.gray.dubbo.autoconfigure;

import cn.springcloud.gray.dubbo.GrayContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

final class GrayIngressFilter implements Filter {
    private final String headerName;

    GrayIngressFilter(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String tag = request instanceof HttpServletRequest httpRequest
                ? httpRequest.getHeader(headerName)
                : null;
        try {
            GrayContext.callWithTag(tag, () -> {
                chain.doFilter(request, response);
                return null;
            });
        } catch (IOException | ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Gray request scope failed", e);
        }
    }
}
