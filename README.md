# 🎲 Dice Game – A Strategic Two-Player Dice Rolling Game
A turn-based Android game developed using Jetpack Compose and Kotlin, where a human player competes against a computer opponent. The game implements two difficulty levels—Easy and Hard, with the Hard mode featuring an advanced AI strategy.

## Project Overview
- Game Type: Turn-based dice game

- Technology Used: Jetpack Compose, Kotlin

- Key Features: Customizable score target, AI-powered computer opponent, engaging UI

Each feature of the game is carefully designed to create a smooth and interactive experience for the player.

## Game Features
Dice Rolling Mechanism
- Players take turns rolling 5 dice per turn.
- Each player has the option to re-roll up to 2 times to improve their dice combination.

Score Calculation & Win Condition
- Players accumulate scores based on dice values.
- The first player to reach or exceed the target score wins the game.
- If both players reach the target in the same number of turns, a tie-breaker round is triggered, where each player re-rolls until a winner is determined.

Two Difficulty Levels
- **Easy Mode**: The computer rolls and keeps the best possible dice but does not optimize rerolls.

- **Hard Mode**: The computer implements a smart AI strategy, deciding when to reroll dice based on the player's score gap and maximizing its chances of winning.

Interactive User Interface
- The game board features a modern, minimalist UI with Jetpack Compose.
- The player's and computer's dice are visually displayed in separate purple-bordered containers.
- A pop-up dialog allows players to choose the difficulty level before starting a game.

Navigation & Game Flow
- The player can return to the main screen only by pressing the Android back button.
- Once a game is won, further play is disabled until a new game is started.
- The New Game button launches a difficulty selection popup.

## Technology Stack
**Kotlin** – Primary programming language

**Jetpack Compose** – UI framework (No XML used)

**Android Activities & Intents** – For game navigation

**State Management** – Handling dice values, scores, and rerolls

### Computer Player Strategy (Hard Mode)
In Hard Mode, the computer makes intelligent reroll decisions based on the following logic:

- If its total score is significantly lower than the human player's, it prioritizes rerolling low-value dice.
- If the score difference is small, the AI optimizes rerolls to maintain a lead.
- The AI never rerolls a high-value dice combination unless strategically necessary.

This strategy ensures a more challenging and competitive gameplay experience.

