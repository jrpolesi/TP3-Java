package com.jrpolesi.exercises.exercise12;

public record Item(Integer id, String type, String isbn13, double price, int numberinstock) {
    @Override
    public String toString() {
        return String.format(
                "-> ID: %d, Type: %s, ISBN: %s, Preço: %.2f, Estoque: %d",
                id(),
                type(),
                isbn13(),
                price(),
                numberinstock()
        );
    }
}
