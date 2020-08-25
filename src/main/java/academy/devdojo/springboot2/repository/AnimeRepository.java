package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class AnimeRepository {

    public List<Anime> listAll() {
        return Arrays.asList(
                new Anime(1L, "Naruto"),
                new Anime(2L, "Boku No Hero"),
                new Anime(3L,"Mob Psycho 100")
        );
    }
}
