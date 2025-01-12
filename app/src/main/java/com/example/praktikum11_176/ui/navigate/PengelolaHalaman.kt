package com.example.praktikum11_176.ui.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.praktikum11_176.ui.view.InsertMhsView

@Composable
fun PengelolaHalaman(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier
    ) {
        composable(DestinasiHome.route) {
            HomeView(
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
            )
        }

        composable(DestinasiInsert.route) {
            InsertMhsView(
                onBack = {navController.popBackStack() },
                onNavigate = {
                    navController.navigate(DestinasiHome.route)
                })
        }
    }

}