package com.foodapp;

public class Order {
    private String username;
    private String restaurantName;
    private String itemName;
    private double price;
    private int quantity;

    public Order(String username, String restaurantName, String itemName, double price, int quantity) {
        this.username = username;
        this.restaurantName = restaurantName;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getUsername() { return username; }
    public String getRestaurantName() { return restaurantName; }
    public String getItemName() { return itemName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s x%d | ₹%.2f", username, restaurantName, itemName, quantity, price * quantity);
    }

    public String toStorageString() {
        // username|restaurant|item|price|qty
        return username + "|" + restaurantName + "|" + itemName + "|" + price + "|" + quantity;
    }

    public static Order fromStorageString(String s) {
        String[] parts = s.split("\\|");
        if (parts.length != 5) return null;
        String user = parts[0];
        String rest = parts[1];
        String item = parts[2];
        double p = Double.parseDouble(parts[3]);
        int q = Integer.parseInt(parts[4]);
        return new Order(user, rest, item, p, q);
    }
}
