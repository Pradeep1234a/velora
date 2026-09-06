package com.velora.tracker.navigation

sealed class VeloraDestination(val route: String) {
    data object Splash : VeloraDestination("splash")
    data object Dashboard : VeloraDestination("dashboard")
    data object Transactions : VeloraDestination("transactions")
    data object Analytics : VeloraDestination("analytics")
    data object Categories : VeloraDestination("categories")
    data object AddTransaction : VeloraDestination("add_transaction")
    data object EditTransaction : VeloraDestination("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Long) = "edit_transaction/$transactionId"
    }
    data object TransactionDetail : VeloraDestination("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: Long) = "transaction_detail/$transactionId"
    }
    data object Settings : VeloraDestination("settings")
}
