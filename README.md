# Desafio Cálculo de Digito Único
> API desenvolvida para o desafio proposto pelo Banco Inter.

API desenvolvida utilizando Java, Maven, Spring Boot, Spring JPA e Banco de dados em mémoria H2.


## Como executar

Na raiz do projeto executar:

```sh
mvnw spring-boot:run
```

## Como testar

Para executar os testes:

```sh
mvnw test
```

Os testes da collection do postman podem ser executados por linha de comando utilizando o newman ou pelo runner  da ferramenta postman:
Pelo terminal deve executar
```sh
newman run postman_collection.json
```

## Documentação

Após executar o projeto, a documentação estará disponível no endereço:
[http://localhost:8080/desafio/v1/swagger-ui/](http://localhost:8080/desafio/v1/swagger-ui/)


### Observação
Projeto entregue com débito técnico em relação a feature de criptografia.
