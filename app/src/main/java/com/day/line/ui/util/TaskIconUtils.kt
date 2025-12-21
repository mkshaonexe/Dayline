package com.day.line.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.sharp.*
import androidx.compose.ui.graphics.vector.ImageVector

object TaskIconUtils {

    val AvailableIcons = listOf(
        // 1. Development & Code (40 keywords)
        "Code" to Icons.Filled.Code,
        "Terminal" to Icons.Filled.Terminal,
        "BugReport" to Icons.Filled.BugReport,
        "DeveloperMode" to Icons.Filled.DeveloperMode,
        "Api" to Icons.Filled.Api,
        "IntegrationInstructions" to Icons.Filled.IntegrationInstructions,
        "Javascript" to Icons.Filled.Javascript,
        "Html" to Icons.Filled.Html,
        "Css" to Icons.Filled.Css,
        "Php" to Icons.Filled.Php,
        "DataObject" to Icons.Filled.DataObject,
        "DataArray" to Icons.Filled.DataArray,
        "Storage" to Icons.Filled.Storage,
        "Cloud" to Icons.Filled.Cloud,
        "CloudUpload" to Icons.Filled.CloudUpload,
        "CloudDownload" to Icons.Filled.CloudDownload,
        "Sync" to Icons.Filled.Sync,
        "Commit" to Icons.Filled.Commit,
        "Merge" to Icons.Filled.Merge,
        "Polyline" to Icons.Filled.Polyline,
        "Schema" to Icons.Filled.Schema,
        "Memory" to Icons.Filled.Memory,
        "Extension" to Icons.Filled.Extension,
        "Sdk" to Icons.Filled.AddToQueue, // Proxy for SDK/Lib
        "SettingsEthernet" to Icons.Filled.SettingsEthernet,
        "Lan" to Icons.Filled.Lan,
        "Wifi" to Icons.Filled.Wifi,
        "Security" to Icons.Filled.Security,
        "VpnKey" to Icons.Filled.VpnKey,
        "Lock" to Icons.Filled.Lock,
        
        // 2. Design & Creative (30 keywords)
        "Brush" to Icons.Filled.Brush,
        "Palette" to Icons.Filled.Palette,
        "ColorLens" to Icons.Filled.ColorLens,
        "Draw" to Icons.Filled.Draw,
        "Edit" to Icons.Filled.Edit,
        "DesignServices" to Icons.Filled.DesignServices,
        "Web" to Icons.Filled.Web,
        "Image" to Icons.Filled.Image,
        "PhotoCamera" to Icons.Filled.PhotoCamera,
        "Crop" to Icons.Filled.Crop,
        "Layers" to Icons.Filled.Layers,
        "AutoFixHigh" to Icons.Filled.AutoFixHigh,
        "Contrast" to Icons.Filled.Contrast,
        "Animation" to Icons.Filled.Animation,
        "MovieFilter" to Icons.Filled.MovieFilter,
        "Texture" to Icons.Filled.Texture,
        "Gradient" to Icons.Filled.Gradient,
        "Looks" to Icons.Filled.Looks,
        "Style" to Icons.Filled.Style,
        
        // 3. Business & Work (50 keywords)
        "Work" to Icons.Filled.Work,
        "Business" to Icons.Filled.Business,
        "BusinessCenter" to Icons.Filled.BusinessCenter,
        "Meeting" to Icons.Filled.MeetingRoom, // Proxy for meeting
        "Groups" to Icons.Filled.Groups,
        "Person" to Icons.Filled.Person,
        "Badge" to Icons.Filled.Badge,
        "Verified" to Icons.Filled.Verified,
        "Copyright" to Icons.Filled.Copyright,
        "Gavel" to Icons.Filled.Gavel,
        "Balance" to Icons.Filled.Balance,
        "TrendingUp" to Icons.Filled.TrendingUp,
        "TrendingDown" to Icons.Filled.TrendingDown,
        "Insights" to Icons.Filled.Insights,
        "Analytics" to Icons.Filled.Analytics,
        "Assessment" to Icons.Filled.Assessment,
        "Leaderboard" to Icons.Filled.Leaderboard,
        "Assignment" to Icons.Filled.Assignment,
        "Task" to Icons.Filled.Task,
        "FactCheck" to Icons.Filled.FactCheck,
        "Rule" to Icons.Filled.Rule,
        "PendingActions" to Icons.Filled.PendingActions,
        "Schedule" to Icons.Filled.Schedule,
        "EventAvailable" to Icons.Filled.EventAvailable,
        "EventBusy" to Icons.Filled.EventBusy,
        "CalendarMonth" to Icons.Filled.CalendarMonth,
        "DateRange" to Icons.Filled.DateRange,
        "PresentToAll" to Icons.Filled.PresentToAll,
        "CoPresent" to Icons.Filled.CoPresent,
        "Slideshow" to Icons.Filled.Slideshow,
        
        // 4. Communication (40 keywords)
        "Email" to Icons.Filled.Email,
        "Mail" to Icons.Filled.Mail,
        "Drafts" to Icons.Filled.Drafts,
        "Inbox" to Icons.Filled.Inbox,
        "Send" to Icons.Filled.Send,
        "Chat" to Icons.Filled.Chat,
        "ChatBubble" to Icons.Filled.ChatBubble,
        "Message" to Icons.Filled.Message,
        "Forum" to Icons.Filled.Forum,
        "Comment" to Icons.Filled.Comment,
        "Call" to Icons.Filled.Call,
        "Phone" to Icons.Filled.Phone,
        "PhoneInTalk" to Icons.Filled.PhoneInTalk,
        "ContactPhone" to Icons.Filled.ContactPhone,
        "VideoCall" to Icons.Filled.VideoCall,
        "Duo" to Icons.Filled.Duo,
        "Mic" to Icons.Filled.Mic,
        "VolumeUp" to Icons.Filled.VolumeUp,
        "Notifications" to Icons.Filled.Notifications,
        "NotificationsActive" to Icons.Filled.NotificationsActive,
        "RingVolume" to Icons.Filled.RingVolume,
        "Campaign" to Icons.Filled.Campaign,
        "Megaphone" to Icons.Filled.RecordVoiceOver,
        
        // 5. Education & Study (40 keywords)
        "School" to Icons.Filled.School,
        "Book" to Icons.Filled.Book,
        "MenuBook" to Icons.Filled.MenuBook,
        "LibraryBooks" to Icons.Filled.LibraryBooks,
        "LocalLibrary" to Icons.Filled.LocalLibrary,
        "Class" to Icons.Filled.Class,
        "HistoryEdu" to Icons.Filled.HistoryEdu,
        "AssignmentReturned" to Icons.Filled.AssignmentReturned,
        "AssignmentLate" to Icons.Filled.AssignmentLate,
        "Quiz" to Icons.Filled.Quiz,
        "Calculate" to Icons.Filled.Calculate,
        "Functions" to Icons.Filled.Functions,
        "Science" to Icons.Filled.Science,
        "Biotech" to Icons.Filled.Biotech,
        "Psychology" to Icons.Filled.Psychology,
        "Engineering" to Icons.Filled.Engineering,
        "Construction" to Icons.Filled.Construction,
        "SquareFoot" to Icons.Filled.SquareFoot,
        "Architecture" to Icons.Filled.Architecture,
        
        // 6. Science & Research (30 keywords)
        "Science" to Icons.Filled.Science,
        "Experiment" to Icons.Filled.Science, // Reuse
        "Biotech" to Icons.Filled.Biotech,
        "Coronavirus" to Icons.Filled.Coronavirus,
        "Bug" to Icons.Filled.PestControl,
        "NaturePeople" to Icons.Filled.NaturePeople,
        
        // 7. Finance & Money (40 keywords)
        "AttachMoney" to Icons.Filled.AttachMoney,
        "MoneyOff" to Icons.Filled.MoneyOff,
        "Euro" to Icons.Filled.Euro,
        "CurrencyPound" to Icons.Filled.CurrencyPound,
        "CurrencyYen" to Icons.Filled.CurrencyYen,
        "CurrencyRub" to Icons.Filled.AttachMoney, // Fallback
        "CurrencyBitcoin" to Icons.Filled.CurrencyBitcoin,
        "Paid" to Icons.Filled.Paid,
        "AccountBalance" to Icons.Filled.AccountBalance,
        "AccountBalanceWallet" to Icons.Filled.AccountBalanceWallet,
        "Savings" to Icons.Filled.Savings,
        "CreditCard" to Icons.Filled.CreditCard,
        "Payment" to Icons.Filled.Payment,
        "Receipt" to Icons.Filled.Receipt,
        "ReceiptLong" to Icons.Filled.ReceiptLong,
        "PriceChange" to Icons.Filled.PriceChange,
        "RequestQuote" to Icons.Filled.RequestQuote,
        "Calculate" to Icons.Filled.Calculate,
        "WaterfallChart" to Icons.Filled.WaterfallChart,
        "PieChart" to Icons.Filled.PieChart,
        "ShowChart" to Icons.Filled.ShowChart,
        
        // 8. Shopping & Retail (40 keywords)
        "ShoppingCart" to Icons.Filled.ShoppingCart,
        "ShoppingBag" to Icons.Filled.ShoppingBag,
        "ShoppingBasket" to Icons.Filled.ShoppingBasket,
        "AddShoppingCart" to Icons.Filled.AddShoppingCart,
        "RemoveShoppingCart" to Icons.Filled.RemoveShoppingCart,
        "Store" to Icons.Filled.Store,
        "Storefront" to Icons.Filled.Storefront,
        "LocalConvenienceStore" to Icons.Filled.LocalConvenienceStore,
        "LocalGroceryStore" to Icons.Filled.LocalGroceryStore,
        "Sell" to Icons.Filled.Sell,
        "Label" to Icons.Filled.Label,
        "Loyalty" to Icons.Filled.Loyalty,
        "CardGiftcard" to Icons.Filled.CardGiftcard,
        "Redeem" to Icons.Filled.Redeem,
        "Discount" to Icons.Filled.Discount,
        "QrCode" to Icons.Filled.QrCode,
        "ProductionQuantityLimits" to Icons.Filled.ProductionQuantityLimits,
        
        // 9. Health & Medical (50 keywords)
        "LocalHospital" to Icons.Filled.LocalHospital,
        "LocalPharmacy" to Icons.Filled.LocalPharmacy,
        "Healing" to Icons.Filled.Healing,
        "Sick" to Icons.Filled.Sick,
        "Medication" to Icons.Filled.Medication,
        "MedicalServices" to Icons.Filled.MedicalServices,
        "MonitorHeart" to Icons.Filled.MonitorHeart,
        "Favorite" to Icons.Filled.Favorite, // Heart
        "Bloodtype" to Icons.Filled.Bloodtype,
        "Vaccines" to Icons.Filled.Vaccines,
        "Masks" to Icons.Filled.Masks,
        "Sanitizer" to Icons.Filled.Sanitizer,
        "Thermostat" to Icons.Filled.Thermostat, // For fever?
        "HealthAndSafety" to Icons.Filled.HealthAndSafety,
        "PregnantWoman" to Icons.Filled.PregnantWoman,
        "BabyChangingStation" to Icons.Filled.BabyChangingStation,
        "WheelchairPickup" to Icons.Filled.WheelchairPickup,
        "Emergency" to Icons.Filled.Emergency,
        
        // 10. Fitness & Sports (50 keywords)
        "FitnessCenter" to Icons.Filled.FitnessCenter,
        "DirectionsRun" to Icons.Filled.DirectionsRun,
        "DirectionsWalk" to Icons.Filled.DirectionsWalk,
        "DirectionsBike" to Icons.Filled.DirectionsBike,
        "Pool" to Icons.Filled.Pool,
        "SelfImprovement" to Icons.Filled.SelfImprovement, // Yoga
        "Sports" to Icons.Filled.Sports,
        "SportsBasketball" to Icons.Filled.SportsBasketball,
        "SportsSoccer" to Icons.Filled.SportsSoccer,
        "SportsBaseball" to Icons.Filled.SportsBaseball,
        "SportsFootball" to Icons.Filled.SportsFootball,
        "SportsTennis" to Icons.Filled.SportsTennis,
        "SportsVolleyball" to Icons.Filled.SportsVolleyball,
        "SportsGolf" to Icons.Filled.SportsGolf,
        "SportsCricket" to Icons.Filled.SportsCricket,
        "SportsMma" to Icons.Filled.SportsMma,
        "SportsMartialArts" to Icons.Filled.SportsMartialArts,
        "Kayaking" to Icons.Filled.Kayaking,
        "Surfing" to Icons.Filled.Surfing,
        "Skateboarding" to Icons.Filled.Skateboarding,
        "Snowboarding" to Icons.Filled.Snowboarding,
        "DownhillSkiing" to Icons.Filled.DownhillSkiing,
        "Hiking" to Icons.Filled.Hiking,
        
        // 11. Food & Drink (40 keywords)
        "Restaurant" to Icons.Filled.Restaurant,
        "RestaurantMenu" to Icons.Filled.RestaurantMenu,
        "LocalCafe" to Icons.Filled.LocalCafe,
        "LocalBar" to Icons.Filled.LocalBar,
        "LocalPizza" to Icons.Filled.LocalPizza,
        "BakeryDining" to Icons.Filled.BakeryDining,
        "LunchDining" to Icons.Filled.LunchDining,
        "DinnerDining" to Icons.Filled.DinnerDining,
        "BreakfastDining" to Icons.Filled.BreakfastDining,
        "BrunchDining" to Icons.Filled.BrunchDining,
        "Run" to Icons.Filled.DirectionsRun,
        // ... replaced elsewhere or just fix here
        // Wait, line 249 is IceCream.
        "IceCream" to Icons.Filled.Kitchen, // Fallback
        "Liquor" to Icons.Filled.Liquor,
        "WineBar" to Icons.Filled.WineBar,
        "Nightlife" to Icons.Filled.Nightlife,
        "Tapas" to Icons.Filled.Tapas,
        "RamenDining" to Icons.Filled.RamenDining,
        "Kitchen" to Icons.Filled.Kitchen,
        "SoupKitchen" to Icons.Filled.SoupKitchen,
        "Fastfood" to Icons.Filled.Fastfood,
        
        // 12. Household & Chores (40 keywords)
        "Home" to Icons.Filled.Home,
        "Cottage" to Icons.Filled.Cottage,
        "Apartment" to Icons.Filled.Apartment,
        "CleaningServices" to Icons.Filled.CleaningServices,
        "LocalLaundryService" to Icons.Filled.LocalLaundryService,
        "Wash" to Icons.Filled.Wash, // If Wash exists
        "DryCleaning" to Icons.Filled.DryCleaning,
        "Iron" to Icons.Filled.Iron,
        "Bed" to Icons.Filled.Bed,
        "BedroomParent" to Icons.Filled.BedroomParent, // Bed alternative
        "Chair" to Icons.Filled.Chair,
        "TableRestaurant" to Icons.Filled.TableRestaurant, // Table
        "Light" to Icons.Filled.Light,
        "Lightbulb" to Icons.Filled.Lightbulb,
        "DoorFront" to Icons.Filled.DoorFront,
        "Window" to Icons.Filled.Window,
        "Yard" to Icons.Filled.Yard,
        "Balcony" to Icons.Filled.Balcony,
        "Garage" to Icons.Filled.Garage,
        "Delete" to Icons.Filled.Delete,
        "DeleteOutline" to Icons.Filled.DeleteOutline,
        
        // 13. Family & Kids (30 keywords)
        "FamilyRestroom" to Icons.Filled.FamilyRestroom,
        "ChildCare" to Icons.Filled.ChildCare,
        "ChildFriendly" to Icons.Filled.ChildFriendly,
        "Stroller" to Icons.Filled.Stroller,
        "BabyChangingStation" to Icons.Filled.BabyChangingStation,
        "Toys" to Icons.Filled.Toys,
        "VideoGameAsset" to Icons.Filled.VideogameAsset,
        "EscalatorWarning" to Icons.Filled.EscalatorWarning, // Parent child
        "PregnantWoman" to Icons.Filled.PregnantWoman,
        "Groups" to Icons.Filled.Groups,
        
        // 14. Social & Events (40 keywords)
        "Celebration" to Icons.Filled.Celebration,
        "Cake" to Icons.Filled.Cake,
        "PartyMode" to Icons.Filled.PartyMode,
        "LocalActivity" to Icons.Filled.LocalActivity,
        "ConfirmationNumber" to Icons.Filled.ConfirmationNumber, // Ticket
        "Festival" to Icons.Filled.Festival,
        "TheaterComedy" to Icons.Filled.TheaterComedy,
        "Stadium" to Icons.Filled.Stadium,
        "EmojiPeople" to Icons.Filled.EmojiPeople,
        "Diversity3" to Icons.Filled.Diversity3,
        
        // 15. Entertainment & Media (40 keywords)
        "Tv" to Icons.Filled.Tv,
        "LiveTv" to Icons.Filled.LiveTv,
        "Movie" to Icons.Filled.Movie,
        "Theaters" to Icons.Filled.Theaters,
        "MusicNote" to Icons.Filled.MusicNote,
        "MusicVideo" to Icons.Filled.MusicVideo,
        "Headphones" to Icons.Filled.Headphones,
        "Speaker" to Icons.Filled.Speaker,
        "SpeakerGroup" to Icons.Filled.SpeakerGroup,
        "Radio" to Icons.Filled.Radio,
        "Mic" to Icons.Filled.Mic,
        "VideogameAsset" to Icons.Filled.VideogameAsset,
        "SportsEsports" to Icons.Filled.SportsEsports,
        "Casino" to Icons.Filled.Casino,
        "Book" to Icons.Filled.Book,
        "Newspaper" to Icons.Filled.Newspaper,
        
        // 16. Travel & Transport (50 keywords)
        "Flight" to Icons.Filled.Flight,
        "FlightTakeoff" to Icons.Filled.FlightTakeoff,
        "FlightLand" to Icons.Filled.FlightLand,
        "Airlines" to Icons.Filled.Airlines,
        "DirectionsCar" to Icons.Filled.DirectionsCar,
        "TaxiAlert" to Icons.Filled.TaxiAlert,
        "DirectionsBus" to Icons.Filled.DirectionsBus,
        "DirectionsSubway" to Icons.Filled.DirectionsSubway,
        "Train" to Icons.Filled.Train,
        "Tram" to Icons.Filled.Tram,
        "DirectionsBoat" to Icons.Filled.DirectionsBoat,
        "Navigation" to Icons.Filled.Navigation,
        "Map" to Icons.Filled.Map,
        "Explore" to Icons.Filled.Explore,
        "CompassCalibration" to Icons.Filled.CompassCalibration,
        "LocationOn" to Icons.Filled.LocationOn,
        "Commute" to Icons.Filled.Commute,
        "Traffic" to Icons.Filled.Traffic,
        "LocalGasStation" to Icons.Filled.LocalGasStation,
        "EvStation" to Icons.Filled.EvStation,
        "Parking" to Icons.Filled.LocalParking,
        "Hotel" to Icons.Filled.Hotel,
        "Luggage" to Icons.Filled.Luggage,
        
        // 17. Nature & Outdoors (30 keywords)
        "Nature" to Icons.Filled.Nature,
        "NaturePeople" to Icons.Filled.NaturePeople,
        "Forest" to Icons.Filled.Forest,
        "Landscape" to Icons.Filled.Landscape,
        "Terrain" to Icons.Filled.Terrain,
        "Park" to Icons.Filled.Park,
        "Volcano" to Icons.Filled.Volcano,
        "Water" to Icons.Filled.Water,
        "Waves" to Icons.Filled.Waves,
        "WbSunny" to Icons.Filled.WbSunny,
        "WbCloudy" to Icons.Filled.WbCloudy,
        "Nightlight" to Icons.Filled.Nightlight,
        "Star" to Icons.Filled.Star,
        "LocalFlorist" to Icons.Filled.LocalFlorist,
        "Grass" to Icons.Filled.Grass,
        "Agriculture" to Icons.Filled.Agriculture,
        "Pets" to Icons.Filled.Pets,
        
        // 18. Maintenance & Tools (30 keywords)
        "Build" to Icons.Filled.Build,
        "Handyman" to Icons.Filled.Handyman,
        "Plumbing" to Icons.Filled.Plumbing,
        "ElectricalServices" to Icons.Filled.ElectricalServices,
        "Construction" to Icons.Filled.Construction,
        "Carpenter" to Icons.Filled.Carpenter,
        "Hardware" to Icons.Filled.Hardware,
        "HomeRepairService" to Icons.Filled.HomeRepairService,
        "PrecisionManufacturing" to Icons.Filled.PrecisionManufacturing,
        
        // 19. Spirituality & Wellness (20 keywords)
        "Spa" to Icons.Filled.Spa,
        "SelfImprovement" to Icons.Filled.SelfImprovement,
        "TempleBuddhist" to Icons.Filled.TempleBuddhist,
        "TempleHindu" to Icons.Filled.TempleHindu,
        "Mosque" to Icons.Filled.Mosque,
        "Church" to Icons.Filled.Church,
        "Synagogue" to Icons.Filled.Synagogue,
        "VolunteerActivism" to Icons.Filled.VolunteerActivism,
        
        // 20. Admin & Utilities (40 keywords)
        "Settings" to Icons.Filled.Settings,
        "SettingsApplications" to Icons.Filled.SettingsApplications,
        "AdminPanelSettings" to Icons.Filled.AdminPanelSettings,
        "ManageAccounts" to Icons.Filled.ManageAccounts,
        "SupervisorAccount" to Icons.Filled.SupervisorAccount,
        "Print" to Icons.Filled.Print,
        "ContentCopy" to Icons.Filled.ContentCopy,
        "ContentPaste" to Icons.Filled.ContentPaste,
        "Save" to Icons.Filled.Save,
        "Folder" to Icons.Filled.Folder,
        "SnippetFolder" to Icons.Filled.SnippetFolder,
        "FileOpen" to Icons.Filled.FileOpen,
        "FileUpload" to Icons.Filled.FileUpload,
        "FileDownload" to Icons.Filled.FileDownload,
        "Cloud" to Icons.Filled.Cloud,
        "Wifi" to Icons.Filled.Wifi,
        "BatteryFull" to Icons.Filled.BatteryFull,
        "BatteryAlert" to Icons.Filled.BatteryAlert,
        
        // 21. Security & Privacy (20 keywords)
        "Security" to Icons.Filled.Security,
        "PrivacyTip" to Icons.Filled.PrivacyTip,
        "Policy" to Icons.Filled.Policy,
        "VerifiedUser" to Icons.Filled.VerifiedUser,
        "AdminPanelSettings" to Icons.Filled.AdminPanelSettings,
        "Lock" to Icons.Filled.Lock,
        "Key" to Icons.Filled.Key,
        "VpnKey" to Icons.Filled.VpnKey,
        
        // 22. Love & Relationships (20 keywords)
        "Favorite" to Icons.Filled.Favorite,
        "FavoriteBorder" to Icons.Filled.FavoriteBorder,
        "VolunteerActivism" to Icons.Filled.VolunteerActivism, // Heart in hand
        "Loyalty" to Icons.Filled.Loyalty, // Heart shape tag
        "Handshake" to Icons.Filled.Handshake,
        "Diversity1" to Icons.Filled.Diversity1,
        "Diversity2" to Icons.Filled.Diversity2,
        
        // 23. Weather & Environment (20 keywords)
        "Thermostat" to Icons.Filled.Thermostat,
        "WbSunny" to Icons.Filled.WbSunny,
        "WbCloudy" to Icons.Filled.WbCloudy,
        "AcUnit" to Icons.Filled.AcUnit,
        "Tsunami" to Icons.Filled.Tsunami,
        "Flood" to Icons.Filled.Flood,
        "Thunderstorm" to Icons.Filled.Thunderstorm,
        "Air" to Icons.Filled.Air,
        "WaterDrop" to Icons.Filled.WaterDrop,
        "Umbrella" to Icons.Filled.Umbrella
    )

