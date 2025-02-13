# 0.4.3
- Removed `exchanger-drop-rate` from the config.json
- Added the exchanger drop when a player completes all 10 missions

# 0.4.2
- Reworked the jar of tips, which has more models as it gets filled!
- Fixed jar of tips not persisting its inventory
- When thrown it will always break
- Click with a currency item to introduce it
- Drill, harvest and saw missions are assignOnlyOnce, although this feature is broken for rerolled missions
- Recolated the info, reward and close button in the missions gui

# 0.4.1
- Fix dupe bug with the Mechanical Exchanger in Forge side

# 0.4
## *IMPORTANT* Updating from 0.3.6 or lower:
- Move your `mods/missions/missions_data.nbt` to `config/missions/missions_data.nbt`, although losing this is not that bad
- Delete `config/missions/config.json`'

## Changelog
- All distance missions are now in meters (must be rebalanced in your configs)
- Fix farmer delight foods not counting for Eat mission in Fabric
- Delete drink mission, add the drinks in the Eat mission too
- Only show "Click to reroll" if mission not completed
- Quilt support
- Fix stats resetting upon server restart
- Added new config format, version 2. This makes missions to have different tiers or types inside the same mission type.
- Removed tag support and the new added tags by this mod, there's nothing regarding tags in the mod
- Updated logo
- Fix keybind not working in Forge
- Now the reward range is decided when a mission is assigned, not when it's completed
- Added support for Track placement in Place missions. Supports SnR tracks, any type of track counts for create:tracks item
- Added support for the ghost placement helper (cogs, shafts, sails, etc) in Place missions
- Fixed a crash when rerolling a mission
- Added formatting to the action number in the mission tooltip (1,000 instead of 1000)
- Now missions can be reassigned weekly or daily, changing it in the config.json

# 0.3.5
- Fix Stats (by TurnDev)

# 0.3.4
- Fix forge not loading
- Fix lots of jar issues
- Added a new texture for the Jar of Tips (Made by Seboso)
- 
# 0.3.3
## IMPORTANT: Update from 0.3.2 or lower
- Delete `config/missions/currency.json`
## Changelog
- Fix #32: Certain modded items not rendering in Reroll Button
- Added Relocate Mission type
- Added Vanilla stats for: Easy, normal and hard total completed missions
- Added stats as tooltip of InfoButton
- Added vanilla stat for missions rerolled
- Added FeedMission
- Fix Mechanical Exchanger item model
- Fix Mission Complete sound being stuck to a block position
- New block: Jar of Tips!
- Reworked currency.json, and removed "exchange-rate" config option: It's all configured per currency in the currency.json now!
- Removed Create deco stuff, and added Create: Numismatics configs for possible default currency.json
- Fix #36: Mission reassignment makes all 10 missions of the same type
- Fix #29: missions_data.nbt now gets saved when world autosaves
- Added BeltWalkMission
- Added #farmersdelight:drinks tag
- Mexican translation by TheLegendofSaram
- Fix players being kicked when they had a mission that no longer was on missions.json
- Fix missions of the same type as the one rerolled losing their progress

# 0.3.2
- Fixed some dedicated servers not saving properly the mission data
- Now train, dive, vote and statistic-based missions can only appear once
- Deleted drinks from Eating mission, and created a new Drinking mission.
- Added new tags: `#railways:tracks`, `#railways:wide_tracks`, `#railways:narrow_tracks`, `#c:drinks`,`#brewinandchewin:drinks`, `#createdeco:catwalks`, `#createdeco:ladders`, `#create:ladders`, `#createdeco:hulls`, `#createdeco:bars`, `#createdeco:coins`, `#create:bars` and `#create:scaffoldings`
- Added support for tags in missions.json
- Added blockstates to Exchanger mode, and colors to the nixies
- Added support for Weights in `missions.json`
- Added a Ponder for the Mechanical Exchanger
- Now the Exchanger inventory is compatible with Chutes and Funnels
- Update fr_fr.json by __Tesseract
- Other minor fixes

# 0.2.7
- Added compat with Create: Missions' addons
- Deleted fan missions

# 0.2.6
- Fix crash when onDismount(), when dismounting a train

# 0.2.5
- (1.19) Fix Mechanical Exchanger GUI texture
- (1.20 Forge) Fix patch F compat

# 0.2.4
- Fix Mechanical Exchanger shaft not updating upon place
- Fix mission menu tooltips bugging when pressing buttons
- (Fabric) /mission command is now recognized as a command

# 0.2.3
- (Fabric) Fix server side crash upon load

# 0.2.2
- Exchange ATM -> Mechanical Exchanger
- Added /missions -> Opens Missions Menu
- Fixed compat with Create: Dreams and Desires (and any other Create addon that adds Fan Processing Types)
- Added a Creative Tab so the Mechanical Exchanger is more accessible

# 0.2.1
- the missions_data.nbt is now located in mods/missions folder, instead of missions/ as previously. So you need to move your data to the new location
- Same happens with the configs, move them to configs/missions/ folder
- Fixed ATM crash (Fabric)
- Solved client side crash when dismounting a train

# 0.2
- Fix mechanical crafter crash upon loading

# 0.1.6
- Fixed Steam n Rails not being able to load with missions mod installed

# 0.1.5
- Added Drill and saw missions
- Added Create Deco compat: if you load the mod for the first time, a missions.json and currency.json for createdeco will be created

# 0.1.4
- Fix forge version (for real)
- Fixed Mechanical Crafter compat for the Craft mission in forge side
- Added "Dive in [] for [] min with a Diving Helmet" missions
- Various fixes to train missions

# 0.1.3a
- Fix claiming rewards (lol sorry)
- Fix many train missions issues/ exploits

# 0.1.3
- Added Train Driver and Train Passenger for X km missions
- Added Train driver with at least X passengers for Y km mission

# 0.1.2b
- Fixed Server startup (Forge)
- Improved some tooltips

# 0.1.2a
- Improved Killing mission tooltip
- Fixed various tooltip issues
- Added "exchange-rate" to the config.json, letting you edit the ATM exchange rate.
- Added a Missions title in the gui

# 0.1.2
- Added a message when a player's missions get reassigned
- Improved mission tooltips and added a temporary tooltip for the info button
- Changed certain missions from Difficulty category

# 0.1.1.e
- Fixed 2 wrongly typed item id's in the example config
- Made swimming mission easier
- Added Flying mission and Sailing mission
- Fixed Mission Reroll gui, just click a mission to reroll it by a price
