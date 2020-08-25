package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService service;

    @GetMapping
    public ResponseEntity<List<Anime>> listaAll() {

        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {

        Anime animeEncontrado = service.findById(id);
        return ResponseEntity.ok(animeEncontrado);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {

        List<Anime> animes = service.findByName(name);
        return ResponseEntity.ok(animes);
    }


    @PostMapping
    public ResponseEntity<Anime> save(@Valid @RequestBody Anime anime) {
        Anime animeSalvo = service.save(anime);
        return ResponseEntity.ok(animeSalvo);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Anime anime) {
        service.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
