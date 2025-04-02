package com.example.sidedemo.User.service.notifiaction;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    /**
     * 인증코드를 HTML 형식으로 이메일 전송
     * @param to 수신자 이메일
     * @param subject 이메일 제목
     * @param authCode 인증코드 (6자리 등)
     */
    public void sendAuthCodeMail(String to, String subject, String authCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 'true'로 하면 multipart 형식 (첨부파일) / 'false'로 하면 singlepart
            // 아래 "UTF-8"은 이메일 인코딩
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // HTML 컨텐츠 작성 예시
            String htmlContent = buildAuthHtmlContent(authCode);

            // 두 번째 파라미터 'true'를 주면 HTML로 인식
            helper.setText(htmlContent, true);

            // 메일 전송
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * 인증코드를 HTML 형식으로 꾸며주는 메서드
     */
    private String buildAuthHtmlContent(String authCode) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #2E86C1;'>인증번호 안내</h2>" +
                "<p>아래의 인증번호를 입력해 인증을 완료하세요:</p>" +
                "<div style='font-size: 24px; font-weight: bold; color: #E74C3C; " +
                "border: 1px solid #E74C3C; display: inline-block; padding: 10px 20px;'>" +
                authCode +
                "</div><br><br>" +
                "<p>감사합니다!</p>" +
                "</body>" +
                "</html>";
    }
}

