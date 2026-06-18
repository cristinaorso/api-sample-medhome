package br.edu.atitus.apisample.dtos;
//DTO = Data Tranfer Object
public record SignupDTO(String name, String email, String password) {
}

// DTOs são objetos simples que definem exatamente o que o cliente precisa enviar em cada requisição. Nada mais, nada menos.
//Por que usar DTOs em vez de receber a entidade direto?
//Imagine se o endpoint de cadastro recebesse um User diretamente.
// O User tem um campo type (Admin/Common) — o cliente poderia mandar "type": "Admin" no JSON e se autopromover.
// Com o DTO você controla o contrato:
//public record SignupDTO(String name, String email, String password) {}
//O cliente envia só name, email e password. O campo type não existe aqui
// — quem define o tipo do usuário é o servidor, não o cliente.