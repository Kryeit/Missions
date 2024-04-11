# 0.3.2
- Now train, dive, vote and statistic-based missions can only appear once
- Deleted drinks from Eating mission, and created a new Drinking mission.
- Added new tags, `#c:drinks` and `#brewinandchewin:drinks`
- Added support for tags in missions.json

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
