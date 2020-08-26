package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
@DisplayName("Anime Repository Tests")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save create anime when successful")
    public void saveAnimeWhenSuccessful() {

        Anime anime = createAnime();
        Anime animeSaved = this.animeRepository.save(anime);
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("Update anime when successful")
    public void updateAnimeWhenSuccessful() {

        Anime anime = createAnime();
        Anime animeSaved = this.animeRepository.save(anime);

        animeSaved.setName("Pokemon");

        Anime animeUpdated = this.animeRepository.save(anime);

        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeUpdated.getName());
    }

    @Test
    @DisplayName("Delete anime when successful")
    public void deleteAnimeWhenSuccessful() {

        Anime anime = createAnime();
        Anime animeSaved = this.animeRepository.save(anime);

        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Find by name return animes when successful")
    public void findByNameReturnAnimesWhenSuccessful() {

        Anime anime = createAnime();
        Anime animeSaved = this.animeRepository.save(anime);

        List<Anime> animeList = this.animeRepository.findByNameIgnoreCase(anime.getName());

        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(animeSaved);
    }

    @Test
    @DisplayName("Find by name return empty list when no anime is not found")
    public void findByNameReturnEmptyListWhenAnimeNotFound() {

        String name = "fake anime";

        List<Anime> animeList = this.animeRepository.findByNameIgnoreCase(name);

        Assertions.assertThat(animeList).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    public void saveThrowConstraintViolationExceptionNameEmpty() {

        Anime anime = new Anime();

//        Assertions.assertThatThrownBy(
//                () -> animeRepository.save(anime)).isInstanceOf(ConstraintViolationException.class
//        );

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> animeRepository.save(anime))
                .withMessageContaining("O nome do anime é obrigatorio");
    }

    private Anime createAnime() {
        return Anime
                .builder()
                .name("Cowboy bebop")
                .build();
    }
}