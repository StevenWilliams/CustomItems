package org.stevenw.customitems.repair;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stevenw.customitems.CustomItem;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.repair.requirements.RequirementItem;
import org.stevenw.customitems.repair.requirements.RequirementMoney;
import org.stevenw.customitems.repair.requirements.RequirementXP;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RepairLevel {
    private int repairAmount;
    private CustomItems plugin;
    private CustomItem itemType;
    private List<RepairRequirement> repairRequirements = new ArrayList<>();
    private ConfigurationSection levelData;
    public RepairLevel(CustomItems plugin, CustomItem itemType, int repairAmount) {
        this.plugin = plugin;
        this.itemType = itemType;
        this.repairAmount = repairAmount;
        this.levelData = itemType.getConfig().getConfigurationSection("repair." + repairAmount);
        loadRepairRequirements();
    }
    public int getRepairAmount() {
        return this.repairAmount;
    }
    public List<RepairRequirement> getRequirements() {
        return this.repairRequirements;
    }


    public boolean canRepair(Player player, ItemStack itemStack) {
        if(itemType.getUsesLeft(itemStack) >= itemType.getMaxUses()) return false;
        for(RepairRequirement requirement : repairRequirements) {
            if(!requirement.isFulfilled(player)) {
                return false;
            }
        }
        return true;
    }

    public void repair(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if(!canRepair(player, item)) return;
        for(RepairRequirement requirement : repairRequirements) {
            requirement.repair(player);
        }
        int newUses = itemType.getUsesLeft(item) + this.repairAmount;
        ItemStack newItem = itemType.setUsesLeft(item, newUses);
        player.getInventory().setItemInMainHand(newItem);
        player.updateInventory();
        ActionBarAPI.sendActionBar(player, newItem.getItemMeta().getLore().get(0), 20);
    }

    private void loadRepairRequirements() {
        Set<String> repairRequirementsKeys = levelData.getKeys(false);
        for(String repairRequirementKey : repairRequirementsKeys) {
            if(repairRequirementKey.equalsIgnoreCase("money")) {
                long amount = levelData.getLong("money");
                repairRequirements.add(new RequirementMoney(plugin, amount));
            } else if (repairRequirementKey.equalsIgnoreCase("xp")){
                int amount = levelData.getInt("xp");
                repairRequirements.add(new RequirementXP(plugin, amount));
            } else if (repairRequirementKey.equalsIgnoreCase("items")) {
                Set<String> itemNames = levelData.getConfigurationSection("items").getKeys(false);
                for(String itemName : itemNames) {
                    Material material = Material.getMaterial(itemName);
                    int amount = levelData.getInt("items." + itemName);
                    repairRequirements.add(new RequirementItem(plugin, material, amount));
                }
            }
        }

    }

}
