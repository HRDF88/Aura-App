package com.aura.viewmodel.login

/**
 *
 * Class that allows to define a finite set of subclasses.
 */
sealed class NavigationEvent {
    object NavigateToHome : NavigationEvent()
}
