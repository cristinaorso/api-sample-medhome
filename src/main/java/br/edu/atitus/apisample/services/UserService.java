package br.edu.atitus.apisample.services;

import br.edu.atitus.apisample.entities.User;
import br.edu.atitus.apisample.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Esta classe será um Bean do Spring
// Ou seja, os objetos serão criados e gerenciados pelo Spring IOC
@Service
public class UserService implements UserDetailsService {
    // Objetos UserService precisam de um UserRepository para funcionar
    // UserService DEPENDE UserRepository
    // UserRepository é uma dependência do UserService
    private final UserRepository repository;

    private final PasswordEncoder encoder;

    // Método construtor com Injeção de dependência
    //O UserService não cria seus próprios objetos — ele recebe o repository e o encoder prontos pelo Spring.
    // Isso se chama Injeção de Dependência. O PasswordEncoder foi configurado no ConfigSecurity
    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User save(User newUser) throws Exception {
        if (newUser == null)
            throw new Exception("Objeto Nulo!");

        if (newUser.getName() == null || newUser.getName().isBlank())
            throw new Exception("Nome informado inválido!");
        newUser.setName(newUser.getName().trim());

        if (newUser.getEmail() == null || newUser.getEmail().isBlank())
            throw new Exception("E-mail informado inválido!");
        newUser.setEmail(newUser.getEmail().trim().toLowerCase());
        // TODO fazer validação do formato de e-mail

        if (repository.existsByEmail(newUser.getEmail()))
            throw new Exception("Já existe usuário cadastrado com este e-mail!");

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8)
            throw new Exception("Password informado inválido!");
        // TODO fazer validação de qualidade de senha

        //criptografar a senha em um hash BCrypt (a senha original nunca é salva no banco)
        newUser.setPassword(encoder.encode(newUser.getPassword()));

        if (newUser.getType() == null)
            throw new Exception("Tipo de usuário informado inválido!");

        // Solicita para camada Repository salvar o registro
        // E retorna o registro salvo
        return repository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com este e-mail não encontrado"));
    }
}
