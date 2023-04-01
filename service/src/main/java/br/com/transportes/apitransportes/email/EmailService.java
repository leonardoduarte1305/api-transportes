package br.com.transportes.apitransportes.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class EmailService {

	final String USERNAME = "apitransportesfloripa@gmail.com";

	@Autowired
	private JavaMailSender emailSender;

	public void enviarEmailSimples(String assunto, String mensagem, String... destinatarios) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(USERNAME);
			message.setTo(destinatarios);
			message.setSubject(assunto);
			message.setText(mensagem);
			emailSender.send(message);
		} catch (MailException e) {
			log.error("Não foi possível enviar o email.");
		}
	}

}
