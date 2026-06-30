<p align="center">
  <img width="200" src="https://kryeit.com/images/missions_logo.png">
</p>


<h1 align="center">Create: Missions  <br>
	<a href="https://www.curseforge.com/minecraft/mc-mods/missions/files"><img src="https://cf.way2muchnoise.eu/versions/missions.svg" alt="Supported Versions"></a>
	<a href="https://github.com/Kryeit/Missions/LICENSE"><img src="https://img.shields.io/github/license/Creators-of-Create/Create?style=flat&color=900c3f" alt="License"></a>
	<a href="https://discord.gg/3Mq6E2tRBU"><img src="https://img.shields.io/discord/1100446990590034041?color=5865f2&label=Discord&style=flat" alt="Discord"></a>
	<a href="https://www.curseforge.com/minecraft/mc-mods/missions"><img src="http://cf.way2muchnoise.eu/missions.svg" alt="CF"></a>
    <a href="https://modrinth.com/mod/missions"><img src="https://img.shields.io/modrinth/dt/missions?logo=modrinth&label=&suffix=%20&style=flat&color=242629&labelColor=5ca424&logoColor=1c1c1c" alt="Modrinth"></a>
    <br>
</h1>

Create: Missions is a Create addon focused for Create mod servers. It will also work in singleplayers worlds, but it's designed to bring an economy throughout a server's lifetime. We hope to give server owners a better way of giving players a currency, while also encouraging players to build their own shop contraptions/exchange banks/banks.

# What's this addon?
It works by giving 10 random missions per player per week. These missions can be vanilla missions like "Break X Veridium" or Create mod related like "Drive a train for 4 km with at least 2 passengers". You can also reroll the missions, and claim prices by completing them.

**Open the missions GUI with `/missions`. Convert coins with `/missions exchange`.**

> **This branch targets NeoForge 1.21.1 and is fully server-sided.** It adds no blocks, items, keybinds or client code: everything happens through the `/missions` command and basic vanilla inventory GUIs, so **vanilla clients can join without installing anything**. Create (NeoForge) is required on the server. Optional integrations: Numismatics (currency) and LuckPerms (per-player free rerolls via the `missions.freerolls` permission).

# Mission types:
As time goes on, more missions will be added. Some missions can have lots of variation like breaking a block, while others are simpler like walking.

There are in total 16 Mission Types that use Create-related mechanics.

cut, compact, sail, craft, ride, harvester, train-driver-passenger, crush, eat, train-passenger, place, dive, press, mix, train-driver, break, saw, kill, drill, drink, mill, minecart, fly, relocate, fish, walk, swim, feed, belt-walk

# Special thanks to:
- TheLegendofSaram, Mexican translation
- __Tesseract, French translation
- MrRedRhino, German translation
- глебикс, Russian translation
- Starlotte, mission complete sound
- Seboso, Jar of Tips texture

## Needed for server owners
<details>
<summary>Spoilers</summary>

Select the currencies and their exchange in `config\missions\currency.json`

As an example with [Create: Numismatics](https://modrinth.com/mod/create-numismatics) it would look like this:

```json
[
  {"numismatics:spur": "8"},
  {"numismatics:bevel": "2"},
  {"numismatics:sprocket": "4"},
  {"numismatics:cog": "8"},
  {"numismatics:crown": "8"},
  {"numismatics:sun": "1"}
]
```

<details>
<summary>missions.json -> How to configure the missions? </summary>
This file can be found in `config\missions\missions.json`.
An example mission configuration:

```json
{
  "place": {
    "reward": {
      "amount": "2-23",
      "item": "numismatics:bevel"
    },
    "weight": 0.7,
    "missions": {
      "create:track_signal": "20-50",
      "#minecraft:logs": "20-50"
    },
    "titles": [
      "Example title"
    ]
  }
}
```

Ranges like 2-23 mean a number at random from 2 to 23, both included.
Mission example: Place 35 Acacia Log(s)
Reward example: 2-23 Iron Coin(s)

Action number is determined when you RECEIVE the mission, and rewards are determined when you COMPLETE the mission.

The "weight" is the chance of this mission to be selected. From 0 to 1.

You can add as many item id's to the "missions" bracket, and add as many titles to "titles" bracket, for a mission to be granted one item and one title, both randomly from those inside the bracket.

</details>
<details>
<summary>Coin Exchange</summary>

Run `/missions exchange` (or click the Coin Exchange button in the `/missions` GUI) to open a basic
inventory GUI. Put coins in the input slot and click **▲ Combine to larger** / **▼ Split to smaller**
to convert between denominations (e.g. 16 iron ⇄ 1 gold, using the rates from `currency.json`).
Anything you placed is returned to you when the GUI closes.

This replaces the old Mechanical Exchanger block, which required client-side rendering and so cannot
exist on a server-only mod.
</details>

<details>
<summary>All-missions bonus</summary>

When a player completes all 10 of their active missions, they receive a bonus item. Configure it in
`config\missions\config.json` via `all-missions-reward` (default `minecraft:diamond_block`).
</details>
</details>

# For developers
It's really possible to add new missions for Create: Missions. You have examples here
- [Create: Missions Template Addon (Fabric)](https://github.com/muriplz/missions-fabric-example-addon)
- [VoteMission](https://modrinth.com/mod/votemission)
- [TelepostMissions](https://modrinth.com/mod/telepostmissions)
