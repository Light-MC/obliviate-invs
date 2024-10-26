package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class DysfunctionalConfigIcon extends ConfigIcon {

    public DysfunctionalConfigIcon(ItemStack item, ConfigurationSection section, Player player) {
        super(item, section, player);
    }

    public DysfunctionalConfigIcon(Material material, ConfigurationSection section, Player player) {
        super(material, section, player);
    }

    @Nonnull
    @Override
    public Icon onDrag(Consumer<InventoryDragEvent> dragAction) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public Icon onClick(Consumer<InventoryClickEvent> clickAction) {
        throw new UnsupportedOperationException();
    }

    /**
     * Reproduces normal Icon clone of the object.
     *
     * @return new {@link Icon} instance using same item.
     */
    public Icon toFunctional() {
        return new Icon(super.getItem());
    }
}
