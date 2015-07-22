package ar.com.wuik.sistema.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MailUtil.class);

	public static void sendMailBackup(String file) throws Exception {

		try {

			Properties props = ParametrosUtil.getProperties();

			String[] to = ((String) props.get("mail.to")).split("\\|");

			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress((String) props.get("mail.from")));

			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) { // changed from a while loop
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) { // changed from a while
				// loop
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}
			message.setSubject((String) props.get("mail.subject"));

			// create the second message part
			MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			FileDataSource fds = new FileDataSource(file);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp2);

			message.setContent(mp);

			Transport transport = session.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"),
					(String) props.get("mail.from"),
					(String) props.get("mail.smtp.password"));
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (MessagingException mexc) {
			LOGGER.error("Error al enviar Mail", mexc);
			throw new Exception("Error al enviar Mail", mexc);
		}
	}

}
