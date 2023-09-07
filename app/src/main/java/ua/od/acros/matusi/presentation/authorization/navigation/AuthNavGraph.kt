package ua.od.acros.matusi.presentation.authorization.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.context.loadKoinModules
import ua.od.acros.matusi.di.authorizationModule
import ua.od.acros.matusi.presentation.authorization.fragment.AuthorizationScreen
import ua.od.acros.matusi.presentation.authorization.fragment.SignInScreen
import ua.od.acros.matusi.presentation.authorization.vm.AuthorizationViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    showSnackBar: (String) -> Unit = {}
) {
    loadKoinModules(authorizationModule)
    navigation(
        route = "auth",
        startDestination = "authorize"
    ) {
        composable("authorize") {
            val authViewModel: AuthorizationViewModel = it.sharedViewModel(navController)
            Log.d("Auth", "$authViewModel  $it")
            AuthorizationScreen(navController)
        }
        composable("sign_in") {
            val authViewModel: AuthorizationViewModel = it.sharedViewModel(navController)
            Log.d("Auth", "$authViewModel  $it")
            SignInScreen(
                navController,
                authViewModel.state.collectAsState(),
                authViewModel.action.collectAsState(),
                {},
                authViewModel::applyIntent
            ) { showSnackBar(it) }
        }
        composable("sign_up") {
            AuthorizationScreen(navController)
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel()
    val parentEntry = remember(this) { navController.getBackStackEntry(navGraphRoute) }
    return koinNavViewModel(viewModelStoreOwner = parentEntry)
}