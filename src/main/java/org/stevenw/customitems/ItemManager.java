package org.stevenw.customitems;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stevenw.customitems.events.CustomItemUseEvent;
import org.stevenw.customitems.listeners.MobDrops;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ItemManager implements Listener {
    private JavaPlugin plugin;
    private ArrayList<CustomItem> itemTypes = new ArrayList<>();
    private ConfigurationSection root;

    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getPluginManager().registerEvents(new MobDrops((CustomItems) plugin), plugin);
    }

    public void loadItems(@NotNull ConfigurationSection itemsSection) {
        Set<String> items = itemsSection.getKeys(false);
        for(String itemName : items) {
            plugin.getLogger().info("Loading: " + itemName);
            itemTypes.add(new CustomItem((CustomItems) plugin, itemsSection.getConfigurationSection(itemName)));
        }
    }

    public List<CustomItem> getItems() {
        return itemTypes;
    }

    public void addItemType(CustomItem itemType) {
        itemTypes.add(itemType);
    }

   @Nullable
    public CustomItem getItemType(ItemStack stack) {
       if(!stack.hasItemMeta() || !stack.getItemMeta().spigot().isUnbreakable()) return null;
        for(CustomItem item : itemTypes) {
            if(item.getMaterial() == stack.getType()) {
                if (item.getDamageValue() == stack.getDurability()) return item;
            }
        }
        return null;
    }

    @Nullable
    public CustomItem getItemType(@NotNull String customItemName) {
        for(CustomItem item : itemTypes) {
            if(item.getName().equalsIgnoreCase(customItemName)) return item;
        }
        return null;
    }


    //make sure is left click. Block is broken or entity is damaged;
    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        if(!e.hasItem()) return;
        CustomItem itemType = getItemType(e.getItem());
        if(itemType != null) {
            if(!itemType.defaultActionEnabled()) {
                CustomItemUseEvent useEvent = new CustomItemUseEvent(e, itemType, e.getItem(), e.getPlayer());
                Bukkit.getServer().getPluginManager().callEvent(useEvent);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        CustomItem itemType = getItemType(item);
        if(itemType != null) {
            CustomItemUseEvent useEvent = new CustomItemUseEvent(e, itemType, item, e.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(useEvent);
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null) return;
        CustomItem itemType = getItemType(item);
        if(itemType != null) {
            CustomItemUseEvent useEvent = new CustomItemUseEvent(e, itemType, item, player);
            Bukkit.getServer().getPluginManager().callEvent(useEvent);
        }
    }

    private static float getUnbreakingEnchantProbability(int level) {
        float probability = 1;
        //why switch? and not probability = 1/(level + 1)
        switch (level) {
            case 0:
                probability = 1F;
                break;
            case 1:
                probability = 1F / 2F;
                break;
            case 2:
                probability = 1F / 3F;
                break;
            case 3:
                probability = 1F / 4F;
                break;
        }
        //System.out.println(probability);
        return probability;
    }

    @EventHandler
    public void customItemDamage(CustomItemUseEvent e) {
        ItemStack item = e.getItem();
        float random = new Random().nextFloat();
        //System.out.println(random);
        if (random > getUnbreakingEnchantProbability(item.getEnchantmentLevel(Enchantment.DURABILITY))) return;

        CustomItem itemType = e.getItemType();

        int usesLeft = itemType.getUsesLeft(item);
        if(usesLeft <= 1) {
            e.getPlayer().getInventory().setItemInMainHand(null);
            e.setCancelled(true);
        } else {
            item = itemType.setUsesLeft(item, itemType.getUsesLeft(item) - 1);
            e.getPlayer().getInventory().setItemInMainHand(item);
            ActionBarAPI.sendActionBar(e.getPlayer(), item.getItemMeta().getLore().get(0), 20);
        }
        e.getPlayer().updateInventory();
    }


}
