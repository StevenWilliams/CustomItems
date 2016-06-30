package org.stevenw.customitems.repair.requirements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.repair.RepairRequirement;

import java.util.ArrayList;
import java.util.List;

public class RequirementMoney extends RepairRequirement {
    private final CustomItems plugin;
    private final double amount;
    public RequirementMoney(CustomItems plugin, long amount) {
        super(plugin);
        this.plugin = plugin;
        this.amount = amount;
    }
    @Override
    public String getType() {
        return this.getClass().getName();
    }

    @Override
    public boolean isFulfilled(Player player) {
        return plugin.getEconomy().getBalance(player) >= amount;
    }

    @Override
    public String getName(Player player) {
        return "$" + Math.round(amount);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> desc = new ArrayList<>();
        desc.add(ChatColor.GOLD + "You have $" + plugin.getEconomy().getBalance(player) + "/" + Math.round(amount) + " needed to rankup");
        return desc;
    }

    @Override
    public void repair(Player player) {
        plugin.getEconomy().withdrawPlayer(player, amount);
    }

    @Override
    public Material getDefaultMaterial() {
        return Material.GOLD_INGOT;
    }

}
