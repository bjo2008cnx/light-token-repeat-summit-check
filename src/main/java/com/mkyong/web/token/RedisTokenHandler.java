package com.mkyong.web.token;

import com.mkyong.cache.RedisMock;
import com.mkyong.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * token 拦截器，token保存在redis中
 */
@Log4j2
@Component
public class RedisTokenHandler extends AbstractTokenHandler {

    private RedisMock redisSimpleClient = new RedisMock();

    /**
     * 生成token 并放入session
     *
     * @param request request
     * @param amount  需要生成的token 数量
     */
    @Override
    protected String[] generateToken(HttpServletRequest request, int amount) {
        log.info("Start to call [generateToken]. Parameter amount is :{}", amount);
        //生成tokens
        String[] tokenKeys = TokenUtil.generateTokenKeys(amount);

        for (int i = 0; i < amount; i++) {
            redisSimpleClient.set(tokenKeys[i], TokenStatusEnum.INIT.getKey());
            redisSimpleClient.expire(tokenKeys[i], TokenConstant.SESSION_EXPIRE_MINUTES * 60);
        }
        log.info("Succeed to call [generateToken]");
        return tokenKeys;
    }

    /**
     * 删除token
     *
     * @param request request
     */
    @Override
    protected void removeToken(HttpServletRequest request) {
        String tokenKey = TokenUtil.parseToken(request); //从request中获取tokenKey
        try {
            redisSimpleClient.del(tokenKey);
        } catch (Exception e) {
            log.warn("fail to del redis key: {}", tokenKey);
        }
        log.info("Succeed to del token:{} from session:{}", tokenKey);
    }

    @Override
    protected boolean lockToken(HttpServletRequest request, String tokenId) {
        if (StringUtil.isEmptyOrNull(tokenId)){
            return false;
        }
        if (redisSimpleClient.exists(tokenId)) {
            redisSimpleClient.set(tokenId, TokenStatusEnum.IN_PROGRESS.getKey());
            return true;
        }
        return false;
    }

    @Override
    protected void resetToken(HttpServletRequest request, String tokenId) {
        if (redisSimpleClient.exists(tokenId)) {
            redisSimpleClient.set(tokenId, TokenStatusEnum.INIT.getKey());
        }
    }


    /**
     * 判断是否重复提交
     *
     * @param request request
     * @return
     */
    @Override
    protected boolean isRepeatSubmit(HttpServletRequest request) {
        String tokenKey = TokenUtil.parseToken(request);//从request中获取tokenKey
        if (tokenKey == null) {
            log.debug("tokenKey is null");
            //如果client 端的token为空，则认为是重复提交,不允许提交
            return true;
        }

        String tokenValue = redisSimpleClient.get(tokenKey);
        log.debug("token value from redis is :{}. Token isRepeatSubmit:{}", tokenValue, !(TokenStatusEnum.INIT.getKey().equals(tokenValue)));
        //判断redis中token的值，如果不是INIT，则是重复提交
        return !TokenStatusEnum.INIT.getKey().equals(tokenValue);
    }
}