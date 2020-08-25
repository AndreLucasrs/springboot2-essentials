package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class AnimeRepository {

    private static List<Anime> animes;

    static {
        animes = new ArrayList<>(
                Arrays.asList(
                        new Anime(1L, "Naruto"),
                        new Anime(2L, "Boku No Hero"),
                        new Anime(3L,"Mob Psycho 100")
                )
        );
    }

    public List<Anime> listAll() {
        return animes;
    }

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(4, 10000));
        animes.add(anime);
        return anime;
    }
}
