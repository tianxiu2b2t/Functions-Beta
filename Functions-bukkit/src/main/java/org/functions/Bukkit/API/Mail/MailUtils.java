package org.functions.Bukkit.API.Mail;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public final class MailUtils {
    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    private String username="";
    private String password="";
    private String body;

    public MailUtils(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    public void setSmtpHost(String hostName) {
        if (props == null) {
            props = System.getProperties();
        }
        props.put("mail.smtp.host", hostName);
        //props.put("mail.stmp.port", 587);
    }
    public boolean createMimeMessage() {
        try {
            session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            mimeMsg = new MimeMessage(session);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*定义SMTP是否需要验证*/
    public void setNeedAuth(boolean need) {
        if (props == null) {
            props = System.getProperties();
        }
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }
    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
    /*定义邮件主题*/
    public boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*定义邮件正文*/
    public boolean setBody(String mailBody) {
        try {
            body = mailBody;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*设置发信人*/
    public boolean setFrom(String from) {
        try {
            mimeMsg.setFrom(new InternetAddress(from));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*定义收信人*/
    public boolean setTo(String to) {
        if (to == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /*定义抄送人*/
    public boolean setCopyTo(String copyto) {
        if (copyto == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /*发送邮件模块*/
    public boolean sendOut() throws MessagingException, IOException,AuthenticationFailedException{
        mimeMsg.setContent(body, "text/html");
        DataHandler dh = new DataHandler(mimeMsg.getDataHandler().getContent(), "text/html");
        mimeMsg.setDataHandler(dh);
        mimeMsg.setSentDate(new Date());
        mimeMsg.saveChanges();
        Transport.send(mimeMsg);
        return true;
    }
}

