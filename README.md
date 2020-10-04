# Quinzical
JavaFX based quiz application created by Hajin Kim and Matt Moran.

## Aim of the game
The aim of the game is to answer all the questions in the main quiz module and get the highest money possible at the end.

## Requirements
- Java8+
- For quiz clues voiceover: Espeak or Festival
- Bash is required to run the script file that runs the program

## How to run it
Firstly, download the required files from github repository or from SOFTENG206 A3 submission (team 24). You must have the following folders/files in one folder (most likely called 'assignment-3-and-project-team-24'):\
- `quinzical.jar` runnable jar file containing compiled JavaFX quiz application
- `categories` folder containing questions and icon resources
- `javafx-sdk-11.0.2` folder that contains JavaFX libraries
- `quinzical.sh` containing script that runs the application

**Option 1: How to run Quinzical with the script file**
Move into the folder that contains all of the folders/files above, and run the script. To do this, follow steps below:

Assuming the folder 'assignment-3-and-project-team-24' is visible at your current directory, type:\
    `cd assignment-3-and-project-team-24`\
to move into the folder. Then type:\
    `chmod 777 quinzical.sh`\
to change the permission of the script to executable. Then lastly, type:\
    `./script.sh`\
which will run the program.\

**Option 2: How to run Quinzical directly from the runnable jar**
Change current directory of bash to the folder containing the required files/folders. You can run the .jar file with this command on bash: `java --module-path ./javafx-sdk-11.0.2/lib --add-modules javafx.controls -jar ./quinzical.jar`

## Features
// 40+ NZ related questions, 7 categories
// Supports multiple answers
// supports maori voice

## How to navigate around the game
// with screenshot?

## Taskbar icons explanation
// can look at tool tips

## Customize questions
// reads files from ./categories folder
// format is <Question>|<Answer>|<Answer2>...
