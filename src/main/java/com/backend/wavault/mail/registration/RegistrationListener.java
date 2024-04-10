package com.backend.wavault.mail.registration;

import com.backend.wavault.utils.EmailUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationEvent> {


    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final HttpServletRequest  request;


    @Override
    public void onApplicationEvent(OnRegistrationEvent event) {

        try {
            this.confirmRegistration(event);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    private void confirmRegistration(OnRegistrationEvent event) throws MessagingException,
            UnsupportedEncodingException {

        String subject = "Email Confirmation";
        String urlVariableName = "confirmationEmailUrl";
        String templateName = "registrationConfirmation";
        String recipientEmail = event.getUser().getEmail();
        String confirmationUrl = EmailUtils.frontEndAppUrl(request) + "/api/v1/account/registration/verify?t=" +
                event.getToken()+"&email="+recipientEmail;

        sendEmail(recipientEmail, event.getUser().getFirstName(),
                subject, templateName, urlVariableName, confirmationUrl);

    }

    public void sendEmail(String email, String name, String subject,
                          String templateName, String urlVariableName,
                          String confirmationUrl) throws MessagingException,
            UnsupportedEncodingException {

        String senderName = "EntryGenius";
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable(urlVariableName, confirmationUrl);
        String mailContent = templateEngine.process(templateName, context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("documentapprovalsystem@gmail.com", senderName);
        helper.setSubject(subject);
        helper.setTo(email);
        helper.setText(mailContent, true);
        javaMailSender.send(mimeMessage);
    }
}
