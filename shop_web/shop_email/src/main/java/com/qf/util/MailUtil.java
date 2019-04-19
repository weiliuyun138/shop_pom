package com.qf.util;

import com.qf.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送邮件
     */
    public void sendEmail(Email email) {
        //创建一封邮件
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        //创建一个邮件的包装对象
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage);

        try {
            //设置发送方
            helper.setFrom(from,"keke在线");
            //设置接收方
            helper.setTo(email.getTo());//设置普通的接收方
//            helper.setCc();//设置抄送方
//            helper.setBcc();//设置密送方
            //设置标题
            helper.setSubject(email.getSubject());

            //设置内容 - 第二个参数表示是否按html解析内容
            helper.setText(email.getContent(), true);

            //设置当前时间
            helper.setSentDate(new Date());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //发送邮件
        javaMailSender.send(mailMessage);


    }

}
