import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Movie {

    private String title = null;
    private String description = null;
    private Integer year = null;
    private Image poster = null;
    private Integer imdbrating = null;
    private List <String> actors = new ArrayList<>();
    private List <String> directors = new ArrayList<>();
    private List <String> genres = new ArrayList<>();
    private List <String> platforms =new ArrayList<>();

    public Movie(String title){
        title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getYear() {
        return year;
    }

    public Image getPoster() {
        return poster;
    }

    public Integer getImdbrating() {
        return imdbrating;
    }

    public void setImdbrating(Integer imdbrating) {
        this.imdbrating = imdbrating;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getGenres() {
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