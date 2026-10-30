package br.com.fiap.nucleo.service;

import br.com.fiap.nucleo.dto.*;
import br.com.fiap.nucleo.entity.Pergunta;
import br.com.fiap.nucleo.exception.RecursoNaoEncontradoException;
import br.com.fiap.nucleo.repository.PerguntaRepository;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PerguntaService {
    private static final Set<String> CAMPOS_ORDENACAO =
            Set.of("id", "enunciado", "resposta", "explicacao", "referencia");
    private final PerguntaRepository repository;
    private final Random sorteador = new Random();

    public PerguntaService(PerguntaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Pergunta cadastrar(PerguntaRequest dto) {
        Pergunta pergunta = new Pergunta();
        preencher(pergunta, dto);
        return repository.save(pergunta);
    }

    public Page<Pergunta> listar(String texto, Pageable pageable) {
        if (pageable.getOffset() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Página solicitada excede o limite permitido");
        }
        for (Sort.Order ordem : pageable.getSort()) {
            if (!CAMPOS_ORDENACAO.contains(ordem.getProperty())) {
                throw new IllegalArgumentException("Campo de ordenação inválido");
            }
        }
        if (texto == null || texto.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByEnunciadoContainingIgnoreCase(texto.trim(), pageable);
    }

    public Pergunta buscarPorId(Long id) {
        return repository
                .findById(id)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException("Pergunta nao encontrada: " + id));
    }

    @Transactional
    public Pergunta atualizar(Long id, PerguntaRequest dto) {
        Pergunta pergunta = buscarPorId(id);
        preencher(pergunta, dto);
        return repository.save(pergunta);
    }

    @Transactional
    public void excluir(Long id) {
        Pergunta p = buscarPorId(id);
        repository.delete(p);
    }

    public CartaoDTO sortear() {
        List<Pergunta> perguntas = repository.findAll();
        if (perguntas.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há perguntas cadastradas");
        }
        // Escolhe uma posicao da lista, pois os IDs podem ter lacunas apos exclusoes.
        int indice = sorteador.nextInt(perguntas.size());
        Pergunta pergunta = perguntas.get(indice);
        return new CartaoDTO(pergunta.getId(), pergunta.getEnunciado());
    }

    public ResultadoDTO responder(Long id, RespostaRequest dto) {
        Pergunta p = buscarPorId(id);
        return new ResultadoDTO(
                p.getId(),
                p.getResposta().equals(dto.resposta()),
                p.getResposta(),
                p.getExplicacao(),
                p.getReferencia());
    }

    private void preencher(Pergunta p, PerguntaRequest dto) {
        p.setEnunciado(dto.enunciado().trim());
        p.setResposta(dto.resposta());
        p.setExplicacao(dto.explicacao().trim());
        p.setReferencia(
                dto.referencia() == null || dto.referencia().isBlank()
                        ? null
                        : dto.referencia().trim());
    }
}
