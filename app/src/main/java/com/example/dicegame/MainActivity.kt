package com.example.dicegame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }  //  State variable to control dialog
    var showDifficultyDialog by remember { mutableStateOf(false) }

    // Background Gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A11CB), Color(0xFF000000)) // Ends in solid black
    )

    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dice Game 🎲",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black, offset = Offset(4f, 4f), blurRadius = 8f
                    )
                ),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // New Game Button
            Button(
                onClick = {
                    showDifficultyDialog = true //  Show the dialog, don't launch activity yet
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(50.dp))
                    .padding(vertical = 8.dp)
            ) {
                Text("New Game", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            if (showDifficultyDialog) {
                AlertDialog(
                    onDismissRequest = { showDifficultyDialog = false },
                    title = {
                        Text("Select Difficulty", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    },
                    text = {
                        Text("Choose the difficulty level to start the game.")
                    },
                    confirmButton = {
                        Button(onClick = {
                            showDifficultyDialog = false
                            context.startActivity(Intent(context, GameActivity::class.java))
                        },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB)),
                        ) {
                            Text("Easy Level", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDifficultyDialog = false
                            context.startActivity(Intent(context, HardGameActivity::class.java))
                        },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB)),
                        ) {
                            Text("Hard Level", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            //  About Button (Fixed)
            Button(
                onClick = { showDialog = true },  //  Just update the state
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(50.dp))
                    .padding(vertical = 8.dp)
            ) {
                Text("About", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    //  Show the dialog when `showDialog` is true
    if (showDialog) {
        AboutDialog(onDismiss = { showDialog = false })  // Close dialog when dismissed
    }
}

//  Use a Composable function for the About Dialog
@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6A11CB), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "About",
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        },

        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Name: Erandi Perera",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Student ID: 20231149",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        },

        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB)),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(45.dp)
                ) {
                    Text("OK", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    )
}






