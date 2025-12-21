package com.day.line.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object TaskIconUtils {

    val AvailableIcons = listOf(
        // Work & Study
        "Work" to Icons.Filled.Work,
        "Business" to Icons.Filled.Business,
        "School" to Icons.Filled.School,
        "Book" to Icons.Filled.Book,
        "Library" to Icons.Filled.LocalLibrary,
        "Computer" to Icons.Filled.Computer,
        "Code" to Icons.Filled.Code,
        "Calculate" to Icons.Filled.Calculate,
        "Design" to Icons.Filled.Brush,
        
        // Health & Fitness
        "Fitness" to Icons.Filled.FitnessCenter,
        "Run" to Icons.Filled.DirectionsRun,
        "Walk" to Icons.Filled.DirectionsWalk,
        "Bike" to Icons.Filled.DirectionsBike,
        "Swim" to Icons.Filled.Pool,
        "Yoga" to Icons.Filled.SelfImprovement,
        "Hospital" to Icons.Filled.LocalHospital,
        "Sleep" to Icons.Filled.Bedtime,
        
        // Daily Living
        "Home" to Icons.Filled.Home,
        "Shopping" to Icons.Filled.ShoppingCart,
        "Groceries" to Icons.Filled.LocalGroceryStore,
        "Cleaning" to Icons.Filled.CleaningServices,
        "Laundry" to Icons.Filled.LocalLaundryService,
        "Cook" to Icons.Filled.Restaurant,
        "Eat" to Icons.Filled.RestaurantMenu,
        "Coffee" to Icons.Filled.LocalCafe,
        
        // Social & Leisure
        "Person" to Icons.Filled.Person,
        "People" to Icons.Filled.Group,
        "Call" to Icons.Filled.Call,
        "Message" to Icons.Filled.Message,
        "Email" to Icons.Filled.Email,
        "Party" to Icons.Filled.Celebration,
        "Music" to Icons.Filled.MusicNote,
        "Movie" to Icons.Filled.Movie,
        "Game" to Icons.Filled.SportsEsports,
        "Travel" to Icons.Filled.Flight,
        "Car" to Icons.Filled.DirectionsCar,
        "Bus" to Icons.Filled.DirectionsBus,
        
        // Misc
        "Edit" to Icons.Filled.Edit,
        "List" to Icons.Filled.List,
        "Star" to Icons.Filled.Star,
        "Time" to Icons.Filled.AccessTime,
        "Alarm" to Icons.Filled.Alarm,
        "Bank" to Icons.Filled.AccountBalance,
        "Money" to Icons.Filled.AttachMoney,
        "Idea" to Icons.Filled.Lightbulb,
        "Gift" to Icons.Filled.CardGiftcard,
        "Pet" to Icons.Filled.Pets
    )

    fun getIconByName(name: String): ImageVector {
        return AvailableIcons.find { it.first == name }?.second ?: Icons.Filled.Edit
    }

    /**
     * Predicts the most likely icon based on the task title using a comprehensive keyword map.
     */
    fun predictIconName(title: String): String {
        val lowerTitle = title.lowercase()
        
        // Comprehensive Keyword Mapping
        // Iterate through categories to find the first match
        
        // 1. Coding & Tech
        if (lowerTitle.containsAny("code", "program", "dev", "fix", "bug", "java", "kotlin", "python", "script", "api", "backend", "frontend", "server", "debug", "git", "commit", "push", "pull")) return "Code"
        if (lowerTitle.containsAny("design", "ui", "ux", "figma", "sketch", "draw", "paint", "art")) return "Design"
        if (lowerTitle.containsAny("computer", "laptop", "pc", "mac", "windows", "install", "update", "software")) return "Computer"
        if (lowerTitle.containsAny("calculate", "math", "finance", "budget", "tax", "excel", "sheet")) return "Calculate"
        
        // 2. Work & Business
        if (lowerTitle.containsAny("meeting", "meet", "zoom", "call", "client", "boss", "interview")) return "Business"
        if (lowerTitle.containsAny("work", "job", "career", "office", "project", "task", "presentation", "slide")) return "Work"
        if (lowerTitle.containsAny("email", "mail", "inbox", "send", "reply")) return "Email"
        
        // 3. Education & Learning
        if (lowerTitle.containsAny("study", "learn", "read", "review", "exam", "test", "quiz", "homework", "assignment", "paper", "essay")) return "School"
        if (lowerTitle.containsAny("book", "novel", "chapter", "page", "reading")) return "Book"
        if (lowerTitle.containsAny("library", "research")) return "Library"
        
        // 4. Fitness & Health
        if (lowerTitle.containsAny("gym", "workout", "lift", "exercise", "train", "muscle", "weight", "crossfit")) return "Fitness"
        if (lowerTitle.containsAny("run", "jog", "marathon", "sprint")) return "Run"
        if (lowerTitle.containsAny("walk", "hike", "stroll", "step")) return "Walk"
        if (lowerTitle.containsAny("swim", "pool", "lap")) return "Swim"
        if (lowerTitle.containsAny("bike", "cycle", "ride")) return "Bike"
        if (lowerTitle.containsAny("yoga", "meditate", "stretch", "mindful")) return "Yoga"
        if (lowerTitle.containsAny("doctor", "nurse", "dentist", "medical", "pill", "medicine", "prescription", "checkup")) return "Hospital"
        if (lowerTitle.containsAny("sleep", "nap", "rest", "bed", "dream", "wake")) return "Sleep"
        
        // 5. Daily Living & Chores
        if (lowerTitle.containsAny("shop", "buy", "purchase", "store", "online", "amazon")) return "Shopping"
        if (lowerTitle.containsAny("grocery", "food", "supermarket", "mart", "fruit", "veg", "milk", "egg", "bread")) return "Groceries"
        if (lowerTitle.containsAny("clean", "tidy", "dust", "vacuum", "mop")) return "Cleaning"
        if (lowerTitle.containsAny("laundry", "wash", "clothes", "dry", "fold", "iron")) return "Laundry"
        if (lowerTitle.containsAny("cook", "bake", "prepare", "meal", "kitchen", "chef")) return "Cook"
        if (lowerTitle.containsAny("coffee", "tea", "cafe", "espresso", "latte", "drink")) return "Coffee"
        if (lowerTitle.containsAny("eat", "dinner", "lunch", "breakfast", "brunch", "snack", "restaurant", "dine")) return "Eat"
        if (lowerTitle.containsAny("home", "house", "apartment", "rent")) return "Home"
        if (lowerTitle.containsAny("dog", "cat", "pet", "feed", "vet", "walk dog")) return "Pet"
        
        // 6. Social & Leisure
        if (lowerTitle.containsAny("friend", "buddy", "pal", "meetup", "hangout")) return "People"
        if (lowerTitle.containsAny("party", "birthday", "celebrate", "wedding", "event")) return "Party"
        if (lowerTitle.containsAny("movie", "film", "cinema", "show", "tv", "watch", "series", "netflix")) return "Movie"
        if (lowerTitle.containsAny("music", "song", "listen", "concert", "band", "playlist")) return "Music"
        if (lowerTitle.containsAny("game", "play", "xbox", "ps5", "steam", "switch")) return "Game"
        if (lowerTitle.containsAny("travel", "flight", "fly", "plane", "airport", "vacation", "trip", "holiday")) return "Travel"
        if (lowerTitle.containsAny("drive", "car", "service", "gas", "fuel")) return "Car"

        // 7. Finance
        if (lowerTitle.containsAny("money", "cash", "pay", "bill", "invoice")) return "Money"
        if (lowerTitle.containsAny("bank", "deposit", "transfer", "withdraw")) return "Bank"
        
        // Default
        return "Edit" 
    }
    
    // Helper extension
    private fun String.containsAny(vararg keywords: String): Boolean {
        for (keyword in keywords) {
            if (this.contains(keyword)) return true
        }
        return false
    }
}
