package uk.ac.mmu.enterpriseprogrammingrest.model.data;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookVO {

  private Integer id;
  private String title;
  private String author;
  private String date;
  private String genres;
  private String characters;
  private String synopsis;
  private String coverUrl;


  public BookVO() {}

  public BookVO(
      Integer id,
      String title,
      String author,
      String date,
      String genres,
      String characters,
      String synopsis,
      String coverUrl
  ) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.date = date;
    this.genres = genres;
    this.characters = characters;
    this.synopsis = synopsis;
    this.coverUrl = coverUrl;
  }

  public void validate() {

    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Title is required");
    }

    if (title.length() > 255) {
      throw new IllegalArgumentException("Title is too long");
    }

    if (author == null || author.trim().isEmpty()) {
      throw new IllegalArgumentException("Author is required");
    }

    if (author.length() > 255) {
      throw new IllegalArgumentException("Author is too long");
    }

    if (date == null || date.trim().isEmpty()) {
      throw new IllegalArgumentException("Date is required (format: YYYY-MM-DD)");
    }

    try {
      LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
    }

    if (genres.length() > 255) {
      throw new IllegalArgumentException("Genres is too long");
    }

  }


  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getAuthor() { return author; }
  public void setAuthor(String author) { this.author = author; }

  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }

  public String getGenres() { return genres; }
  public void setGenres(String genres) { this.genres = genres; }

  public String getCharacters() { return characters; }
  public void setCharacters(String characters) { this.characters = characters; }

  public String getSynopsis() { return synopsis; }
  public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

  public String getCoverUrl() { return coverUrl; }
  public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}