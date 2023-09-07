package ua.od.acros.matusi.presentation.authorization.fragment

import android.text.method.ScrollingMovementMethod
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavHostController
import ua.od.acros.matusi.databinding.FragmentAuthorizationBinding

@Composable
fun AuthorizationScreen(navHostController: NavHostController) {
    AndroidViewBinding(FragmentAuthorizationBinding::inflate) {

        tvTitle.movementMethod = ScrollingMovementMethod()

        btnExistingUser.setOnClickListener {
            navHostController.navigate("sign_in")
        }

        btnNewUser.setOnClickListener {
            navHostController.navigate("sign_up")
        }
    }
}