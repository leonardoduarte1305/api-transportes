package br.com.transportes.apitransportes.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.transportes.apitransportes.exception.EntidadeNaoEncontradaException;
import br.com.transportes.apitransportes.mapper.MateriaisMapper;
import br.com.transportes.apitransportes.repository.MateriaisRepository;
import br.com.transportes.server.model.Material;
import br.com.transportes.server.model.UpsertMaterial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MateriaisService {

	private final MateriaisRepository materiaisRepository;

	private final MateriaisMapper materiaisMapper;

	public Material upsertMaterial(String id, UpsertMaterial upsertMaterial) {
		if (id.isBlank() || id.isEmpty()) {
			br.com.transportes.apitransportes.entity.Material veiculoSalvo = materiaisRepository.save(
					materiaisMapper.toMaterialEntity(upsertMaterial));

			return materiaisMapper.toMaterialDto(veiculoSalvo);
		} else {
			br.com.transportes.apitransportes.entity.Material encontrado = encontrarMaterialPorId(id);
			BeanUtils.copyProperties(upsertMaterial, encontrado);

			br.com.transportes.apitransportes.entity.Material materialEditado = materiaisRepository.save(encontrado);
			return materiaisMapper.toMaterialDto(materialEditado);
		}
	}

	public List<Material> listarTodos() {
		List<br.com.transportes.apitransportes.entity.Material> encontrados = materiaisRepository.findAll();
		return encontrados.stream().map(materiaisMapper::toMaterialDto).toList();
	}

	public void excluirMaterialPorId(String id) {
		encontrarMaterialPorId(id);
		materiaisRepository.deleteById(Long.valueOf(id));
	}

	public Material trazerMaterialPorId(String id) {
		return materiaisMapper.toMaterialDto(encontrarMaterialPorId(id));
	}

	public br.com.transportes.apitransportes.entity.Material encontrarMaterialPorId(String id)
			throws NumberFormatException {
		Long idLong = Long.parseLong(id);
		return materiaisRepository.findById(idLong)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format("Material com o id: %d não foi encontrado", idLong)));
	}
}
