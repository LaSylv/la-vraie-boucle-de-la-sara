package com.github.lasylv.lavraieboucledelasara.runs;

public record RunDto(
        String date,
        String duration,
        String link,
        long numberOfLaps,
        String lastName,
        String firstName,
        String profilePictureUrl
) {}
