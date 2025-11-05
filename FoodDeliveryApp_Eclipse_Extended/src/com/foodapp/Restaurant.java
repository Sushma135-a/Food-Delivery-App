package com.foodapp;

import java.util.*;

public class Restaurant {
    private String name;
    private List<FoodItem> menu = new ArrayList<>();

    public Restaurant(String name) {
        this.name = name;
    }

    public void addFood(FoodItem item) {
        menu.add(item);
    }

    public void showMenu() {
        System.out.println("\nMenu of " + name + ":");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).getName() + " - ₹" + menu.get(i).getPrice());
        }
    }

    public String getName() {
        return name;
    }

    public List<FoodItem> getMenu() {
        return menu;
    }
}
