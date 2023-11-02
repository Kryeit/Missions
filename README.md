<p align="center">
  <img width="200" src="https://kryeit.com/images/missions_logo.png">
</p>


<h1 align="center">Create: Missions  <br>
	<a href="https://www.curseforge.com/minecraft/mc-mods/missions/files"><img src="https://cf.way2muchnoise.eu/versions/missions.svg" alt="Supported Versions"></a>
	<a href="https://github.com/Kryeit/Missions/LICENSE"><img src="https://img.shields.io/github/license/Creators-of-Create/Create?style=flat&color=900c3f" alt="License"></a>
	<a href="https://discord.gg/3Mq6E2tRBU"><img src="https://img.shields.io/discord/1100446990590034041?color=5865f2&label=Discord&style=flat" alt="Discord"></a>
	<a href="https://www.curseforge.com/minecraft/mc-mods/missions"><img src="http://cf.way2muchnoise.eu/missions.svg" alt="CF"></a>
    <a href="https://modrinth.com/mod/missions"><img src="https://img.shields.io/modrinth/dt/missions?logo=modrinth&label=&suffix=%20&style=flat&color=242629&labelColor=5ca424&logoColor=1c1c1c" alt="Modrinth"></a>
    <br><br>
</h1>

Create: Missions is a Create addon focused for Create mod servers. It will also work in singleplayers worlds, but it's designed to bring an economy throughout a server's lifetime. We hope to give server owners a better way of giving players a currency, while also encouraging players to build their own shop contraptions/exchange banks/banks.

|Versions|Supported?|Create version|
|:---------:|:---------:|:---------:|
| 1.18        | 0.1.1.e, unsupported|0.5.1.c+|
| 1.19  | 0.1.1.e, unsupported|0.5.1.c+|
| 1.20| latest, supported|0.5.1.d+|

# What's this addon?
It works by giving **10 random missions per player per week**. These missions can be vanilla missions like **"Break X Veridium"** or Create mod related like **"Drive a train for 4 km with at least 2 passengers"**. You can also reroll the missions, and claim prices by completing them.

**Open the mission GUI pressing H key (by default)**

## Needed for server owners
<details>
<summary>Spoilers</summary>

Select the currency in `missions\currency.json`, it will be 64 -> 1 in the same order the list is in, by default. You can change it by going to `missions\config.json` and changing the value of "exchange-rate". This **only** affects the exchange rate of the Exchange ATM Block, which is the only way of exchanging coins this mod gives.

<details>
<summary>missions.json -> How to configure the missions? </summary>
This file can be found in your server files, inside a "missions" folder.
An example mission configuration:

```json
"place": {
    "reward": {
      "amount": "2-23",
      "item": "createdeco:iron_coin"
    },
    "missions": {
      "create:track_signal": "20-50"
    },
    "titles": [
      "Example title"
    ]
  }
```

Ranges like 2-23 mean a number at random from 2 to 23, both included.
Mission example: Place 35 Track Signal(s)
Reward example: 2-23 Iron Coin(s)

As you can see, the action number is randomly selected from its range at the time of receiving that mission, whereas the reward range is randomly selected from its range at the moment of *redeeming/claiming* the completed mission.

#

You can add as many item id's to the "missions" bracket, and add as many titles to "titles" bracket, for a mission to be granted one item and one title, both randomly from those inside the bracket.

</details>
<details>
<summary>Exchange ATM Block </summary>

<p align="center">
  <img width="200" src="https://cdn.modrinth.com/data/KN33kvHF/images/c3e00905e1082e33477a90274f27b09ec4919f3a.png">
The Exchange ATM doesn't have a crafting recipe, and can only be obtained with a 1% change after compleating a hard mission.

It lets you to exchange currencies from smaller to bigger currency. Depends on which rotation direction the shaft has. It also requires 100 rpm, and consumes much more SU.
</p>
</details>
<details>
<summary>Suggestions </summary>
- I suggest changing the default **missions\currency.json** to coin items that you have in your modpack. In my case, I use Create Deco coins:

```json
[
  "createdeco:zinc_coin",
  "createdeco:copper_coin",
  "createdeco:brass_coin"
]
```

- Incentive in your server an economy with no virtual balance or top lists
- Modify from time to time the missions to they get refreshed for the players, also touchups to the missions ranges are really needed for balancing. If someone uses a better mission config file and more balanced feel free to share it with me!
- Have in mind always the ability for a player to reroll a mission, dont give them money to infinitely reroll missions as that takes fun away. Build your economy balancing having that cost in mind.
- Use the Exchange ATM to build a public bank!
</details>

</details>

