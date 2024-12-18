package com.example.androidlab.helpers;

public class Constants {
    public static final String BASE_URL = "http://172.20.10.3:8080"; // - LAN IP
    public static final String VALIDATE_USER = BASE_URL + "/validateClient";
    public static final String GET_ALL_USERS = BASE_URL + "/allUsers";
    public static final String GET_AVAILABLE_BOOKS = BASE_URL + "/allAvailablePublications";
    public static final String GET_MY_BOOKS = BASE_URL + "/getOwnedPublications";
    public static final String DELETE_PUBLICATION = BASE_URL + "/deletePublication";
    public static final String SET_PUBLICATION_OWNER_TO_NULL = BASE_URL + "/setOwnerToNull";
    public static final String DELETE_USER = BASE_URL + "/deleteUser";
}
