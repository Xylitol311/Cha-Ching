package com.example.chaching.user.mail;

import jakarta.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailComponent {

    private static final String fromName = "Cha-Ching 매니저";
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerifyLink(Long id, String toEmail, String toName) {
        String title = "[Cha-Ching] 회원가입을 위해 메일을 인증해 주세요.";
        String verification_url = "http://localhost:8080/api/user/verify/" + id;

        Context context = new Context();
        context.setVariable("name", toName);
        context.setVariable("verification_url", verification_url);

        String contents = templateEngine.process("registerUser", context);

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            InternetAddress from = addressFromTo(fromEmail, fromName);

            InternetAddress to = addressFromTo(toEmail, toName);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(contents, true);
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private InternetAddress addressFromTo(String email, String name) {
        InternetAddress address = new InternetAddress();
        try {
            address.setAddress(email);
            address.setPersonal(name);
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
        }
        return address;
    }

    public void sendUsername(String toEmail, String toName) {
        String title = "[ZB E-Commerce] 요청하신 아이디를 확인해주세요.";

        Context context = new Context();
        context.setVariable("name", toName);

        String contents = templateEngine.process("findUsername", context);

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            InternetAddress from = addressFromTo(fromEmail, fromName);

            InternetAddress to = addressFromTo(toEmail, toName);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(contents, true);
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void sendTemporaryPassword(String toEmail, String toName, String temporaryPassword) {
        String title = "[ZB E-Commerce] 요청하신 비밀번호를 확인해주세요.";

        Context context = new Context();
        context.setVariable("temporaryPassword", temporaryPassword);

        String contents = templateEngine.process("findPassword", context);

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            InternetAddress from = addressFromTo(fromEmail, fromName);

            InternetAddress to = addressFromTo(toEmail, toName);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(contents, true);
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}