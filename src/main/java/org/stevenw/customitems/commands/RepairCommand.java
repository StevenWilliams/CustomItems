package org.stevenw.customitems.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stevenw.customitems.CustomItem;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.ItemManager;
import org.stevenw.customitems.repair.RepairLevel;
import org.stevenw.customitems.repair.menus.RepairLevelsMenu;

import java.util.List;

public class RepairCommand implements CommandExecutor{
    private ItemManager manager;
    private CustomItems plugin;
    public RepairCommand(CustomItems plugin, ItemManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(manager.getItemType(itemInHand) != null) {
            CustomItem itemType = manager.getItemType(itemInHand);
            List<RepairLevel> repairLevels = itemType.getRepairLevels();
            if(repairLevels.size() > 0) {
                new RepairLevelsMenu(plugin, repairLevels, player).open(player);
            } else {
                player.sendMessage(ChatColor.RED + "This item cannot be repaired!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You must have the custom item in your main hand.");
        }
        return true;
    }
//use amps menus and prison requirements
}

