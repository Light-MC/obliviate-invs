package mc.obliviate.inventory;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MenuStackHandler {
    private static final Map<Player, Stack<Gui>> playerMenuMap = new HashMap<>();

    private final Stack<Gui> menuStack = new Stack<>();

    public static Gui getTopMenu(Player player) {
        initPlayerIfAbsent(player);
        return playerMenuMap.get(player).peek();
    }

    public static void pushMenu(Player player, Gui gui) {
        initPlayerIfAbsent(player);
        playerMenuMap.get(player).push(gui);
    }

    public static Gui getPreviousMenu(Player player) {
        Stack<Gui> stack = playerMenuMap.get(player);
        if (stack == null || stack.size() < 2) return null;

        return stack.get(stack.size() - 2);
    }

    public static void navigateBack(Player player) {
        Stack<Gui> stack = playerMenuMap.get(player);
        if (stack == null || stack.isEmpty()) return;

        stack.pop();
        Gui previousMenu = stack.isEmpty() ? null : stack.peek();
        if (previousMenu != null) {
            previousMenu.open();
        }
    }

    public static void clearMenu(Player player) {
        initPlayerIfAbsent(player);
        playerMenuMap.get(player).clear();
    }

    private static void initPlayerIfAbsent(Player player) {
        playerMenuMap.putIfAbsent(player, new Stack<>());
    }
}