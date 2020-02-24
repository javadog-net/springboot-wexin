package net.javadog.springbootwexin.service;

import lombok.Getter;
import net.javadog.springbootwexin.utils.WxUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 一个低端小气没档次的程序狗 JavaDog
 * blog.javadog.net
 *
 * @BelongsProject: springboot-wexin
 * @BelongsPackage: net.javadog.springbootwexin.service
 * @Author: hdx
 * @CreateTime: 2020-02-14 20:43
 * @Description: 微信相关service
 */
@Service
public class WxService {
    @Getter
    private static String AppId;
    @Value("${wx.appId}")
    public void setAppId(String appId) {
        AppId = appId;
    }
    /**
    *@Author: hdx
    *@CreateTime: 20:46 2020/2/14
    *@param:  shareUrl 分享的url
    *@Description: 初始化JSSDKConfig
    */
    public Map initJSSDKConfig(String url) throws Exception {
        //获取AccessToken
        String accessToken = WxUtil.getJSSDKAccessToken();
        //获取JssdkGetticket
        String jsapiTicket = WxUtil.getJssdkGetticket(accessToken);
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString();
        String signature = WxUtil.buildJSSDKSignature(jsapiTicket,timestamp,nonceStr,url);
        Map<String,String> map = new HashMap<String,String>();
        map.put("url", url);
        map.put("jsapi_ticket", jsapiTicket);
        map.put("nonceStr", nonceStr);
        map.put("timestamp", timestamp);
        map.put("signature", signature);
        map.put("appid", AppId);
        return map;
    }
}
