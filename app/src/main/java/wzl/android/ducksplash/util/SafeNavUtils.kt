package wzl.android.ducksplash.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * NavController extension functions to navigate safely
 */
fun NavController.navigateSafe(
        navDirections: NavDirections,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
) {
    navigateSafe(navDirections.actionId, navDirections.arguments, navOptions, navigatorExtras)
}

fun NavController.navigateSafe(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(resId, args, navOptions, navExtras)
    }
} 