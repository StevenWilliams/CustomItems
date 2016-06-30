package org.stevenw.customitems.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String tag, String[] args) {
        if(args.length < 2 || args[0] == null || args[1] == null || !(sender instanceof Player)) return false;

        Player player = (Player) sender;
        Material material = Material.getMaterial(args[0].toUpperCase());
        short damage = Short.valueOf(args[1]);

        ItemStack item = new ItemStack(material, 1, damage);
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        player.sendMessage("Item given");

        return true;
    }
}