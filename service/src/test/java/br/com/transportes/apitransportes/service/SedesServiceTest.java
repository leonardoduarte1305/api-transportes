package br.com.transportes.apitransportes.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@Test
	void forcaBruta1() {
		/*
			Força Bruta:
			a) Exercício 1: Escreva um programa que encontre todos os pares de números em uma lista cuja soma seja
			igual a um valor fornecido.
		 */
		// lista de numeros
		// parear todas as alternativas
		// somar os pares
		// verificar a maior soma dentre elas

		Integer primeiroNumero = 0;
		Integer segundoNumero = 0;
		Integer soma = 0;

		Integer valorFornecido = 205;

		Set<Integer> listaDeNumeros = new HashSet<>();
		for (int index = 0; index < 200; index++) {
			Integer numero = gerarNumeroAleatorioAte(120);
			listaDeNumeros.add(numero);
		}

		for (int normal = 0; normal < listaDeNumeros.size(); normal++) {
			for (int reverse = listaDeNumeros.size() - 1; reverse >= 0; reverse--) {
				if (normal == reverse) {
					break;
				}

				Integer somados = normal + reverse;
				if (somados == valorFornecido) {
					soma = somados;
					primeiroNumero = normal;
					segundoNumero = reverse;
				}
			}
		}

		System.err.println("Primeiro Numero: " + primeiroNumero);
		System.err.println("Segundo Numero: " + segundoNumero);
		System.err.println("Soma: " + soma);

	}

	@Test
	void forcaBruta2() {
		/*
			Força Bruta:
			b) Exercício 2: Escreva um programa que encontre a combinação de números em uma lista que maximize a soma,
			 sem repetir nenhum elemento.
		 */
	}

	@Test
	void dividirEConquistar1() {
		/*
			Dividir e Conquistar:
			a) Exercício 1: Implemente uma função recursiva para calcular o fatorial de um número inteiro.
		 */
	}

	@Test
	void dividirEConquistar2() {
		/*
			Dividir e Conquistar:
			b) Exercício 2: Escreva um algoritmo para encontrar o número máximo em uma matriz 2D dividindo-a em
			 submatrizes menores.
		 */
	}

	@Test
	void programacaoDinamica1() {
		/*
			Programação Dinâmica:
			a) Exercício 1: Implemente um programa que calcule o n-ésimo número da sequência de Fibonacci usando
			programação dinâmica.
		 */
	}

	@Test
	void programacaoDinamica2() {
		/*
			Programação Dinâmica:
			b) Exercício 2: Resolva o problema da mochila (knapsack problem) usando programação dinâmica para
			encontrar a
			combinação de itens que maximize o valor total sem exceder uma capacidade pré-definida.
		 */
	}

	@Test
	void algoritmosGulosos1() {
		/*
			Algoritmos Gulosos:
			a) Exercício 1: Implemente um algoritmo que encontre a quantidade mínima de moedas para dar o troco em um
			determinado valor.
		 */
	}

	@Test
	void algoritmosGulosos2() {
		/*
			Algoritmos Gulosos:
			b) Exercício 2: Escreva um programa que encontre a programação de atividades com o maior número possível de
			tarefas não sobrepostas.
		 */
	}

	@Test
	void backTracking1() {
		/*
			Backtracking:
			a) Exercício 1: Implemente um programa que encontre todas as permutações de uma string fornecida.
		 */
	}

	@Test
	void backTracking2() {
		/*
			Backtracking:
			b) Exercício 2: Resolva o problema das N Rainhas, encontrando todas as configurações válidas do
			tabuleiro de xadrez com N rainhas sem se atacarem.
		 */
	}

	@Test
	void buscaEmProfundidadeBuscaEmLargura1() {
		/*
			Busca em Profundidade (DFS) e Busca em Largura (BFS):
			a) Exercício 1: Escreva um programa que encontre um caminho de um vértice inicial para um vértice final
			em um grafo usando busca em profundidade.
		 */
	}

	@Test
	void buscaEmProfundidadeBuscaEmLargura2() {
		/*
			Busca em Profundidade (DFS) e Busca em Largura (BFS):
			b) Exercício 2: Implemente um algoritmo de busca em largura para encontrar o menor caminho entre dois
			vértices em um grafo.
		 */
	}

	private int gerarNumeroAleatorioAte(Integer limiteMaximo) {
		return Math.toIntExact(Math.round(Math.random() * limiteMaximo));
	}
}
