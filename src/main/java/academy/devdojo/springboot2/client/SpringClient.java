package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SpringClient {
    
    private static final String URL = "http://localhost:8080/animes";
    
    public static void main(String[] args) {

        System.out.println(new BCryptPasswordEncoder().encode("test"));

        //GET
//        testGetWithRestTemplate();
        ResponseEntity<PageableResponse<Anime>> animeListPage = new RestTemplate()
                .exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PageableResponse<Anime>>() {}
                        );
        log.info("Anime List{} ", animeListPage.getBody());

        //POST
//        testPostWithRestTemplate();
        Anime animeCowboy = Anime
                .builder()
                .name("Cowboy Bebop")
                .build();

        ResponseEntity<Anime> animeResponse = new RestTemplate()
                .exchange(
                        URL,
                        HttpMethod.POST,
                        new HttpEntity<>(animeCowboy, createJsonHeader()),
                        Anime.class
                );
        log.info("Anime SaveResponse{} ", animeResponse.getBody());

        //PUT
        Anime animeUdate = animeResponse.getBody();
        animeUdate.setName("Digimon");
        ResponseEntity<Void> updateResponse = new RestTemplate()
                .exchange(
                        URL + "/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(animeUdate, createJsonHeader()),
                        Void.class,
                        animeResponse.getBody().getId()
                );
        log.info("Anime UpdateResponse{} ", updateResponse.getStatusCode());

        //DELETE
        ResponseEntity<Void> deleteResponse = new RestTemplate()
                .exchange(
                        URL + "/{id}",
                        HttpMethod.DELETE,
                        null,
                        Void.class,
                        animeResponse.getBody().getId()
                );

        log.info("Anime DeleteResponse{} ", deleteResponse.getStatusCode());
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setBearerAuth("token");
        return httpHeaders;
    }

    private static void testPostWithRestTemplate() {
        Anime anime = Anime
                .builder()
                .name("Attack on Titan")
                .build();

        Anime animeSave = new RestTemplate().postForObject(URL, anime, Anime.class);

        log.info("Anime Save{} ", animeSave);
    }

    private static void testGetWithRestTemplate() {
        ResponseEntity<Anime> animeResponseEntity = new RestTemplate().getForEntity(URL+"/{id}", Anime.class, 1);
        log.info("Response Entity {} ", animeResponseEntity);
        log.info("Response Data {} ", animeResponseEntity.getBody());

        Anime anime = new RestTemplate().getForObject(URL+"/{id}", Anime.class, 1);
        log.info("Anime {} ", anime);

        Anime[] animeArray = new RestTemplate().getForObject(URL+"/list", Anime[].class);
        log.info("Anime Array{} ", Arrays.toString(animeArray));

        ResponseEntity<List<Anime>> animeList = new RestTemplate().exchange(URL+"/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        });
        log.info("Anime List{} ", animeList.getBody());
    }
}
