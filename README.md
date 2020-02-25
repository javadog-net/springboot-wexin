# springboot-wexin
史上最全接入微信JSSDK菜鸟教程
> 虽然我很菜鸟，但我还想分享
> **有需要csdn积分下载需求的同学**,评论关注回复我咋都可以，**免费给大家下载**⚽
> 我坚信爱分享的人运气一定不差，进步很快乐，分享更快乐😬

### GitHub
先给猴急的客官上干货代码，[接入微信JSSDK GitHub地址](https://github.com/javadog-net/springboot-wexin)

### 前言
事情的起因是因为疫情严重，领导要求做一个专题页，能够尽可能帮助所需要的人。
于是乎本狗与同事挑灯奋战，加班加点赶工出来。
部署上线完成，用微信内置浏览器分享后，理想状态应该是这样的，如下图⬇️

![](https://img-blog.csdnimg.cn/20200223141656148.png)

但是，结果却不是理想的这样，默默地留下了没有技术的泪水，如下图⬇️
![](https://img-blog.csdnimg.cn/20200223141735564.png)

竟然没有关键字和展示图片，在本菜狗的不懈努力下，终于承认技术不行，去请教了大佬，得出如下结论。

- 1.微信内置浏览器分享若需要自定义展示描述及右侧封面图，必须接入**微信JSSDK**,并且一定需要有配合本站的**微信公众号（appId和appSecret）才可自定义分享，切记小程序（appId和appSecret）的不可以**
- 2.非微信分享，如QQ浏览器，UC浏览器等各大厂商，会根据自身定义获取HTML页面中的TDK(title，description，keyword)，举例UC浏览器分享⬇️
![](https://img-blog.csdnimg.cn/20200223142824730.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223143025602.png)

所以，对接微信JSSDK,势在必行！
*******
### Tip
史上最详细的**接入微信JSSDK菜鸟教程**，本文全面的记录了接入微信JSSDK的步骤，具体的代码及遇到的坑，并且展示发布最终效果,并将代码发布GitHub。随篇幅较长，但史上最全。大佬勿喷，新手入门，亲测可用！！！

#### 本文试用人群
- 需要接入微信JSSDK却**看不懂文档**的同学
- 看懂文档但是**实操不知如何下手**的同学
- 下了手但是出错不知道**如何调试修改**的同学
- 成功接入过但是想**重温具体流程**的同学

#### 本文目标
- 实战进行H5网站**微信自定义分享**
- 实战进行H5网站**调取相册选取图片**

放松心态，慢慢来看

************
### 正文
#### 官方文档
任何平台接入，官方文档是标杆，虽有些关键点一笔带过，我们也要通读有个印象，点击[微信官方文档](https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html)打开文档，如下⬇️
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223145741670.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
##### 总览
- **1.x是接入关键步骤，需仔细品读**，与接入有关
- 2.x - 12.x 具体接口接入，需要对接时具体参考
- 13.x 需要注意下，**api_ticket 微信临时票据**，与接入有关
- 16-22 均是附录，可查阅错误列表对应含义，及接口菜单列表等描述
******
#### 实操步骤
使用IDEA工具，新建SpringBoot项目，项目名为springboot-wexin，目录结构如下
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223153820797.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
**AjaxJson.java**  - 自定义接口返回前台数据格式的封装类
```java
/**
 * Copyright &copy; 2005-2020 <a href="http://www.jhmis.com/">jhmis</a> All rights reserved.
 */
package net.javadog.springbootwexin.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * $.ajax后需要接受的JSON
 *
 */
public class AjaxJson {

	private boolean success = true;// 是否成功
	private String errorCode = "-1";//错误代码
	private String msg = "操作成功";// 提示信息
    private Long count;             //返回表格记录数量
    private List<?> data;           //返回表格数据
	private LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();//封装json的map

	public static AjaxJson ok(){
		AjaxJson j = new AjaxJson();
		return j;
	}

	public static AjaxJson ok(String msg){
		AjaxJson j = new AjaxJson();
		j.setMsg(msg);
		return j;
	}

	public static AjaxJson ok(String msg, Object object){
		AjaxJson j = new AjaxJson();
		j.setMsg(msg);
		j.setResult(object);
		return j;
	}

	public static AjaxJson ok(Object object){
		AjaxJson j = new AjaxJson();
		j.setResult(object);
		return j;
	}

	public static AjaxJson fail(String errorMsg){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		j.setErrorCode("999");//默认错误码
		j.setMsg(errorMsg);
		return j;
	}

	public static AjaxJson fail(String errorCode,String errorMsg){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		j.setErrorCode(errorCode);
		j.setMsg(errorMsg);
		return j;
	}
	//返回不分页的layui表数据
    public static AjaxJson layuiTable(List<?> list){
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setCount(Long.valueOf(list.size()));
        j.setData(list);
        return j;
    }
	public LinkedHashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(LinkedHashMap<String, Object> body) {
		this.body = body;
	}

	public void put(String key, Object value){//向json中添加属性，在js中访问，请调用data.map.key
		body.put(key, value);
	}
	
	public void remove(String key){
		body.remove(key);
	}

	/**
	 * 直接设置result内容
	 * @param result
	 */
	public void setResult(Object result){
		body.put("result", result);
	}
	@JsonIgnore//返回对象时忽略此属性
	public Object getResult(){
		return body.get("result");
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
		this.msg = msg;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
```
*****
**WxInitController.java**  - 微信初始化接入Controller控制器
```java
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
```
*****
**WxService.java**  - 初始化JSSDKConfig 
```java
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
```
*****
**WxUtil.java**  - 微信工具类
```java
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
```
*****
**SpringbootWexinApplication.java**  - SpringBoot启动类
```java
package net.javadog.springbootwexin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootWexinApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWexinApplication.class, args);
    }

}
```
*****
**config/application.yml**  - 基础配置文件
```yml
spring:
  profiles:
    #激活配置文件
    active: prod
  #配置静态资源路径
  resources:
    static-locations: classpath:/static/

#日志相关
logging:
  #配置文件日志路径
  config: classpath:logback-spring.xml

#微信相关配置
wx:
  #appId （到时候换成自己公众号的）
  appId: wx4ad618620f8c3528
  #appSecret（到时候换成自己公众号的）
  appSecret: b772c7863b29e270aa86e40f9b9e6215
  #参考以下文档获取access_token（有效期7200秒，开发者必须在自己的服务全局缓存access_token）
  jssdk_accesstoken_url: https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
  #用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）
  jssdk_getticket_url: https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
```
**application-dev.yml**  -开发配置文件（可选）
```yml
# 开发环境配置
spring:
  profiles: dev

#端口设置
server:
  port: 8000
```

**application-prod.yml**  -生产配置文件（因JS接口安全域名限制，则采取正式生产配置）
```yml
# 生产环境配置
spring:
  profiles: prod

#端口设置
server:
  port: 8002
```

**application-test.yml**  -测试配置文件（可选）
```yml
# 生产环境配置
spring:
  profiles: prod

#端口设置
server:
  port: 8002
```
******
**demo.html **  - 测试h5页面
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>测试jssdk</title>
    <!--引入微信JS文件-->
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
    <!--引入jquery-->
    <script src="http://libs.baidu.com/jquery/2.1.1/jquery.min.js"></script>
<script>
    //获取当前页面地址
    var url = (window.location.href).split('#')[0];
    //调取后台接口获取权限验证配置
    $.ajax({
        type : "get",
        /*！！！切记到时候改成自己的*/
        url : "http://wxjssdk.javadog.net/weixin/initWXJSSDKConfigInfo?url="+url,//替换网址，xxx根据自己jssdk文件位置修改
        success : function(data){
            console.log("返回值为=" + data);
            var msg = "";
            if(data.success){
                msg = JSON.parse(data.msg);
            }
            //通过config接口注入权限验证配置
            wx.config({
                debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印
                appId: msg.appid,
                timestamp: msg.timestamp,
                nonceStr: msg.nonceStr,
                signature: msg.signature,
                /*！！！切记到时候按需自己选择，参考文档填写*/
                jsApiList: [
                    "onMenuShareAppMessage",//分享给好友
                    "chooseImage"
                ]
            });
        },
        error:function(data){
            alert(JSON.stringify(data));
        }
    });

    //通过ready接口处理成功验证
    wx.ready(function (){
        wx.checkJsApi({
            jsApiList: ['chooseImage','onMenuShareAppMessage'],
            success: function (res) {JSON.stringify(res)}
        });
        var shareData = {
            title: '标题',
            desc: '简介',//这里请特别注意是要去除html
            link: url,
            imgUrl: 'http://b2b.haier.com/shop/userfiles/sys/1/files/201912/af656b3a-8c2c-424d-937b-a8035deb78f5.jpg'
        };
        wx.onMenuShareAppMessage(shareData);


    });
    //从相册选取图片
    function wxchooseImage(){
        wx.chooseImage({
            count: 1, // 默认9
            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            }
        });
    }
</script>
</head>
<body>
<button onclick="wxchooseImage();">点我选取相册</button>
</body>
</html>
```
******
🔥nginx 配置，此处不是项目中的代码！！！
**nginx.config**  - nginx服务器配置
```shell
 server
    {
	    listen   80;                           #监听端口设为 80。
	    server_name  wxjssdk.javadog.net;      #请绑定自己的前缀域名。
	    location / {
        proxy_set_header HOST $host;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://127.0.0.1:8002/;
      }
    }
```
**MP_verify_B0vMQLCguxRzP1Rc.txt**  - JS接口安全域名验证文件（到时候替换成自己公众号上的），必须在域名根路径下可以访问
```java
#一定把自己公众号上的txt验证文件放上!!!
B0vMQLCguxRzP1Rc
```
******
##### 步骤详解
打开文档JSSDK使用步骤段落，如下⬇️
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223151159594.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
##### 1.绑定域名
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224091753515.png)
先登录[微信公众平台](https://mp.weixin.qq.com)进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。如下⬇️
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223151711529.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
点击设置如下⬇️
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223151951781.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
###### 关键点
- 1.只能是三个域名或路径，**中文，ip，带端口等路径均不可**
- 2.域名必须是ICP备案过的，有些同学**使用内网穿透花生壳之类的无法设置JS安全域名**
- 3.必须将txt文件放置安全域名所对应的目录，如wxjssdk.javadog.net/xxx.txt。可由nginx配置，只要能访问即可，**如果访问不到则无法设置JS安全域名**

##### 2.引入JS文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224091726780.png)
实际引用在我们的项目Demo.html页面中第9行，如
![](https://img-blog.csdnimg.cn/20200224091137161.png) 
##### 3.通过config接口注入权限验证配置
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224091822341.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
###### 关键点
必须在**后台开放一个对外获取config接口注入权限的接口**
	对应我们代码中**WxInitController.java 中的initWXJSSDKConfigInfo()**方法，会返回文档中所需的必填项**appId,timestamp,nonceStr,signature**验证参数
	![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224092342176.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)

###### 实现步骤
     1.先通过appId和appSecret参数请求指定微信地址 获取AccessToken
     2.在通过第一步中的AccessToken作为参数请求微信地址 获取jsapi_ticket临时票据(此处不考虑调用频率，使用者根据情况放入缓存或定时任务)
     3.通过第二步的JssdkGetticket和timestamp,nonceStr,url作为参数请求微信地址，获取签名signature
     4.将第三步获得的signature和jsapi_ticket,nonceStr,timestamp,url返回给前端，作为Config初始化验证的信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224094803470.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)

- 1.先通过appId和appSecret参数请求指定微信地址 获取**AccessToken**
	对应我们代码中WxUtil.java第61行**getJSSDKAccessToken()**方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224094129176.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
通过**自己公众号的appId和appSecret**调用**微信服务器access_token接口地址获取token**,地址如下
https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
将其中APPID和APPSECRET**替换成自己公众号的appId和appSecret**，调取后返回Json字符串结果，获取access_token

- 2.通过第一步中的AccessToken作为参数请求微信地址 获取jsapi_ticket临时票据
 对应我们代码中WxUtil.java第81行**getJssdkGetticket()**方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224095454973.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
通过上一步获得的**access_token**调用**微信服务器jsapi_ticket接口地址获取jsapi_ticket**,地址如下
https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
将其中ACCESS_TOKEN**替换成上一步获取的access_token**，调取后返回Json字符串结果，获取jsapi_ticket

- 3.通过第二步的JssdkGetticket和timestamp,nonceStr,url作为参数请求微信地址，**获取签名signature**
对应我们代码中WxUtil.java第102行buildJSSDKSignature()方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224101244262.png)
通过上一步获得的**jsapi_ticket**，加上timestamp(支付签名时间戳),nonceStr(随机字符串),url(当前网页的URL),按照字段名的ASCII 码从小到大排序（字典序）后通过sha1进行签名，生成最终签名数据。
对应我们代码中WxUtil.java第115行sha1()方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224153849877.png)
- 4.前端成功接到返回值
	
对应我们代码中demo.html第16行$.ajax方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224160028903.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
接口返回值为
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224155638274.png)
JSON.parse(msg)转化一下JSON对象，对应我们代码中的Demo.html中24行，转化后数据做wx.config接口注入权限验证，对应我们代码demo.html第37行
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224155914945.png)

##### 4.通过ready接口处理成功验证
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224205700482.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
###### 关键点
**所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行**
在我们代码在demo.html第46行，自定义分享接口，需要在页面初始化加载时就放入ready才可生效
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224205909331.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
**反之不需要初始化加载的即可通过用户事件触发执行即可**
在我们代码在demo.html第63行，用户点击按钮触发-拍照或从手机相册中选图接口
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224211957299.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
#### 发布
我采用的IDEA插件Alibaba Cloud Toolkit工具一键部署本地应用到ECS服务器，可百度或等我下篇文章详解一下这个插件的用法。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224213138387.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224213221413.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
Target ECS：目标服务器，我买的是阿里的服务器，则直接可以搜索到。
Target Directory: 目标目录，需要把打成的jar包上传至哪个路径下 如：/usr/local/hdx/web/
Command: 上传后执行的操作命令  如：nohup java -jar /usr/local/hdx/web/springboot-wexin.jar

发布成功后会在终端出现成功提示信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224213613751.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
然后大功告成，访问一下试试 [http://wxjssdk.javadog.net/demo.html](http://wxjssdk.javadog.net/demo.html)
如果调试推荐使用**微信开发者工具**，也就是
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224214236995.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
切记**配置nginx**和**服务器安全端口访问权限**！！！否则会404哦！！！


#### 测试
- 1.先来测试下拍照或从手机相册中选图接口
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224215022360.gif)
调试正常

- 2.再来测试微信内置分享
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224215535267.gif)
调试报错，这是个小坑。本狗在这调试了好久，原因出在个人的**订阅号是没有自定义分享权限**的！！
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224215906222.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)

#### 小坑总结
1. 订阅号和服务号所涉及权限不同，需详细查看微信开发文档，查询公众号权限
2. IP白名单未设置,会报40164
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224220440497.png)
IP白名单需要在公众号**安全中心设置**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200224220551848.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2JhaWR1XzI1OTg2MDU5,size_16,color_FFFFFF,t_70)
3. invalid signature 签名异常
建议使用微信JSSDK[签名验证工具](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=jsapisign)验证是否有误


我是JavaDog，谢谢博友耐心看完了，**点个赞👍**再走呗，不行批评点评两句也行啊！！！

![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xOTM1NTczNS1mZTkzZWY5NDIyMjg1Yzg5?x-oss-process=image/format,png)
![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xOTM1NTczNS01MWVlYzhiNmRkMzRjZmM3?x-oss-process=image/format,png)
    

