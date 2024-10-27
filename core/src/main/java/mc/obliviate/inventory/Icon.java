package mc.obliviate.inventory;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Icon implements GuiIcon {

	private final ItemStack item;
	private Consumer<InventoryClickEvent> clickAction;
	private Consumer<InventoryDragEvent> dragAction;

	public Icon(final ItemStack item) {
		this.item = item;
		this.dragAction = event -> {
		};
		this.clickAction = event -> {
		};
	}

	public Icon(final Material material) {
		this(new ItemStack(material));
	}

	/**
	 * @return ComponentProxy of the Icon. The object supports applying adventure components to the Icon.
	 */
	public ComponentIcon toComp() {
		return ComponentIcon.fromIcon(this);
	}

	/**
	 * sets durability of icon
	 *
	 * @param newDamage durability
	 * @return this
	 */
	@SuppressWarnings("deprecation")
	@Nonnull
	public Icon setDurability(final short newDamage) {
		item.setDurability(newDamage);
		return this;
	}

	/**
	 * sets durability of the icon
	 *
	 * @param newDamage durability
	 * @return this
	 */
	@Nonnull
	public Icon setDurability(final int newDamage) {
		setDurability((short) newDamage);
		return this;
	}

	/**
	 * sets display name of the icon
	 *
	 * @param name display name
	 * @return this
	 */
	@Nonnull
	public Icon setName(final String name) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}


	/**
	 * sets lore of the icon
	 *
	 * @param lore lore
	 * @return this
	 */
	@Nonnull
	public Icon setLore(final List<String> lore) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * sets lore of icon
	 *
	 * @param lore lore
	 * @return this
	 */
	@Nonnull
	public Icon setLore(final String... lore) {
		return setLore(new ArrayList<>(Arrays.asList(lore)));
	}

	/**
	 * adds new string lines to end of lore of the icon
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon appendLore(final List<String> newLines) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) lore.addAll(newLines);
		else lore = newLines;
		return setLore(lore);
	}

	/**
	 * adds new string lines to end of lore of the icon
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon appendLore(final String... newLines) {
		return appendLore(new ArrayList<>(Arrays.asList(newLines)));
	}

	/**
	 * inserts new lines to lore of the icon
	 *
	 * @param index    line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon insertLore(final int index, final String... newLines) {
		return insertLore(index, new ArrayList<>(Arrays.asList(newLines)));
	}

	/**
	 * inserts new lines to lore of the icon
	 *
	 * @param index    line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon insertLore(final int index, final List<String> newLines) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) lore.addAll(index, newLines);
		else lore = newLines;
		return setLore(lore);
	}

	/**
	 * sets item amount of the icon
	 *
	 * @param amount new amount
	 * @return this
	 */
	@Nonnull
	public Icon setAmount(final int amount) {
		item.setAmount(amount);
		return this;
	}

	/**
	 * hides a flag of the icon
	 *
	 * @param itemFlag item flag on meta
	 * @return this
	 */
	@Nonnull
	public Icon hideFlags(final ItemFlag... itemFlag) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.addItemFlags(itemFlag);
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * hides all flags
	 *
	 * @return this
	 */
	@Nonnull
	public Icon hideFlags() {
		hideFlags(ItemFlag.values());
		return this;
	}

	/**
	 * enchants the item
	 *
	 * @param enchantment enchant
	 * @return this
	 */
	@Nonnull
	public Icon enchant(final Enchantment enchantment) {
		return enchant(enchantment, enchantment.getStartLevel());
	}

	/**
	 * enchants the item
	 *
	 * @param enchantments enchant
	 * @return this
	 */
	@Nonnull
	public Icon enchant(final Map<Enchantment, Integer> enchantments) {
		for (Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
			enchant(enchant.getKey(), enchant.getValue());
		}
		return this;
	}

	/**
	 * enchants the item
	 *
	 * @param enchantment enchant
	 * @param level       enchantment level
	 * @return this
	 */
	@Nonnull
	public Icon enchant(final Enchantment enchantment, final int level) {
		if (item.getType().equals(Material.ENCHANTED_BOOK)) {
			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

			if (meta == null) return this;

			meta.addStoredEnchant(enchantment, level, true);

			item.setItemMeta(meta);
		} else {
			item.addUnsafeEnchantment(enchantment, level);
		}
		return this;
	}

	@Nonnull
	public Consumer<InventoryClickEvent> getClickAction() {
		return clickAction;
	}

	@Nonnull
	public Icon onClick(Consumer<InventoryClickEvent> clickAction) {
		this.clickAction = clickAction;
		return this;
	}

	@Nonnull
	public Consumer<InventoryDragEvent> getDragAction() {
		return dragAction;

	}

	@Nonnull
	public Icon onDrag(Consumer<InventoryDragEvent> dragAction) {
		this.dragAction = dragAction;
		return this;
	}

	public ItemStack getItem() {
		return item;
	}

	/**
	 * sets display name of the icon with placeholders
	 *
	 * @param name display name
	 * @return this
	 */
	@Nonnull
	public Icon setName(final String name, final Player player) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, name));
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * sets lore of the icon with placeholders
	 *
	 * @param lore lore
	 * @return this
	 */
	@Nonnull
	public Icon setLore(final List<String> lore, final Player player) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> translatedLore = new ArrayList<>();
		for (String line : lore) {
			translatedLore.add(PlaceholderAPI.setPlaceholders(player, line));
		}
		meta.setLore(translatedLore);
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * sets lore of icon with placeholders
	 *
	 * @param lore lore
	 * @return this
	 */
	@Nonnull
	public Icon setLore(final Player player, final String... lore) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		return setLore(new ArrayList<>(Arrays.asList(lore)), player);
	}

	/**
	 * adds new string lines to end of lore of the icon with placeholders
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon appendLore(final List<String> newLines, final Player player) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) {
			for (String line : newLines) {
				lore.add(PlaceholderAPI.setPlaceholders(player, line));
			}
		} else {
			lore = new ArrayList<>();
			for (String line : newLines) {
				lore.add(PlaceholderAPI.setPlaceholders(player, line));
			}
		}
		return setLore(lore, player);
	}

	/**
	 * adds new string lines to end of lore of the icon with placeholders
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon appendLore(final Player player, final String... newLines) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		return appendLore(new ArrayList<>(Arrays.asList(newLines)), player);
	}

	/**
	 * inserts new lines to lore of the icon with placeholders
	 *
	 * @param index    line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon insertLore(final int index, final Player player, final String... newLines) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		return insertLore(index, new ArrayList<>(Arrays.asList(newLines)), player);
	}

	/**
	 * inserts new lines to lore of the icon with placeholders
	 *
	 * @param index    line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon insertLore(final int index, final List<String> newLines, final Player player) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) {
			for (int i = 0; i < newLines.size(); i++) {
				lore.add(index + i, PlaceholderAPI.setPlaceholders(player, newLines.get(i)));
			}
		} else {
			lore = new ArrayList<>();
			for (String line : newLines) {
				lore.add(PlaceholderAPI.setPlaceholders(player, line));
			}
		}
		return setLore(lore, player);
	}

	@Nonnull
	public Icon forceTranslateAllPlaceholders(Player player) {
		if (!InventoryAPI.translatePlaceholders()) return this;
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		if (meta.hasDisplayName()) {
			this.setName(meta.getDisplayName(), player);
		}
		if (meta.hasLore()) {
			this.setLore(meta.getLore(), player);
		}
		return this;
	}

	@Nonnull
	public Icon replaceCustomPlaceholder(String placeholder, String value) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		if (meta.hasDisplayName()) {
			this.setName(meta.getDisplayName().replace(placeholder, value));
		}
		if (meta.hasLore()) {
			List<String> lore = new ArrayList<>();
			for (String line : meta.getLore()) {
				lore.add(line.replace(placeholder, value));
			}
			this.setLore(lore);
		}
		return this;
	}

	@Override
	protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
        }
    }
}
