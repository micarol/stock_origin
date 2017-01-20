package com.micarol.stock.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sun.mail.util.MailSSLSocketFactory;

public class SendMail {

	private static String filePath = "classes/mail.properties";
	private static Properties props = new Properties();
	
	static{
		try {
			props.load(new FileInputStream(getConfigPathName()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Properties prop;
	
	private static Properties mailServerConfig() throws Exception{
		if(prop == null) {
			prop = new Properties();
//	        prop.put("mail.smtp.starttls.enable", "true");
//	        prop.put("mail.smtp.socketFactory.fallback", "false");
//	        // props.put("mail.smtp.debug", "true");
//	        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//	        prop.put("mail.smtp.port", "465");
//	        prop.put("mail.smtp.socketFactory.port", "465");
//	        prop.put("mail.transport.protocol", "smtp");
	        prop.put("mail.service.username", "ser-micarol@qq.com");
	        prop.put("mail.service.password", "oveztqhcsykecjhf");
	     // 开启debug调试
//	        props.setProperty("mail.debug", "true");
	        // 发送服务器需要身份验证
	        props.setProperty("mail.smtp.auth", "true");
	        // 设置邮件服务器主机名
	        props.setProperty("mail.host", "smtp.qq.com");
	        // 发送邮件协议名称
	        props.setProperty("mail.transport.protocol", "smtp");

	        props.put("mail.smtp.ssl.enable", "true");
	        MailSSLSocketFactory sf = new MailSSLSocketFactory();
	        sf.setTrustAllHosts(true);
	        props.put("mail.smtp.ssl.socketFactory", sf);

		}
        return prop;
	}
	
	public static String getValue(String key){
		return props.getProperty(key);
	}

    public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    }
    private static String getConfigPathName() {
        String str = SendMail.class.getResource("")+"";//
        
        int endIndex=str.indexOf("WEB-INF")+8;
        String strPathName= str.substring(0,endIndex) + filePath;
        if (strPathName.startsWith("file:")) {
            if (strPathName.charAt(7) == ':') { // Windows系统路径
                strPathName = strPathName.substring(6);
            } else { // Unix系统路径
                strPathName = strPathName.substring(5);
            }
            return strPathName;
        }
        if (strPathName.startsWith("jar:file:")) {
            if (strPathName.charAt(11) == ':') { // Windows系统路径
                strPathName = strPathName.substring(10);
            } else { // Unix系统路径
                strPathName = strPathName.substring(9);
            }
            return strPathName;
        }
        if (strPathName.startsWith("vfsfile:")) {
            if (strPathName.charAt(7) == ':') { // Windows系统路径
                strPathName = strPathName.substring(8);
            } else { // Unix系统路径
                strPathName = strPathName.substring(7);
            }
        }
        return strPathName;
    }
	/**
	 * 
	 * @param setTo email to
	 * @param setFrom  email from
	 * @param subject title
	 * @param text  邮件的征文，必须是html的格式
	 * @throws MessagingException
	 */
	public static void sendFileMail(String setTo, String setFrom, String subject, String text) throws Exception {  
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
  
        // 设定mail server  
        senderImpl.setHost(SendMail.getValue("mail.host"));  
        senderImpl.setUsername(SendMail.getValue("mail.service.username"));  
        senderImpl.setPassword(SendMail.getValue("mail.service.password"));
        senderImpl.setJavaMailProperties(mailServerConfig());
        // 建立HTML邮件消息  
        MimeMessage mailMessage = senderImpl.createMimeMessage();  
        // true表示开始附件模式  
        try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");  
			// 设置收件人，寄件人  
			messageHelper.setTo(setTo);  
			messageHelper.setFrom(SendMail.getValue("mail.service.username"), setFrom);  
			messageHelper.setSubject(subject);  
			// true 表示启动HTML格式的邮件  
			messageHelper.setText(text, true);
			senderImpl.send(mailMessage);
			Loggers.RUNNING_LOG.info("邮件发送成功.to:{}, from:{}, subject:{}, content", new Object[]{setTo, setFrom, subject, text});
		} catch (Exception e) {
			Loggers.ERROR_LOG.error("邮件发送失败.to:{}, from:{}, subject:{}, content", new Object[]{setTo, setFrom, subject, text});
			Loggers.ERROR_LOG.error(e.getMessage(), e);
		}          
    }
	
	public static boolean send(String to, String subject, String text, String fromAccout) throws Exception {  
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();    
        // 设定mail server  
        senderImpl.setHost(SendMail.getValue("mail.host"));  
        senderImpl.setUsername(SendMail.getValue("mail."+fromAccout+".username"));  
        senderImpl.setPassword(SendMail.getValue("mail."+fromAccout+".password"));       
        senderImpl.setJavaMailProperties(mailServerConfig());
        // 建立HTML邮件消息  
        MimeMessage mailMessage = senderImpl.createMimeMessage();  
        // true表示开始附件模式  
        try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");  
  
			// 设置收件人，寄件人  
			messageHelper.setTo(to);
			messageHelper.setFrom(SendMail.getValue("mail."+fromAccout+".username"), "");  
			messageHelper.setSubject(subject);  
			// true 表示启动HTML格式的邮件  
			messageHelper.setText(text, true);
			senderImpl.send(mailMessage);  
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        Loggers.RUNNING_LOG.info("邮件发送成功.to:{}, from:{}, subject:{}, content", new Object[]{to, fromAccout, subject, text});
        return true; 
    }
	
	public static void qqMailSend(String to, String subject, String text, String fromAccout) throws Exception{
		Session session = Session.getInstance(props);
        Message msg = new MimeMessage(session);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(new InternetAddress(getValue("mail.service.username")));

        Transport transport = session.getTransport();
        transport.connect(getValue("mail.host"), getValue("mail.service.username"), getValue("mail.service.password"));

        transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
        transport.close();
    }
	
	public static void main(String[] args) throws Exception {
		String subject = "600303提醒,关键词:[股东大会决议公告]";
		String body = "标题:600303:曙光股份2016年11月产销数据快报\n链接:http://data.eastmoney.com/notice/20161207/2Wvl2cbDS7pGaz.html 日期:2016-12-07";
		SendMail.qqMailSend("49134598@qq.com", subject, body, "fromQQTest");
	}
}
