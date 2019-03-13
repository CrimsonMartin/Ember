import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Movie {

    private String title = null;
    private String description = null;
    private Integer year = null;
    private Image poster = null;
    private List <String> actors = new ArrayList<>();
    private List <String> directors = new ArrayList<>();
    private List <String> genres = new ArrayList<>();
    private List <String> platforms =new ArrayList<>();

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

}
