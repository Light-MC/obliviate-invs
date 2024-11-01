package mc.obliviate.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class CycleIcon<T> extends Icon {

    private T value;
    private final Map<T, ItemStack> toggleItems;
    private final Consumer<T> onSwitch;

    public CycleIcon(T initialValue, Map<T, ItemStack> toggleItems, Consumer<T> onSwitch) {
        super(toggleItems.get(initialValue));

        this.value = initialValue;
        this.toggleItems = toggleItems;
        this.onSwitch = onSwitch;
    }

    public T getValue() {
        return value;
    }

    public void nextValue() {
        value = toggleItems.keySet().stream().skip((new ArrayList<>(toggleItems.keySet()).indexOf(value) + 1) % toggleItems.size()).findFirst().get();
        onSwitch.accept(value);
    }

    @Override
    public @NotNull Icon onClick(Consumer<InventoryClickEvent> clickAction) {
        nextValue();
        return this;
    }
}
