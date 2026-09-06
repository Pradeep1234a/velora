package com.velora.tracker.navigation

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.velora.tracker.VeloraApplication
import com.velora.tracker.presentation.addtransaction.AddTransactionScreen
import com.velora.tracker.presentation.addtransaction.AddTransactionViewModel
import com.velora.tracker.presentation.analytics.AnalyticsScreen
import com.velora.tracker.presentation.analytics.AnalyticsViewModel
import com.velora.tracker.presentation.categories.CategoriesScreen
import com.velora.tracker.presentation.categories.CategoriesViewModel
import com.velora.tracker.presentation.dashboard.DashboardScreen
import com.velora.tracker.presentation.dashboard.DashboardViewModel
import com.velora.tracker.presentation.settings.SettingsScreen
import com.velora.tracker.presentation.settings.SettingsViewModel
import com.velora.tracker.presentation.splash.SplashScreen
import com.velora.tracker.presentation.transactions.TransactionDetailScreen
import com.velora.tracker.presentation.transactions.TransactionDetailViewModel
import com.velora.tracker.presentation.transactions.TransactionsScreen
import com.velora.tracker.presentation.transactions.TransactionsViewModel
import com.velora.tracker.ui.components.VeloraNavigationBar

@Composable
fun VeloraNavHost(application: Application) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val app = application as VeloraApplication

    val mainTabs = listOf(
        VeloraDestination.Dashboard.route,
        VeloraDestination.Transactions.route,
        VeloraDestination.Analytics.route,
        VeloraDestination.Categories.route
    )

    val showBottomBar = currentRoute in mainTabs
    val showFab = currentRoute == VeloraDestination.Dashboard.route || currentRoute == VeloraDestination.Transactions.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                VeloraNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { navController.navigate(VeloraDestination.AddTransaction.route) }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Transaction")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = VeloraDestination.Splash.route,
            modifier = Modifier.padding(if (currentRoute == VeloraDestination.Splash.route) androidx.compose.foundation.layout.PaddingValues(0.dp) else innerPadding)
        ) {
            composable(VeloraDestination.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(VeloraDestination.Dashboard.route) {
                            popUpTo(VeloraDestination.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(VeloraDestination.Dashboard.route) {
                val viewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModel.provideFactory(app)
                )
                DashboardScreen(
                    viewModel = viewModel,
                    onTransactionClick = { id ->
                        navController.navigate(VeloraDestination.TransactionDetail.createRoute(id))
                    },
                    onSeeAllTransactions = {
                        navController.navigate(VeloraDestination.Transactions.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onSettingsClick = {
                        navController.navigate(VeloraDestination.Settings.route)
                    }
                )
            }

            composable(VeloraDestination.Transactions.route) {
                val viewModel: TransactionsViewModel = viewModel(
                    factory = TransactionsViewModel.provideFactory(app)
                )
                TransactionsScreen(
                    viewModel = viewModel,
                    onTransactionClick = { id ->
                        navController.navigate(VeloraDestination.TransactionDetail.createRoute(id))
                    },
                    onAddTransaction = {
                        navController.navigate(VeloraDestination.AddTransaction.route)
                    }
                )
            }

            composable(VeloraDestination.Analytics.route) {
                val viewModel: AnalyticsViewModel = viewModel(
                    factory = AnalyticsViewModel.provideFactory(app)
                )
                AnalyticsScreen(
                    viewModel = viewModel,
                    onTransactionClick = { id ->
                        navController.navigate(VeloraDestination.TransactionDetail.createRoute(id))
                    }
                )
            }

            composable(VeloraDestination.Categories.route) {
                val viewModel: CategoriesViewModel = viewModel(
                    factory = CategoriesViewModel.provideFactory(app)
                )
                CategoriesScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(VeloraDestination.AddTransaction.route) {
                val viewModel: AddTransactionViewModel = viewModel(
                    factory = AddTransactionViewModel.provideFactory(app)
                )
                AddTransactionScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    transactionId = null
                )
            }

            composable(
                route = VeloraDestination.EditTransaction.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getLong("transactionId")
                val viewModel: AddTransactionViewModel = viewModel(
                    factory = AddTransactionViewModel.provideFactory(app)
                )
                AddTransactionScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    transactionId = transactionId
                )
            }

            composable(
                route = VeloraDestination.TransactionDetail.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
                val viewModel: TransactionDetailViewModel = viewModel(
                    factory = TransactionDetailViewModel.provideFactory(app)
                )
                TransactionDetailScreen(
                    viewModel = viewModel,
                    transactionId = transactionId,
                    onNavigateBack = { navController.popBackStack() },
                    onEditTransaction = { id ->
                        navController.navigate(VeloraDestination.EditTransaction.createRoute(id))
                    }
                )
            }

            composable(VeloraDestination.Settings.route) {
                val viewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModel.provideFactory(app)
                )
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCategories = {
                        navController.navigate(VeloraDestination.Categories.route)
                    }
                )
            }
        }
    }
}
