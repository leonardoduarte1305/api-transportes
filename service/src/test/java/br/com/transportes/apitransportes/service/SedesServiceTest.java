package br.com.transportes.apitransportes.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.itextpdf.text.DocumentException;

import br.com.transportes.apitransportes.entity.Confirmacao;
import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Material;
import br.com.transportes.apitransportes.entity.MaterialQuantidadeSetor;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Sede;
import br.com.transportes.apitransportes.entity.Setor;
import br.com.transportes.apitransportes.entity.Uf;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.entity.Viagem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SedesServiceTest extends HelperGeralParaTestes {

	@Test
	void testeA() throws DocumentException, FileNotFoundException {

		// MaterialQuantidadeSetor 1
		Material material1 = criaMaterial(2, "Monitor 32\"", "Monitor 32\" LCD PANASONIC");
		Setor setor1 = criaSetor(1, "RH");
		MaterialQuantidadeSetor materialQuantidadeSetor1 = criaMaterialQuantidadeSetor(1, material1, 5, setor1, null);

		// MaterialQuantidadeSetor 2
		Material material2 = criaMaterial(1, "Monitor 24\"", "Monitor 24\" LCD DELL");
		MaterialQuantidadeSetor materialQuantidadeSetor2 = criaMaterialQuantidadeSetor(2, material2, 3, setor1, null);

		List<MaterialQuantidadeSetor> materiaisQuantidadeSetor = Arrays.asList(materialQuantidadeSetor1,
				materialQuantidadeSetor2);

		List<String> inscritos = Arrays.asList("leonardoduarte1305@gmail.com", "leonardoduarte13052@gmail.com");
		Sede sede = criaSede(1L, "Rua 01", 1234, "88060-000", "Whiterun", Uf.BA, "Procuradoria Geral do Estado",
				"Sem observações", inscritos);

		Destino destino = criaDestino(1, sede, materiaisQuantidadeSetor, Confirmacao.CONFIRMADO, null, false);
		materialQuantidadeSetor1.setDestino(destino);
		materialQuantidadeSetor2.setDestino(destino);

		// -------------------------------------------------------------------------------------------------------------------
		// MaterialQuantidadeSetor 1
		Material material3 = criaMaterial(3, "Caneta Bic", "Caneta esferográfica azul.");
		Setor setor2 = criaSetor(2, "Secretaria");
		MaterialQuantidadeSetor materialQuantidadeSetor3 = criaMaterialQuantidadeSetor(3, material3, 5, setor2, null);

		// MaterialQuantidadeSetor 2
		Material material4 = criaMaterial(4, "Como virar um Milionário", "Livros de finanças em geral.");
		Setor setor3 = criaSetor(3, "Financeiro");
		MaterialQuantidadeSetor materialQuantidadeSetor4 = criaMaterialQuantidadeSetor(4, material4, 15, setor3, null);

		List<MaterialQuantidadeSetor> materiaisQuantidadeSetor2 = Arrays.asList(materialQuantidadeSetor3,
				materialQuantidadeSetor4);

		List<String> inscritos2 = Arrays.asList("leopoldojardim@gmail.com");
		Sede sede2 = criaSede(2L, "Rua 123 de Oliveira 4", 50, "88060-100", "Riften", Uf.SC,
				"Juizado de Pequenas Causas", "Advogados pilantras", inscritos2);

		Destino destino2 = criaDestino(1, sede2, materiaisQuantidadeSetor2, Confirmacao.CONFIRMADO, null, false);
		materialQuantidadeSetor3.setDestino(destino2);
		materialQuantidadeSetor4.setDestino(destino2);

		// -------------------------------------------------------------------------------------------------------------------

		Motorista motorista = criaMotorista(1, "Leopoldo Jardim", "12345678910", "leopoldojardim@gmail.com");
		Veiculo veiculo = criaVeiculo(1, 2021, "Toyotabajata", "Hiluxation Plus", new BigDecimal("123456789"),
				"Grande",
				"ABC-1234");
		String dataSaida = LocalDateTime.now().plusHours(3).toString();
		String dataVolta = LocalDateTime.now().plusDays(1).toString();

		Viagem viagem = criaViagem(1, motorista, veiculo,
				List.of(destino, destino2, destino, destino2, destino, destino2, destino, destino2), dataSaida,
				dataVolta, Math.toIntExact(sede.getId()), Confirmacao.CONFIRMADO, false, false);

		//		CriadorDeRelatorioDeViagem criadorDeRelatorioDeViagem = new CriadorDeRelatorioDeViagem();
		//		criadorDeRelatorioDeViagem.criaRelatorioDeViagem(viagem);
	}
}
