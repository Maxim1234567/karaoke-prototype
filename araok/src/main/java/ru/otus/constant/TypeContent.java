package ru.otus.constant;

public enum TypeContent {
    ALL("ALL"), NEW("NEW"), POPULAR("POPULAR"), RECOMMENDED("RECOMMENDED");

    private String type;

    TypeContent(String type) {
        this.type = type;
    }
}
