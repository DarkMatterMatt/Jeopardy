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

**Option 1: How to run Quinzical with the script file**\
Move into the folder that contains all of the folders/files above, and run the script. To do this, follow steps below:

Assuming the folder 'assignment-3-and-project-team-24' is visible at your current directory, type:\
    `cd assignment-3-and-project-team-24`\
to move into the folder. Then type:\
    `chmod 777 quinzical.sh`\
to change the permission of the script to executable. Then lastly, type:\
    `./script.sh`\
which will run the program.\

**Option 2: How to run Quinzical directly from the runnable jar**\
Change current directory of bash to the folder containing the required files/folders. You can run the .jar file with this command on bash: `java --module-path ./javafx-sdk-11.0.2/lib --add-modules javafx.controls -jar ./quinzical.jar`

## Features
- Our Quinzical application features _40+ NZ related questions, 7 categories_. That's huge!
- If your system has Festival, it will pronounce questions with appropriate NZ accent, including Maori macrons.
- Unlike our previous application, _Jeopardy_, our brand-new application comes with __Practice module__! Here you can get comfortable with our application interface, try out some questions before attempting the __Main module__.
- We also have option to disable quiz for those who want to improve listening ability, or want to put yourself into a challenge (why would you?).
- We have a speed slider for the clue voiceover!
- Want to start over during the quiz, or have to suddenly exit the game for a family dinner? We got you covered :) Exit at any screen and you will land on the exact page or question you were at last time.

## Two Modules
There are two main modules in this game.

**Practice Module**\
You can attempt any of the categories and questions available from the supplied set of questions and categories! Press any category on the left, and you will get a random question selected from that category.\
Rules:\
- You can attempt the question three times.
- The category will display the same question until you have attempted it 3 times, have correctly answered it, or pressed skip.
- You can skip question any time.
- You will not earn any money in this module.
- Hint will be displayed at the third attempt; the hint given is the first letter of one of the acceptable answers.

**Main Module**\
You can 

## How to navigate around the game
// with screenshot?

## Taskbar icons explanation
// can look at tool tips

## Customize questions
// reads files from ./categories folder
// format is <Question>|<Answer>|<Answer2>...
