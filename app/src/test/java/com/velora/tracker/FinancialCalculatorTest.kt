package com.velora.tracker

import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.usecase.FinancialCalculator
import com.velora.tracker.ui.components.MoneyFormatter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class FinancialCalculatorTest {

    @Test
    fun testIncomeExpenseAndNetCalculations() {
        val transactions = listOf(
            Transaction(
                id = 1,
                amountMinorUnits = 5000000L, // 50,000 INR
                type = TransactionType.INCOME,
                title = "Salary",
                categoryId = 1,
                dateTime = LocalDateTime.now()
            ),
            Transaction(
                id = 2,
                amountMinorUnits = 1000000L, // 10,000 INR
                type = TransactionType.INCOME,
                title = "Freelance",
                categoryId = 2,
                dateTime = LocalDateTime.now()
            ),
            Transaction(
                id = 3,
                amountMinorUnits = 1500000L, // 15,000 INR
                type = TransactionType.EXPENSE,
                title = "Rent",
                categoryId = 3,
                dateTime = LocalDateTime.now()
            ),
            Transaction(
                id = 4,
                amountMinorUnits = 50000L, // 500 INR
                type = TransactionType.EXPENSE,
                title = "Groceries",
                categoryId = 4,
                dateTime = LocalDateTime.now()
            )
        )

        val totalIncome = FinancialCalculator.totalIncome(transactions)
        val totalExpenses = FinancialCalculator.totalExpenses(transactions)
        val net = FinancialCalculator.netCashFlow(totalIncome, totalExpenses)

        assertEquals(6000000L, totalIncome) // 60,000 INR
        assertEquals(1550000L, totalExpenses) // 15,500 INR
        assertEquals(4450000L, net) // 44,500 INR
    }

    @Test
    fun testCurrentBalanceIsCumulative() {
        val openingBalance = 2000000L // 20,000 INR
        val totalIncome = 6000000L // 60,000 INR
        val totalExpenses = 1550000L // 15,500 INR

        val balance = FinancialCalculator.currentBalance(openingBalance, totalIncome, totalExpenses)
        assertEquals(6450000L, balance) // 20k + 60k - 15.5k = 64,500 INR (6450000 paise)
    }

    @Test
    fun testCategoryBreakdownPercentages() {
        val categories = listOf(
            Category(id = 1, name = "Food", iconName = "restaurant", colorHex = "#E57373", type = TransactionType.EXPENSE),
            Category(id = 2, name = "Rent", iconName = "home", colorHex = "#A1887F", type = TransactionType.EXPENSE)
        )

        val transactions = listOf(
            Transaction(id = 1, amountMinorUnits = 250000L, type = TransactionType.EXPENSE, title = "Groceries", categoryId = 1, dateTime = LocalDateTime.now()),
            Transaction(id = 2, amountMinorUnits = 750000L, type = TransactionType.EXPENSE, title = "Apartment", categoryId = 2, dateTime = LocalDateTime.now())
        )

        val breakdown = FinancialCalculator.categoryBreakdown(transactions, categories)
        assertEquals(2, breakdown.size)
        // First is Rent (75%)
        assertEquals(2L, breakdown[0].categoryId)
        assertEquals(75f, breakdown[0].percentage, 0.01f)
        // Second is Food (25%)
        assertEquals(1L, breakdown[1].categoryId)
        assertEquals(25f, breakdown[1].percentage, 0.01f)
    }

    @Test
    fun testMoneyFormatter() {
        // ₹500.00
        val formatted = MoneyFormatter.format(50000L, "₹")
        assertEquals("₹500", formatted)

        // ₹500.50
        val formattedDecimal = MoneyFormatter.format(50050L, "₹")
        assertEquals("₹500.50", formattedDecimal)

        // Expense with minus
        val formattedExpense = MoneyFormatter.format(150000L, "₹", showSign = true, isExpense = true)
        assertEquals("− ₹1,500", formattedExpense)

        // Income with plus
        val formattedIncome = MoneyFormatter.format(5000000L, "₹", showSign = true, isExpense = false)
        assertEquals("+ ₹50,000", formattedIncome)

        // Compact format
        assertEquals("₹50K", MoneyFormatter.formatCompact(5000000L, "₹"))
        assertEquals("₹2L", MoneyFormatter.formatCompact(20000000L, "₹"))
        assertEquals("₹1Cr", MoneyFormatter.formatCompact(1000000000L, "₹"))
    }
}
