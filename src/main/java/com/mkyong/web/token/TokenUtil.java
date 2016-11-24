package com.mkyong.web.token;


import com.mkyong.util.JSONUtil;
import com.mkyong.util.MapUtil;
import com.mkyong.util.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * token工具
 */
@Log4j2
public class TokenUtil {

    public static final String ENCODING = "utf-8";

    /**
     * 生成Token key
     *
     * @return 生成的token key
     */
    public static String generateTokenKey() {
        return System.currentTimeMillis() + RandomUtil.randomStr(10);
    }

    /**
     * 生成Token key
     *
     * @return 生成的token key
     */
    public static String[] generateTokenKeys(int amount) {
        String[] tokenKeys = new String[amount];
        for (int i = 0; i < amount; i++) {
            tokenKeys[i] = generateTokenKey();
        }
        return tokenKeys;
    }

    /**
     * 生成Token key
     *
     * @return 生成的token key
     */
    public static boolean isRepeatedSubmission(HttpServletRequest request) {
        return Boolean.TRUE.equals(request.getAttribute(TokenConstant.IS_REPEATED_SUBMISSION));
    }

    /**
     * 增加删除token标记
     *
     * @param request
     */
    public static void addRemoveTokenFlag(HttpServletRequest request) {
        request.setAttribute(TokenConstant.IS_REMOVE_TOKEN, Boolean.TRUE);
    }

    /**
     * 解析token的key,先用getParameter方式获取(form方式)，如果未取到，则从json中获取(ajax方式)
     *
     * @param request
     * @return
     */
    public static String parseToken(HttpServletRequest request) {
        String tokenKey = request.getParameter(TokenConstant.REQUEST_KEY_PREFIX); //从request中获取tokenKey
        log.info("Token Key is:" + tokenKey);
        if (tokenKey == null) {
            log.info("Getting Token key by  getParameter fails; try to get token key from input stream ");
            try {
                ServletInputStream inputStream = request.getInputStream();
                if (inputStream.markSupported()) {
                    inputStream.mark(0);//加标记以便reset
                    tokenKey = parseTokenJson(tokenKey, inputStream);
                    inputStream.reset();//reset以便重复读
                } else {
                    log.warn("input stream's mark support is FALSE");
                    tokenKey = parseTokenJson(tokenKey, inputStream);
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
        log.info("Token key after parseToken is : ", tokenKey);
        return tokenKey;
    }

    private static String parseTokenJson(String tokenKey, ServletInputStream inputStream) throws IOException {
        String requestJson = IOUtils.toString(inputStream, ENCODING);
        log.debug("request json is :: " + requestJson);
        Map properties = JSONUtil.fromJson(requestJson, Map.class);
        if (log.isDebugEnabled()) {
            log.debug(MapUtil.print(properties));
        }
        tokenKey = (String) properties.get(TokenConstant.REQUEST_KEY_PREFIX);
        return tokenKey;
    }
}