package com.qf.listner;

import com.qf.entity.Email;
import com.qf.util.MailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailRabbitMQListner {

    @Autowired
    private MailUtil mailUtil;

    /**
     * 处理rabbitMQ中的邮件对象
     * @param email
     */
    @RabbitListener(queues = "email_queue")
    public void emailHandler(Email email) {

        //发送邮件
        try {
            mailUtil.sendEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
