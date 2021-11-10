package com.example.my_imdb;

public class Movie {

    private String id;
    private String title;
    private String year;
    private String image;
    private String crew;
    private String imDbRating;


    public Movie() {
    }

    public Movie(String id, String title, String year, String image, String crew, String imDbRating) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.image = image;
        this.crew = crew;
        this.imDbRating = imDbRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getImDbRating() {
        return imDbRating;
    }

    public void setImDbRating(String imDbRating) {
        this.imDbRating = imDbRating;
    }
}
