package com.foodapp;

import java.util.*;
import java.io.*;

public class App {
    private List<Restaurant> restaurants = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private User currentUser = null;
    private DataStore store = new DataStore();

    public void seedData() {
        Restaurant r1 = new Restaurant("Pizza Hub");
        r1.addFood(new FoodItem("Margherita Pizza", 199));
        r1.addFood(new FoodItem("Cheese Burst", 249));
        r1.addFood(new FoodItem("Farmhouse", 299));

        Restaurant r2 = new Restaurant("Burger Town");
        r2.addFood(new FoodItem("Veg Burger", 99));
        r2.addFood(new FoodItem("Paneer Burger", 149));
        r2.addFood(new FoodItem("Fries", 59));

        Restaurant r3 = new Restaurant("Punjabi Dhaba");
        r3.addFood(new FoodItem("Paneer Butter Masala", 179));
        r3.addFood(new FoodItem("Roti (per pcs)", 20));
        r3.addFood(new FoodItem("Jeera Rice", 89));

        restaurants.add(r1);
        restaurants.add(r2);
        restaurants.add(r3);
    }

    public void run() {
        System.out.println("🍴 Welcome to Food Delivery App (Core Java - Extended) 🍴");
        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. Signup\n2. Login\n3. Exit");
                System.out.print("Choose: ");
                int ch = readInt();
                switch (ch) {
                    case 1: signup(); break;
                    case 2: login(); break;
                    case 3: exitApp(); break;
                    default: System.out.println("Invalid choice."); 
                }
            } else {
                System.out.println("\nHello, " + currentUser.getUsername() + "!");
                System.out.println("1. View Restaurants\n2. Place Order\n3. View My Orders\n4. Logout\n5. Exit");
                System.out.print("Choose: ");
                int ch = readInt();
                switch (ch) {
                    case 1: showRestaurants(); break;
                    case 2: placeOrderMulti(); break;
                    case 3: viewMyOrders(); break;
                    case 4: logout(); break;
                    case 5: exitApp(); break;
                    default: System.out.println("Invalid choice.");
                }
            }
        }
    }

    private void signup() {
        System.out.print("Choose a username: ");
        String u = sc.next();
        System.out.print("Choose a password: ");
        String p = sc.next();
        boolean ok = store.addUser(new User(u, p));
        if (ok) System.out.println("Signup successful. You can login now.");
        else System.out.println("Username already exists. Try login or pick another username.");
    }

    private void login() {
        System.out.print("Username: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();
        User user = store.authenticate(u, p);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful. Welcome " + currentUser.getUsername() + "!");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    private void showRestaurants() {
        System.out.println("\nAvailable Restaurants:");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println((i + 1) + ". " + restaurants.get(i).getName());
        }
    }

    private void placeOrderMulti() {
        showRestaurants();
        System.out.print("Select restaurant number: ");
        int resIndex = readInt() - 1;
        if (resIndex < 0 || resIndex >= restaurants.size()) { System.out.println("Invalid restaurant."); return; }
        Restaurant selected = restaurants.get(resIndex);

        List<Order> sessionOrders = new ArrayList<>();
        boolean ordering = true;
        while (ordering) {
            selected.showMenu();
            System.out.print("Enter food number (or 0 to finish): ");
            int f = readInt();
            if (f == 0) break;
            int foodIndex = f - 1;
            if (foodIndex < 0 || foodIndex >= selected.getMenu().size()) { System.out.println("Invalid food choice!"); continue; }
            FoodItem food = selected.getMenu().get(foodIndex);
            System.out.print("Quantity: ");
            int qty = readInt();
            if (qty <= 0) { System.out.println("Quantity must be >=1"); continue; }
            Order order = new Order(currentUser.getUsername(), selected.getName(), food.getName(), food.getPrice(), qty);
            sessionOrders.add(order);
            System.out.println("Added to cart: " + food.getName() + " x" + qty);
            System.out.print("Add more from this restaurant? (y/n): ");
            String more = sc.next();
            if (!more.equalsIgnoreCase("y")) ordering = false;
        }

        if (sessionOrders.isEmpty()) {
            System.out.println("No items ordered.");
            return;
        }

        double total = 0;
        System.out.println("\n======= BILL =======");
        for (Order o : sessionOrders) {
            double itemTotal = o.getPrice() * o.getQuantity();
            System.out.printf("%s (%s) x%d = ₹%.2f\n", o.getItemName(), o.getRestaurantName(), o.getQuantity(), itemTotal);
            total += itemTotal;
        }
        System.out.printf("Total: ₹%.2f\n", total);
        System.out.print("Confirm order? (y/n): ");
        String confirm = sc.next();
        if (confirm.equalsIgnoreCase("y")) {
            for (Order o : sessionOrders) { store.saveOrder(o); }
            System.out.println("Order placed! Saved to order history.");
        } else {
            System.out.println("Order cancelled.");
        }
    }

    private void viewMyOrders() {
        List<Order> orders = store.getOrdersForUser(currentUser.getUsername());
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        System.out.println("\nYour Orders:");
        for (Order o : orders) {
            System.out.println(o);
        }
    }

    private void exitApp() {
        System.out.println("Thank you for using the app. Bye!");
        System.exit(0);
    }

    private int readInt() {
        try {
            return sc.nextInt();
        } catch (InputMismatchException ex) {
            sc.nextLine();
            return -1;
        }
    }
}
