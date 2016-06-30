package org.stevenw.customitems.repair;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stevenw.customitems.CustomItems;

import java.util.List;

public abstract class RepairRequirement {
    private final CustomItems plugin;
    protected RepairRequirement(CustomItems plugin) {
        this.plugin = plugin;
    }

    public String getType() {
        return this.getClass().getName();
    }
    public abstract boolean isFulfilled(Player player);
    public abstract String getName(Player player); //ChatColor stripped
    public List<String> getDescription(Player player) {
        return null;
    }
    public void repair(Player player) {
    }
    public abstract Material getDefaultMaterial();
    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(getDefaultMaterial());
        ItemMeta meta = item.getItemMeta();
        if(isFulfilled(player)) {
            meta.setDisplayName(ChatColor.GREEN + ChatColor.stripColor(getName(player)));
        } else {
            meta.setDisplayName(ChatColor.RED + ChatColor.stripColor(getName(player)));
        }
        meta.setLore(getDescription(player));
        item.setItemMeta(meta);
        return item;
    }
}
