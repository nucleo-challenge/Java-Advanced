package br.com.fiap.nucleo.dto;

import br.com.fiap.nucleo.entity.Pergunta;

public record PerguntaResponse(
        Long id, String enunciado, String resposta, String explicacao, String referencia) {
    public static PerguntaResponse from(Pergunta pergunta) {
        return new PerguntaResponse(
                pergunta.getId(),
                pergunta.getEnunciado(),
                pergunta.getResposta(),
                pergunta.getExplicacao(),
                pergunta.getReferencia());
    }
}
