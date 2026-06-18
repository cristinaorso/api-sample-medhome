package br.edu.atitus.apisample.services;

import br.edu.atitus.apisample.entities.PointEntity;
import br.edu.atitus.apisample.entities.User;
import br.edu.atitus.apisample.repositories.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service //Bean do tipo service
public class PointService {

    private final PointRepository repository;

    public PointService(PointRepository repository) { //injeção de dependência no construtor
        this.repository = repository;
    }

    @Transactional
    public PointEntity save(PointEntity point) throws Exception {
        if (point == null)
            throw new Exception("Objeto nulo");

        if (point.getDescription() == null || point.getDescription().isEmpty())
            throw new Exception("Descrição inválida");

        if (point.getLatitude() < -90 || point.getLatitude() > 90)
            throw new Exception("Latitude inválida");

        if (point.getLongitude() < -180 || point.getLongitude() > 180)
            throw new Exception("Longitude inválida");

        //É como uma memória da requisição atual.
        // Quando o token JWT chega, o filtro de segurança (no AuthTokenFilter) lê o token,
        // identifica o usuário e coloca esse usuário no SecurityContextHolder.
        // A partir daí, qualquer ponto do código pode perguntar "quem está logado agora?" — e é exatamente isso que essa linha faz.
        // O getPrincipal() retorna um Object, por isso o cast (User) — estamos dizendo ao Java: "pode tratar isso como User".
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        point.setUser(userAuth); //define esse usuário como dono do ponto

        return repository.save(point);
    }

    @Transactional
    public void deleteById(UUID id) throws Exception {
        var pointInBD = repository.findById(id).orElseThrow(() -> new Exception("Ponto não localizado"));
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!pointInBD.getUser().getId().equals(userAuth.getId()))
            throw new Exception("Você não tem permissão para essa ação");

        repository.deleteById(id);
    }

    public List<PointEntity> findAll() {
        return repository.findAll();
    }
}
