package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Expense;
import models.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String USERS_FILE = "data/users.json";
    private static final String EXPENSES_FILE = "data/expenses.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        new File("data").mkdirs();
    }

    public static void saveUsers(List<User> users) {
        try {
            mapper.writeValue(new File(USERS_FILE), users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static List<User> loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static void saveExpenses(List<Expense> expenses) {
        try {
            mapper.writeValue(new File(EXPENSES_FILE), expenses);
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    public static List<Expense> loadExpenses() {
        try {
            File file = new File(EXPENSES_FILE);
            if (file.exists()) {
                return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Expense.class));
            }
        } catch (IOException e) {
            System.err.println("Error loading expenses: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
