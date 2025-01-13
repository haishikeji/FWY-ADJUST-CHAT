package com.px.framework.web.service;

import com.px.aj.appuser.domain.AppUserInfo;
import com.px.common.constant.AppConstants;
import com.px.common.core.redis.RedisCache;
import com.px.common.utils.ServletUtils;
import com.px.common.utils.StringUtils;
import com.px.common.utils.ip.AddressUtils;
import com.px.common.utils.ip.IpUtils;
import com.px.common.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author 品讯科技
 */
@Component
public class AppTokenService
{
    // 令牌自定义标识
    @Value("${tokenapp.header}")
    private String header;

    // 令牌秘钥
    @Value("${tokenapp.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${tokenapp.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 100000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 100000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public AppUserInfo getAppUserInfo(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(AppConstants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                AppUserInfo user = redisCache.getCacheObject(userKey);
                return user;
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setAppUserInfo(AppUserInfo AppUserInfo)
    {
        if (StringUtils.isNotNull(AppUserInfo) && StringUtils.isNotEmpty(AppUserInfo.getToken()))
        {
            refreshToken(AppUserInfo);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delAppUserInfo(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param AppUserInfo 用户信息
     * @return 令牌
     */
    public String createToken(AppUserInfo AppUserInfo)
    {
        String token = IdUtils.fastUUID();
        AppUserInfo.setToken(token);
        setUserAgent(AppUserInfo);
        refreshToken(AppUserInfo);

        Map<String, Object> claims = new HashMap<>();
        claims.put(AppConstants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param AppUserInfo
     * @return 令牌
     */
    public void verifyToken(AppUserInfo AppUserInfo)
    {
        long expireTime = AppUserInfo.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(AppUserInfo);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param AppUserInfo 登录信息
     */
    public void refreshToken(AppUserInfo AppUserInfo)
    {
        AppUserInfo.setLoginTime(System.currentTimeMillis());
        AppUserInfo.setExpireTime(AppUserInfo.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将AppUserInfo缓存
        String userKey = getTokenKey(AppUserInfo.getToken());
        redisCache.setCacheObject(userKey, AppUserInfo, expireTime, TimeUnit.HOURS);
    }

    /**
     * 设置用户代理信息
     *
     * @param AppUserInfo 登录信息
     */
    public void setUserAgent(AppUserInfo AppUserInfo)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        AppUserInfo.setIpaddr(ip);
        AppUserInfo.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        AppUserInfo.setBrowser(userAgent.getBrowser().getName());
        AppUserInfo.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(AppConstants.TOKEN_PREFIX))
        {
            token = token.replace(AppConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid)
    {
        return AppConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
