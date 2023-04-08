package br.com.transportes.apitransportes.service;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SedesServiceTest {

		@Test
		void testeA() {

			List<String> jaInscritos = Arrays.asList("ia", "ib", "ic", "id", "ie", "ig");

			List<String> emails = Arrays.asList("id", "ie", "ig", "if", "ih");

			List<String> emailsParaSalvar = emails.stream()
					.filter(jaInscrito -> jaInscritos.stream().noneMatch(email -> email.equals(jaInscrito)))
					.toList();

			log.error("emailsParaSalvar = " + emailsParaSalvar);
		}
}
