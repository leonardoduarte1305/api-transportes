package br.com.transportes.apitransportes.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.transportes.apitransportes.entity.Destino;
import br.com.transportes.apitransportes.entity.Motorista;
import br.com.transportes.apitransportes.entity.Veiculo;
import br.com.transportes.apitransportes.entity.Viagem;

public class CriadorDeRelatorioDeViagem {

	private static final Font FONT_14 = fontePadraoComTamanhoDe(14);
	private static final Font FONT_12 = fontePadraoComTamanhoDe(12);

	public void criaRelatorioDeViagem(Viagem viagem) throws FileNotFoundException, DocumentException {

		Document documento = criaNovoDocumento();
		documento.open();

		cabecalhoComDadosGerais(viagem, documento);
		separarSecoesComUmaLinha(documento);

		criaTabelasDeDestinos(viagem.getDestinos(), documento);

		documento.close();
	}

	private Document criaNovoDocumento() throws DocumentException, FileNotFoundException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(Instant.now().getEpochSecond() + ".pdf"));
		return document;
	}

	private void cabecalhoComDadosGerais(Viagem viagem, Document document) throws DocumentException {
		document.add(dadosDaViagem(viagem));
		document.add(dadosDoMotorista(viagem.getMotorista()));
		document.add(dadosDoVeiculo(viagem.getVeiculo()));
	}

	private static Font fontePadraoComTamanhoDe(int size) {
		return FontFactory.getFont(FontFactory.COURIER, size, BaseColor.BLACK);
	}

	private Paragraph dadosDaViagem(Viagem viagem) {
		StringBuilder dadosDaViagem = new StringBuilder()
				.append("Viagem ID: ").append(viagem.getId()).append("\n")
				.append("    Saida: ").append(formatarData(viagem.getDatetimeSaida())).append("\n")
				.append("    Volta: ").append(formatarData(viagem.getDatetimeVolta())).append("\n");
		return new Paragraph(dadosDaViagem.toString(), FONT_14);
	}

	private String formatarData(String data) {
		try {
			return LocalDateTime.parse(data)
					.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		} catch (NullPointerException e) {
			return "";
		}
	}

	private Paragraph dadosDoMotorista(Motorista motorista) {
		StringBuilder dadosDoMotorista = new StringBuilder()
				.append("Motorista: ").append(motorista.getNome()).append("\n")
				.append("      CNH: ").append(motorista.getCarteira()).append("\n")
				.append("    Email: ").append(motorista.getEmail()).append("\n");
		return new Paragraph(dadosDoMotorista.toString(), FONT_14);
	}

	private Paragraph dadosDoVeiculo(Veiculo veiculo) {
		StringBuilder dadosDoVeiculo = new StringBuilder()
				.append("Veículo: ").append(veiculo.getModelo()).append("\n")
				.append("  Marca: ").append(veiculo.getMarca()).append("\n")
				.append("  Placa: ").append(veiculo.getPlaca()).append("\n");
		return new Paragraph(dadosDoVeiculo.toString(), FONT_14);
	}

	private void separarSecoesComUmaLinha(Document documento) throws DocumentException {
		documento.add(
				new Paragraph("______________________________________________________________________________\n"));
	}

	private void criaTabelasDeDestinos(List<Destino> destinos, Document documento) {

		destinos.forEach(destino -> {
			try {
				documento.add(dadosDoDestino(destino, FONT_12));
				PdfPTable table = criaTabelaComHeaders();

				destino.getMateriaisQntdSetor().forEach(itemDaLista -> {
					table.addCell(itemDaLista.getMaterial().getNome());
					table.addCell(itemDaLista.getQuantidade().toString());
					table.addCell(itemDaLista.getSetorDestino().getNome());
					table.addCell("");
				});
				documento.add(table);
				documento.add(new Paragraph("\n"));

			} catch (DocumentException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private Paragraph dadosDoDestino(Destino destino, Font font) {
		StringBuilder dadosDoDestino = new StringBuilder()
				.append("Destino: ").append(destino.getSede().getNome()).append("\n")
				.append(" Status: ").append(destino.statusToString()).append("\n\n");
		return new Paragraph(dadosDoDestino.toString(), font);
	}

	private PdfPTable criaTabelaComHeaders() throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		table.setSpacingBefore(0f);
		table.setSpacingAfter(0f);
		table.setWidths(new int[] { 2, 1, 1, 1 });
		adicionarHeader(table, "Material", "Quant", "Setor Destino", "Ass. Recebedor");
		return table;
	}

	private void adicionarHeader(PdfPTable table, String... headers) {
		Stream.of(headers)
				.forEach(tituloDaColuna -> {
					PdfPCell header = new PdfPCell();
					header.setBackgroundColor(BaseColor.LIGHT_GRAY);
					header.setBorderWidth(1);
					header.setPhrase(new Phrase(tituloDaColuna));
					header.setHorizontalAlignment(1);
					table.addCell(header);
				});
	}
}