    fun getIconByName(name: String): ImageVector {
        return AvailableIcons.find { it.first == name }?.second ?: Icons.Filled.Edit
    }

    /**
     * Predicts the most likely icon based on the task title using a massive keyword map.
     */
    fun predictIconName(title: String): String {
        val lowerTitle = title.lowercase()
        
        // Helper to quickly return if any keyword matches
        fun matches(vararg keywords: String): Boolean {
            for (keyword in keywords) {
                if (lowerTitle.contains(keyword)) return true
            }
            return false
        }

        // --- 1. Development & Code ---
        if (matches(
            "code", "coding", "program", "programming", "dev", "develop", "developer", "software", "engineer",
            "java", "kotlin", "python", "js", "javascript", "ts", "typescript", "c++", "c#", "swift", "go",
            "rust", "php", "html", "css", "sql", "nosql", "db", "database", "query", "mongo", "postgres",
            "mysql", "sqlite", "room", "realm", "firebase", "aws", "azure", "gcp", "cloud", "server", "backend",
            "frontend", "fullstack", "api", "rest", "graphql", "json", "xml", "yaml", "endpoint", "request",
            "response", "debug", "fix", "bug", "error", "exception", "crash", "stacktrace", "log", "trace",
            "git", "github", "gitlab", "bitbucket", "commit", "push", "pull", "merge", "branch", "checkout",
            "clone", "repo", "repository", "pr", "pull request", "issue", "ticket", "jira", "trello", "agile",
            "scrum", "sprint", "kanban", "standup", "daily", "retro", "demo", "deploy", "release", "build",
            "compile", "run", "test", "unit", "integration", "e2e", "qa", "quality", "review", "refactor",
            "optimize", "performance", "memory", "cpu", "leak", "algo", "algorithm", "structure", "class",
            "object", "function", "method", "variable", "int", "string", "bool", "loop", "if", "else", "try",
            "catch", "terminal", "console", "cmd", "shell", "bash", "zsh", "script", "hack", "cyber", "security",
            "encrypt", "decrypt", "auth", "token", "jwt", "login", "signup", "user", "admin", "role", "permission"
        )) return "Code"

        if (matches("ui", "ux", "design", "figma", "sketch", "adobe", "photoshop", "illustrator", "xd", "wireframe", "prototype", "mockup", "layout", "color", "palette", "font", "typography", "icon", "logo", "brand", "style", "theme", "css", "flexbox", "grid", "responsive", "mobile", "web", "desktop", "app", "application", "interface", "experience", "user")) return "DesignServices"

        // --- 2. Business & Work ---
        if (matches("work", "job", "career", "profession", "office", "business", "company", "corp", "startup", "inc", "llc", "freelance", "contract", "consult", "client", "customer", "partner", "vendor", "supplier", "boss", "manager", "ceo", "cto", "cfo", "coo", "director", "lead", "supervisor", "colleague", "coworker", "team", "staff", "employee", "hr", "human resources", "recruit", "hire", "interview", "resume", "cv", "application", "offer", "salary", "wage", "bonus", "promotion", "raise", "quit", "resign", "fire", "layoff", "meeting", "meet", "call", "conference", "zoom", "teams", "slack", "discord", "google meet", "skype", "webex", "presentation", "present", "slide", "deck", "powerpoint", "keynote", "spreadsheet", "excel", "sheets", "doc", "word", "pdf", "report", "analysis", "analytics", "data", "chart", "graph", "metric", "kpi", "okr", "goal", "target", "quarter", "q1", "q2", "q3", "q4", "annual", "yearly", "monthly", "weekly", "daily", "plan", "strategy", "roadmap", "project", "task", "todo", "deadline", "due", "priority", "urgent", "important")) return "BusinessCenter"

        // --- 3. Education & Study ---
        if (matches("study", "learn", "read", "reading", "book", "textbook", "novel", "literature", "paper", "essay", "article", "journal", "thesis", "dissertation", "research", "homework", "assignment", "project", "exam", "test", "quiz", "midterm", "final", "grade", "score", "mark", "gpa", "pass", "fail", "course", "class", "subject", "lesson", "lecture", "seminar", "workshop", "tutorial", "school", "college", "university", "campus", "student", "teacher", "professor", "tutor", "mentor", "degree", "diploma", "certificate", "phd", "master", "bachelor", "math", "calculus", "algebra", "geometry", "statistics", "physics", "chemistry", "biology", "science", "history", "geography", "english", "language", "spanish", "french", "german", "mandarin", "japanese", "art", "music", "drama", "pe", "gym", "library", "lab")) return "School"

        // --- 4. Finance ---
        if (matches("money", "cash", "dollar", "euro", "pound", "yen", "currency", "coin", "bill", "banknote", "wallet", "purse", "pocket", "bank", "account", "saving", "checking", "credit", "debit", "card", "visa", "mastercard", "amex", "pay", "payment", "paid", "transaction", "transfer", "deposit", "withdraw", "atm", "invoice", "receipt", "fee", "charge", "cost", "price", "expensive", "cheap", "deal", "discount", "sale", "offer", "coupon", "voucher", "tax", "income", "expense", "revenue", "profit", "loss", "budget", "finance", "invest", "investment", "stock", "share", "market", "trade", "crypto", "bitcoin", "ethereum", "blockchain", "nft", "loan", "debt", "mortgage", "rent", "lease", "insurance", "premium", "claim")) return "AttachMoney"

        // --- 5. Shopping ---
         if (matches("shop", "shopping", "buy", "purchase", "order", "store", "mall", "market", "marketplace", "online", "amazon", "ebay", "walmart", "target", "bestbuy", "costco", "ikea", "grocer", "grocery", "supermarket", "food", "fruit", "veg", "vegetable", "meat", "dairy", "bakery", "deli", "frozen", "pantry", "fridge", "freezer", "cart", "basket", "bag", "checkout", "register", "cashier", "receipt", "return", "exchange", "refund", "gift", "present", "wishlist")) return "ShoppingCart"

        // --- 6. Health & Fitness ---
        if (matches("gym", "fitness", "fit", "workout", "exercise", "train", "training", "lift", "weight", "dumbbell", "barbell", "kettlebell", "bench", "squat", "deadlift", "pushup", "pullup", "cardio", "run", "running", "jog", "jogging", "sprint", "marathon", "treadmill", "walk", "walking", "hike", "hiking", "step", "swim", "swimming", "pool", "lap", "bike", "cycle", "cycling", "ride", "spin", "peloton", "yoga", "pilates", "stretch", "flexible", "mat", "meditate", "meditation", "mind", "mindful", "breathe", "breath", "health", "healthy", "diet", "nutrition", "calorie", "protein", "carb", "fat", "vitamin", "supplement", "water", "hydrate", "drink")) return "FitnessCenter"

        if (matches("doctor", "dr", "physician", "nurse", "medic", "hospital", "clinic", "urgent care", "er", "emergency", "ambulance", "dentist", "dental", "tooth", "teeth", "ortho", "braces", "vision", "eye", "glasses", "contact", "lens", "optometrist", "therapy", "therapist", "psych", "counsel", "counseling", "mental", "ill", "sick", "pain", "hurt", "ache", "flu", "cold", "covid", "virus", "fever", "cough", "sneeze", "med", "medicine", "pill", "drug", "prescription", "rx", "pharmacy", "vaccine", "shot", "injection", "blood", "test", "scan", "xray", "mri", "surgery", "operation", "recover", "heal")) return "LocalHospital"

        // --- 7. Household ---
        if (matches("home", "house", "apartment", "condo", "flat", "room", "living", "bed", "bath", "kitchen", "garage", "attic", "basement", "roof", "yard", "garden", "lawn", "patio", "deck", "porch", "clean", "cleaning", "tidy", "organize", "declutter", "mess", "dust", "vacuum", "sweep", "mop", "scrub", "wipe", "wash", "laundry", "clothes", "fabric", "linen", "towel", "sheet", "pillow", "blanket", "detergent", "soap", "bleach", "softener", "fold", "iron", "dry", "hang", "trash", "garbage", "rubbish", "waste", "recycle", "bin", "can", "bag", "dishes", "dishwasher", "sink", "faucet", "leak", "plumb", "fix", "repair", "maintain", "paint", "renovate", "decorate", "furniture", "couch", "sofa", "chair", "table", "desk", "lamp", "light", "bulb", "switch", "plug", "outlet")) return "Home"

        // --- 8. Food & Cooking ---
        if (matches("cook", "cooking", "bake", "baking", "make", "prepare", "prep", "kitchen", "chef", "recipe", "ingredient", "spice", "herb", "salt", "pepper", "oil", "pan", "pot", "oven", "stove", "grill", "bbq", "microwave", "fridge", "freezer", "blender", "mixer", "knife", "spoon", "fork", "plate", "bowl", "cup", "glass", "mug", "breakfast", "lunch", "dinner", "supper", "snack", "meal", "food", "eat", "hungry", "thirsty", "drink", "beverage", "water", "coffee", "tea", "milk", "juice", "soda", "wine", "beer", "alcohol", "cocktail", "bar", "pub", "club", "restaurant", "cafe", "diner", "bistro", "buffet", "takeout", "delivery", "uber", "eats", "doordash", "grubhub", "pizza", "burger", "taco", "sushi", "pasta", "salad", "steak", "chicken", "beef", "pork", "fish", "seafood", "vegan", "vegetarian", "keto", "paleo")) return "Restaurant"

        // --- 9. Coffee ---
        if (matches("coffee", "cafe", "latte", "espresso", "cappuccino", "mocha", "macchiato", "americano", "brew", "grind", "bean", "roast", "starbucks", "dunkin", "tim hortons", "cup of joe")) return "LocalCafe"

        // --- 10. Social & Family ---
        if (matches("family", "parent", "mother", "father", "mom", "dad", "sister", "brother", "sibling", "child", "kid", "baby", "infant", "toddler", "son", "daughter", "grandma", "grandpa", "aunt", "uncle", "cousin", "niece", "nephew", "relative", "friend", "buddy", "pal", "mate", "bestie", "partner", "spouse", "husband", "wife", "boyfriend", "girlfriend", "date", "relationship", "love", "romance", "meetup", "hangout", "visit", "guest", "party", "celebration", "birthday", "anniversary", "wedding", "shower", "funeral", "wake", "reunion", "get together", "gathering", "dinner party", "bbq", "picnic")) return "Groups"

        // --- 11. Pets ---
        if (matches("pet", "animal", "dog", "puppy", "pup", "cat", "kitten", "kitty", "fish", "bird", "hamster", "guinea pig", "rabbit", "bunny", "turtle", "lizard", "snake", "feed", "walk", "groom", "vet", "veterinarian", "litter", "cage", "tank", "leash", "collar", "toy", "treat", "bark", "meow")) return "Pets"

        // --- 12. Entertainment ---
        if (matches("movie", "film", "cinema", "theater", "show", "series", "tv", "television", "episode", "season", "netflix", "hulu", "disney", "prime", "hbo", "youtube", "twitch", "video", "stream", "watch", "actor", "actress", "director", "producer", "game", "gaming", "play", "player", "xbox", "playstation", "ps4", "ps5", "nintendo", "switch", "pc", "steam", "esports", "minecraft", "fortnite", "roblox", "call of duty", "league of legends", "overwatch", "music", "song", "album", "artist", "band", "concert", "gig", "festival", "listen", "spotify", "apple music", "playlist", "podcast", "radio", "audio", "sound")) return "Movie"

        // --- 13. Travel ---
        if (matches("travel", "trip", "journey", "voyage", "vacation", "holiday", "tour", "tourist", "explore", "adventure", "flight", "fly", "plane", "airplane", "airport", "airline", "ticket", "boarding", "gate", "seat", "luggage", "baggage", "suitcase", "passport", "visa", "customs", "immigration", "hotel", "motel", "hostel", "airbnb", "resort", "stay", "book", "reservation", "checkin", "checkout", "room", "train", "rail", "subway", "metro", "tube", "station", "bus", "coach", "stop", "taxi", "cab", "uber", "lyft", "car", "drive", "ride", "road", "highway", "freeway", "toll", "gas", "petrol", "fuel", "charge", "park", "parking", "map", "gps", "navigate", "direction", "north", "south", "east", "west")) return "Flight"

        // --- 14. Chores & Maintenance ---
        if (matches("laundry", "wash", "dry", "fold", "iron", "dry clean", "trash", "recycle", "garbage", "rubbish", "clean", "scrub", "mop", "sweep", "vacuum", "dust", "tidy", "organize", "fix", "repair", "mend", "patch", "replace", "install", "setup", "assemble", "build", "construct", "paint", "garden", "mow", "water", "plant", "weed", "rake", "shovel", "snow", "ice", "car wash", "oil change", "tire", "mechanic")) return "CleaningServices"

        // --- 15. Sleep ---
        if (matches("sleep", "nap", "rest", "snooze", "bed", "bedtime", "wake", "up", "alarm", "dream", "nightmare", "insomnia", "tired", "exhausted", "fatigue", "drowsy")) return "Bedtime"

        // --- 16. Admin ---
        if (matches("admin", "tax", "form", "application", "license", "permit", "id", "passport", "renew", "register", "vote", "election", "ballot", "legal", "lawyer", "attorney", "court", "judge", "jury", "sue", "will", "trust", "notary", "sign", "signature", "stamp", "post", "mail", "ship", "package", "parcel", "delivery", "box", "letter", "envelope", "stamp")) return "AdminPanelSettings"

        // Default
        return "Edit"
    }
}
