package org.stevenw.customitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.stevenw.customitems.CustomItem;
import org.stevenw.customitems.CustomItems;

import java.util.List;

public class MobDrops implements Listener {
    private CustomItems plugin;
    public MobDrops(CustomItems plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void mobDeathEvent(EntityDeathEvent e) {
        EntityType type = e.getEntityType();
        List<CustomItem> items = plugin.getItemManager().getItems();
        for(CustomItem item : items)
        {
            if(item.getDropProbabilities().containsKey(type))
            {
                double probability = item.getDropProbabilities().get(type);
                if(Math.random() <= probability)
                {
                    e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), item.getItem(1));
                }
            }
        }
    }
}
