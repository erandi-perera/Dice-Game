package com.example.dicegame

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class HardGameActivity : ComponentActivity() {
    companion object {
        var totalHumanWins = 0
        var totalComputerWins = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HardGameScreen(
                onWin = { isHuman ->
                    if (isHuman) totalHumanWins++ else totalComputerWins++
                },
                getWinCounts = { totalHumanWins to totalComputerWins }
            )
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun HardGameScreen(
    onWin: (isHuman: Boolean) -> Unit,
    getWinCounts: () -> Pair<Int, Int>
) {
    val (totalHumanWins, totalComputerWins) = getWinCounts()

    val selectedDiceSaver = Saver<MutableSet<Int>, List<Int>>(
        save = { it.toList() },
        restore = { it.toMutableSet() }
    )

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A11CB), Color(0xFF000000))
    )

    var humanDice by rememberSaveable { mutableStateOf(List(5) { Random.nextInt(1, 7) }) }
    var computerDice by rememberSaveable { mutableStateOf(List(5) { Random.nextInt(1, 7) }) }

    var humanScore by rememberSaveable { mutableStateOf(0) }
    var computerScore by rememberSaveable { mutableStateOf(0) }

    var rollCount by rememberSaveable { mutableStateOf(0) }
    var rerollCount by rememberSaveable { mutableStateOf(0) }

    var selectedDice by rememberSaveable(stateSaver = selectedDiceSaver) {
        mutableStateOf(mutableSetOf<Int>())
    }
    var showRerollButton by rememberSaveable { mutableStateOf(false) }
    var isScoreButtonEnabled by rememberSaveable { mutableStateOf(false) }

    var showGameOverDialog by rememberSaveable { mutableStateOf(false) }
    var winnerText by rememberSaveable { mutableStateOf("") }
    var winnerColor by remember { mutableStateOf(Color.White) }
    var gameOver by rememberSaveable { mutableStateOf(false) }

    var isTieBreaker by rememberSaveable { mutableStateOf(false) }
    var showTieBreakerDialog by rememberSaveable { mutableStateOf(false) }

    var humanAttempts by rememberSaveable { mutableStateOf(0) }
    var computerAttempts by rememberSaveable { mutableStateOf(0) }

    var targetScoreText by rememberSaveable { mutableStateOf("") }
    val targetScore = targetScoreText.toIntOrNull() ?: 101

    val activity = (LocalContext.current as? ComponentActivity)

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
            Text("Dice Game 🎲", fontSize = 32.sp,fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    text = "H:$totalHumanWins / C:$totalComputerWins",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            OutlinedTextField(
                value = targetScoreText,
                onValueChange = { targetScoreText = it.filter { ch -> ch.isDigit()} },
                label = { Text("Target Score (default 101)", color = Color.White) },
                placeholder = { Text("Enter the Target Score", color = Color.White.copy(alpha = 0.5f)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                enabled = rollCount == 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )


            if (isTieBreaker) {
                Text("Tie Breaker Round!", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    text = "HScore: $humanScore  /  CScore: $computerScore",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            Text("Human", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x886A11CB), RoundedCornerShape(12.dp)) // Transparent purple container
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    humanDice.forEachIndexed { index, dice ->
                        DiceImage(
                            diceValue = dice,
                            isSelected = selectedDice.contains(index),
                            onClick = {
                                if (selectedDice.contains(index)) selectedDice.remove(index)
                                else selectedDice.add(index)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            if (showRerollButton && rerollCount < 2 && !isTieBreaker) {
                Text("Tap on dice to keep", fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
                Button(
                    onClick = {
                        humanDice = humanDice.mapIndexed { index, dice ->
                            if (selectedDice.contains(index)) dice else Random.nextInt(1, 7)
                        }
                        rerollCount++
                        if (rerollCount == 2) {
                            updateScores(humanDice.sum(), computerDice.sum(),
                                onHumanScoreUpdated = { humanScore += it },
                                onComputerScoreUpdated = { computerScore += it }
                            )
                            humanAttempts++
                            computerAttempts++
                            checkForWinner(humanScore, computerScore, humanAttempts, computerAttempts, targetScore,
                                onWin = {
                                    winnerText = it.first
                                    winnerColor = it.second
                                    showGameOverDialog = true
                                    gameOver = true
                                    if (it.first == "You win!") onWin(true) else onWin(false)

                                },
                                onTie = {
                                    isTieBreaker = true
                                    showTieBreakerDialog = true
                                }
                            )
                            rollCount = 0
                            rerollCount = 0
                            selectedDice.clear()
                            showRerollButton = false
                            isScoreButtonEnabled = false
                        }
                    },
                    enabled = !gameOver,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFF00)),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.height(60.dp).padding(8.dp)
                ) {
                    Text("Reroll (${2 - rerollCount} left)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF000000))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Computer", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x886A11CB), RoundedCornerShape(12.dp)) // Transparent purple container
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    computerDice.forEach { dice -> DiceImage(dice) }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        humanDice = List(5) { Random.nextInt(1, 7) }
                        computerDice = List(5) { Random.nextInt(1, 7) }
                        rollCount = 1
                        rerollCount = 0
                        selectedDice.clear()
                        showRerollButton = !isTieBreaker
                        isScoreButtonEnabled = true
                    },
                    enabled = !gameOver,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A11CB),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF808080),
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.weight(1f).height(60.dp).padding(4.dp)
                ) {
                    Text("Throw", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (isTieBreaker) {
                            val hTotal = humanDice.sum()
                            val cTotal = computerDice.sum()
                            if (hTotal > cTotal) {
                                winnerText = "You win!"
                                winnerColor = Color(0xFF009845)
                                showGameOverDialog = true
                                gameOver = true
                                onWin(true)
                            } else if (cTotal > hTotal) {
                                winnerText = "You lose"
                                winnerColor = Color(0xFFD50000)
                                showGameOverDialog = true
                                gameOver = true
                                onWin(false)
                            }
                        } else {
                            repeat(3 - rollCount) {
                                computerDice = applyEfficientStrategy(computerDice, humanScore, computerScore)
                            }
                            updateScores(humanDice.sum(), computerDice.sum(),
                                onHumanScoreUpdated = { humanScore += it },
                                onComputerScoreUpdated = { computerScore += it }
                            )
                            humanAttempts++
                            computerAttempts++
                            checkForWinner(humanScore, computerScore, humanAttempts, computerAttempts, targetScore,
                                onWin = {
                                    winnerText = it.first
                                    winnerColor = it.second
                                    showGameOverDialog = true
                                    gameOver = true
                                    if (it.first == "You win!") onWin(true) else onWin(false)

                                },
                                onTie = {
                                    isTieBreaker = true
                                    showTieBreakerDialog = true
                                }
                            )
                        }
                        rollCount = 0
                        rerollCount = 0
                        selectedDice.clear()
                        showRerollButton = false
                        isScoreButtonEnabled = false
                    },
                    enabled = isScoreButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A11CB),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF808080),
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.weight(1f).height(60.dp).padding(4.dp)
                ) {
                    Text("Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showGameOverDialog) {
            AlertDialog(
                onDismissRequest = {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                },
                confirmButton = {},
                title = {
                    Text(
                        text = winnerText,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = winnerColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "Press the Android back button to return to the main menu.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.padding(16.dp)
            )
        }

        if (showTieBreakerDialog) {
            AlertDialog(
                onDismissRequest = { showTieBreakerDialog = false },
                confirmButton = {
                    Button(onClick = { showTieBreakerDialog = false }) {
                        Text("OK")
                    }
                },
                title = {
                    Text(
                        text = "Tie Breaker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFCC9A00),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "Both players reached the target with the same score and attempts! Keep using the Throw and Score buttons — no rerolls allowed. Highest roll wins!",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify
                    )
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * Smart Computer Strategy
 *
 * Strategy Explanation:
 * - The computer player cannot see human dice, but can see both scores.
 * - The goal is to increase the likelihood of beating the human in the long run by rerolling low dice.
 *
 * Strategy Logic:
 * - If the computer's current score is lower than the human's by more than 10, it's in a risky position, so reroll all dice below 5.
 * - If scores are close (within 10 points), reroll dice below 4.
 * - If computer is leading, be conservative: reroll only dice below 3.
 * - Max 2 rerolls per turn are allowed, handled by repeat loop where this function is called.
 *
 * Advantages:
 *      Simple and efficient — no unnecessary rerolls.
 *      Risk-aware strategy adapts based on score gap.
 *      Avoids rerolling high-value dice (5 or 6).
 *
 * Disadvantages:
 *      Doesn’t guarantee best possible combination (e.g., full house, etc.)
 *      Doesn’t account for dice combinations (just raw totals).
 */
fun applyEfficientStrategy(dice: List<Int>, humanScore: Int, computerScore: Int): List<Int> {
    val scoreDifference = computerScore - humanScore
    val rerollThreshold = when {
        scoreDifference < -10 -> 5 // Reroll dice < 5 when losing badly
        scoreDifference in -10..10 -> 4 // Reroll dice < 4 when close
        else -> 3 // Reroll dice < 3 when leading
    }

    return dice.map { if (it < rerollThreshold) Random.nextInt(1, 7) else it }
}
/*
===============================
Assumptions & Enhancements
===============================

7. Efficient Computer Strategy (optional for hard mode)
   - In HardGameActivity, the computer rerolls dice based on game state (score difference).
   - Reroll threshold adapts based on how far behind/ahead the computer is.

*/



