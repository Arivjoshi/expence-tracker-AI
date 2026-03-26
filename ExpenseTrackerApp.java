package main;

import models.Category;
import models.Expense;
import services.AnalyticsService;
import services.ExpenseService;
import services.UserService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class ExpenseTrackerApp {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserService();
    private static ExpenseService expenseService = new ExpenseService();
    private static String currentUser = null;

    public static void main(String[] args) {
        System.out.println("💰 Smart Expense Tracker with AI Insights 🚀");
        System.out.println("=======================================");

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n👤 [1] Login | [2] Register | [3] Exit");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> login();
            case 2 -> register();
            case 3 -> {
                System.out.println("👋 Thank you for using Smart Expense Tracker!");
                System.exit(0);
            }
            default -> System.out.println("❌ Invalid option!");
        }
    }

    private static void login() {
        System.out.print("👤 Username: ");
        String username = scanner.nextLine();
        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();

        if (userService.login(username, password).isPresent()) {
            currentUser = username;
            System.out.println("✅ Welcome back, " + username + "!");
        } else {
            System.out.println("❌ Invalid credentials!");
        }
    }

    private static void register() {
        System.out.print("👤 New Username: ");
        String username = scanner.nextLine();
        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();
        System.out.print("💰 Monthly Budget (₹): ");
        double budget = scanner.nextDouble();
        scanner.nextLine();

        if (userService.register(username, password, budget)) {
            currentUser = username;
            System.out.println("✅ Registration successful! Welcome, " + username + "!");
        } else {
            System.out.println("❌ Username already exists!");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n💰 Expense Tracker Dashboard");
        System.out.println("=============================");
        System.out.println("[1] ➕ Add Expense");
        System.out.println("[2] 📋 View Expenses");
        System.out.println("[3] 📊 Monthly Report");
        System.out.println("[4] 🤖 AI Insights");
        System.out.println("[5] 🗑️ Delete Expense");
        System.out.println("[6] ✏️ Edit Expense");
        System.out.println("[7] 🚪 Logout");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addExpense();
            case 2 -> viewExpenses();
            case 3 -> showMonthlyReport();
            case 4 -> showAIInsights();
            case 5 -> deleteExpense();
            case 6 -> editExpense();
            case 7 -> {
                currentUser = null;
                System.out.println("👋 Logged out successfully!");
            }
            default -> System.out.println("❌ Invalid option!");
        }
    }

    private static void addExpense() {
        System.out.print("💰 Amount (₹): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("📂 Choose Category:");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println("[" + (i + 1) + "] " + categories[i]);
        }
        System.out.print("Enter choice: ");
        int catChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        Category category = categories[catChoice];
        System.out.print("📝 Note (optional): ");
        String note = scanner.nextLine();

        expenseService.addExpense(currentUser, amount, category, note);
        System.out.println("✅ Expense added successfully!");
    }

    private static void viewExpenses() {
        List<Expense> userExpenses = expenseService.getExpensesByUser(currentUser);
        if (userExpenses.isEmpty()) {
            System.out.println("📭 No expenses found!");
            return;
        }

        System.out.println("\n📋 Your Expenses:");
        System.out.println("ID\t\tAmount\tCategory\tDate\t\tNote");
        System.out.println("------------------------------------------------");
        for (Expense expense : userExpenses) {
            System.out.println(expense.getId() + "\t₹" + expense.getAmount() + "\t" +
                    expense.getCategory() + "\t" + expense.getFormattedDate() + "\t" +
                    (expense.getNote().isEmpty() ? "-" : expense.getNote()));
        }
    }

    private static void showMonthlyReport() {
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        List<Expense> monthlyExpenses = expenseService.getExpensesByUserAndMonth(currentUser, now.getYear(), now.getMonthValue());

        System.out.println("\n📊 Monthly Report - " + currentMonth);
        System.out.println("Total Expenses: ₹" + String.format("%.2f", monthlyExpenses.stream().mapToDouble(Expense::getAmount).sum()));

        AnalyticsService analytics = new AnalyticsService(monthlyExpenses, currentUser);
        var breakdown = analytics.getCategoryBreakdown(now.getYear(), now.getMonthValue());
        
        System.out.println("\nCategory Breakdown:");
        for (var summary : breakdown) {
            System.out.println("• " + summary.category + ": ₹" + String.format("%.2f", summary.total) + " (" + summary.count + " transactions)");
        }
    }

    private static void showAIInsights() {
        LocalDate now = LocalDate.now();
        List<Expense> monthlyExpenses = expenseService.getExpensesByUserAndMonth(currentUser, now.getYear(), now.getMonthValue());
        
        var userOpt = userService.findByUsername(currentUser);
        if (userOpt.isEmpty()) return;

        AnalyticsService analytics = new AnalyticsService(monthlyExpenses, currentUser);
        String insights = analytics.generateInsights(now.getYear(), now.getMonthValue(), userOpt.get().getMonthlyBudget());
        
        System.out.println("\n🤖 AI Insights:");
        System.out.println("=================");
        System.out.println(insights
