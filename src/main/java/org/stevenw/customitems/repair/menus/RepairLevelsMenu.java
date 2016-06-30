package org.stevenw.customitems.repair.menus;

import ninja.amp.ampmenus.items.CloseItem;
import ninja.amp.ampmenus.items.MenuItem;
import ninja.amp.ampmenus.items.SubMenuItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.repair.RepairLevel;

import java.util.List;

public class RepairLevelsMenu extends ItemMenu{
    public RepairLevelsMenu(CustomItems plugin, List<RepairLevel> repairLevels, Player player) {
        super("Repair", Size.FOUR_LINE, plugin);
        CloseItem close = new CloseItem();
        MenuItem.setNameAndLore(new ItemStack(Material.FENCE_GATE, 1), ChatColor.RED + "Close", null); //TODO: not changing
        setItem(0, close);
        int i = 18;
        for(RepairLevel level : repairLevels) {
                RepairLevelMenu rankmenu = new RepairLevelMenu(plugin, this, level);
                setItem(i, new SubMenuItem(plugin, ChatColor.GOLD + "" + ChatColor.BOLD + level.getRepairAmount() + " more uses", new ItemStack(Material.EXP_BOTTLE, 1), rankmenu));
                i++;
        }
    }

}
