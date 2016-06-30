package org.stevenw.customitems.repair.menus;


import ninja.amp.ampmenus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.stevenw.customitems.repair.RepairRequirement;

public class RequirementItem extends MenuItem {
    private final RepairRequirement requirement;
    public RequirementItem(RepairRequirement requirement) {
        super("requirement", new ItemStack(Material.STONE, 1));
        this.requirement = requirement;
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        return requirement.getItem(player);
    }

}
