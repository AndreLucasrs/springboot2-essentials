package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> animeResponseEntity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 1);
        log.info("Response Entity {} ", animeResponseEntity);
        log.info("Response Data {} ", animeResponseEntity.getBody());

        Anime anime = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 1);
        log.info("Anime {} ", anime);

        Anime[] animeArray = new RestTemplate().getForObject("http://localhost:8080/animes/list", Anime[].class );
        log.info("Anime Array{} ", Arrays.toString(animeArray));

        ResponseEntity<List<Anime>> animeList = new RestTemplate().exchange("http://localhost:8080/animes/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        });
        log.info("Anime List{} ", animeList.getBody());
    }
}
