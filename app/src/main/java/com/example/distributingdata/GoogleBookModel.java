package com.example.distributingdata;

public class GoogleBookModel {
    private String title;
    private String authors;
    private String description;
    private String publisher;
    private String publishedDate;

    public GoogleBookModel (String title, String authors, String description, String publisher, String publishedDate) {
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }

    public GoogleBookModel () {

    }

    // region Getters

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    // end region

    // region Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPubishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    // end region

    @Override
    public String toString()
    {
        return "Book: " + title + " " + authors + " " + description + " " + publisher + " " + publishedDate;
    }
}
