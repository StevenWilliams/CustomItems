package org.stevenw.customitems;

import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stevenw.customitems.repair.RepairLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CustomItem {
    //private CustomItems plugin;
    private short damage;
    private String name;
    private Material material;
    @Nullable  private String displayName;
    private ConfigurationSection config;
    private int maxUses;
    private boolean defaultAction;
    private List<RepairLevel> repairLevels = new ArrayList<>();
    private CustomItems plugin;
    private HashMap<EntityType, Double> dropProbabilities = new HashMap<>();

    public CustomItem(CustomItems plugin, @NotNull ConfigurationSection config) {
        this.plugin = plugin;
        this.config = config;
        this.name = config.getName();
        this.displayName = ChatColor.translateAlternateColorCodes('&', config.getString("display-name"));
        this.damage = (short) config.getInt("durability-value");
        this.maxUses = getConfig().getInt("uses");
        this.material = Material.getMaterial(config.getString("base"));
        this.defaultAction = getConfig().getBoolean("use-default-action", true);
        loadRepairLevels();
        loadDropProbabilities();
    }
    public ConfigurationSection getConfig() {
        return this.config;
    }

    private void loadDropProbabilities() {
        if(config.getConfigurationSection("drops") == null) return;
        Set<String> mobs = config.getConfigurationSection("drops").getKeys(false);
        for(String mob : mobs) {
            EntityType type = EntityType.valueOf(mob.toUpperCase());
            double probability = config.getDouble("drops." + mob);
            dropProbabilities.put(type, probability);
        }

    }
    private void loadRepairLevels() {
        if(config.getConfigurationSection("repair") == null) return;
        Set<String> levelAmounts = config.getConfigurationSection("repair").getKeys(false);
        if(levelAmounts.size() > 0) {
            for (String levelAmount : levelAmounts) {
                int repairAmount = Integer.valueOf(levelAmount);
                repairLevels.add(new RepairLevel(plugin, this, repairAmount));
            }
        }
    }

    @NotNull
    public ItemStack getItem(int amount){
        ItemStack item = new ItemStack(material, amount, damage);
        ItemMeta meta = item.getItemMeta();
        if (displayName != null) {
            meta.setDisplayName(displayName);
        } else {
          meta.setDisplayName(name);
        }
        meta.spigot().setUnbreakable(true);
     //   meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        if(defaultAction) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        } else {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_UNBREAKABLE);
        }
        item.setItemMeta(meta);
        item = setUsesLeft(item, maxUses);
        return item;
    }

    @NotNull
    public HashMap<EntityType, Double> getDropProbabilities() {
        return dropProbabilities;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public Material getMaterial() {
        return material;
    }

    public short getDamageValue() {
        return damage;
    }

    public int getUsesLeft(@NotNull ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        return ((nbt.hasKey("cuses")&& nbt.getInteger("cuses") != 0) ? nbt.getInteger("cuses") : maxUses);
    }

    public ItemStack setUsesLeft(@NotNull ItemStack item, int value) {
        NBTItem nbt = new NBTItem(item);
        nbt.setInteger("cuses", value);
        item = nbt.getItem();
        ItemMeta meta = item.getItemMeta();
        List<String> lore;
        if(meta.getLore() != null)  {
           lore =  meta.getLore();
           lore.set(0, ChatColor.AQUA + String.valueOf(value) + " uses left");
        } else {
            lore = new ArrayList<>();
           lore.add( ChatColor.AQUA + String.valueOf(value) + " uses left");
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public boolean defaultActionEnabled() {
        return defaultAction;
    }



    public List<RepairLevel> getRepairLevels() {
        return this.repairLevels;
    }

}
