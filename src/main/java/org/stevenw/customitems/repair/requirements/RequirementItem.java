package org.stevenw.customitems.repair.requirements;

import net.milkbowl.vault.item.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.repair.RepairRequirement;

import java.util.ArrayList;
import java.util.List;

public class RequirementItem extends RepairRequirement {
    private final CustomItems plugin;
    private final ItemStack item;

    public RequirementItem(CustomItems plugin, Material material, int amount) {
        super(plugin);
        this.plugin = plugin;
        this.item = new ItemStack(material, amount);
    }

    @Override
    public boolean isFulfilled(Player player) {
        return count(player) >= item.getAmount();
    }

    private int count(Player player) {
        int count = 0;
        for(ItemStack itemStack : player.getInventory()) {
            if(item.isSimilar(itemStack)) {
                count += itemStack.getAmount();
            }
        }
        return count;
    }

    @Override
    public String getName(Player player) {
        return item.getAmount() + " " + Items.itemByStack(item).getName();
    }
    @Override
    public List<String> getDescription(Player player){
        List<String> desc = new ArrayList<>();
        desc.add(ChatColor.GOLD + "Must have items in inventory!");
        return desc;
    }

    @Override
    public Material getDefaultMaterial() {
        return item.getType();
    }

    @Override
    public void repair(Player player) {
        player.getInventory().removeItem(item);
        player.updateInventory();
    }
}
