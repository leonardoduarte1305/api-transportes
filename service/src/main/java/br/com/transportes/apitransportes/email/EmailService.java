package br.com.transportes.apitransportes.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import br.com.transportes.apitransportes.entity.Viagem;
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
			e.printStackTrace();
			log.error("Não foi possível enviar o email.");
		}
	}

	public void enviarConfirmacaoDeDestino(String sedeDestino, List<String> destinatarios) {

		String assunto = "AVISO: Destino relevante: " + sedeDestino;
		String mensagem = "Acaba se ser solicitada uma viagem para: " + sedeDestino +
				", aproveite e insira seus ativos imobilizados para transporte.";

		String[] emails = new String[destinatarios.size()];
		String[] destinos = destinatarios.toArray(emails);

		enviarEmailSimples(assunto, mensagem, destinos);
	}

	public void enviarConfirmacaoDeViagem(Viagem viagem) {
		String assunto = "AVISO: Viagem Confirmada, partida: " + viagem.getDatetimeSaida();

		StringBuilder builder = new StringBuilder();
		String mensagem = builder.append("A viagem com o id: ").append(viagem.getId()).append(" foi confirmada.\n")
				.append("Confira abaixo os dados completos desta viagem.\n\n")
				.append(viagem).toString();

		enviarEmailSimples(assunto, mensagem, viagem.getMotorista().getEmail());
	}

}
