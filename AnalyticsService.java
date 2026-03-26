package services;

import models.Category;
import models.Expense;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    public static class CategorySummary {
        Category category;
        double total;
        int count;

        CategorySummary(Category category, double total, int count) {
            this.category = category;
            this.total = total;
            this.count = count;
        }
    }

    private List<Expense> expenses;
    private String username;

    public AnalyticsService(List<Expense> expenses, String username) {
        this.expenses = expenses;
        this.username = username;
    }

    public double getMonthlyTotal(int year, int month) {
        return expenses.stream()
                .filter(e -> e.getUsername().equals(username))
                .filter(e -> e.getDate().getYear() == year)
                .filter(e -> e.getDate().getMonthValue() == month)
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public List<CategorySummary> getCategoryBreakdown(int year, int month) {
        return expenses.stream()
                .filter(e -> e.getUsername().equals(username))
                .filter(e -> e.getDate().getYear() == year)
                .filter(e -> e.getDate().getMonthValue() == month)
                .collect(Collectors.groupingBy(
                    Expense::getCategory,
                    Collectors.summarizingDouble(Expense::getAmount)
                ))
                .entrySet().stream()
                .map(entry -> new CategorySummary(
                    entry.getKey(),
                    entry.getValue().getSum(),
                    (int) entry.getValue().getCount()
                ))
                .sorted((a, b) -> Double.compare(b.total, a.total))
                .collect(Collectors.toList());
    }

    public String generateInsights(int year, int month, double monthlyBudget) {
        List<CategorySummary> breakdown = getCategoryBreakdown(year, month);
        double totalSpending = getMonthlyTotal(year, month);
        StringBuilder insights = new StringBuilder();

        // Overspending alert
        if (totalSpending > monthlyBudget * 0.9) {
            insights.append("🚨 OVERSpending Alert! You've spent ")
                    .append(String.format("%.0f%%", (totalSpending / monthlyBudget) * 100))
                    .append(" of your budget (₹").append(totalSpending).append("/₹").append(monthlyBudget).append(")\n");
        }

        // Highest spending category
        if (!breakdown.isEmpty()) {
            CategorySummary topCategory = breakdown.get(0);
            double topCategoryPercent = (topCategory.total / totalSpending) * 100;
            if (topCategoryPercent > 40) {
                insights.append("🍔 You spent ").append(String.format("%.0f%%", topCategoryPercent))
                        .append(" on ").append(topCategory.category.getDisplayName())
                        .append(" this month (₹").append(topCategory.total).append(")\n");
            }
        }

        // Savings suggestion
        double remainingBudget = monthlyBudget - totalSpending;
        if (remainingBudget > 0) {
            insights.append("💰 Great job! You have ₹").append(String.format("%.2f", remainingBudget))
                    .append(" remaining this month!\n");
        }

        return insights.length() > 0 ? insights.toString() : "📊 Everything looks good! Keep tracking your expenses. 💪";
    }

    public CategorySummary getHighestExpenseCategory(int year, int month) {
        return getCategoryBreakdown(year, month).stream().findFirst().orElse(null);
    }
}
