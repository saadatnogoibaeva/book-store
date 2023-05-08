package kg.alatoo.demooauth.Sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String toEmail, String subject, String code, int buying_book_id){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("addafaidn184@gmail.com");
        message.setTo(toEmail);
        message.setText("Good job");
        message.setSubject(subject+" and ur good's id is: " + buying_book_id);
        message.setSubject(code);
        mailSender.send(message);
    }
}
