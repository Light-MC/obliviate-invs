package mc.obliviate.inventory.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import mc.obliviate.inventory.GuiIcon;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IconBuilder {
	public static GuiIcon fillIcon() {
		return new IconBuilder(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
				.setName(" ")
				.build();
	}

	private final ItemStack itemStack;
	private Consumer<InventoryClickEvent> clickAction;
	private String name;
	private List<String> lore;

	public IconBuilder(Material material) {
		this.itemStack = new ItemStack(material);
	}

	public IconBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public IconBuilder setName(String name) {
		this.name = name.replace("&", "§");
		return this;
	}

	public IconBuilder setLore(List<String> lore) {
		this.lore = lore.stream()
				.map(line -> line.replace("&", "§"))
				.collect(Collectors.toList());
		return this;
	}

	public IconBuilder glow() {
		itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);

		ItemMeta meta = itemStack.getItemMeta();

		if (meta != null) {
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemStack.setItemMeta(meta);
		}

		return this;
	}

	public IconBuilder setItemFlags(ItemFlag... flags) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.addItemFlags(flags);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public IconBuilder glow(boolean glow) {
		if (glow) {
			itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);

			ItemMeta meta = itemStack.getItemMeta();

			if (meta != null) {
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				itemStack.setItemMeta(meta);
			}
		}

		return this;
	}

	public IconBuilder setSkullTexture(String texture) {
		if (itemStack.getItemMeta() instanceof SkullMeta) {
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);

			profile.getProperties().put("textures", new Property("textures", texture));

			try {
				Field profileField = meta.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(meta, profile);

			} catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
				error.printStackTrace();
			}

			itemStack.setItemMeta(meta);
		}

		return this;
	}

	public IconBuilder setClickAction(Consumer<InventoryClickEvent> clickAction) {
		this.clickAction = clickAction;
		return this;
	}

	public Icon build() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			if (name != null) {
				meta.setDisplayName(name);
			}
			if (lore != null) {
				meta.setLore(lore);
			}
			itemStack.setItemMeta(meta);
		}
		Icon icon = new Icon(itemStack);

		if (clickAction != null) {
			icon.onClick(clickAction);
		}

		return icon;
	}
}