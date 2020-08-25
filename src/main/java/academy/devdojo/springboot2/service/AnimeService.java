package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final Utils utils;
    private final AnimeRepository repository;

    public List<Anime> listAll() {
        return repository.findAll();
    }

    public List<Anime> findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

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
