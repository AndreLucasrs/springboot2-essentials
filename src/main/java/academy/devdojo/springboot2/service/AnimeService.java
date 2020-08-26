package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final Utils utils;
    private final AnimeRepository repository;

    @Transactional(readOnly = true)
    public Page<Anime> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Anime> listAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Anime> findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    // se vc tiver usando uma exeception chechked save (Anime anime) throws Exceptions
    // o rollback para funcionar tem que adicionar (rollbackFor = Exception.class)
    @Transactional
    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(utils.findAnimeOrThrowNotFound(id, repository));
    }

    @Transactional(readOnly = true)
    public Anime findById(Long id) {
        return utils.findAnimeOrThrowNotFound(id, repository);
    }

    @Transactional
    public void update(Anime anime) {
        repository.save(anime);
    }

}
