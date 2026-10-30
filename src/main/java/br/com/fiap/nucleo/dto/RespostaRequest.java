package br.com.fiap.nucleo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RespostaRequest(
        @NotBlank
                @Pattern(regexp = "[VF]", message = "Use V para verdade ou F para mito")
                @Schema(
                        description = "V para verdade; F para mito",
                        allowableValues = {"V", "F"},
                        example = "V")
                String resposta) {}
