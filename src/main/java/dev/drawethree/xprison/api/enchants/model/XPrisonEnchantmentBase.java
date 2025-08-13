package dev.drawethree.xprison.api.enchants.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.drawethree.xprison.api.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
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
public abstract class XPrisonEnchantmentBase implements XPrisonEnchantment, RefundableEnchant {

    /** The configuration file for this enchantment */
    @Setter
    private File configFile;
    private Expression compiledExpression;

    protected int id;
    protected String rawName;
    protected String name;
    protected String nameWithoutColor;
    protected boolean enabled;
    protected int maxLevel;
    protected long baseCost;
    protected String costFormula;
    protected XPrisonEnchantmentGuiProperties guiProperties;
    protected String currencyName;
    protected boolean refundEnabled;
    protected int refundGuiSlot;
    protected double refundPercentage;

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
        this.id = JsonUtils.getRequiredInt(config,"id");
        this.rawName = JsonUtils.getRequiredString(config, "rawName");
        this.name = ChatColor.translateAlternateColorCodes('&', JsonUtils.getRequiredString(config,"name"));
        this.nameWithoutColor = name.replaceAll("§.", "");
        this.enabled = JsonUtils.getRequiredBoolean(config,"enabled");
        this.maxLevel = JsonUtils.getRequiredInt(config,"maxLevel");
        this.baseCost = JsonUtils.getRequiredLong(config, "initialCost");
        this.currencyName = JsonUtils.getOptionalString(config,"currency", "Tokens");

        JsonElement increaseCostElement = config.get("increaseCostBy");
        if (increaseCostElement != null && increaseCostElement.isJsonPrimitive()) {
            JsonPrimitive primitive = increaseCostElement.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                this.costFormula = primitive.getAsString();
            } else if (primitive.isString()) {
                this.costFormula = primitive.getAsString();
            }
        } else {
            this.costFormula = "0";
        }

        if (!costFormula.matches("\\d+")) {
            compiledExpression = new ExpressionBuilder(costFormula)
                    .variables("baseCost", "level")
                    .build();
        }

        JsonObject refundObject = JsonUtils.getRequiredObject(config,"refund");
        this.refundEnabled = JsonUtils.getRequiredBoolean(refundObject,"enabled");
        this.refundGuiSlot = JsonUtils.getRequiredInt(refundObject,"guiSlot");
        this.refundPercentage = JsonUtils.getRequiredDouble(refundObject, "percentage");
    }

    /**
     * Loads GUI related properties for the enchantment.
     *
     * @param config The root JSON object containing the "gui" section.
     */
    private void loadGuiProperties(JsonObject config) {
        JsonObject gui = JsonUtils.getRequiredObject(config, "gui");

        int slot = JsonUtils.getRequiredInt(gui, "slot");
        String guiName = ChatColor.translateAlternateColorCodes('&', JsonUtils.getRequiredString(gui, "name"));
        Material mat = Material.valueOf(JsonUtils.getRequiredString(gui, "material"));
        String base64 = JsonUtils.getOptionalString(gui, "base64", null);

        List<String> desc = JsonUtils.getRequiredStringList(gui, "description");
        desc = desc.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .toList();

        int customModelData = JsonUtils.getOptionalInt(gui, "customModelData", 0);

        this.guiProperties = new XPrisonEnchantmentGuiPropertiesBase(slot, guiName, base64, mat, desc, customModelData);
    }

    /**
     * Calculate the cost for this enchantment at a specific level.
     * Supports formulas with variables: baseCost, level.
     *
     * @param level The enchantment level.
     * @return The calculated cost at this level.
     */
    @Override
    public long getCostAtLevel(int level) {
        if (level == 0) {
            return baseCost;
        }
        if (compiledExpression == null) {
            long increaseCost = Long.parseLong(costFormula);
            return baseCost + (level * increaseCost);
        }
        try {
            compiledExpression
                    .setVariable("baseCost", getBaseCost())
                    .setVariable("level", level);
            return Math.round(compiledExpression.evaluate());
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Loads any subclass-specific properties from the configuration.
     * Must be implemented by subclasses.
     *
     * @param config The JSON object for the enchantment config.
     */
    protected abstract void loadCustomProperties(JsonObject config);
}
