package dev.drawethree.xprison.api.enchants.model;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import dev.drawethree.xprison.api.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lucko.helper.random.RandomSelector;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandRewardEnchantBase extends XPrisonEnchantmentBase implements BlockBreakEnchant, ChanceBasedEnchant {

    protected double chance;
    protected List<CommandWithChance> commandsToExecute;

    /**
     * Constructs a new enchantment with the given config file.
     *
     * @param configFile The file containing enchantment configuration in JSON format.
     */
    public CommandRewardEnchantBase(File configFile) {
        super(configFile);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {
        CommandWithChance randomCmd = getRandomCommandToExecute();
        if (randomCmd != null) {
            for (String cmd : randomCmd.getCommands()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", e.getPlayer().getName()));
            }
        }
    }

    private CommandWithChance getRandomCommandToExecute() {
        if (this.commandsToExecute.isEmpty()) {
            return null;
        }
        return RandomSelector.weighted(this.commandsToExecute, CommandWithChance::getChance).pick();
    }

    @Override
    public double getChanceToTrigger(int enchantLevel) {
        return this.chance * enchantLevel;
    }

    @Override
    public void loadCustomProperties(JsonObject config) {
        this.chance = JsonUtils.getRequiredDouble(config, "chance");
        JsonElement element = config.get("commands");

        if (element == null) {
            this.commandsToExecute = new ArrayList<>();
            return;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommandWithChance.class, new CommandWithChanceDeserializer())
                .create();

        this.commandsToExecute = gson.fromJson(
                element,
                new TypeToken<List<CommandWithChance>>(){}.getType()
        );
    }

    private static class CommandWithChanceDeserializer implements JsonDeserializer<CommandWithChance> {

        @Override
        public CommandWithChance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();

            CommandWithChance result = new CommandWithChance();

            result.setChance(obj.get("chance").getAsInt());

            JsonElement commandsElement = obj.get("commands");
            if (commandsElement == null) {
                commandsElement = obj.get("command");
            }
            List<String> commandsList = new ArrayList<>();

            if (commandsElement.isJsonArray()) {
                for (JsonElement elem : commandsElement.getAsJsonArray()) {
                    commandsList.add(elem.getAsString());
                }
            } else if (commandsElement.isJsonPrimitive()) {
                commandsList.add(commandsElement.getAsString());
            } else {
                throw new JsonParseException("Unexpected commands element type");
            }

            result.setCommands(commandsList);
            return result;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class CommandWithChance {
        private List<String> commands;
        private double chance;
    }
}