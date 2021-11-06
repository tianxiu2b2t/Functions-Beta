package org.functions.Bukkit.API.Mail;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPSenderFailedException;
import com.sun.mail.util.MailSSLSocketFactory;
import org.bukkit.entity.Player;
import org.functions.Bukkit.Main.Functions;

import javax.mail.*;
import javax.mail.internet.InternetAddress;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Mail {
    public static String toUnicode(String s) {
        String[] as = new String[s.length()];
        StringBuilder s1 = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            as[i] = Integer.toHexString(s.charAt(i) & 0xffff);
            s1.append("\\u").append(as[i]);
        }
        return s1.toString();
    }
    public static boolean sendRecoveryPassword(String to, Player player, String code, String minutes) {
        try {
            Properties props = new Properties();

            // 开启debug调试
            props.setProperty("mail.debug", Functions.instance.getConfig().getString("Debug","false"));
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", Functions.instance.getConfiguration().getSettings().getBoolean("Mail.Auth", true) + "");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", Functions.instance.getConfiguration().getSettings().getString("Mail.Host", "smtp.qq.com"));
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", Functions.instance.getConfiguration().getSettings().getString("Mail.protocol", "smtp"));

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", Functions.instance.getConfiguration().getSettings().getBoolean("Mail.SSL", true));
            props.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(props);

            Message msg = new MimeMessage(session);
            msg.setSubject(Functions.instance.getConfiguration().getSettings().getString("Mail.Subject", "This is a server send you test."));
            StringBuilder builder = new StringBuilder();
            String temp = /*"<h1>亲爱的 <playername />，你好</h1>\n" +
                    "\n" +
                    "<p>\n" +
                    "    你有一个可以重置你的密码的服务器 <servername/>.\n" +
                    "    请使用这串验证码：<h1> <code/> </h1> 或者使用这串指令： /mailcode <code/>.\n" +
                    "</p>\n" +
                    "<p>\n" +
                    "    验证码有效时间为 <timeout/> 分钟\n" +
                    "</p>\n";*/ "";
            temp = Functions.instance.getConfiguration().ReadRecoveryFile();
            if (temp.endsWith("\n")) temp = temp.substring(0,temp.length()-2);
            builder.append(temp.replace("<code/>",code).replace("<timeout/>",minutes).replace("<servername/>",Functions.instance.getConfiguration().getConfig().getString("RecoveryPasswordServerName","RecoveryPassword Server.")).replace("<playername/>", player.getName()).replace("<uuid/>", player.getUniqueId().toString()).replace("<playeraddress/>",Functions.instance.location.getCountry(player.getAddress().getAddress().getHostAddress())));
            msg.setContent(builder.toString(),"text/html;");
            String name = Functions.instance.getConfiguration().getSettings().getString("Mail.FromName",Functions.instance.getConfiguration().getConfig().getString("RecoveryPasswordServerName","RecoveryPassword Server."));
            //name = toUnicode(name);
            msg.setFrom(new InternetAddress(Functions.instance.getConfiguration().getSettings().getString("Mail.From"),name));
            Transport transport = session.getTransport();
            transport.connect(Functions.instance.getConfiguration().getSettings().getString("Mail.Host", "smtp.qq.com"), Functions.instance.getConfiguration().getSettings().getString("Mail.From"), Functions.instance.getConfiguration().getSettings().getString("Mail.Password"));
            transport.sendMessage(msg, new Address[]{new InternetAddress(to)});
            transport.close();
        } catch (MessagingException | GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /*public static void sendUser1(String to,String text) throws MessagingException, GeneralSecurityException {
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
        msg.setSubject("This is a server send you test.");
        StringBuilder builder = new StringBuilder();
        builder.append(text);
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress("794609509@qq.com"));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "794609509@qq.com", "usdqrscvyuyjbbaj");
        transport.sendMessage(msg, new Address[]{new InternetAddress("984494218@qq.com")});
        transport.close();
    }*/
    public static void sendUsers(String[] to) throws MessagingException, GeneralSecurityException {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", Functions.instance.getConfiguration().getSettings().getBoolean("Mail.Debug",true)+"");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", Functions.instance.getConfiguration().getSettings().getBoolean("Mail.Auth",true)+"");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", Functions.instance.getConfiguration().getSettings().getString("Mail.Host","smtp.qq.com"));
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", Functions.instance.getConfiguration().getSettings().getString("Mail.protocol","smtp"));

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", Functions.instance.getConfiguration().getSettings().getBoolean("Mail.SSL",true));
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject(Functions.instance.getConfiguration().getSettings().getString("Mail.Subject","This is a server send you test."));
        StringBuilder builder = new StringBuilder();
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress(Functions.instance.getConfiguration().getSettings().getString("Mail.From")));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", Functions.instance.getConfiguration().getSettings().getString("Mail.From"), Functions.instance.getConfiguration().getSettings().getString("Mail.Password"));
        for (String s : to) {
            transport.sendMessage(msg, new Address[]{new InternetAddress(s)});
        }
        transport.close();
    }
}