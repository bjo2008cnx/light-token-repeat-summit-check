package com.mkyong.util;

import com.mkyong.web.filter.RepeatedlyReadRequestWrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * RequestUtil
 *
 * @author Michael.Wang
 * @date 2017/9/8
 */
public class RequestUtil {

    public static final String ACCEPT = "accept";
    public static final String CONTENT_TYPE = "content-type";
    public static final String APPLICATION_JSON = "application/json";

    public static boolean isAjaxRequest(HttpServletRequest request) {
        boolean isAjax = request.getHeader(CONTENT_TYPE).contains(APPLICATION_JSON);
        if (isAjax) {
            return true;
        }

        isAjax = request.getHeader(ACCEPT).contains(APPLICATION_JSON);
        return isAjax;
    }

    public static String parseRequestBody(ServletRequest req) {
        String requestBody = null;
        //判断是否是包装过的request
        if (req instanceof RepeatedlyReadRequestWrapper) {
            RepeatedlyReadRequestWrapper request = (RepeatedlyReadRequestWrapper) req;
            requestBody = request.getBody();
        } else {
            HttpServletRequest request = (req instanceof HttpServletRequest) ? (HttpServletRequest) req : null;
            if (request != null) {
                boolean isAjaxRequest = isAjaxRequest(request);
                if (isAjaxRequest) {
                    throw new RuntimeException("please add RequestInputStreamReplacedFilter first");
                } else {
                    requestBody = parseFormRequestBody(request);
                }
            }
        }
        return requestBody;
    }

    public static String parseFormRequestBody(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        StringBuilder builder =  new StringBuilder();
        builder.append("{");
        while(names.hasMoreElements()){
            String element = names.nextElement();
            String value = request.getParameter(element);
            builder.append(StringUtil.quote(element)).append(":").append(StringUtil.quote(value));
            if (names.hasMoreElements()){
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}