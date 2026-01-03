package com.example.moviehub.common.error;

import java.util.Set;

// DTO filmu zwracany przez API bez encji
public record MovieDto(
        Long id,            // identyfikator filmu
        String title,       // tytuł
        String description, // opis / fabuła
        String director,    // reżyser
        String producer,    // producent
        Integer releaseYear,// rok wydania
        Integer rtScore,    // ocena
        Set<String> genres  // gatunki jako lista nazw
) {}
