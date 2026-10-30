package br.com.fiap.nucleo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PerguntaRequest(
        @NotBlank(message = "O enunciado é obrigatório") @Size(max = 500) String enunciado,
        @NotBlank(message = "A resposta é obrigatória")
                @Pattern(regexp = "[VF]", message = "Use V para verdade ou F para mito")
                @Schema(
                        description = "V para verdade; F para mito",
                        allowableValues = {"V", "F"},
                        example = "V")
                String resposta,
        @NotBlank(message = "A explicação é obrigatória") @Size(max = 1000) String explicacao,
        @Size(max = 300) String referencia) {}
