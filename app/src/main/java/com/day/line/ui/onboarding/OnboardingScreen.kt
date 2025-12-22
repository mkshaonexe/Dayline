package com.day.line.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.day.line.ui.theme.DaylineOrange
import com.day.line.ui.theme.GlassWhite
import com.day.line.ui.theme.NeonOrange
import com.day.line.ui.theme.SoftTeal
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true // Allow swiping? Maybe restrict last page if name empty? Let's allow for now.
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Welcome to Dayline",
                    description = "Refine your day, track your journey, and see your progress unfold. It's time to take control.",
                    icon = Icons.Rounded.AutoAwesome,
                    backgroundColor = MaterialTheme.colorScheme.surface
                )
                1 -> OnboardingPage(
                    title = "Timeline & Notifications",
                    description = "Add your daily timeline, stay on track, and get timely notifications. Never miss a beat again.",
                    icon = Icons.Default.AccessTime,
                    backgroundColor = MaterialTheme.colorScheme.surface
                )
                2 -> OnboardingPage(
                    title = "Journal & Progress",
                    description = "Reflect on your day. Journal your thoughts and watch your personal growth stats climb.",
                    icon = Icons.Default.Book,
                    backgroundColor = MaterialTheme.colorScheme.surface
                )
                3 -> MisoIntroPage(
                    userName = userName,
                    onNameChange = { userName = it },
                    onFinish = {
                        if (userName.isNotBlank()) {
                            viewModel.completeOnboarding(userName)
                            onFinishOnboarding()
                        }
                    }
                )
            }
        }

        // Bottom Navigation (Indicators & Buttons)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Page Indicators
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { iteration ->
                    val color = if (pagerState.currentPage == iteration) DaylineOrange else Color.LightGray
                    val width by animateFloatAsState(if (pagerState.currentPage == iteration) 24f else 8f, label = "dotWidth")
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            // Next / Finish Button
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < 3) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            if (userName.isNotBlank()) {
                                viewModel.completeOnboarding(userName)
                                onFinishOnboarding()
                            }
                        }
                    }
                },
                containerColor = DaylineOrange,
                contentColor = Color.White,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = if (pagerState.currentPage == 3) Icons.Default.Check else Icons.Default.ArrowForward,
                    contentDescription = if (pagerState.currentPage == 3) "Get Started" else "Next"
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(
    title: String,
    description: String,
    icon: ImageVector,
    backgroundColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NeonOrange.copy(alpha = 0.2f), DaylineOrange.copy(alpha = 0.1f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = DaylineOrange
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(100.dp)) // Spacing for bottom nav
    }
}

@Composable
fun MisoIntroPage(
    userName: String,
    onNameChange: (String) -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Miso Avatar / Icon
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(SoftTeal.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for Miso - maybe a cute robot or face? Using AutoAwesome for now.
            Icon(
                imageVector = Icons.Rounded.AutoAwesome, 
                contentDescription = "Miso",
                modifier = Modifier.size(80.dp),
                tint = SoftTeal
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Hi, I'm Miso!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your Dayline assistant. I'm here to help, support, and occasionally roast your schedule (with love).",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "What should I call you?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = onNameChange,
            placeholder = { Text("Your Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DaylineOrange,
                cursorColor = DaylineOrange
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onFinish() }
            )
        )
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}
