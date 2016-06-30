package org.stevenw.customitems.repair.menus;


import ninja.amp.ampmenus.events.ItemClickEvent;
import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stevenw.customitems.repair.RepairLevel;

import java.util.ArrayList;
import java.util.List;

public class RepairItem extends MenuItem {
    private final RepairLevel repairLevel;
    public RepairItem(RepairLevel repairLevel) {
        super("Repair", new ItemStack(Material.EXP_BOTTLE, 1));
        this.repairLevel = repairLevel;
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        ItemStack item = new ItemStack(Material.LADDER, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(repairLevel.canRepair(player, item)) {
            meta.setDisplayName(ChatColor.GREEN + "Repair");
            lore.add(ChatColor.GOLD + "Click to repair!");
        } else {
            meta.setDisplayName(ChatColor.RED + "Repair");
            lore.add("You cannot repair!");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(repairLevel.canRepair(event.getPlayer(), item)) {
            repairLevel.repair(event.getPlayer());
            event.getPlayer().sendMessage(ChatColor.GOLD + "You have added " + repairLevel.getRepairAmount() + " more uses.");
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot rankup!");
        }
    }
}
