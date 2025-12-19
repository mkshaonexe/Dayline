# Dayline App - Product Requirements Document (PRD)

**Version:** 1.0  
**Status:** Draft  
**Target Platform:** Android (Native)  
**Tech Stack:** Kotlin, Jetpack Compose, Room, Hilt, Coroutines

---

## 1. Executive Summary

**Dayline** is a modern, premium day-planner application designed to structure the user's day through a visually engaging, continuous timeline. Inspired by "Structured" but refined with a unique "Dayline" identity, the app combines task management with habit tracking in a seamless, flow-based interface.

**Core Vision:** To transform the mundane to-do list into a beautiful, flowing user experience that encourages productivity through visual clarity and premium aesthetics.

---

## 2. Visual Identity & Design System

The visual language of Dayline is its most critical differentiator. It must feel "alive" and "premium".

### 2.1 Theme & Colors
*   **Base Theme:** Clean, Bright Light Theme.
*   **Primary Accent:** **Dayline Orange** (Energetic, warmth). Used for primary actions, current active tasks, and FAB (Floating Action Button).
*   **Secondary Accents:**
    *   **Soft Teal/Blue:** For evening/night events (e.g., "Wind Down").
    *   **Warm Pink/Salmon:** For morning events (e.g., "Rise and Shine").
    *   **Soft Gray:** For inactive timeline tracks and completed items.
*   **Aesthetics:**
    *   **Glassmorphism:** Subtle blur effects on overlays (e.g., specific task details).
    *   **Rounded Typography:** Usage of modern sans-serif fonts (e.g., Outfit or Google Sans) with rounded corners on cards and icons.
    *   **Micro-Animations:** Every interaction (check, scroll, expand) has a physics-based spring animation.

---

## 3. Core Feature: The Dynamic Timeline

The Timeline is the heart of Dayline. It is a vertical scrolling view that connects all daily events.

### 3.1 Visual Structure
*   **The Line:** A continuous vertical line runs down the left-center of the screen.
    *   **Style:** Dotted/Dashed line for gaps between tasks; Solid line during active task duration.
    *   **Dynamic Coloring:** The line gradient matches the task colors it connects (e.g., fading from Pink morning node to Orange work node).
*   **Nodes (Events):**
    *   **Circular Icons:** Each task is anchored by a circular icon on the timeline.
    *   **Size Hierarchy:**
        *   **Major Anchors:** Large circles for "Rise and Shine" and "Wind Down" (See Reference Image 1).
        *   **Standard Tasks:** Medium circles for regular to-dos.
        *   **Gaps/Free Time:** Small indicators showing free time blocks.
*   **Time Labels:** Meaningful timestamps displayed to the left of the timeline nodes.

### 3.2 Timeline States (User Flow)
*   **State A: The Fresh Day (Reference Image 1)**
    *   **Scenario:** A new user opens the app or starts a fresh day with no custom tasks.
    *   **Visuals:** A clean, minimal timeline showing only two anchors:
        *   **Start Node:** "Rise and Shine" (08:00 AM) - Pink/Salmon styling.
        *   **End Node:** "Wind Down" (10:00 PM) - Blue/Teal styling.
    *   **Middle Section:** A "Ghost" or "Empty State" indicating potential. Text hint: *"Reflect on the respite"* or *"Plan your day"*.
*   **State B: The Populated Day (Reference Image 2)**
    *   **Scenario:** The user adds tasks.
    *   **Visuals:** The timeline fills up.
        *   "Get Started with Structured/Dayline" (Orange/Coral node).
        *   "Fill Your Inbox" (Light Grey node).
        *   "Make It Your Own" (Settings/Customization node).
    *   **Collision Handling:** Overlapping tasks offset slightly but remain connected to the timeline flow.

---

## 4. Functional Requirements

### 4.1 The Calendar Strip
*   **Location:** Top of the screen.
*   **Features:**
    *   Horizontal scrolling week view.
    *   Current Day highlighting (Orange circle).
    *   **Visual Indicators:** Small icons under dates showing summary of that day (e.g., Clock icon for planned tasks, Moon icon for rest).

### 4.2 Task Management
*   **Creation:**
    *   **FAB (Floating Action Button):** A prominent Orange "+" button at the bottom right.
    *   **Inbox:** A holding area for unscheduled tasks.
*   **Task Attributes:** Title, Duration, Icon, Color, Sub-tasks, Notes.
*   **Interaction:**
    *   **Tap:** Expands to show details (Notes, Sub-tasks).
    *   **Long Press:** Drag and drop to reschedule on the timeline.
    *   **Swipe:** Mark as done or delete.

### 4.3 "Rise & Wind" Anchors
*   Fixed (but adjustable) anchors for the day.
*   **Rise and Shine:** Triggers morning routine suggestions.
*   **Wind Down:** Triggers "Do Not Disturb" mode or reflection prompts.

### 4.4 Advanced "Pro" Features
*   **AI Suggestions:** (Future Scope) suggesting tasks based on gaps.
*   **Focus Timer:** Tapping a task can start a focus timer for that specific block.

---

## 5. Technical Architecture (Android Native)

Following the Modern Android Development (MAD) standards identified in `imagine.txt`.

*   **Language:** Kotlin.
*   **UI Framework:** **Jetpack Compose**. This is non-negotiable for the complex drawing required for the timeline and custom animations.
*   **Architecture:** MVVM (Model-View-ViewModel).
*   **Data Persistence:** Room Database (Offline first).
*   **Dependency Injection:** Hilt.
*   **Asynchronous Processing:** Kotlin Coroutines & Flow.

---

## 6. Implementation Stages

### Phase 1: Foundation
*   Setup Project (Gradle, Hilt, Room).
*   Build the **Design System** (Colors, Typography, Reusable Timeline Components).
*   Implement the **Home Screen** with the Calendar Strip and Empty Timeline State.

### Phase 2: Core Timeline Logic
*   Develop the **Custom Canvas Drawing** for the connected dotted line.
*   Implement Task CRUD (Create, Read, Update, Delete).
*   Implement the "Populated State" logic (rendering tasks dynamically between Anchors).

### Phase 3: Polish & Animation
*   Add entry animations for tasks.
*   Implement the "Drag and Drop" reordering.
*   Finalize the "Glassmorphic" details and themes.

---

**Note to Developer:** The timeline is not a standard RecyclerView. It requires a custom Layout approach in Compose to draw the connecting lines dynamically based on task states.
