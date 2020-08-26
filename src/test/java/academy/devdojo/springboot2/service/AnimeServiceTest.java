package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.controller.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.ResourceNotFoundException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.Utils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private Utils utils;

    @BeforeEach
    public void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(Arrays.asList(AnimeCreator.createAnimeValidAnime()));
        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepository.findById(anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeRepository.findByNameIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Arrays.asList(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));

        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeUpdated()))
                .thenReturn(AnimeCreator.createAnimeToBeUpdated());

        BDDMockito.when(utils.findAnimeOrThrowNotFound(anyLong(), ArgumentMatchers.any(AnimeRepository.class)))
                .thenReturn(AnimeCreator.createAnimeToBeUpdated());
    }

    @Test
    @DisplayName("listAll returns a pageable of animes when successful")
    public void listAllReturnListAnimesPageObjectWhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(0, 5));
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    public void findByIdReturnAnimeWhenSuccessful() {
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeService.findById(1L);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    public void findByNameReturnListAnimesWhenSuccessful() {
        Anime animeExpected = AnimeCreator.createAnimeValidAnime();

        List<Anime> listAnimes = animeService.findByName("Cowboy bebop");
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

        Anime animeSaved = animeService.save(animeToBeSaved);
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void deleteAnimeWhenSuccessful() {

        Assertions.assertThatCode(() -> animeService.delete(1L))
        .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("update an anime when successful")
    public void updateAnimeWhenSuccessful() {

        Anime animeToBeUpdated = AnimeCreator.createAnimeToBeUpdated();

        Assertions.assertThatCode(() -> animeService.update(animeToBeUpdated))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throw ResourceNotFoundException when the anime does not exists")
    public void deleteThrowResourceNotFoundExceptionWhenAnimeNotExists() {

        BDDMockito.when(utils.findAnimeOrThrowNotFound(anyLong(), ArgumentMatchers.any(AnimeRepository.class)))
                .thenThrow(new ResourceNotFoundException("Anime not found"));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> animeService.delete(19L))
                .withMessageContaining("Anime not found");
    }

}