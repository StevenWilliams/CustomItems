package org.stevenw.customitems.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.stevenw.customitems.CustomItem;
import org.stevenw.customitems.CustomItems;

import java.util.List;

public class ItemCommand implements CommandExecutor {
    private CustomItems plugin;
    public ItemCommand(CustomItems plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if(args.length < 2) {
            sender.sendMessage("Loaded item types:");
            List<CustomItem> itemTypes = plugin.getItemManager().getItems();
            for(CustomItem itemType : itemTypes) {
                sender.sendMessage(itemType.getName());
            }
            return false;
        }
        String customItemName = args[0];
        Player player;
        int amount = 1;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            player = Bukkit.getPlayer(args[1]);
        }
        if(args[2] != null) {
            amount = Integer.valueOf(args[2]);
        }

        CustomItem item = plugin.getItemManager().getItemType(customItemName);
        if(item == null) {
            sender.sendMessage("Item " + customItemName + " not found!");
            return false;
        }
        player.getInventory().addItem(item.getItem(amount));
        sender.sendMessage("Item given!");
        player.sendMessage("You have received " + amount + " " + item.getDisplayName());
        return true;
    }
}
