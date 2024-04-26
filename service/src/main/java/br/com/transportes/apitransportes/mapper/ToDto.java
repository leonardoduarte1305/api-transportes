package br.com.transportes.apitransportes.mapper;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(source = "id", target = "id")
public @interface ToDto {
}
