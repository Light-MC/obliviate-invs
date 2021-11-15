# ObliviateInvs

Obliviate Invs is a inventory GUI api for Bukkit.

## Features
- Create custom GUIs easly
- Create advanced slots like real GUI slots. (Enchantment Table Slots, Anvil Slots etc.)
- Make advanced custom GUI icons! edit their names, lores, enchantments, flags, click/drag actions...
- Order your data sets with easy-to-use pagination algorithms.
- Automatic update methods.
- Tested and experienced in live.
- Use onClose(), onOpen(), onClick() events.
- Put GUI a lot of icons with fillColumn(), fillRow(), fillGui() methods.


## How to use?

```java
public class SelectServerGUI extends GUI {

	public SelectServerGUI(Player player) {
		super(player, "selcet-server-gui", "Select Server to Connect", 6);
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {

		//add stone to first available slot of gui
		addItem(Material.STONE); 

		//add stone to first available slot of gui
		addItem(new ItemStack(Material.STONE));

		//add emerald to 13. slot of gui
		addItem(13, new ItemStack(Material.EMERALD));

		//add emerald to 14. slot of gui
		addItem(14, new Icon(Material.EMERALD));

		//add emerald named 'GREEN DIAMOND' to 15. slot of gui
		addItem(15, new Icon(Material.EMERALD).setName("Green Diamond"));

		//create custom icon. set their name and lore.
		Icon anIcon = new Icon(Material.GRASS).setName("Skyblock Server").setLore("You wanna play skyblock?", "");

		if (!player.isOp()) {
			//if player doesn't have permission, append new lore
			anIcon.appendLore("We are in maintenance!", "come back later!"); 
		} else {
			//if player is op, append another lore
			anIcon.appendLore("Click to connect!"); 
		}

		anIcon.setAmount(10); // set amount of icon

		anIcon.hideFlags(); // hide flags of icon (ex: damage of sword)

		anIcon.enchant(Enchantment.ARROW_DAMAGE, 100); //enchant the icon

		anIcon.setDamage(10); // set damage of icon

		anIcon.onClick(e -> { //define click event as variable named 'e'
			player.sendMessage("You clicked the icon!"); //send message when player clicked
			e.getCursor(); //you can use click action as 'e' variable
		});

		anIcon.onDrag(e -> { //define drag event as variable named 'e'
			player.sendMessage("You draged the icon!"); //send message when player clicked
			e.getCursor(); //you can use drag action as 'e' variable
		});

		updateTask(0, 20, update -> {
			player.sendMessage("hi"); //say hi every second until gui is closed
		});


		/*
		Other GUI class methods

		getInventory(); bukkit inventory of gui
		getAdvancedSlotManager(); advanced slot manager of gui
		getId(); id of gui
		getItems(); item map of gui
		getPagination(); pagination manager of gui
		getPlugin(); You plugin instance
		getSize(); row amount of the gui
		getTitle(); title text of the gui
		 */
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		player.sendMessage("You clicked on this gui!");
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		open(); //update menu
		player.sendMessage("You can not close this menu!");
	}


}


```