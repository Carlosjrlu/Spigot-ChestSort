package de.jeffclan.JeffChestSort;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JeffChestSortSettingsGUI implements Listener {
	
	JeffChestSortPlugin plugin;
	
	public static int slotMiddleClick = 1 + 9;
	public static int slotShiftClick = 3 + 9;
	public static int slotDoubleClick = 5 + 9;
	public static int slotShiftRightClick = 7 + 9;
	
	final static Material red = Material.REDSTONE_BLOCK;
	final static Material green = Material.EMERALD_BLOCK;
	
	enum Hotkey {
		MiddleClick, ShiftClick, DoubleClick, ShiftRightClick;
	}
	
	JeffChestSortSettingsGUI(JeffChestSortPlugin plugin) {
		this.plugin=plugin;
	}
	
	ItemStack getItem(boolean active, Hotkey hotkey) {
		ItemStack is = null;
		String suffix;
		
		if(active) {
			is = new ItemStack(green);
			suffix = plugin.messages.MSG_GUI_ENABLED;
		}
		else {
			is = new ItemStack(red);
			suffix = plugin.messages.MSG_GUI_DISABLED;
		}
		
		ItemMeta meta = is.getItemMeta();
				
		switch(hotkey) {
		case MiddleClick:
			meta.setDisplayName(ChatColor.RESET + plugin.messages.MSG_GUI_MIDDLECLICK + ": " + suffix);
			break;
		case ShiftClick:
			meta.setDisplayName(ChatColor.RESET + plugin.messages.MSG_GUI_SHIFTCLICK + ": " + suffix);
			break;
		case DoubleClick:
			meta.setDisplayName(ChatColor.RESET + plugin.messages.MSG_GUI_DOUBLECLICK + ": " + suffix);
			break;
		case ShiftRightClick:
			meta.setDisplayName(ChatColor.RESET + plugin.messages.MSG_GUI_SHIFTRIGHTCLICK + ": " + suffix);
			break;
		default:
			break;
		}
		
		is.setItemMeta(meta);
		
		return is;
	}
	
    void openGUI(Player player) {
        Inventory inventory = createGUI("ChestSort", player);
        
        JeffChestSortPlayerSetting setting = plugin.perPlayerSettings.get(player.getUniqueId().toString());
        
        inventory.setItem(slotMiddleClick, getItem(setting.middleClick,Hotkey.MiddleClick));
        inventory.setItem(slotShiftClick, getItem(setting.shiftClick,Hotkey.ShiftClick));
        inventory.setItem(slotDoubleClick, getItem(setting.doubleClick,Hotkey.DoubleClick));
        inventory.setItem(slotShiftRightClick, getItem(setting.shiftRightClick,Hotkey.ShiftRightClick));
        
        setting.guiInventory = inventory;
        player.openInventory(inventory);
    }
    
    Inventory createGUI(String name, Player inventoryHolder) {
        Inventory inventory = Bukkit.createInventory(inventoryHolder, InventoryType.CHEST, name);
        return inventory;
    }
    
    @EventHandler
	void onGUIInteract(InventoryClickEvent event) {
		if(plugin.hotkeyGUI==false) {
			return;
		}
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) event.getWhoClicked();
		plugin.listener.plugin.registerPlayerIfNeeded(p);
		JeffChestSortPlayerSetting setting = plugin.perPlayerSettings.get(p.getUniqueId().toString());
		
		if(setting.guiInventory==null) {
			return;
		}
		
		if(event.getClickedInventory()==null) {
			return;
		}
		if(!event.getClickedInventory().equals(setting.guiInventory)) {
			return;
		}
		
		// We only get this far if the player has clicked inside his GUI inventory
		event.setCancelled(true);
		if(event.getClick() != ClickType.LEFT) {
			return;
		}
		
		if(event.getSlot() == JeffChestSortSettingsGUI.slotMiddleClick) {
			setting.toggleMiddleClick();
			plugin.settingsGUI.openGUI(p);
			return;
		}
		else if(event.getSlot() == JeffChestSortSettingsGUI.slotShiftClick) {
			setting.toggleShiftClick();
			plugin.settingsGUI.openGUI(p);
			return;
		} else 	if(event.getSlot() == JeffChestSortSettingsGUI.slotDoubleClick) {
			setting.toggleDoubleClick();
			plugin.settingsGUI.openGUI(p);
			return;
		} else if(event.getSlot() == JeffChestSortSettingsGUI.slotShiftRightClick) {
			setting.toggleShiftRightClick();
			plugin.settingsGUI.openGUI(p);
			return;
		}
		
	}
}
