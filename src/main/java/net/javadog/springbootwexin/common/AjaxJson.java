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
