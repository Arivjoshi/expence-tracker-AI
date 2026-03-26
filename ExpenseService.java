package services;

import models.Category;
import models.Expense;
import utils.FileManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpenseService {
    private List<Expense> expenses;

    public ExpenseService() {
        this.expenses = FileManager.loadExpenses();
    }

    public void addExpense(String username, double amount, Category category, String note) {
        Expense expense = new Expense(username, amount, category, note);
        expenses.add(expense);
        FileManager.saveExpenses(expenses);
    }

    public List<Expense> getExpensesByUser(String username) {
        return expenses.stream()
                .filter(e -> e.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByUserAndMonth(String username, int year, int month) {
        return expenses.stream()
                .filter(e -> e.getUsername().equals(username))
                .filter(e -> e.getDate().getYear() == year)
                .filter(e -> e.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    public Optional<Expense> findExpenseById(String id) {
        return expenses.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public boolean deleteExpense(String id) {
        Optional<Expense> expense = findExpenseById(id);
        if (expense.isPresent()) {
            expenses.remove(expense.get());
            FileManager.saveExpenses(expenses);
            return true;
        }
        return false;
    }

    public void updateExpense(String id, double amount, Category category, String note) {
        Optional<Expense> expenseOpt = findExpenseById(id);
        if (expenseOpt.isPresent()) {
            Expense expense = expenseOpt.get();
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setNote(note);
            FileManager.saveExpenses(expenses);
        }
    }
}
