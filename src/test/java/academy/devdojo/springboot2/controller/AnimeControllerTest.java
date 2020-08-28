package academy.devdojo.springboot2.controller;

import static org.junit.jupiter.api.Assertions.*;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;


@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeService;

    @BeforeEach
    public void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(Arrays.asList(AnimeCreator.createAnimeValidAnime()));
        BDDMockito.when(animeService.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Arrays.asList(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeService.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.doNothing().when(animeService).delete(ArgumentMatchers.anyLong());

        BDDMockito.doNothing().when(animeService).update(AnimeCreator.createAnimeToBeUpdated());
    }

    @Test
    @DisplayName("listAll returns a pageable of animes when successful")
    public void listAllReturnListAnimesPageObjectWhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = animeController.listaAll(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    public void findByIdReturnAnimeWhenSuccessful() {
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeController.findById(1L, null).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    public void findByNameReturnListAnimesWhenSuccessful() {
        Anime animeExpected = AnimeCreator.createAnimeValidAnime();

        List<Anime> listAnimes = animeController.findByName("Cowboy bebop").getBody();
        Assertions.assertThat(listAnimes).isNotNull();
        Assertions.assertThat(listAnimes).isNotEmpty();
        Assertions.assertThat(listAnimes).contains(animeExpected);
        Assertions.assertThat(listAnimes.get(0).getName()).isEqualTo(animeExpected.getName());
    }

    @Test
    @DisplayName("save creates an anime when successful")
    public void saveAnimeWhenSuccessful() {
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        Anime animeSaved = animeController.save(animeToBeSaved).getBody();
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void deleteAnimeWhenSuccessful() {

        ResponseEntity<Void> responseEntity = animeController.delete(1L);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update an anime when successful")
    public void updateAnimeWhenSuccessful() {

        Anime animeToBeUpdated = AnimeCreator.createAnimeToBeUpdated();

        ResponseEntity<Void> responseEntity = animeController.update(animeToBeUpdated);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }
}