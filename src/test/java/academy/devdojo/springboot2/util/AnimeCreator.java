package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime
                .builder()
                .name("Cowboy bebop")
                .build();
    }
    public static Anime createAnimeValidAnime() {
        return Anime
                .builder()
                .name("Cowboy bebop")
                .id(1L)
                .build();
    }
    public static Anime createAnimeToBeUpdated() {
        return Anime
                .builder()
                .name("Pokemon")
                .id(1L)
                .build();
    }
}
