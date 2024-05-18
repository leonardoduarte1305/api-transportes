package br.com.transportes.apitransportes.email;

import br.com.transportes.apitransportes.FullApiImplApplication;
import br.com.transportes.apitransportes.entity.Viagem;
import br.com.transportes.apitransportes.helper.HelperGeral;
import br.com.transportes.apitransportes.pdf.CriadorDeRelatorioDeViagem;
import com.itextpdf.text.DocumentException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = FullApiImplApplication.class)
class EmailServiceTest {

    final String USERNAME = "apitransportesfloripa@gmail.com";

    @Autowired
    EmailService emailService;
    @MockBean
    JavaMailSender emailSender;
    @MockBean
    CriadorDeRelatorioDeViagem criadorDeRelatorioDeViagem;

    @Test
    void enviaConfirmacaoDeviagemCorretamente() throws DocumentException, FileNotFoundException {
        criaMimeMessageQuandoNecessario();
        naoFazerNadaQuandoForEnviarOEmail();

        Viagem viagem = new HelperGeral().criarViagemCompleta();
        when(criadorDeRelatorioDeViagem.criaRelatorioDeViagem(viagem)).thenReturn(viagem.toString().getBytes());

        emailService.enviarConfirmacaoDeViagem(viagem);

        Mockito.verify(emailSender, Mockito.times(1)).send(any(MimeMessage.class));
    }

    @Test
    void lancaExceptionQuandoTentaAnexarOArquivo() throws DocumentException, FileNotFoundException {
        criaMimeMessageQuandoNecessario();

        Viagem viagem = new HelperGeral().criarViagemCompleta();
        when(criadorDeRelatorioDeViagem.criaRelatorioDeViagem(viagem)).thenThrow(FileNotFoundException.class);

        Assertions.assertThrows(RuntimeException.class, () -> emailService.enviarConfirmacaoDeViagem(viagem));

        Mockito.verifyNoInteractions(emailSender);
    }

    @Test
    void enviaEmailSimplesCorretamente() {
        criaMimeMessageQuandoNecessario();
        naoFazerNadaQuandoForEnviarOEmail();

        emailService.enviarEmailSimples("assunto", "uma mensagem curta",
                new byte[0], "leonardoduarte13052@gmail.com");

        Mockito.verify(emailSender, Mockito.times(1)).send(any(MimeMessage.class));
    }

    @Test
    void deveEnviarConfirmacaoDeDestinoCorretamente() {
        criaMimeMessageQuandoNecessario();
        naoFazerNadaQuandoForEnviarOEmail();

        List<String> destinatarios = Arrays.asList("leonardoduarte13052@gmail.com", "apitransportesfloripa@gmail.com");
        emailService.enviarConfirmacaoDeDestino("Urussanga", destinatarios);

        Mockito.verify(emailSender, Mockito.times(1)).send(any(MimeMessage.class));
    }

    private void naoFazerNadaQuandoForEnviarOEmail() {
        Mockito.doNothing().when(emailSender).send(any(MimeMessage.class));
    }

    private void criaMimeMessageQuandoNecessario() {
        when(emailSender.createMimeMessage()).thenReturn(new JavaMailSenderImpl().createMimeMessage());
    }
}
