package com.jt.manager.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.PropertieService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.PicUploadResult;

@Controller
public class PicUploadController {
	//spring中注解拓展 在service层生效	
	/*@PropertyConfig
	private String REPOSITORY_PATH;*/
	
	@Autowired
	private PropertieService propertieService;
	
	//设置请求路径 action ="/pic/upload"
	//返回的json有格式要求，需要url 、width 、height 、 error（图片非法--上传出错：判断） 封装成PicUploadResult对象
	//页面上的文件就被封装成这个MultipartFile 获取文件相应的属性
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult upload(MultipartFile uploadFile) throws Exception{
		/*
		 * 检查图片的类型是否符合要求
		 */
		PicUploadResult result = new PicUploadResult();
		//错误标识
		boolean isLegal =false;
		//允许（合法的）类型 正则
		String[] allowType = new String[]{".jpg",".png"};
		for (String type : allowType) {
			if(uploadFile.getOriginalFilename().endsWith(type)){//判断是否是合法类型的文件
				//是，将标识改为true
				isLegal=true;
				break;//匹配类型之一 则跳出循环
			}
		}
		if(!isLegal){
			result.setError(1); //0没错 1有错
		}else{
			//设置初始值
			result.setError(0);
		}
		
		//扩展名
		String extName = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
		//文件名称 yyyy-MM-dd hh:mm:ss：SSSS+3位随机值 ： 避免高并发场景下时间可能相同的情况
		String fileName = ""+System.currentTimeMillis()+RandomUtils.nextInt(100, 999)+extName;
		
		/*
		 *文件保存路径的处理 
		 */
		
		//1、获取当前时间
		Date curDate = new Date();
		//2、使用时间值设置多级路径 simplDate
		String path = new DateTime(curDate).toString("yyyy")+File.separator+
				new DateTime(curDate).toString("MM")+File.separator+
				new DateTime(curDate).toString("dd")+File.separator;
		
		//图片访问路径
		String url = propertieService.IMAGE_BASE_URL+"/images/"+path;
		
		//拼接图片保存路径 绝对路径
		path = propertieService.REPOSITORY_PATH+"/images/"+path;
		
		//文件夹的处理
		File dir = new File(path);
		//目录不存在的话 创建目录
		if(!dir.exists()){
			dir.mkdirs();//防止目录为空
		}
		
		//把文件保存下来
		uploadFile.transferTo(new File(path+fileName));
		
		/*
		 * 判断木马 前提：图片文件是有特殊属性的，width 、height
		 */
		//此时文件已经上传了
		File newFile = new File(path+fileName);
		BufferedImage image = ImageIO.read(newFile);
		try {
			//正确
			result.setUrl(url+fileName);
			//将获取到得图片文件的宽高设置给上传的图片
			result.setWidth(""+image.getWidth());
			result.setHeight(""+image.getHeight());
		} catch (Exception e) {
			result.setError(1);//出错
			newFile.delete();//表示是木马文件 删除
		}
		return result;	
	}

}
