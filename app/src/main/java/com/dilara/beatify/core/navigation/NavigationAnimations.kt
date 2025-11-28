package com.dilara.beatify.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationAnimations {
    const val ANIMATION_DURATION = 300

    /**
     * Slide in from left (for Home screen - initial screen)
     */
    fun slideInFromLeft(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(ANIMATION_DURATION)
        ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
    }

    /**
     * Slide out to right (for Home screen - initial screen)
     */
    fun slideOutToRight(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(ANIMATION_DURATION)
        ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
    }

    /**
     * Slide in from right (for screens to the right of Home)
     */
    fun slideInFromRight(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(ANIMATION_DURATION)
        ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
    }

    /**
     * Slide out to left (for screens to the right of Home)
     */
    fun slideOutToLeft(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(ANIMATION_DURATION)
        ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
    }

    /**
     * Default animations for bottom navigation screens (sliding from right)
     * Returns lambda functions for enter and exit transitions
     */
    fun bottomNavScreenTransitions(): Pair<
        (AnimatedContentTransitionScope<*>.() -> EnterTransition),
        (AnimatedContentTransitionScope<*>.() -> ExitTransition)
    > {
        return Pair(
            { slideInFromRight() },
            { slideOutToLeft() }
        )
    }

    /**
     * Default pop animations (reverse direction)
     * Returns lambda functions for pop enter and exit transitions
     */
    fun bottomNavScreenPopTransitions(): Pair<
        (AnimatedContentTransitionScope<*>.() -> EnterTransition),
        (AnimatedContentTransitionScope<*>.() -> ExitTransition)
    > {
        return Pair(
            { slideInFromLeft() },
            { slideOutToRight() }
        )
    }

    /**
     * Home screen transitions (sliding from left)
     * Returns lambda functions for enter and exit transitions
     */
    fun homeScreenTransitions(): Pair<
        (AnimatedContentTransitionScope<*>.() -> EnterTransition),
        (AnimatedContentTransitionScope<*>.() -> ExitTransition)
    > {
        return Pair(
            { slideInFromLeft() },
            { slideOutToRight() }
        )
    }

    /**
     * Home screen pop transitions (reverse direction)
     * Returns lambda functions for pop enter and exit transitions
     */
    fun homeScreenPopTransitions(): Pair<
        (AnimatedContentTransitionScope<*>.() -> EnterTransition),
        (AnimatedContentTransitionScope<*>.() -> ExitTransition)
    > {
        return Pair(
            { slideInFromRight() },
            { slideOutToLeft() }
        )
    }
}
