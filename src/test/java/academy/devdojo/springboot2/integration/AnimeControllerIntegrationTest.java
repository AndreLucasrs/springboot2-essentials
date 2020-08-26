package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.controller.AnimeController;
import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private AnimeRepository animeRepository;

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
    }

    @Test
    @DisplayName("listAll returns a pageable of animes when successful")
    public void listAllReturnListAnimesPageObjectWhenSuccessful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = restTemplate.exchange(
                "/animes", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Anime>>() {})
                .getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns an anime when successful")
    public void findByIdReturnAnimeWhenSuccessful() {
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = restTemplate.getForObject("/animes/1", Anime.class);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of animes when successful")
    public void findByNameReturnListAnimesWhenSuccessful() {
        Anime animeExpected = AnimeCreator.createAnimeValidAnime();

        List<Anime> listAnimes = restTemplate.exchange(
                "/animes/find?name='Cowboy bebop'", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {})
                .getBody();
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

        Anime animeSaved = restTemplate.exchange(
                "/animes", HttpMethod.POST,
                createJsonHttpEntity(animeToBeSaved), Anime.class)
                .getBody();
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete removes the anime when successful")
    public void deleteAnimeWhenSuccessful() {

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/animes/1", HttpMethod.DELETE,
                null, Void.class);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update an anime when successful")
    public void updateAnimeWhenSuccessful() {

        Anime animeToBeUpdated = AnimeCreator.createAnimeValidAnime();

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/animes/1", HttpMethod.PUT,
                createJsonHttpEntity(animeToBeUpdated), Void.class);
        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    private HttpEntity<Anime> createJsonHttpEntity(Anime anime) {
        return new HttpEntity<>(anime, createJsonHeader());
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setBearerAuth("token");
        return httpHeaders;

    }
}