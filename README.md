# Núcleo — Mitos e Verdades Nucleares

API educativa sobre tecnologia nuclear, desenvolvida para a Sprint 1 de Java Advanced da FIAP / AMAZUL, turma **2TDSPR**. O jogo apresenta afirmações de mito ou verdade e fornece uma explicação com referência após a resposta.

## Equipe

| Integrante | RM |
| --- | --- |
| Pedro Gonçalves Rodrigues | 567651 |
| Davi Isac Colin Pereira | 567265 |
| Hugo Souza de Jesus | 568542 |
| Matheus Peres Moraes de Jesus | 567300 |
| Cassio Ribeiro | 567295 |

## Tecnologias e organização

Java 25, Spring Boot 4.1.1, Maven, Spring Web, Spring Data JPA, Spring Validation, H2 e Swagger/OpenAPI.

A aplicação segue o fluxo `controller → service → repository`. A entidade `Pergunta` representa os dados persistidos. Os records `PerguntaRequest` e `RespostaRequest` recebem os dados enviados pelo cliente; `PerguntaResponse` representa a resposta do CRUD. O tratamento de erros fica em `GlobalExceptionHandler`.

## Como executar

Configure `JAVA_HOME` com um **JDK 25**. Na pasta do projeto, execute:

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
sh mvnw spring-boot:run
```

O Maven Wrapper baixa as dependências na primeira execução. Com a aplicação iniciada, abra o [Swagger](http://localhost:8080/swagger-ui). O [índice da API](http://localhost:8080/) e o [OpenAPI JSON](http://localhost:8080/v3/api-docs) também ficam disponíveis.

O banco **H2 em memória** é criado automaticamente pelo JPA e começa vazio. Os registros existem enquanto a aplicação está em execução e são descartados ao encerrá-la. Não é necessário instalar ou configurar um servidor de banco.

## Testar pelo Swagger

1. Abra `POST /perguntas` e selecione **Try it out**.
2. Envie o exemplo abaixo e selecione **Execute**.
3. Use `GET /perguntas` para listar ou pesquisar.
4. Use `GET /jogo/perguntas/aleatoria` para sortear um cartão e o endpoint de respostas para conferir V/F.

```json
{
  "enunciado": "A tecnologia nuclear possui aplicações na medicina.",
  "resposta": "V",
  "explicacao": "O material de apoio apresenta medicina nuclear e produção de radiofármacos.",
  "referencia": "AMAZUL - Kick-off FIAP - 13OUT26.pdf, p. 34"
}
```

Há dez exemplos em [dados/perguntas-exemplo.json](dados/perguntas-exemplo.json) e uma [coleção Postman](postman/Nucleo.postman_collection.json) para testar as operações. Os exemplos são enviados manualmente pela API.

## Endpoints

| Método | Caminho | Função |
| --- | --- | --- |
| GET | / | Índice da API |
| POST | /perguntas | Cadastrar; retorna 201, Location e PerguntaResponse |
| GET | /perguntas | Listar e pesquisar com paginação |
| GET | /perguntas/{id} | Consultar por ID |
| PUT | /perguntas/{id} | Atualizar |
| DELETE | /perguntas/{id} | Excluir; retorna 204 |
| GET | /jogo/perguntas/aleatoria | Sortear ID e enunciado, sem gabarito |
| POST | /jogo/perguntas/{id}/respostas | Conferir a resposta e retornar explicação/referência |

Exemplo de busca: `/perguntas?texto=medicina&page=0&size=10&sort=id,asc`. O Query Method `findByEnunciadoContainingIgnoreCase` pesquisa trechos sem diferenciar maiúsculas e minúsculas. A listagem retorna `Page<PerguntaResponse>`, com página padrão de dez registros e limite de cem.

Para responder ao jogo, envie `{"resposta":"V"}` ou `{"resposta":"F"}`. O sorteio usa uma posição da lista de perguntas, por isso funciona mesmo após exclusões.

## Validação e escopo

| Campo de Pergunta | Regra |
| --- | --- |
| id | Gerado pelo JPA |
| enunciado | Obrigatório, até 500 caracteres |
| resposta | Obrigatória: V para verdade ou F para mito |
| explicacao | Obrigatória, até 1000 caracteres |
| referencia | Opcional, até 300 caracteres |

A API retorna 400 para dados inválidos e 404 para pergunta inexistente ou sorteio com acervo vazio. O CRUD inclui o gabarito; o sorteio retorna apenas ID e enunciado. Autenticação, ranking e histórico não fazem parte desta Sprint 1.

A [pesquisa de apoio](docs/pesquisa.md) apresenta fontes e contexto para o conteúdo educativo.

## Gerar o JAR

Windows / PowerShell: `.\mvnw.cmd clean package`. Linux / macOS: `sh mvnw clean package`.

O arquivo gerado fica em `target/nucleo-0.1.0-SNAPSHOT.jar` e pode ser executado com `java -jar target/nucleo-0.1.0-SNAPSHOT.jar`.
