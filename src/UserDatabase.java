import java.io.*;
import java.util.ArrayList;

public class UserDatabase {
    private ArrayList<User> users;
    private String filename;

    public UserDatabase(String filename) {
        this.filename = filename;
        users = new ArrayList<>();
        loadFromFile();
    }

    public void addUser(User user) {
        users.add(user);
        saveToFile();
    }

    public void removeUser(User user) {
        users.remove(user);
        saveToFile();
    }

    public ArrayList<User> getUsers() { return users; }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User u : users) {
                if (u instanceof Student) {
                    bw.write("STUDENT|" + u.getId() + "|" + u.getName() + "|" + u.getEmail() + "|" + u.getHashPassword());
                } else if (u instanceof Instructor) {
                    bw.write("INSTRUCTOR|" + u.getId() + "|" + u.getName() + "|" + u.getEmail() + "|" + u.getHashPassword());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals("STUDENT")) {
                    users.add(new Student(parts[1], parts[2], parts[3], parts[4]));
                } else if (parts[0].equals("INSTRUCTOR")) {
                    users.add(new Instructor(parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

}

