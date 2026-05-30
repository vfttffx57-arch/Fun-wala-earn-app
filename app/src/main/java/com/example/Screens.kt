package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.AuthState
import com.example.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        delay(2000)
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(DeepBlue, PurpleMain))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.MonetizationOn, contentDescription = "Logo", modifier = Modifier.size(100.dp), tint = GoldAccent)
            Spacer(modifier = Modifier.height(16.dp))
            Text("FUN EARN", color = Color.White, fontSize = 44.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Lock, contentDescription = "Login", modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Welcome Back", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(32.dp))
        
        if (authState is AuthState.Error) {
            Text((authState as AuthState.Error).message, color = ErrorRed, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { authViewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("Don't have an account? Register", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var refCode by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) { popUpTo(Screen.Register.route) { inclusive = true } }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Icon(Icons.Default.PersonAdd, contentDescription = "Register", modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Create Account", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(32.dp))
        
        if (authState is AuthState.Error) {
            Text((authState as AuthState.Error).message, color = ErrorRed, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = refCode, onValueChange = { refCode = it }, label = { Text("Referral Code (Optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { authViewModel.register(username, email, password, refCode.takeIf { it.isNotBlank() }) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Already have an account? Login", color = MaterialTheme.colorScheme.secondary)
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun HomeScreen(userViewModel: UserViewModel, navController: NavController) {
    val user by userViewModel.user.collectAsState()
    val message by userViewModel.message.collectAsState()
    
    LaunchedEffect(message) {
        if (message != null) {
            delay(3000)
            userViewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(listOf(DeepBlue, PurpleMain)),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Welcome back,", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                        Text(user?.username ?: "Guest", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Balance", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                            Text("${user?.points ?: 0} pts", color = GoldAccent, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet", tint = GoldAccent, modifier = Modifier.size(40.dp))
                    }
                }
            }
        }

        if (message != null) {
            Snackbar(modifier = Modifier.padding(16.dp)) { Text(message!!) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Tracker
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Quick Actions", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ActionCard("Check In", Icons.Default.DoneOutline, SuccessGreen, Modifier.weight(1f)) {
                    userViewModel.checkIn()
                }
                Spacer(modifier = Modifier.width(16.dp))
                ActionCard("Refer", Icons.Default.Share, PurpleMain, Modifier.weight(1f)) {
                    navController.navigate(Screen.Referral.route)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ActionCard("Play Game", Icons.Default.VideogameAsset, DeepBlue, Modifier.weight(1f)) {
                    navController.navigate(Screen.TicTacToe.route)
                }
                Spacer(modifier = Modifier.width(16.dp))
                ActionCard("Offers", Icons.Default.LocalOffer, ErrorRed, Modifier.weight(1f)) {
                    navController.navigate(Screen.Offerwall.route)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Recent Announcements", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Campaign, contentDescription = "Announcement", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("New Offerwall Available!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Complete tasks to earn 100+ points.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.7f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp)) // padding for bottom nav
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCard(title: String, icon: ImageVector, iconColor: Color, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun LeaderboardScreen(userViewModel: UserViewModel) {
    val leaderboard by userViewModel.leaderboard.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(PurpleMain).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column {
                Text("Leaderboard", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("Top 50 earners today", color = Color.White.copy(alpha=0.8f), fontSize = 16.sp)
            }
        }
        
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            if (leaderboard.isEmpty()) {
                Text("No data available.", modifier = Modifier.padding(16.dp))
            } else {
                leaderboard.forEachIndexed { index, user ->
                    LeaderboardItem(index + 1, user.username, user.points)
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun LeaderboardItem(rank: Int, username: String, points: Int) {
    val medalColor = when(rank) {
        1 -> GoldAccent
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha=0.5f)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("#$rank", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = medalColor, modifier = Modifier.width(40.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha=0.1f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = "User", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(username, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Text("$points pts", fontWeight = FontWeight.Bold, color = SuccessGreen)
        }
    }
}

@Composable
fun EarnScreen(userViewModel: UserViewModel, navController: NavController) {
    val message by userViewModel.message.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current as? android.app.Activity
    
    LaunchedEffect(message) {
        if (message != null) {
            delay(3000)
            userViewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(DeepBlue).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Text("Earn Points", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        
        if (message != null) {
            Snackbar(modifier = Modifier.padding(16.dp)) { Text(message!!) }
        }
        
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Daily Tasks", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            TaskItemCard("Watch Video Ad", "Earn 3 points", Icons.Default.PlayCircle, PurpleMain) {
                if (context != null) {
                    com.unity3d.ads.UnityAds.load("Rewarded_Android", object : com.unity3d.ads.IUnityAdsLoadListener {
                        override fun onUnityAdsAdLoaded(placementId: String) {
                            com.unity3d.ads.UnityAds.show(context, "Rewarded_Android", com.unity3d.ads.UnityAdsShowOptions(), object : com.unity3d.ads.IUnityAdsShowListener {
                                override fun onUnityAdsShowFailure(placementId: String, error: com.unity3d.ads.UnityAds.UnityAdsShowError, message: String) {}
                                override fun onUnityAdsShowStart(placementId: String) {}
                                override fun onUnityAdsShowClick(placementId: String) {}
                                override fun onUnityAdsShowComplete(placementId: String, state: com.unity3d.ads.UnityAds.UnityAdsShowCompletionState) {
                                    if (state == com.unity3d.ads.UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                                        val randomPoints = (1..3).random()
                                        userViewModel.addPoints(randomPoints, "Watched Ad")
                                    }
                                }
                            })
                        }
                        override fun onUnityAdsFailedToLoad(placementId: String, error: com.unity3d.ads.UnityAds.UnityAdsLoadError, message: String) {}
                    })
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            TaskItemCard("Daily Check-in", "Earn 5 points", Icons.Default.EventAvailable, SuccessGreen) {
                userViewModel.checkIn()
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Extra Rewards", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            TaskItemCard("Complete Offers", "Offerwall (+1000 pts)", Icons.Default.LocalOffer, ErrorRed) {
                navController.navigate(Screen.Offerwall.route)
            }
            Spacer(modifier = Modifier.height(12.dp))
            TaskItemCard("Tic Tac Toe", "Play & Win (+5 pts)", Icons.Default.VideogameAsset, DeepBlue) {
                navController.navigate(Screen.TicTacToe.route)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemCard(title: String, subtitle: String, icon: ImageVector, iconColor: Color, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(iconColor.copy(alpha=0.1f)), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = title, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.7f), fontSize = 14.sp)
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = MaterialTheme.colorScheme.onSurface.copy(alpha=0.5f))
        }
    }
}

@Composable
fun WalletScreen(userViewModel: UserViewModel) {
    val user by userViewModel.user.collectAsState()
    val transactions by userViewModel.transactions.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(DeepBlue).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column {
                Text("My Wallet", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Available Balance", color = Color.White.copy(alpha=0.8f), fontSize = 16.sp)
                Text("${user?.points ?: 0}", color = GoldAccent, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
        
        Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {}, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp)) {
                    Text("Withdraw")
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(onClick = { userViewModel.fetchTransactions() }, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp)) {
                    Text("History")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Recent Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (transactions.isEmpty()) {
                Text("No recent transactions finding...")
            } else {
                transactions.forEach { trans ->
                    val dateStr = java.text.SimpleDateFormat("MMM dd, yyyy hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(trans.timestamp))
                    val color = if (trans.amount >= 0) SuccessGreen else ErrorRed
                    val amountStr = if (trans.amount >= 0) "+${trans.amount} pts" else "${trans.amount} pts"
                    TransactionItem(trans.title, amountStr, color, dateStr)
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun TransactionItem(title: String, amount: String, amountColor: Color, date: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text(amount, fontWeight = FontWeight.Bold, color = amountColor)
        }
        Text(date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f))
        Divider(modifier = Modifier.padding(top = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable
fun ProfileScreen(userViewModel: UserViewModel, authViewModel: AuthViewModel, navController: NavController) {
    val user by userViewModel.user.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(PurpleMain, DeepBlue))).padding(top = 48.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White.copy(alpha=0.2f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(60.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(user?.username ?: "Guest", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(user?.email ?: "", color = Color.White.copy(alpha=0.8f))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ref Code: ${user?.referralCode ?: ""}", color = GoldAccent, fontSize = 14.sp)
            }
        }
        
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            
            ProfileSettingItem(Icons.Default.ManageAccounts, "Account Settings") {}
            ProfileSettingItem(Icons.Default.Security, "Security") {}
            ProfileSettingItem(Icons.Default.Notifications, "Notifications") {}
            ProfileSettingItem(Icons.Default.SupportAgent, "Support & FAQ") {
                navController.navigate(Screen.Support.route)
            }
            
            if (user?.isAdmin == true) {
                ProfileSettingItem(Icons.Default.AdminPanelSettings, "Admin Panel") {
                    navController.navigate(Screen.AdminDashboard.route)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Log Out", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileSettingItem(icon: ImageVector, title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = MaterialTheme.colorScheme.onSurface.copy(alpha=0.5f))
    }
}

@Composable
fun ReferralScreen(userViewModel: UserViewModel, navController: NavController) {
    val user by userViewModel.user.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(PurpleMain).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Column {
                Text("Refer & Earn", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Invite friends and earn points!", color = Color.White.copy(alpha=0.8f))
            }
        }
        
        Column(modifier = Modifier.padding(24.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Referral Code", color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(user?.referralCode ?: "LOADING...", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { /* Implement copy to clipboard */ }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
                        Text("Copy Code")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("How it works", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            Text("1. Share your code with friends.\n2. They register using your code.\n3. You get 4 points when they play 5 matches.", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SupportScreen(userViewModel: UserViewModel, navController: NavController) {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(DeepBlue).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Text("Help & Support", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Create Ticket", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().height(150.dp), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* Implementation of sending ticket */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Send Ticket", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Contact Us: support@funearn.com", color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.7f))
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun OfferwallScreen(userViewModel: UserViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(ErrorRed).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Text("Offerwall", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        
        Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
            TaskItemCard("Download & Play App X", "Reach level 10 (+500 pts)", Icons.Default.GetApp, ErrorRed) {}
            Spacer(modifier = Modifier.height(12.dp))
            TaskItemCard("Complete Survey", "Quick 5 min (+100 pts)", Icons.Default.Assignment, ErrorRed) {}
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun TicTacToeScreen(userViewModel: UserViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.VideogameAsset, contentDescription = "Game", modifier = Modifier.size(100.dp), tint = DeepBlue)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Tic Tac Toe Multiplayer", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Compete with others and earn points!", color = MaterialTheme.colorScheme.onBackground.copy(alpha=0.7f))
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /* Matchmaking */ },
            modifier = Modifier.fillMaxWidth(0.8f).height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Find Match", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AdminDashboardScreen(userViewModel: UserViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.Black).padding(top = 48.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Text("Admin Dashboard", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
        
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Admin Controls", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            TaskItemCard("User Management", "View and edit users", Icons.Default.People, DeepBlue) {}
            Spacer(modifier = Modifier.height(12.dp))
            TaskItemCard("Offerwall Management", "Create/Edit Offers", Icons.Default.LocalOffer, ErrorRed) {}
            Spacer(modifier = Modifier.height(12.dp))
            TaskItemCard("Announcement Management", "Post updates", Icons.Default.Campaign, PurpleMain) {}
            Spacer(modifier = Modifier.height(12.dp))
            TaskItemCard("Ticket Management", "View support tickets", Icons.Default.SupportAgent, SuccessGreen) {}
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

