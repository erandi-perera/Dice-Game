package com.example.dicegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// Shared utility function to update both scores

@Composable
fun DiceImage(diceValue: Int, isSelected: Boolean = false, onClick: (() -> Unit)? = null) {
    Image(
        painter = painterResource(id = getDiceDrawable(diceValue)),
        contentDescription = "Dice $diceValue",
        modifier = Modifier
            .size(50.dp)
            .border(2.dp, if (isSelected) Color.Yellow else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { onClick?.invoke() }
    )
}

fun getDiceDrawable(value: Int): Int {
    return when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
}

fun updateScores(humanTotal: Int, computerTotal: Int, onHumanScoreUpdated: (Int) -> Unit, onComputerScoreUpdated: (Int) -> Unit) {
    onHumanScoreUpdated(humanTotal)
    onComputerScoreUpdated(computerTotal)
}

fun checkForWinner(
    humanScore: Int,
    computerScore: Int,
    humanAttempts: Int,
    computerAttempts: Int,
    targetScore: Int,
    onWin: (Pair<String, Color>) -> Unit,
    onTie: () -> Unit
) {
    if (humanScore >= targetScore || computerScore >= targetScore) {
        if (humanScore >= targetScore && computerScore >= targetScore && humanScore == computerScore && humanAttempts == computerAttempts) {
            onTie()
        } else if (humanScore >= targetScore && humanScore > computerScore) {
            onWin("You win!" to Color(0xFF009845))
        } else if (computerScore >= targetScore && computerScore > humanScore) {
            onWin("You lose" to Color(0xFFD50000))
        }
    }
}

