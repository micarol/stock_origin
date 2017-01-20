package com.micarol.stock.util;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 发送腾讯邮件的例子, 可删除
 * @author micarol
 *
 */
public class MailTool {
    public static void main(String[] args) throws MessagingException, GeneralSecurityException {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject("testqqmail");
//        StringBuilder builder = new StringBuilder();
//        builder.append("url = " + "http://blog.csdn.net/never_cxb/article/details/50524571");
//        builder.append("\n页面爬虫错误");
//        builder.append("\n时间 " + new Date().toString());
//        msg.setText(builder.toString());
        msg.setText("test micarol");
        msg.setFrom(new InternetAddress("ser-micarol@qq.com"));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "ser-micarol@qq.com", "oveztqhcsykecjhf");

        transport.sendMessage(msg, new Address[] { new InternetAddress("49134598@qq.com") });
        transport.close();
    }
}