package br.com.fiap.nucleo.dto;

public record ResultadoDTO(
        Long perguntaId,
        boolean acertou,
        String respostaCorreta,
        String explicacao,
        String referencia) {}
