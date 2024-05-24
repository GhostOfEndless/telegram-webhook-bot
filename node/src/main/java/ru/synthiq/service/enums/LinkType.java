package ru.synthiq.service.enums;

public enum LinkType {

    GET_DOC("file/get-id"),
    GET_PHOTO("file/get-photo");

    private final String link;

    LinkType(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}
