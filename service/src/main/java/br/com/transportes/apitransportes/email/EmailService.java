package br.com.transportes.apitransportes.email;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.itextpdf.text.DocumentException;

import br.com.transportes.apitransportes.entity.Viagem;
import br.com.transportes.apitransportes.pdf.CriadorDeRelatorioDeViagem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class EmailService {

	final String USERNAME = "apitransportesfloripa@gmail.com";
	private final CriadorDeRelatorioDeViagem criadorDeRelatorioDeViagem;
	private final JavaMailSender emailSender;

	public void enviarEmailSimples(String assunto, String mensagem, byte[] anexo, String... destinatarios) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(USERNAME);
			helper.setTo(destinatarios);
			helper.setSubject(assunto);
			helper.setText(mensagem);

			ByteArrayResource documento = new ByteArrayResource(anexo);
			helper.addAttachment("confirmacao_de_viagem", documento);

			emailSender.send(message);
		} catch (MessagingException e) {
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

		enviarEmailSimples(assunto, mensagem, new byte[0], destinos);
	}

	public void enviarConfirmacaoDeViagem(Viagem viagem) {
		String assunto = "AVISO: Viagem Confirmada, partida: " + viagem.getDatetimeSaida();

		StringBuilder builder = new StringBuilder();
		String mensagem = builder.append("A viagem com o id: ").append(viagem.getId()).append(" foi confirmada.\n")
				.append("Confira abaixo os dados completos desta viagem.\n\n")
				.append(viagem).toString();

		byte[] confirmacao = new byte[0];

		try {
			confirmacao = criadorDeRelatorioDeViagem.criaRelatorioDeViagem(viagem);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			log.error("Relatório não pôde ser criado.");
		}

		enviarEmailSimples(assunto, mensagem, confirmacao, viagem.getMotorista().getEmail());
	}

}
