package com.jrpolesi.exercises.exercise5;

public class Entity {
    public String id;
    public String name;
    public String description;

    private Entity(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Entity() {
    }

    public static Entity of(String name) {
        return new Entity(null, name, null);
    }
}
