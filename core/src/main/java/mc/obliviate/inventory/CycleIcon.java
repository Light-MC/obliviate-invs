package mc.obliviate.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CycleIcon<T extends Enum<T>> extends Icon {
    private T value;
    private final List<T> values; // Store the enum values in a list
    private final Map<T, ItemStack> toggleItems;
    private final Consumer<T> onSwitch;

    public CycleIcon(T initialValue, Map<T, ItemStack> toggleItems, Consumer<T> onSwitch) {
        super(toggleItems.get(initialValue)); // Use the item for the initial value
        this.value = initialValue;
        this.toggleItems = toggleItems;
        this.values = new ArrayList<>(toggleItems.keySet()); // Initialize the list of values
        this.onSwitch = onSwitch; // Ensure onSwitch is initialized
    }

    public T getValue() {
        return this.value;
    }

    public void nextValue() {
        // Get the index of the current value
        int currentIndex = values.indexOf(this.value);
        // Calculate the next index, wrapping around if necessary
        int nextIndex = (currentIndex + 1) % values.size();
        this.value = values.get(nextIndex); // Update the value to the next
        this.onSwitch.accept(this.value); // Call the consumer with the new value
    }

    public @NotNull Icon onClick(Consumer<InventoryClickEvent> clickAction) {
        // Handle click action and switch to the next value
        this.nextValue();
        return this;
    }
}
