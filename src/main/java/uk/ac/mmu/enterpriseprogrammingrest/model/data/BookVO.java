package uk.ac.mmu.enterpriseprogrammingrest.model.data;

public record BookVO(
    Integer id,
    String title,
    String author,
    String date,
    String genres,
    String characters,
    String synopsis,
    String coverUrl
) {}