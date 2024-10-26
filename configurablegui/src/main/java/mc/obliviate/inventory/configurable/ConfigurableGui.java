package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.configurable.util.GuiSerializer;
import mc.obliviate.util.placeholder.PlaceholderUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigurableGui extends Gui {

    private final ConfigurableGuiCache guiCache = ConfigurableGuiCache.getCache(getId());
    private final GuiConfigurationTable guiConfigurationTable;

    public ConfigurableGui(@Nonnull Player player, @Nonnull String id) {
        this(player, id, GuiConfigurationTable.getDefaultConfigurationTable());
    }

    public ConfigurableGui(@Nonnull Player player, @Nonnull String id, @Nonnull GuiConfigurationTable guiConfigurationTable) {
        super(player, id, "No title found", 0);
        this.guiConfigurationTable = guiConfigurationTable;
        setTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(guiConfigurationTable.getMenusSection(getSectionPath()).getString(guiConfigurationTable.getTitleSectionName(), "No Title Found"))));
        setSize(guiConfigurationTable.getMenusSection(getSectionPath()).getInt(guiConfigurationTable.getSizeSectionName(), 0) * 9);
    }

    public List<DysfunctionalConfigIcon> getDysfunctionalIcons() {
        return super.getItems().values().stream()
                .filter(icon -> icon instanceof DysfunctionalConfigIcon)
                .map(icon -> (DysfunctionalConfigIcon) icon)
                .collect(Collectors.toList());
    }

    public ConfigurableGuiCache getGuiCache() {
        return this.guiCache;
    }

    public GuiConfigurationTable getGuiConfigurationTable() {
        return this.guiConfigurationTable;
    }

    public String getSectionPath() {
        return getId();
    }

    /**
     * Example section
     * <pre>example-gui:
     *   title: 'Test'
     *   row: 3
     *   icons:
     *     example-icon:
     *       material: STONE
     *       slot: 0</pre>
     *
     * @return Configuration section of Gui.
     */
    public ConfigurationSection getSection() {
        return this.guiConfigurationTable.getMenusSection(getSectionPath());
    }

    /**
     * Example section for<br>
     * subSection = "icons"
     *
     * <pre>
     * icons:
     *   example-icon:
     *     material: STONE
     *     slot: 0</pre>
     *
     * @param subSection name of any section that sub of gui section.
     * @return sub configuration section of gui section
     */
    public ConfigurationSection getSection(String subSection) {
        return getSection().getConfigurationSection(subSection);
    }

    public String getIconsSectionPath() {
        return getSectionPath() + "." + this.guiConfigurationTable.getIconsSectionName();
    }

    /**
     * Example section for<br>
     * iconSection = "example-icon"
     *
     * <pre>
     * example-icon:
     *   material: STONE
     *   slot: 0</pre>
     *
     * @param iconSection name of icon
     * @return sub configuration section of icons section
     */
    public ConfigurationSection getIconsSection(@Nonnull String iconSection) {
        return this.guiConfigurationTable.getMenusSection(getIconsSectionPath() + "." + iconSection);
    }

    /**
     * Example section
     * <pre>
     * icons:
     *   example-icon:
     *     material: STONE
     *     slot: 0</pre>
     *
     * @return sub configuration section of gui section
     */
    public ConfigurationSection getIconsSection() {
        return this.guiConfigurationTable.getMenusSection(getIconsSectionPath());
    }

    public int getConfigSlot(@Nonnull String sectionName) {
        return this.guiCache.getConfigSlot(getIconsSection(sectionName), this.guiConfigurationTable);
    }

    public ItemStack getConfigItem(@Nonnull String sectionName) {
        return this.guiCache.getConfigItem((getIconsSection(sectionName)), this.guiConfigurationTable);
    }

    public ItemStack getConfigItem(@Nonnull String sectionName, @Nullable PlaceholderUtil placeholderUtil) {
        return this.guiCache.getConfigItem(getIconsSection(sectionName), placeholderUtil, this.guiConfigurationTable);
    }

    public ConfigIcon getConfigIcon(@Nonnull String sectionName) {
        return new ConfigIcon(getConfigItem(sectionName), getIconsSection(sectionName), player);
    }

    public ConfigIcon getConfigIcon(@Nonnull String sectionName, @Nullable PlaceholderUtil placeholderUtil) {
        return new ConfigIcon(getConfigItem(sectionName, placeholderUtil), getIconsSection(sectionName), player);
    }

    public void putDysfunctionalIcons() {
        GuiSerializer.putDysfunctionalIcons(this, this.guiConfigurationTable,
                this.guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, new ArrayList<>(), player);
    }

    public void putDysfunctionalIcons(@Nullable PlaceholderUtil placeholderUtil) {
        GuiSerializer.putDysfunctionalIcons(this, this.guiConfigurationTable,
                this.guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, new ArrayList<>(), player);
    }

    public void putDysfunctionalIcons(@Nonnull List<String> functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, this.guiConfigurationTable,
                this.guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, functionalSlots, player);
    }

    public void putDysfunctionalIcons(@Nonnull String... functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, this.guiConfigurationTable,
                this.guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, Arrays.asList(functionalSlots), player);
    }

    public void putDysfunctionalIcons(@Nullable PlaceholderUtil placeholderUtil, @Nonnull String... functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, this.guiConfigurationTable,
                this.guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, Arrays.asList(functionalSlots), player);
    }

    public void putDysfunctionalIcons(@Nullable PlaceholderUtil placeholderUtil, @Nonnull List<String> functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, this.guiConfigurationTable,
                this.guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, functionalSlots, player);
    }

    /**
     * Use {@link #addConfigIcon(String)}
     */
    @Deprecated
    public void putIcon(@Nonnull String configName) {
        addItem(getConfigSlot(configName), getConfigIcon(configName));
    }

    /**
     * Use {@link #addConfigIcon(String, PlaceholderUtil)}
     */
    @Deprecated
    public void putIcon(@Nonnull String configName, @Nullable PlaceholderUtil placeholderUtil) {
        addItem(getConfigSlot(configName), getConfigIcon(configName, placeholderUtil));
    }

    /**
     * Use {@link #addConfigIcon(String)} and {@link Icon#onClick(Consumer<InventoryClickEvent>)}<br>
     * <p>
     * Deprecated: {@code putIcon("any-icon", e -> {})}<br>
     * Modern: {@code addConfigIcon("any-icon").onClick({})}
     */
    @Deprecated
    public void putIcon(@Nonnull String configName, @Nonnull Consumer<InventoryClickEvent> click) {
        addItem(getConfigSlot(configName), getConfigIcon(configName).onClick(click));
    }

    /**
     * Use {@link #addConfigIcon(String, PlaceholderUtil)} and {@link Icon#onClick(Consumer<InventoryClickEvent>)}<br>
     * <p>
     * Deprecated: {@code putIcon("any-icon", placeholderUtil, e -> {})}<br>
     * Modern: {@code addConfigIcon("any-icon", placeholderUtil).onClick({})}
     */
    @Deprecated
    public void putIcon(@Nonnull String configName, @Nullable PlaceholderUtil placeholderUtil, @Nonnull Consumer<InventoryClickEvent> click) {
        addItem(getConfigSlot(configName), getConfigIcon(configName, placeholderUtil).onClick(click));
    }

    @Nonnull
    public ConfigIcon addConfigIcon(@Nonnull String configName, @Nullable PlaceholderUtil placeholderUtil, Function<ItemStack, ItemStack> refactorFunction) {
        final ConfigIcon icon = new ConfigIcon(refactorFunction.apply(getConfigItem(configName, placeholderUtil)), getIconsSection(configName), player);
        addItem(getConfigSlot(configName), icon);
        return icon;
    }

    @Nonnull
    public ConfigIcon addConfigIcon(@Nonnull String configName, Function<ItemStack, ItemStack> refactorFunction) {
        final ConfigIcon icon = new ConfigIcon(refactorFunction.apply(getConfigItem(configName)), getIconsSection(configName), player);
        addItem(getConfigSlot(configName), icon);
        return icon;
    }

    public ConfigIcon addConfigIcon(@Nonnull String configName) {
        final ConfigIcon icon = getConfigIcon(configName);
        addItem(getConfigSlot(configName), icon);
        return icon;
    }

    public ConfigIcon addConfigIcon(@Nonnull String configName, @Nullable PlaceholderUtil placeholderUtil) {
        final ConfigIcon icon = getConfigIcon(configName, placeholderUtil);
        addItem(getConfigSlot(configName), icon);
        return icon;
    }
}