package com.foodapp;

import java.io.*;
import java.util.*;

public class DataStore {
    private File userFile;
    private File orderFile;

    public DataStore() {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdir();
        userFile = new File(dataDir, "users.txt");
        orderFile = new File(dataDir, "orders.txt");
        try {
            userFile.createNewFile();
            orderFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean addUser(User u) {
        // check exists
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                User ex = User.fromStorageString(line);
                if (ex != null && ex.getUsername().equals(u.getUsername())) return false;
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (FileWriter fw = new FileWriter(userFile, true)) {
            fw.write(u.toStorageString() + System.lineSeparator());
            fw.flush();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public User authenticate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                User u = User.fromStorageString(line);
                if (u != null && u.getUsername().equals(username) && u.getPassword().equals(password)) return u;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    public synchronized void saveOrder(Order o) {
        try (FileWriter fw = new FileWriter(orderFile, true)) {
            fw.write(o.toStorageString() + System.lineSeparator());
            fw.flush();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Order> getOrdersForUser(String username) {
        List<Order> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                Order o = Order.fromStorageString(line);
                if (o != null && o.getUsername().equals(username)) out.add(o);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }
}
