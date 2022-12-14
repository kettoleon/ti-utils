# Terra Invicta Utils
This project contains a set of utilities that might help during our Terra Invicta games.

## Motivation
As the game is still in early access and unpolished, there are a few things in the UI that makes it hard
to check certain things while you are playing. You might need to make many clicks to switch from one panel that
shows info to another, or the information is not displayed in a way that helps comparisons or helps you take decisions.

So as I realised the game stores an autosave file every turn, I figured I could write a dashboard that shows
on a secondary screen and eases the process of decision-making while playing.

## Disclaimer
This project is in its very early stages and experimental. I keep building and redesigning while I play the game.
It also has some harcoded information that is coupled to my playstyle. 
If you happen to stumble into this project and want to try it, any suggestions from a different point of view are welcome :).

## Modules so far
* metadata: Allows to read metadata from the game's folder. Not all info is stored in the save file.
* saves: Allows to read information from savefiles, and monitor the savefile folder for changes.
* dashboard: Barebones swing ui with the dashboard, alerts, tables, etc. Planning to move it to a nicer web ui eventually.
