package ru.synthiq.service;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");

    private final String command;

    ServiceCommands(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public boolean equals(String command) {
        return this.toString().equals(command);
    }
}
