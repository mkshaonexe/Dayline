package com.day.line.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object TaskIconUtils {

    val AvailableIcons = listOf(
        "Work" to Icons.Filled.Work,
        "Book" to Icons.Filled.Book,
        "FitnessCenter" to Icons.Filled.FitnessCenter,
        "MusicNote" to Icons.Filled.MusicNote,
        "Bedtime" to Icons.Filled.Bedtime,
        "LocalCafe" to Icons.Filled.LocalCafe,
        "Create" to Icons.Filled.Create,
        "Code" to Icons.Filled.Code,
        "PlayArrow" to Icons.Filled.PlayArrow,
        "ShoppingCart" to Icons.Filled.ShoppingCart,
        "Home" to Icons.Filled.Home,
        "Star" to Icons.Filled.Star,
        "Notifications" to Icons.Filled.Notifications,
        "List" to Icons.Filled.List,
        "Face" to Icons.Filled.Face,
        "Call" to Icons.Filled.Call,
        "Build" to Icons.Filled.Build,
        "Flight" to Icons.Filled.Flight,
        "Event" to Icons.Filled.Event,
        "Favorite" to Icons.Filled.Favorite
    )

    fun getIconByName(name: String): ImageVector {
        return AvailableIcons.find { it.first == name }?.second ?: Icons.Filled.Star
    }

    fun predictIconName(title: String): String {
        val lowerTitle = title.lowercase()
        return when {
            lowerTitle.contains("code") || lowerTitle.contains("dev") || lowerTitle.contains("program") || lowerTitle.contains("fix") -> "Code"
            lowerTitle.contains("study") || lowerTitle.contains("read") || lowerTitle.contains("book") || lowerTitle.contains("learn") || lowerTitle.contains("homework") -> "Book"
            lowerTitle.contains("gym") || lowerTitle.contains("workout") || lowerTitle.contains("run") || lowerTitle.contains("walk") || lowerTitle.contains("exercise") || lowerTitle.contains("fit") -> "FitnessCenter"
            lowerTitle.contains("music") || lowerTitle.contains("listen") || lowerTitle.contains("song") || lowerTitle.contains("piano") || lowerTitle.contains("guitar") -> "MusicNote"
            lowerTitle.contains("sleep") || lowerTitle.contains("nap") || lowerTitle.contains("bed") || lowerTitle.contains("rest") -> "Bedtime"
            lowerTitle.contains("eat") || lowerTitle.contains("lunch") || lowerTitle.contains("dinner") || lowerTitle.contains("breakfast") || lowerTitle.contains("food") || lowerTitle.contains("coffee") -> "LocalCafe"
            lowerTitle.contains("work") || lowerTitle.contains("job") || lowerTitle.contains("meeting") || lowerTitle.contains("meet") -> "Work"
            lowerTitle.contains("write") || lowerTitle.contains("journal") || lowerTitle.contains("diary") || lowerTitle.contains("note") -> "Create"
            lowerTitle.contains("watch") || lowerTitle.contains("movie") || lowerTitle.contains("film") || lowerTitle.contains("tv") || lowerTitle.contains("video") -> "PlayArrow"
            lowerTitle.contains("shop") || lowerTitle.contains("buy") || lowerTitle.contains("store") || lowerTitle.contains("grocer") -> "ShoppingCart"
            lowerTitle.contains("clean") || lowerTitle.contains("house") || lowerTitle.contains("chore") -> "Home"
            lowerTitle.contains("call") || lowerTitle.contains("phone") -> "Call"
            lowerTitle.contains("flight") || lowerTitle.contains("fly") || lowerTitle.contains("travel") || lowerTitle.contains("trip") -> "Flight"
            else -> "Star" // Default
        }
    }
}
