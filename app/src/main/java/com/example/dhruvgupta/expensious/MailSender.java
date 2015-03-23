package com.example.dhruvgupta.expensious;

import android.util.Log;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class MailSender extends Authenticator
{
    final String host = "smtp.gmail.com";
    private static final String address = "shubhampuri87@gmail.com";
    final String pass = "Shhubhamrock27";
    final String to = "grvgupta36@gmail.com";
    Multipart multiPart;

    public void sendEmail() throws MessagingException
	{
        String finalString = "";
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", address);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Log.i("Check", "done pops");
        try 
		{
            MailAuthenticator auth = new MailAuthenticator(address, pass);
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(false);

            DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(address));
            message.setDataHandler(handler);
            Log.i("Check", "done sessions");

            multiPart = new MimeMultipart();
            InternetAddress toAddress;
            toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            Log.i("Check", "added recipient");

            message.setSubject("Send Auto-Mail");
            message.setContent(multiPart);
            message.setText("Demo For Sending Mail in Android Automatically");
            Log.i("check", "transport");

            Transport transport = session.getTransport("smtps");
            Log.i("check", "connecting");

            transport.connect(host, address, pass);
            Log.i("check", "wanna send");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            Log.i("check", "sent");
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    class MailAuthenticator extends Authenticator
	{
        String user;
        String pw;
        public MailAuthenticator (String username, String password)
        {
            super();
            this.user = username;
            this.pw = password;
        }
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(user, pw);
        }
    }
}

