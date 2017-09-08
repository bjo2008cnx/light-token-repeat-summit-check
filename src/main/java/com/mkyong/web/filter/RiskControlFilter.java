package com.mkyong.web.filter;

import com.mkyong.util.RequestUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RiskControlFilter
 *
 * @author Michael.Wang
 * @date 2017/9/8
 */
public class RiskControlFilter implements Filter {
    private static final String ERROR_PAGE = "http://www.weimob.com";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request = new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
        }

        if ("GET".equals(((HttpServletRequest)request).getMethod())){
            chain.doFilter(request, response);
            return;
        }

        String requestBody = RequestUtil.parseRequestBody(request);
        HttpServletResponse res = (response instanceof HttpServletResponse) ? (HttpServletResponse) response : null;
        boolean validateResult = false;
        if (validateResult && res != null) {
            res.sendRedirect(ERROR_PAGE);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}