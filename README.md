[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=21900992&assignment_repo_type=AssignmentRepo)
# Blocky

## Learning goals
By the end of this assignment, you should be able to:

+ Model hierarchical data using a quadtree.  
+ Implement array-based tree node representations
+ Implement recursive operations on trees (both non-mutating and mutating)
+ Convert a tree into a flat, two-dimensional structure
+ Use inheritance to design classes according to a common interface, and apply polymorphism to create flexible, extensible class hierarchies.

## Introduction: the Blocky game
Blocky is a game with simple moves on a simple structure, but like a Rubik’s Cube, it is quite challenging to play. The game is played on a randomly-generated game board made of squares of four different colors, such as this:<br>
<image width= 300, height=325 src="Images/Blocky _Levels5.jpg">

Each player has their own goal that they are working towards, such as creating the largest connected “blob” of blue. After each move, the player sees their score, determined by how well they have achieved their goal. The game continues for a certain number of turns, and the player with the highest score at the end is the winner.

Now let’s look in more detail at the rules of the game and the different ways it can be configured for play.

### The Blocky board

This game uses a data structure called a Quadtree. This is the same technology used to optimize collision detection in Fortnite and load map data in Google Maps.

We call the game board a ‘block’. It is best defined recursively. A block is either:

- a square of one color, or
- a square that is subdivided into 4 equal-sized blocks.
The largest block of all, containing the whole structure, is called the top-level block. We say that the top-level block is at level 0. If the top-level block is subdivided, we say that its four sub-blocks are at level 1. More generally, if a block at level k is subdivided, its four sub-blocks are at level k+1.

A Blocky board has a maximum allowed depth, which is the number of levels down it can go. A board with maximum allowed depth 0 would not be fun to play on – it couldn’t be subdivided beyond the top level, meaning that it would be of one solid color. This board was generated with maximum depth 5:<br>
<image width= 300, height=325 src="Images/Blocky _Levels5.jpg"><br>
For scoring, the units of measure are squares the size of the blocks at the maximum allowed depth. We will call these blocks unit cells.

### Choosing a block and levels
The moves that can be made are things like rotating a block. What makes moves interesting is that they can be applied to any block at any level. For example, if the user selects the entire top-level block for this board:
<br>
<image width= 300, height=325 src="Images/beforeRotate.jpg"><br>
and chooses to rotate it counter-clockwise, the resulting board is this:<br>
<image width= 300, height=325 src="Images/afterRotateLevel0.jpg">


### Moves
These are the moves that are allowed on a Blocky board:

- Rotate the selected block either clockwise or counterclockwise
- Swap the 4 sub-blocks within the selected block horizontally or vertically
- “Smash” the selected block: if it is a solid-colored block, give it four new, randomly-generated sub-blocks. Smashing the top-level block is not allowed – that would be creating a whole new game. And smashing a unit cell is also not allowed, since it’s already at the maximum allowed depth.
### Goals and scoring
At the beginning of the game, each player is assigned a randomly-generated goal. There are two types of goal:
<br>
<b>Blob goal.</b><br>
The player must aim for the largest “blob” of a given color c. A blob is a group of connected blocks with the same color. Two blocks are connected if their sides touch; touching corners doesn’t count. The player’s score is the number of unit cells in the largest blob of color c.<br>
<b>Perimeter goal.</b><br>
The player must aim to put the most possible units of a given color c on the outer perimeter of the board. The player’s score is the total number of unit cells of color c that are on the perimeter. There is a premium on corner cells: they count twice towards the score.
Notice that both goals are relative to a particular color. We will call that the target color for the goal.

### Players
The game can be played solitaire (with a single player) or with two or more players. I have built my version to limit to 4 players, but you can modify this if you want.  The game would not likely be fun to play with a very large number of players.

There are three kinds of players, represented by subclasses of the `Player` class:

- `HumanPlayer`: A human player chooses moves based on user input. 
- `SimpleAI`: A computer player that chooses moves randomly. Random players have no limit on their smashes. 
- `SmartAI`: A computer player that chooses moves more intelligently: It generates a set of random moves and, for each, checks what its score would be if it were to make that move. Then it picks the one that yields the best score. 
- *optional:* `SmartAI2`: A computer player that chooses moves more intelligently: The SmartAI2 must look 1 move ahead. It simulates every possible move, calculates the resulting score, and picks the highest one.

### Code Structure
To help you get started, here is a brief overview of the key files:

- **`Block.java`**: The core recursive data structure. You will implement the recursive methods here. Note that children are stored in an array and accessed using the `Quadrant` enum.
- **`Quadrant.java`**: An enum representing the four sub-blocks (UR, UL, LL, LR).
- **`Player.java`**: The abstract base class for all players.
- **`HumanPlayer.java`, `SimpleAI.java`, `SmartAI.java`**: Concrete implementations of the player types.
- **`Game.java`**: Manages the game state, turns, and board.
- **`BlockyMain.java`**: The entry point of the application, handling the GUI setup.

### Configurations of the game
A Blocky game can be configured in several ways:

- Maximum allowed depth.
While the specific color pattern for the board is randomly generated, we control how finely subdivided the squares can be.
- Number and type of players.
There can be any number of players of each type. 
- Number of moves.
A game can be configured to run for any desired number of moves. (A game will end early if any player closes the game window.)


### Possible additions
Improve AI, make a smart computer player.  <br>
New goals:

- maximum of an assigned color on the diagonal
- distributing one color as much as possible. (Even defining the measure for this goal is interesting.)

Add new moves options:
- Unsmash (previous color, majority color, or random)
- Randomly change colors while keeping the structure
- Undo 

## How to Run
To play the game, run the `main` method in `src/ui/BlockyMain.java`.

## How to Test
To test the game, run the `main` method in `src/test/BlockyTest.java`.
