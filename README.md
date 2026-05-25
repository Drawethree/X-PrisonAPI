# X-PRISON API
Official API for X-Prison plugin.

**Links:**
- [SpigotMC (Download)](https://www.spigotmc.org/resources/86845/)
- [Discord (Support)](https://discord.gg/ZeSkmEC6mG)
- [Wiki (Documentation)](https://github.com/Drawethree/X-Prison/wiki)
- [Javadocs](https://www.drawethree.dev/plugins/x-prison/javadoc/index.html)

## Dependency [![](https://jitpack.io/v/drawethree/X-PrisonAPI.svg)](https://jitpack.io/#drawethree/X-PrisonAPI)

### Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.drawethree:X-PrisonAPI:LATEST'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.drawethree</groupId>
        <artifactId>X-PrisonAPI</artifactId>
        <version>LATEST</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### plugin.yml
Add X-Prison as a dependency so your plugin loads after it:
```yaml
depend: [X-Prison]
```

---

## Getting the API

```java
import dev.drawethree.xprison.api.XPrisonAPI;

XPrisonAPI api = XPrisonAPI.getInstance();
```

> `getInstance()` throws `IllegalStateException` if called before X-Prison has finished enabling.
> Call it inside `onEnable()` or later.

---

## Module APIs

| Method | Returns |
|--------|---------|
| `api.getRanksApi()` | `XPrisonRanksAPI` |
| `api.getPrestigesApi()` | `XPrisonPrestigesAPI` |
| `api.getRebirthApi()` | `XPrisonRebirthAPI` |
| `api.getCurrencyApi()` | `XPrisonCurrencyAPI` |
| `api.getMultipliersApi()` | `XPrisonMultipliersAPI` |
| `api.getMinesApi()` | `XPrisonMinesAPI` |
| `api.getAutoSellApi()` | `XPrisonAutoSellAPI` |
| `api.getAutoMinerApi()` | `XPrisonAutoMinerAPI` |
| `api.getEnchantsApi()` | `XPrisonEnchantsAPI` |
| `api.getGangsApi()` | `XPrisonGangsAPI` |
| `api.getHistoryApi()` | `XPrisonHistoryAPI` |
| `api.getPickaxeLevelsApi()` | `XPrisonPickaxeLevelsAPI` |
| `api.getPickaxeSkinsApi()` | `XPrisonPickaxeSkinsAPI` |
| `api.getMiningStatsApi()` | `XPrisonMiningStatsAPI` |
| `api.getBombsApi()` | `XPrisonBombsAPI` |
| `api.getBlocksApi()` | `XPrisonBlocksAPI` |

---

## Usage Examples

### Plugin / Module Info

```java
XPrisonAPI api = XPrisonAPI.getInstance();

// Plugin version
String version = api.getPluginVersion(); // e.g. "2026.2.2.4-BETA"

// Check if a module is active
boolean ranksEnabled = api.isModuleEnabled("ranks");
boolean minesEnabled = api.isModuleEnabled("mines");

// Enable / disable a module at runtime
api.enableModule("gangs");
api.disableModule("bombs");

// List all modules
api.getModules().forEach(m -> System.out.println(m.getModuleName() + " enabled=" + m.isEnabled()));

// List loaded addons
api.getLoadedAddons().forEach(info ->
    System.out.println(info.getName() + " v" + info.getVersion() + " enabled=" + info.isEnabled())
);

// Load an addon JAR from the addons/ folder at runtime
boolean loaded = api.loadAddonFromFile("MyAddon.jar");
```

---

### Ranks

```java
XPrisonRanksAPI ranks = XPrisonAPI.getInstance().getRanksApi();

// Get a player's current rank
Rank rank = ranks.getPlayerRank(player);
System.out.println(rank.getName()); // e.g. "A"

// Set rank
Rank targetRank = ranks.getRankById(5);
ranks.setPlayerRank(player, targetRank);

// Check max rank / reset
if (ranks.isMaxRank(player)) {
    ranks.resetPlayerRank(player);
}

// Progression
double progress = ranks.getRankupProgress(player); // 0.0 – 100.0
Optional<Rank> next = ranks.getNextPlayerRank(player);

// Offline support
Rank offlineRank = ranks.getPlayerRankOffline(uuid);
ranks.setPlayerRankOffline(uuid, 3L);

// All ranks
List<Rank> all = ranks.getAllRanks();
Rank max     = ranks.getMaxRank();
Rank def     = ranks.getDefaultRank();

// Leaderboard
Map<UUID, Integer> top10 = ranks.getTopByRank(10);
List<UUID> allUuids      = ranks.getAllPlayerUUIDs();
```

---

### Prestiges

```java
XPrisonPrestigesAPI prestiges = XPrisonAPI.getInstance().getPrestigesApi();

Prestige current = prestiges.getPlayerPrestige(player);
prestiges.setPlayerPrestige(player, 5L);          // by ID
prestiges.setPlayerPrestige(player, somePrestige); // by object

boolean maxed = prestiges.isMaxPrestige(player);
prestiges.resetPlayerPrestige(player);

double progress = prestiges.getPrestigeProgress(player); // 0.0 – 100.0
Optional<Prestige> next = prestiges.getNextPlayerPrestige(player);

// Offline
Prestige offlinePrestige = prestiges.getPlayerPrestigeOffline(uuid);
prestiges.setPlayerPrestigeOffline(uuid, 10L);

// All prestiges
List<Prestige> all = prestiges.getAllPrestiges();
Prestige max       = prestiges.getMaxPrestige(); // null on unlimited-prestige servers

// Leaderboard / bulk
Map<UUID, Long> top = prestiges.getTopByPrestige(10);
List<UUID> allUuids = prestiges.getAllPlayerUUIDs();
```

---

### Rebirths

```java
XPrisonRebirthAPI rebirth = XPrisonAPI.getInstance().getRebirthApi();

Rebirth current = rebirth.getPlayerRebirth(player);
rebirth.setPlayerRebirth(player, someRebirth);
rebirth.resetPlayerRebirth(player);

boolean maxed = rebirth.isMaxRebirth(player);
boolean success = rebirth.tryRebirth(player); // checks all requirements

Optional<Rebirth> next = rebirth.getNextPlayerRebirth(player);

// Offline
Rebirth offlineRebirth = rebirth.getPlayerRebirthOffline(uuid);
rebirth.setPlayerRebirthOffline(uuid, 3L);

// All rebirths
List<Rebirth> all = rebirth.getAllRebirths();
Rebirth max       = rebirth.getMaxRebirth();

// Leaderboard
Map<UUID, Integer> top  = rebirth.getTopByRebirth(10);
List<UUID> allUuids     = rebirth.getAllPlayerUUIDs();
```

---

### Currency

```java
XPrisonCurrencyAPI currency = XPrisonAPI.getInstance().getCurrencyApi();

// Look up a currency
XPrisonCurrency tokens = currency.getCurrency("TOKENS");

// Balance operations
double bal = currency.getBalance(player, "TOKENS");
currency.addBalance(player, "TOKENS", 500.0, ReceiveCause.SELL);
currency.removeBalance(player, "TOKENS", 100.0, LostCause.RANKUP);
currency.setBalance(player, "TOKENS", 1000.0);
boolean hasEnough = currency.has(player, "TOKENS", 250.0);

// Transfer between players (atomic, fails if source has insufficient funds)
boolean ok = currency.transferBalance(fromPlayer, toPlayer, "TOKENS", 200.0);

// Leaderboard — simple and paginated
Map<UUID, Double> top10   = currency.getTopByBalance("TOKENS", 10);
Map<UUID, Double> page2   = currency.getTopByBalance("TOKENS", 10, 10); // offset=10

// All currencies
Collection<XPrisonCurrency> all = currency.getAllCurrencies();

// Create / update / delete a currency at runtime
boolean created = currency.createCurrency(myCurrency);
boolean updated = currency.updateCurrency(myCurrency);
boolean deleted = currency.deleteCurrency("MYCURRENCY");
```

---

### Multipliers

```java
XPrisonMultipliersAPI multi = XPrisonAPI.getInstance().getMultipliersApi();
XPrisonCurrency tokens = XPrisonAPI.getInstance().getCurrencyApi().getCurrency("TOKENS");

// Read current multipliers
Multiplier global    = multi.getGlobalMultiplier(tokens);
PlayerMultiplier personal = multi.getPlayerMultiplier(player, tokens);
PlayerMultiplier rank     = multi.getRankMultiplier(player, tokens);

// Combined effective multiplier (global × rank × personal)
double effective = multi.getEffectiveMultiplier(player, tokens);

// Set a 2x global multiplier for 30 minutes
multi.setGlobalMultiplier(tokens, 2.0, TimeUnit.MINUTES, 30);

// Give a player a personal 1.5x multiplier for 1 hour
multi.givePlayerMultiplier(player, tokens, 1.5, TimeUnit.HOURS, 1);

// All active personal multipliers for a currency (across all online players)
Collection<PlayerMultiplier> active = multi.getActivePlayerMultipliers(tokens);
```

---

### Mines

```java
XPrisonMinesAPI mines = XPrisonAPI.getInstance().getMinesApi();

// All mines / by name / at location
Collection<Mine> all = mines.getMines();
Mine mine            = mines.getMineByName("A");
Mine atLoc           = mines.getMineAtLocation(someLocation);

// Mines in a specific world
Collection<Mine> worldMines = mines.getMinesInWorld(player.getWorld());

// Reset
mines.resetMine(mine);

// Change reset type
mines.setMineResetType("A", "GRADUAL"); // or "INSTANT"

// Mine properties
String name         = mine.getName();
World world         = mine.getWorld();
long resetSeconds   = mine.getResetIntervalSeconds(); // e.g. 300 = 5 min
Location teleport   = mine.getTeleportLocation();
int fillPercentage  = mine.getFillPercentage();

// Create / delete
MineSelection sel = ...; // from WorldEdit selection or similar
Mine created = mines.createMine(sel, "Z");
boolean deleted = mines.deleteMine(mine);
```

---

### AutoSell

```java
XPrisonAutoSellAPI autoSell = XPrisonAPI.getInstance().getAutoSellApi();

// Toggle auto-sell
autoSell.enableAutoSell(player);
autoSell.disableAutoSell(player);
boolean enabled = autoSell.hasAutoSellEnabled(player);

// Instant sell — clears sellable items, returns total earned
double earned = autoSell.sellPlayerInventory(player);

// Sell a specific list of blocks
double blockEarnings = autoSell.sellBlocks(player, blocks);

// Prices
double price = autoSell.getPriceForItem(itemStack);
autoSell.addSellPrice(mineBlock, 5.0);
autoSell.removeSellPrice(mineBlock);
Map<String, Double> globalPrices = autoSell.getGlobalPrices();

// Regions
Collection<SellRegion> regions = autoSell.getSellRegions();
SellRegion region = autoSell.getSellRegionAtLocation(location);
autoSell.addRegionSellPrice("mine_a", mineBlock, 7.5);
autoSell.removeRegionSellPrice("mine_a", mineBlock);
```

---

### AutoMiner

```java
XPrisonAutoMinerAPI autoMiner = XPrisonAPI.getInstance().getAutoMinerApi();

// Check region membership
boolean inRegion = autoMiner.isInAutoMinerRegion(player);

// Time management
int remaining = autoMiner.getAutoMinerTime(player);  // seconds
autoMiner.addAutoMinerTime(player, 3600);             // +1 hour
autoMiner.setAutoMinerTime(player, 7200);             // = 2 hours
autoMiner.removeAutoMinerTime(player, 600);           // -10 min (clamped to 0)

// Regions
Collection<AutoMinerRegion> regions = autoMiner.getAutoMinerRegions();
Optional<AutoMinerRegion> region    = autoMiner.getAutoMinerRegionByName("mine_a");

// Leaderboard (all players in DB, ordered by remaining time)
Map<UUID, Integer> top10 = autoMiner.getTopByAutoMinerTime(10);
```

---

### Enchants

```java
XPrisonEnchantsAPI enchants = XPrisonAPI.getInstance().getEnchantsApi();

// All enchantments registered in X-Prison
Collection<XPrisonEnchant> all = enchants.getAllEnchantments();

// Check / get enchants on an item
enchants.getEnchants(itemStack).forEach((enchant, level) ->
    System.out.println(enchant.getId() + " lv" + level)
);
```

---

### Gangs

```java
XPrisonGangsAPI gangs = XPrisonAPI.getInstance().getGangsApi();

// Lookup
Optional<Gang> gang = gangs.getPlayerGang(player);
Optional<Gang> byName = gangs.getByName("BreakersUnited");
Collection<Gang> all  = gangs.getAllGangs();

// Create / disband
GangCreateResult result = gangs.createGang("BreakersUnited", player);
if (result == GangCreateResult.SUCCESS) { ... }
gangs.disbandGang(gang.get());

// Membership
gangs.kickPlayerFromGang(gang.get(), offlinePlayer);
boolean transferred = gangs.transferGangOwnership(gang.get(), newOwner);

// Gang info
Gang g = gang.get();
int members     = g.getMemberCount();   // owner + all members
long value      = g.getGangValue();
String owner    = g.getGangOwner();

// Modify value
gangs.setGangValue(g, 5000L);
gangs.addGangValue(g, 250L);   // or negative to subtract

// Leaderboard
LinkedHashMap<String, Long> top10 = gangs.getTopGangsByValue(10);

// Validate a name before creating
GangNameCheckResult nameCheck = gangs.checkGangName("BreakersUnited");
```

---

### History

```java
XPrisonHistoryAPI history = XPrisonAPI.getInstance().getHistoryApi();

// Full history
Collection<HistoryLine> all = history.getPlayerHistory(player);

// Paginated (1-based page, newest-first)
int total = history.getPlayerHistoryCount(player);
Collection<HistoryLine> page1 = history.getPlayerHistory(player, 1, 10);
Collection<HistoryLine> page2 = history.getPlayerHistory(player, 2, 10);

// Filter by module
XPrisonModule ranksModule = XPrisonAPI.getInstance().getRanksApi();
Collection<HistoryLine> rankHistory = history.getPlayerHistory(player, ranksModule);

// Add a custom history entry
HistoryLine entry = history.createHistoryLine(player, ranksModule, "Ranked up to A via API");
```

---

### Pickaxe Levels

```java
XPrisonPickaxeLevelsAPI levels = XPrisonAPI.getInstance().getPickaxeLevelsApi();

// Get level
Optional<PickaxeLevel> current = levels.getPickaxeLevel(player);
Optional<PickaxeLevel> byItem  = levels.getPickaxeLevel(itemStack);
Optional<PickaxeLevel> byNum   = levels.getPickaxeLevel(5);

// Set level
levels.setPickaxeLevel(player, itemStack, 10);
levels.setPickaxeLevel(player, itemStack, somePickaxeLevel);

// Progress bar string (configured in pickaxe-levels.yml)
String bar = levels.getProgressBar(player);

// All levels / max level
List<PickaxeLevel> all = levels.getAllPickaxeLevels();
PickaxeLevel max       = levels.getMaxPickaxeLevel();

// Leaderboard (online players only — level is NBT-based, not in DB)
Map<UUID, Integer> top10 = levels.getTopByPickaxeLevel(10);
```

---

### Pickaxe Skins

```java
XPrisonPickaxeSkinsAPI skins = XPrisonAPI.getInstance().getPickaxeSkinsApi();

// Get the active skin on an item or for a player
Optional<PickaxeSkin> skin = skins.getPickaxeSkin(player);
Optional<PickaxeSkin> bySkin = skins.getPickaxeSkin("GoldRush");

// Apply / remove
skins.setPickaxeSkin(player, itemStack, someSkin);
skins.removePickaxeSkin(player, itemStack);

// All available skins
Collection<PickaxeSkin> all = skins.getAllSkins();
```

---

## Custom Enchants Example

A full working example plugin using the Enchants API is available here:

**[X-PrisonAPI-CustomEnchants](https://github.com/Drawethree/X-PrisonAPI-CustomEnchants)**

---

## Events

X-Prison fires several Bukkit events you can listen to. A few notable ones:

| Event | Fired when |
|-------|-----------|
| `PlayerRankupEvent` | A player ranks up |
| `PlayerPrestigeEvent` | A player prestiges |
| `PlayerRebirthEvent` | A player rebirths |
| `PlayerPickaxeLevelUpEvent` | A player's pickaxe levels up |
| `XPrisonSellAllEvent` | A player's inventory is sold (cancellable) |
| `XPrisonMineResetEvent` | A mine is reset |

```java
@EventHandler
public void onRankup(PlayerRankupEvent e) {
    Player player = e.getPlayer();
    Rank newRank  = e.getNewRank();
    player.sendMessage("You ranked up to " + newRank.getName() + "!");
}
```

---

## Building an Addon

Addons are special plugins that integrate directly with X-Prison's lifecycle.  
Register your addon's dashboard URL (or any metadata) via:

```java
XPrisonAPI api = XPrisonAPI.getInstance();
api.setDashboardUrl("https://your-dashboard.example.com");

// On disable:
api.setDashboardUrl(null);
```

Enable/disable other addons at runtime:
```java
api.enableAddon("X-Prison-Dashboard");
api.disableAddon("X-Prison-Dashboard");
api.loadAddonFromFile("NewAddon.jar"); // loads from plugins/X-Prison/addons/
```
