package com.example

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.UserViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Splash : Screen("splash", "Splash")
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    
    // Bottom Nav Screens
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Leaderboard : Screen("leaderboard", "Ranks", Icons.Default.Leaderboard)
    object Earn : Screen("earn", "Earn", Icons.Default.MonetizationOn)
    object Wallet : Screen("wallet", "Wallet", Icons.Default.AccountBalanceWallet)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    
    // Sub screens
    object Referral : Screen("referral", "Refer & Earn")
    object Support : Screen("support", "Support")
    object Offerwall : Screen("offerwall", "Offers")
    object TicTacToe : Screen("tictactoe", "Tic Tac Toe")
    object AdminDashboard : Screen("admin_dashboard", "Admin Dashboard", Icons.Default.AdminPanelSettings)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController, authViewModel) }
        composable(Screen.Login.route) { LoginScreen(navController, authViewModel) }
        composable(Screen.Register.route) { RegisterScreen(navController, authViewModel) }
        
        composable(Screen.Home.route) { MainLayout(navController) { HomeScreen(userViewModel, navController) } }
        composable(Screen.Leaderboard.route) { MainLayout(navController) { LeaderboardScreen(userViewModel) } }
        composable(Screen.Earn.route) { MainLayout(navController) { EarnScreen(userViewModel, navController) } }
        composable(Screen.Wallet.route) { MainLayout(navController) { WalletScreen(userViewModel) } }
        composable(Screen.Profile.route) { MainLayout(navController) { ProfileScreen(userViewModel, authViewModel, navController) } }
        
        composable(Screen.Referral.route) { ReferralScreen(userViewModel, navController) }
        composable(Screen.Support.route) { SupportScreen(userViewModel, navController) }
        composable(Screen.Offerwall.route) { OfferwallScreen(userViewModel, navController) }
        composable(Screen.TicTacToe.route) { TicTacToeScreen(userViewModel, navController) }
        composable(Screen.AdminDashboard.route) { AdminDashboardScreen(userViewModel, navController) }
    }
}


@Composable
fun MainLayout(navController: NavController, content: @Composable () -> Unit) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
         Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
             content()
         }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Leaderboard,
        Screen.Earn,
        Screen.Wallet,
        Screen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}
