package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfigIcon extends Icon {

    private final ConfigurationSection section;

    public ConfigIcon(ItemStack item, ConfigurationSection section, Player player) {
        super(item);
        this.section = section;
        this.forceTranslateAllPlaceholders(player);
    }

    public ConfigIcon(Material material, ConfigurationSection section, Player player) {
        super(material);
        this.section = section;
        this.forceTranslateAllPlaceholders(player);
    }

    public ConfigurationSection getSection() {
        return section;
    }

    @Override
    public ConfigIcon clone() {
        return (ConfigIcon) super.clone();
    }
}
