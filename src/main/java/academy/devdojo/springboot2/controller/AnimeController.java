package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final DateUtil dateUtil;
    private final AnimeRepository repository;

    @GetMapping
    public ResponseEntity<List<Anime>> listaAll() {
        log.info("Formatando data {}", dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));

        return ResponseEntity.ok(repository.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {

        Anime animeEncontrado = repository.listAll()
                .stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Anime não encontrado"));

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
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
