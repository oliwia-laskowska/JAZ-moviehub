package com.example.moviehub.integration.model;

import java.util.List;

public record TmdbCredits(
        Long id,
        List<CrewMember> crew
) {
    public record CrewMember(
            String name,
            String job,
            String department
    ) {}
}
