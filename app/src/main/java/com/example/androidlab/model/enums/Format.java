package com.example.androidlab.model.enums;

public enum Format {
    HARDCOVER,
    PAPERBACK,
    EBOOK;

    @Override
    public String toString() {
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).replace("_", " ");
    }
}
