package br.edu.atitus.apisample.dtos;

//record: tipo especial de classe focado em carregar dados

//Em vez de escrever construtor, getters, equals, hashCode
// — o Java gera tudo automaticamente. Como os DTOs só precisam receber e repassar dados, o record é perfeito para isso.

public record SigninDTO(String email, String password) {
}
