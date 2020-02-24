package net.javadog.springbootwexin.controller;
import net.javadog.springbootwexin.common.AjaxJson;
import net.javadog.springbootwexin.service.WxService;
import net.javadog.springbootwexin.utils.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 一个低端小气没档次的程序狗 JavaDog
 * blog.javadog.net
 *
 * @BelongsProject: springboot-wexin
 * @BelongsPackage: net.javadog.springbootwexin.controller
 * @Author: hdx
 * @CreateTime: 2020-02-14 14:52
 * @Description: 微信初始化接入Controller控制器
 */
@RestController
@RequestMapping("/weixin")
public class WxInitController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WxService wxService;

    /**
    *@Author: hdx
    *@CreateTime: 20:39 2020/2/14
    *@param:  shareUrl 分享url地址
    *@Description: 初始化微信JSSDK Config信息
     1.先通过appId和appSecret参数请求指定微信地址 获取AccessToken
     2.在通过第一步中的AccessToken作为参数请求微信地址 获取jsapi_ticket临时票据(此处不考虑调用频率，使用者根据情况放入缓存或定时任务)
     3.通过第二步的JssdkGetticket和timestamp,nonceStr,url作为参数请求微信地址，获取签名signature
     4.将第三步获得的signature和jsapi_ticket,nonceStr,timestamp,url返回给前端，作为Config初始化验证的信息
    */
    @RequestMapping("/initWXJSSDKConfigInfo")
    public AjaxJson initWXJSConfig (@RequestParam(required = false) String url) throws Exception{
        logger.info("url=" + url);
        String json = "";
        try {
            Map map = wxService.initJSSDKConfig(url);
            json = WxUtil.mapToJson(map);
        }catch (Exception e){
            AjaxJson.fail(e.getMessage());
        }
        return AjaxJson.ok(json);
    }

}
