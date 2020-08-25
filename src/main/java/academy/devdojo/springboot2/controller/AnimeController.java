package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService repository;

    @GetMapping
    public ResponseEntity<List<Anime>> listaAll() {

        return ResponseEntity.ok(repository.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {

        Anime animeEncontrado = repository.findById(id);
        return ResponseEntity.ok(animeEncontrado);
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody Anime anime) {
        Anime animeSalvo = repository.save(anime);
        return ResponseEntity.ok(animeSalvo);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@RequestBody Anime anime) {
        repository.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
