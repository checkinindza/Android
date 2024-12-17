package com.example.androidlab.model;

import com.example.androidlab.model.enums.Language;
import com.example.androidlab.model.enums.PublicationStatus;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public abstract class Publication {

    protected int id;
    protected String title;
    protected Language language;
    protected LocalDate publicationDate;
    protected LocalDate requestDate;
    protected int pageCount;
    protected Client owner;
    protected Client borrowerClientList;
    protected String publisher;
    protected String author;
    protected String summary;
    protected PublicationStatus publicationStatus;
    protected String dtype;

    public Publication(String title, Language language, LocalDate publicationDate, int pageCount, String publisher, String author, String summary, PublicationStatus publicationStatus) {
        this.title = title;
        this.language = language;
        this.publicationDate = publicationDate;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.author = author;
        this.summary = summary;
        this.publicationStatus = publicationStatus;
    }

    public Publication(int pageCount, String title, LocalDate publicationDate, String publisher, String author, String summary, PublicationStatus publicationStatus, Language language, Client owner) {
        this.pageCount = pageCount;
        this.title = title;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.author = author;
        this.summary = summary;
        this.publicationStatus = publicationStatus;
        this.language = language;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
