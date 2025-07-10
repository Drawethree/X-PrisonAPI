package dev.drawethree.xprison.api.enchants.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.drawethree.xprison.api.currency.CurrencyType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Abstract base class for all XPrison enchantments.
 * Handles common properties and configuration loading from JSON files.
 */
@Getter
public abstract class XPrisonEnchantmentBase implements XPrisonEnchantment {

    /** The configuration file for this enchantment */
    @Setter
    private File configFile;

    protected int id;
    protected String rawName;
    protected String name;
    protected String nameWithoutColor;
    protected boolean enabled;
    protected int maxLevel;
    protected long baseCost;
    protected long increaseCost;
    protected XPrisonEnchantmentGuiProperties guiProperties;
    protected CurrencyType currencyType;

    /**
     * Constructs a new enchantment with the given config file.
     *
     * @param configFile The file containing enchantment configuration in JSON format.
     */
    public XPrisonEnchantmentBase(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Loads the enchantment configuration from the JSON file.
     * Calls abstract {@link #loadCustomProperties(JsonObject)} for subclass-specific properties.
     *
     * @throws RuntimeException if the config fails to load or is invalid
     */
    @Override
    public void load() {
        try (FileReader reader = new FileReader(configFile)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();

            this.loadBaseProperties(config);
            this.loadGuiProperties(config);
            this.loadCustomProperties(config);

        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to load enchant config: " + configFile.getName(), e);
        }
    }

    private void loadBaseProperties(JsonObject config) {
        this.id = config.get("id").getAsInt();
        this.rawName = config.get("rawName").getAsString();
        this.name = ChatColor.translateAlternateColorCodes('&', config.get("name").getAsString());
        this.nameWithoutColor = name.replaceAll("§.", "");
        this.enabled = config.get("enabled").getAsBoolean();
        this.maxLevel = config.get("maxLevel").getAsInt();
        this.baseCost = config.get("initialCost").getAsLong();
        this.increaseCost = config.get("increaseCostBy").getAsLong();
        this.currencyType = CurrencyType.valueOf(config.get("currency").getAsString());
    }

    /**
     * Loads GUI related properties for the enchantment.
     *
     * @param config The root JSON object containing the "gui" section.
     */
    private void loadGuiProperties(JsonObject config) {
        JsonObject gui = config.getAsJsonObject("gui");
        int slot = gui.get("slot").getAsInt();
        String guiName = ChatColor.translateAlternateColorCodes('&', gui.get("name").getAsString());
        Material mat = Material.valueOf(gui.get("material").getAsString());
        String base64 = gui.has("base64") ? gui.get("base64").getAsString() : null;

        List<String> desc = new Gson().fromJson(gui.get("description"), List.class);
        desc = desc.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .toList();

        int customModelData = gui.get("customModelData").getAsInt();

        this.guiProperties = new XPrisonEnchantmentGuiPropertiesBase(slot, guiName, base64, mat, desc,customModelData);
    }

    /**
     * Loads any subclass-specific properties from the configuration.
     * Must be implemented by subclasses.
     *
     * @param config The JSON object for the enchantment config.
     */
    protected abstract void loadCustomProperties(JsonObject config);
}
