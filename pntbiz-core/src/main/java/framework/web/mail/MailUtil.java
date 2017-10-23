package framework.web.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("MailUtil")
public class MailUtil 
{    
    private static MailSender mailSender;
    
    @Autowired
    public void setMailSernder(MailSender ms) {
    	mailSender = ms;
    }

    public static void sendMail(String from, String to, String subject, String text)
    {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}