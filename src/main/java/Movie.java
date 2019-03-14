import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Movie {

    private String title;
    private String description;
    private Integer year;
    private Image poster;
    private Integer imdbrating;
    private List <String> actors = new ArrayList<>();
    private List <String> directors = new ArrayList<>();
    private List <String> genres = new ArrayList<>();
    private List <String> platforms =new ArrayList<>();

    Movie(String title){
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    Integer getYear() {
        return year;
    }

    Image getPoster() {
        return poster;
    }

    Integer getIMDBrating() {
        return imdbrating;
    }

    void setIMDBrating(Integer imdbrating) {
        this.imdbrating = imdbrating;
    }

    List<String> getActors() {
        return actors;
    }

    List<String> getDirectors() {
        return directors;
    }

    List<String> getGenres() {
        return genres;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getTitle(), movie.getTitle()) &&
                Objects.equals(getYear(), movie.getYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear());
    }
}