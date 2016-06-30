package org.stevenw.customitems.repair.menus;

import ninja.amp.ampmenus.items.BackItem;
import ninja.amp.ampmenus.menus.ItemMenu;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.repair.RepairLevel;
import org.stevenw.customitems.repair.RepairRequirement;

import java.util.Collection;

public class RepairLevelMenu extends ItemMenu {
    public RepairLevelMenu(CustomItems plugin, ItemMenu parent, RepairLevel level) {
        super(level.getRepairAmount() + " more uses", Size.fit(9 + level.getRequirements().size()), plugin, parent);
        setItem(0, new BackItem());
        setItem(8, new RepairItem(level));
        int i = 9;
        Collection<RepairRequirement> requirements = level.getRequirements();
        for(RepairRequirement requirement : requirements) {
            setItem(i, new RequirementItem(requirement));
            i++;
        }
    }

}
