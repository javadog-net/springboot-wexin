package net.javadog.springbootwexin.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.security.MessageDigest;
import java.util.Map;

/**
 * 一个低端小气没档次的程序狗 JavaDog
 * blog.javadog.net
 *
 * @BelongsProject: springboot-wexin
 * @BelongsPackage: net.javadog.springbootwexin.utils
 * @Author: hdx
 * @CreateTime: 2020-02-14 21:19
 * @Description: 微信工具类
 */
@Component
public class WxUtil {
    @Getter
    protected static String AppId;
    @Getter
    protected static String AppSecret;
    @Getter
    protected static String JssdkAccesstokenUrl;
    @Getter
    protected static String JssdkGetticketUrl;

    @Value("${wx.appId}")
    public void setAppId(String appId) {
        AppId = appId;
    }

    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    @Value("${wx.jssdk_accesstoken_url}")
    public void setJssdkAccesstokenUrl(String jssdkAccesstokenUrl) {
        JssdkAccesstokenUrl = jssdkAccesstokenUrl;
    }

    @Value("${wx.jssdk_getticket_url}")
    public void setJssdkGetticketUrl(String jssdkGetticketUrl) {
        JssdkGetticketUrl = jssdkGetticketUrl;
    }

    /**
    *@Author: hdx
    *@CreateTime: 21:31 2020/2/14
    *@param:  * @param null
    *@Description:
    
    */
    public static String getJSSDKAccessToken() {
        String token = null;
        String url = JssdkAccesstokenUrl.replaceAll("APPID",
                AppId).replaceAll("APPSECRET",
                AppSecret);
        String json = postRequestForWeiXinService(url);
        Map map = jsonToMap(json);
        if (map != null) {
            token = (String) map.get("access_token");
        }
        return token;
    }

    /**
    *@Author: hdx
    *@CreateTime: 21:40 2020/2/14
    *@param:  * @param null
    *@Description: 根据accessToken获取JssdkGetticket

    */
    public static String getJssdkGetticket(String accessToken) {
        String url = JssdkGetticketUrl.replaceAll("ACCESS_TOKEN", accessToken);
        String json = postRequestForWeiXinService(url);
        Map map = jsonToMap(json);
        String jsapi_ticket = null;
        if (map != null) {
            jsapi_ticket = (String) map.get("ticket");
        }
        return jsapi_ticket;
    }

    /**
    *@Author: hdx
    *@CreateTime: 21:41 2020/2/14
    *@param:ticket 根据accessToken生成的JssdkGetticket
    *@param:timestamp 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
    *@param:nonceStr 随机字符串
    *@param:url 当前网页的URL
    *@Description: 构建分享链接的签名

    */
    public static String buildJSSDKSignature(String ticket,String timestamp,String nonceStr ,String url) throws Exception {
        String orderedString = "jsapi_ticket=" + ticket
                + "&noncestr=" + nonceStr + "&timestamp=" + timestamp
                + "&url=" + url;

        return sha1(orderedString);
    }

    /**
     * sha1 加密JSSDK微信配置参数获取签名。
     *
     * @return
     */
    public static String sha1(String orderedString) throws Exception {
        String ciphertext = null;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(orderedString.getBytes());
        ciphertext = byteToStr(digest);
        return ciphertext.toLowerCase();
    }
    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }
    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }
    /**
    *@Author: hdx
    *@CreateTime: 21:49 2020/2/14
    *@param:  map
    *@Description:  mapToJson

    */
    public static String mapToJson(Map map){
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    /**
    *@Author: hdx
    *@CreateTime: 21:37 2020/2/14
    *@param:  json
    *@Description: jsonToMap

    */
    private static Map jsonToMap(String json) {
        Gson gons = new Gson();
        Map map = gons.fromJson(json, new TypeToken<Map>(){}.getType());
        return map;
    }

    /**
    *@Author: hdx
    *@CreateTime: 21:36 2020/2/14
    *@param:  * @param null
    *@Description: 调取微信接口

    */
    private static String postRequestForWeiXinService(String getAccessTokenUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(getAccessTokenUrl, null, String.class);
        String json = postForEntity.getBody();
        return json;
    }

}
