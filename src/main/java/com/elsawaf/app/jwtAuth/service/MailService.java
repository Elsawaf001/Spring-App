package com.elsawaf.app.jwtAuth.service;





import com.sun.mail.smtp.SMTPTransport;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;


import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


import static com.elsawaf.app.jwtAuth.constant.EmailConstant.*;

@Service
public class MailService  {

    @SneakyThrows
    public void sendNewPasswordEmail(String firstName ,
                                     String password ,
                                     String email){
        Message message = createEmail(firstName,password , email);
        SMTPTransport transport = (SMTPTransport) getMailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        transport.connect(GMAIL_SMTP_SERVER,USERNAME,PASSWORD);
        final Address[] allRecipients = message.getAllRecipients();
        transport.sendMessage( message,allRecipients);
        transport.close();
    }



@SneakyThrows
private Message createEmail(String firstName , String password , String email){
    Message message = new MimeMessage(getMailSession());
    message.setFrom(new InternetAddress(FROM_EMAIL));
    message.setRecipients(Message.RecipientType.TO , InternetAddress.parse(email,false));
    message.setRecipients(Message.RecipientType.CC , InternetAddress.parse(CC_EMAIL , false));
    message.setSubject(EMAIL_SUBJECT);
    message.setText("Hello " + firstName +" , \n \n Your New Account Password is "
    + password + "\n \n \n The Support Team");
    message.setSentDate(new Date());
    message.saveChanges();
    return message;
}

    private Session getMailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST , GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH,true);
        properties.put(SMTP_PORT , DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE , true);
        properties.put(SMTP_STARTTLS_REQUIRED , true);
        return Session.getInstance(properties,null);
    }
}
