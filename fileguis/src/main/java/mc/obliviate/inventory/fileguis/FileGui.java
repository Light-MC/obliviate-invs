package mc.obliviate.inventory.fileguis;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.GuiIcon;
import mc.obliviate.inventory.util.IconBuilder;
import me.nemo_64.spigotutilities.playerinputs.chatinput.PlayerChatInput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class FileGui extends Gui {

	String[] excludedKeys;
	YamlConfiguration config;
	ConfigurationSection section;
	JavaPlugin plugin;
	String name;
	File file;

	public FileGui(@Nonnull Player player, String name, File file, JavaPlugin plugin, String... excludedKeys) {
		super(player, file.getName() + "_gui", name, 6);

		this.excludedKeys = excludedKeys;
		this.config = YamlConfiguration.loadConfiguration(file);
		this.file = file;
		this.plugin = plugin;
		this.name = name;
	}

	private FileGui(@Nonnull Player player, String name, File file, ConfigurationSection config, JavaPlugin plugin, String... excludedKeys) {
		super(player, config.getName() + "_gui", name, 6);

		this.excludedKeys = excludedKeys;
		this.section = config;
		this.file = file;
		this.plugin = plugin;
		this.name = name;
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		fillGui(IconBuilder.fillIcon());
		addItemsFromYaml();
	}

	private void addItemsFromYaml() {
		Set<String> keys;

		if (section != null) {
			keys = section.getKeys(false);
		} else {
			keys = config.getKeys(false);
		}

		for (String key : keys) {
			if (excludedKeys != null) {
				for (String excludedKey : excludedKeys) {
					if (key.equalsIgnoreCase(excludedKey)) {
						continue;
					}
				}
			}

			if (config.isConfigurationSection(key)) {
				Material material = Material.CHEST;
				List<String> lore = new ArrayList<>();
				lore.add("&7Click to open");
				GuiIcon icon = new IconBuilder(material)
						.setName(key)
						.setLore(lore)
						.setClickAction(event -> {
							FileGui fileGui = new FileGui(player, name, file, config.getConfigurationSection(key), plugin, excludedKeys);
							fileGui.open();
						})
						.build();

				addItem(icon);
				continue;
			}

			Object value = config.get(key);
			Material material = getIconFromType(value);

			List<String> lore = new ArrayList<>();

			lore.add("Current Value: " + value);
			lore.add("Type: " + value.getClass().getSimpleName());
			lore.add("&8");
			lore.add("&7Click to edit");

			GuiIcon icon = new IconBuilder(material)
					.setName(key)
					.setLore(lore)
					.setClickAction(getClickAction(key, value))
					.build();

			addItem(icon);
		}

	}

	private Consumer<InventoryClickEvent> getClickAction(String key, Object value) {
		return event -> {
			PlayerChatInput.PlayerChatInputBuilder<Object> builder = new PlayerChatInput.PlayerChatInputBuilder<>(plugin, player);

			if (value instanceof String) {
				builder.isValidInput((p, str) -> true);
				builder.setValue((p, str) -> str);
			} else if (value instanceof Boolean) {
				builder.isValidInput((p, str) -> str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false"));
				builder.onInvalidInput((p, str) -> {
					p.sendMessage("Invalid input. Please type 'true' or 'false'");
					return true;
				});
				builder.setValue((p, str) -> str.equalsIgnoreCase("true"));
			} else if (value instanceof Integer) {
				builder.isValidInput((p, str) -> {
					try {
						Integer.parseInt(str);
						return true;
					} catch (NumberFormatException e) {
						return false;
					}
				});
				builder.onInvalidInput((p, str) -> {
					p.sendMessage("Invalid input. Please type a number");
					return true;
				});
				builder.setValue((p, str) -> Integer.parseInt(str));
			} else if (value instanceof Double) {
				builder.isValidInput((p, str) -> {
					try {
						Double.parseDouble(str);
						return true;
					} catch (NumberFormatException e) {
						return false;
					}
				});
				builder.onInvalidInput((p, str) -> {
					p.sendMessage("Invalid input. Please type a number");
					return true;
				});
				builder.setValue((p, str) -> Double.parseDouble(str));
			}

			builder.onFinish((p, newValue) -> {
				config.set(key, newValue);
				try {
					config.save(file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				plugin.reloadConfig();
				updateAllViewers();
				new FileGui(p, name, file, plugin, excludedKeys).open();
			});

			builder.onCancel((p) -> new FileGui(p, name, file, plugin, excludedKeys).open());

			PlayerChatInput<Object> input = builder.build();

			input.start();
		};
	}

	private Material getIconFromType(Object value) {
		if (value instanceof String) {
			return Material.PAPER;
		} else if (value instanceof Integer || value instanceof Double) {
			return Material.DIAMOND;
		} else if (value instanceof Boolean) {
			return Material.REDSTONE;
		}

		return Material.BARRIER;
	}

	private void updateAllViewers() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (player.getOpenInventory().getTopInventory().equals(this.getInventory())) {
				new FileGui(player, name, file, plugin, excludedKeys).open();
			}
		});
	}
}
