package com.brocast.api.notification.service;

import com.brocast.api.notification.enums.EnumErrors;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by sardor on 1/7/14.
 */
public final class MailNotifier {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MailNotifier.class);

    public MailNotifier() {
    }

    public static void sendMailBox(String body, String title, String email) {

        Pattern rfc2822 =
                Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
        if (!rfc2822.matcher(email).matches()) {
            log.debug("ERROR MAIL MATCHER");
            return;
        }

        final String username = "info@brocast.com";
        final String password = "XXXXXXXXXXXXXXx";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.zoho.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            MimeMessage message = new MimeMessage(session);
            if (title == null || title.isEmpty()) {
                title = "BroCast: Live Stream";
            }

            message.setFrom(new InternetAddress("info@brocast.com", "BroCast"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));
            message.setSubject(title, "UTF-8");
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setContent(body, "text/html; charset=UTF-8");
            log.debug("SENDING MAIL: " + email);
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("CANNOT SEND EMAIL ", e);
        }

    }


}
