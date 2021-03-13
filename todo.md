# todo.md
      
  + Title screen
      - New game
          - Choose from one of five cans
          - Cans unlocked via progression?
      - Statistics
          - Number of sodas thrown
          - Number of sodas drunk
          - Highest score
          - Total points scored
          - Animals sated
              - Horses
              - Guinea pigs
              - Others?
          - Trees destroyed
          - Sodas unlocked
          - Longest session
          - Total time played
      - Settings
          - Music volume
          - Sound volume
          - Credits
  
  + Add score particle to Animal/Plant when points are scored
      
  + Create a tutorial at start of game
          
  + Can unlock ideas
      - Score more than 50,000 points overall
          - Should I calculate this based on fire rate and points per can? This is my guess at 5
            minutes of super-sating animals
      - Play one session for longer than 5 minutes
      - Super-sate 50 guinea pigs
      - Destroy 100 plants
      
  + Add two new Animals
      - Blue hedgehog
      - Yellow rat    
          
  + Does my font have weird spacing on Web?
  
  + Re-evaluate how much Y-offset there is from touch point on mobile
      - Currently it feels like it might be too much
      - Now setting Player x/y to centre of sprite. Has this made a difference?

  + Do Animals need a smiling had-a-can state before exploding?
  
  + Consider a more composed method for distributing plants/animals
      - On a grid?
      - Max number of items per grid square?
      - Rules?
      - Is this procgen?
      
  + Investigate using Pools for
      - AnimatedCan
      - Explosion
      - Animal
      - Plant
      
  + Try to understand display cutouts
          
  + **BUG**: There is a 1 (?) pixel gap between the side banners and the game area in GameScreen
      - Do I care enough to fix?
      - The easy fix is probably just going back to and ExtendViewport
      
  + Can I work out how to resize screen elements and fonts for fullscreen toggling on desktop?
  
  + Do I care about buffer overflow for high scores?

## Done
      
  + ~~**WILL NOT FIX** Launching from app menu of Moto G test device shows a black bar where bottom
    system bar would display. Doesn't seem to when "running" game from Android Studio~~
      - ~~Problem doesn't seem to happen on AVD devices running Android 5.1, 6.0, 10~~ 
      - ~~Problem does not go away when starting with a TitleScreen and changing screens~~
      - ~~Problem appears to be specific to Android 6.0, and looking online suggests it was fixed
        in Android 6.0.1. Unfortunately the Moto G (2nd gen) I'm using never got an upgrade
        to 6.0.1~~

  + ~~Replace animal superhit sprites with smiling sprite~~
      - ~~No violence!~~
      - ~~Animal-Hit can explosion should be soda coloured~~
      - ~~Give Animal a little shake on hit~~
      
  + ~~Implement Statistics class~~
      - ~~Use Preferences~~
      - ~~Loaded on game load in CoolSodaCan.create()~~
      - ~~During GameScreen these are incremented according to game actions~~
      - ~~Saved out to file when exiting GameScreen~~
      - ~~Saved out to file when exiting game~~
      - ~~Cans thrown~~
      - ~~Cans delivered~~
      - ~~High score~~
      - ~~Total points scored~~
      - ~~Longest session~~
      - ~~Total time played~~
      - ~~Animals super hit~~
          - ~~Horses~~
          - ~~Guinea pigs~~
      - ~~Trees super hit~~
      - ~~getters for everything~~

  + ~~Implement game/scoring UI~~
      - ~~Cans thrown~~
      - ~~Cans delivered~~
      - ~~Score~~
      - ~~Time~~
      - ~~Menu button~~
          - ~~ESC (menu) on desktop/html~~
          - ~~"Menu" button on Android/iOS~~
      - ~~Make this look a lot better~~
      
  + ~~Implement in-game menu~~
      - ~~When Menu button/ESC pressed pause game and show menu~~
      - ~~Continue~~
      - ~~Exit~~

  + ~~Test IOSFontLoader and IOSFormatter~~
      - ~~Add localization support to IOS~~

  + ~~Work out nice way to put thousands commas in `scoreLabel` across all platforms~~
      - ~~Formatter interface~~
      - ~~printScore(int score) method~~
      - ~~implement in AndroidFormatter, DesktopFormatter, HtmlFormatter, IOSFormatter~~
      
  + ~~Implement localization~~
      
  + ~~Create destroyed tree sprite~~
      - ~~After you hit a tree with 3 cans~~
          
  + ~~Create Hittable interface~~
      - ~~Implemented by Animal and Plant classes~~
      - ~~Provides getHitBox() function~~
      - ~~Provides hit() function~~
      
  + ~~Create animal objects~~
      - ~~Coco, Horse1, Horse2~~
      - ~~States:~~
          - ~~Normal~~
          - ~~Hit~~
          - ~~Superhit (has exploded, black silhouette)~~
      - ~~All animals: wiggle~~
          - ~~Define hitbox before wiggle starts~~
      - ~~Change state when hit by can~~
          - ~~Superhit state!~~
          
  + ~~Create explosion animation~~
      - ~~Used for trees and animals~~
      - ~~Also a tiny one for cans~~
      - ~~Actually a single ParticleEffect that I modify for different sizes/colours~~

  + ~~Create animated small can for throwing~~
      - ~~Each colour of big can gets its own small can~~
          - ~~Blue can~~
          - ~~Orange can~~
          - ~~Purple can~~
          - ~~Silver can~~
          - ~~Yellow can~~
      - ~~AnimatedCan object that does things~~
          - ~~Update y position based on delta~~
          - ~~New can added to GameScreen only after a time has elapsed~~
          - ~~Only add new can when "firing"~~
      - ~~Create getHitBox() function~~
      - ~~GameScreen uses getHitBox() to check against GameObjects~~
      - ~~Explodes when it hits something Hittable~~
      
  + ~~Change grass colour to differentiate from plants~~

  + ~~Set grass/plant/animal spawn randomisation using Constants~~

  + ~~Try out the different soda colours to see if this game is at all legible~~
      - ~~Add drop shadow to small cans to help stand out~~
      
  + ~~Add animal sprites to the game~~
      - ~~Like grass/trees but more exciting!~~
      - ~~Game scrolls over time~~
      - ~~New animals "randomly" added to the top~~
  
  + ~~Add trees to game~~
      - ~~"Randomly" distributed~~
      - ~~Game scrolls over time~~
      - ~~New trees "randomly" added to the top~~
  
  + ~~Add grass to game~~
      - ~~"Randomly" distributed~~
      - ~~Game scrolls over time~~
      - ~~New grass "randomly" added to the top~~
      
  + ~~The higher on the screen an object is, the "further back" from the camera it is~~
      - ~~Sort gameObjectArray by Y value~~
      - ~~Draw objects in gameObjectArray from highest to lowest Y value~~

  + ~~GameScreen side banners displayed using FillViewport~~
  