# todo.md

  + Quality of life fixes for Html
      - ~~Capture SPACE so it doesn't get captured by the browser~~
      - Show "MENU" button on GameScreen like on mobile as web users are likely using their
        mouse primarily
      
  + Can I work out how to resize screen elements and fonts for fullscreen toggling on desktop?
  
  + Music
  
  + Sound
      - Throw can
      - Can hit
      - Can explode
      - Plant explode?
      - Animal explode?
      
  + Catch cursor on Desktop before doing release build
      - Catching cursor clashes with Android Studio debugging on Linux

## Done
      
  + ~~App icon~~
      - ~~Android~~
      - ~~Desktop~~
      - ~~Html~~
      - ~~Ios~~
      
  + ~~Display game in phone cutout areas~~
      - ~~But don't display UI here~~
      - ~~Set padding at top of Scene2d.ui tables to size of Gdx.graphics.getSafeInsetTop~~
      - ~~Test Android~~
      - ~~Test iOS~~
          - ~~Offset seems fine on iPhone 11 but not on iPhone 12 Pro Max. Do I care as long as I
            can get my screenshots?~~
          - ~~I'll look up if there's anything I need to know, but as I can get the screenshots I 
            need for 6.5-inch iPhone using iPhone 11 I won't pull my hair out. Crossing my
            fingers that this doesn't come up in App Store review~~

  + ~~**FIXED**: Right banner displaying over game area on iOS in Simulator iPhone 12 Pro Max~~
      - ~~Also on iPad Pro (12.9-inch) (4th generation)~~
      - ~~Appears to be due to incorrect placement of bannerViewport (which is a FillViewport)
        on these devices, but not others~~
      - ~~Drop using FillViewport and draw banners in gameViewport, using game-scale dimension~~
      - ~~Hard code banner width in Constants as this should never change~~
      - ~~Adjust banner height using CoolSodaCan.getGameHeight (even though in cases where
        this is different to Constants.GAME_HEIGHT the banners would not show...)~~
  
  + ~~Do I care about buffer overflow for high scores?~~
      - ~~I have decided not to care~~

  + ~~**FIXED**: AnimatedCan objects which hit soon after being spawned appear to be "smearing"
    their explosion position~~
      - ~~Looks like explosion position is updating after AnimatedCanState changes to INACTIVE
        and explosion is initially drawn~~
      - ~~In GameScreen.render if GameState.ACTIVE update animatedCanArray, gameObjectArray,
        and player *before* inactive/hit checks, drawing~~
          
  + ~~**FIXED**: There is a 1 (?) pixel gap between the side banners and the game area in GameScreen~~
      - ~~Do I care enough to fix?~~
      - ~~Make GameScreen.gameViewport an ExtendViewport so it doesn't cut off abruptly~~
      
  + ~~**FIXED**: What happens if player unlocks two new sodas at the same time? Something messy~~
      - ~~Adding `menuUiTable.clear` to `GameScreen.showSodaUnlocked` appears to have been a fix for
        the weird table alignent issue I has when I stumbled across this bug~~
        
  + ~~**FIXED**: AnimatedCan objects which immediately hit and explode are starting their
    explosion effect at position 0, 0~~
      - ~~This is probably due to explosion.setPosition not being called in AnimatedCan.init which
        means the effect doesn't get a set to the proper position until AnimatedCan.update is
        called~~
      
  + ~~**FIXED**: Trees are exploding soon after Spawn, probably because of Pooling error~~
      - ~~This appears to be resolved by removing off-screen objects from 
        GameScreen.hittableArray immediately before doing the same for GameScreen.gameObjectArray,
        which also frees Poolable objects~~
      
  + ~~Use object pooling~~
      - ~~<https://github.com/libgdx/libgdx/wiki/Memory-management#object-pooling>~~
      - ~~AnimatedCan~~
      - ~~Animal~~
      - ~~Plant~~
      - ~~Grass~~

  + ~~Add option of using spacebar to throw cans~~
      - ~~Pretty annoying to play using laptop trackpad otherwise~~

  + ~~Do Animals need a smiling had-a-can state before exploding?~~
      - ~~They already have one! How did this task get left on here?~~
      
  
  + ~~Consider a more composed method for distributing plants/animals~~
      - ~~I might be overthinking this little toy goof of a game~~
  
  + ~~Re-evaluate how much Y-offset there is from touch point on mobile~~
      - ~~Currently it feels like it might be too much~~
      - ~~Now setting Player x/y to centre of sprite. Has this made a difference?~~
      - ~~I'm actually happy with this right now so no changes to be mad~~
      
  + ~~Does my font have weird spacing on Web?~~
      - ~~No~~
      
  + ~~Pretty print integers on Statistics page~~
      - ~~Sodas thrown~~
      - ~~Sodas drunk~~
      
  + ~~Add two new Animals~~
      - ~~Blue hedgehog~~
      - ~~Yellow rat~~
          
  + ~~Create a tutorial at start of game~~

  + ~~Make unlocked cans do something~~
      - ~~ORANGE: fire three cans at once in a W shape~~
      - ~~SILVER: fire cans three times as quickly as normal~~
      - ~~PURPLE: fire 8 cans at once in a star pattern~~
      - ~~YELLOW: fire cans twice as quickly as SILVER, going round 8 directions creating a spiral~~
  
  + ~~Add score particle to Animal/Plant when points are scored~~
      
  + ~~Title screen~~
      - ~~Cool title graphic~~
      - ~~New game~~
          - ~~Choose from one of five cans~~
          - ~~Cans unlocked via progression~~
      - ~~Statistics~~
          - ~~Number of sodas thrown~~
          - ~~Number of sodas drunk~~
          - ~~High score~~
          - ~~Total points scored~~
          - ~~Animals quenched~~
          - ~~Plants pruned~~
          - ~~Longest session~~
          - ~~Total time played~~
          - ~~Sodas unlocked~~
          - ~~Reset progess button~~
      - ~~Settings~~
          - ~~Music volume~~
          - ~~Sound volume~~
          - ~~Credits~~
      - ~~Calculate button widths and heights to stay the same across devices~~

  + ~~**FIXED** Incorrectly sized spinning soda can in GameScreen.showSodaUnlocked on
    different sized devices~~
      - ~~Move TitleScreen.calculateImageWidth and .calculateImageHeight to public
        CoolSodaCan.calculateImageWidth and .calculateImageHeight~~
      - ~~Use these in GameScreen.showSodaUnlocked to set screen appropriate size of sodaImage~~
  
  + ~~**FIXED** Continuous firing if firing when unlocked soda popup appears~~
      - ~~Set playerIsFiring = false in showSodaUnlocked and showMenu~~
      
  + ~~Make soda can sizes on TitleScreen consistent across devices~~

  + ~~Show big can after soda select and spin and shrink to center to start~~
      - ~~Use an ExtendViewport so the can will show in wider screens~~

  + ~~Unlock sodas during game play~~
      - ~~Check if new unlock has occurred~~
      - ~~Display in popup which pauses game~~
          
  + ~~Can unlock conditions~~
      - ~~ORANGE: Score more than 10,000 points overall~~
      - ~~SILVER: Play one session for longer than 1 minute~~
      - ~~PURPLE: Super-sate 15 guinea pigs~~
      - ~~YELLOW: Destroy 100 plants~~
      
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
  
