package com.course.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.dom4j.DocumentException;

import com.course.common.security.Digests;
import com.eg.lockcloud.cxf.IEGLockCloudProxy;


public class CheckXml {
	
	public static String CheckTaskSucc(String taskId) throws IOException, DocumentException{
		String pass = "";
		IEGLockCloudProxy  ieglockcloudproxy=new IEGLockCloudProxy();
		InputStream data;
		try {
			data = new ByteArrayInputStream("123456".getBytes("UTF-8"));
			pass = Encodes.encodeHex(Digests.md5(data)).toLowerCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String checkxml="<?xml version='1.0' encoding='UTF-8'?>"+
				"<eglockcloud name='CheckTaskSucc'>"+
				"<pkgId>1</pkgId>"+
				"<user>wzzx</user>"+
				"<pass>"+pass+"</pass>"+
				"<taskId>"+taskId+"</taskId>"+
				"</eglockcloud>";
		String checksml=ieglockcloudproxy.transXml(checkxml);

		return checksml;
	}
	
	public static String ErrorRemark(Integer result){
		String errorremark="正确";
		if(result==1){
			errorremark="xml格式错误";
		}else if(result==2){
			errorremark="xml请求中缺少必要的参数数据";
		}else if(result==3){
			errorremark="用户名和密码所表示的用户账号不存在";
		}else if(result==4){
			errorremark="用户账号的类型错误。只有一级管理员类型的账号才可以调用Webservice接口";
		}else if(result==5){
			errorremark="请求Xml的根节点中的 name 属性值不正确";
		}else if(result==6){
			errorremark="请求中lockAddr所对应的门锁记录不存在";
		}else if(result==7){
			errorremark="请求中lockAddr所对应的门锁配置未生效";
		}else if(result==8){
			errorremark="该用户账号对该门锁没有管辖权限";
		}else if(result==9){
			errorremark="就绪";
		}else if(result==10){
			errorremark="数据：主控器未发送给主控器时候发生错误";
		}else if(result==11){
			errorremark="每次设置权限的数量不能超过30";
		}else if(result==12){
			errorremark="根据传入的taskId无法找到任务记录数据";
		}else if(result==13){
			errorremark="任务正在处理中，未完成";
		}else if(result==14){
			errorremark="发卡小软件未连接";
		}else if(result==15){
			errorremark="卡片信息读取失败";
		}else if(result==16){
			errorremark="数据写入卡片失败";
		}else if(result==20){
			errorremark="无门锁开门权限";
		}else if(result==21){
			errorremark="主控器不存在";
		}else if(result==22){
			errorremark="取电开关不存在";
		}else if(result==23){
			errorremark="取电开关配置未生效";
		}else if(result==24){
			errorremark="该用户账号对该取电开关没有管辖权限";
		}else if(result==25){
			errorremark="解密密文失败";
		}else if(result==26){
			errorremark="参数类型转换出错";
		}else if(result==27){
			errorremark="扩展模块不存在";
		}else if(result==28){
			errorremark="请求中的扩展模块配置未生效";
		}else if(result==29){
			errorremark="设置的配置不能超过最大的允许数量";
		}else if(result==30){
			errorremark="设备配置列表中已存在";
		}else if(result==31){
			errorremark="主控器RMI地址不存在";
		}else if(result==32){
			errorremark="主控器未联机";
		}else if(result==33){
			errorremark="设备已存在";
		}else if(result==34){
			errorremark="取电开关地址为空";
		}else if(result==35){
			errorremark="取电开关地址为空，至少应有一个地址作为查询条件";
		}
		return errorremark;
	}
}
