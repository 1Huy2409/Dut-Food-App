package Helper;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
public class SendingEmail {
    public static String randomOtp(int length)
    {
        String chain = "0123456789";
        String res = "";
        for (int i = 0; i < length; i++)
        {
            res += chain.charAt((int) Math.floor(Math.random()*10));
        }
        return res;
    }
    public static void sendMail(String recepient, String OTP) throws MessagingException {
        Dotenv dotenv = Dotenv.load();
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccEmail = dotenv.get("APP_ACCOUNT");
        String password = dotenv.get("APP_PASSWORD");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccEmail, password);
            }
        });
        Message message = prepareMessage(session, myAccEmail, recepient, OTP);
        Transport.send(message);
    }
    private static Message prepareMessage(Session session, String myAccEmail, String recepient, String OTP)
    {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("OTP Reset password authentication!");
            String htmlCode = "<h2>This is your OTP code! This will be expired after 5 minutes!<b>" + OTP + "</b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
