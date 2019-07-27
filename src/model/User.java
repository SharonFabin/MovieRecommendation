package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private int id;
    private double correlation;
    private Map<Integer,Double> movieRatings;
    private List<Integer> movies;

    public User(int id) {
        this.id = id;
        this.correlation = 0;
        this.movieRatings = new HashMap<>();
        this.movieRatings.clear();
        this.movies = new ArrayList<>();
    }

    public void putMovieRating(int movieId, double rating){
        this.movieRatings.put(movieId,rating);
    }

    public void putMovies(int moviesId){
        this.movies.add(moviesId);
    }

    public List<Integer> getMovies(){
        return this.movies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }

    public Map<Integer, Double> getMovieRatings() {
        return movieRatings;
    }

    public void setMovieRatings(Map<Integer, Double> movieRatings) {
        this.movieRatings = movieRatings;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", correlation=" + correlation +
                ", movieRatings=" + movieRatings +
                '}';
    }
}
