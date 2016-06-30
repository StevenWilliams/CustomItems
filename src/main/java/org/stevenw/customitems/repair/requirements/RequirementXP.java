package org.stevenw.customitems.repair.requirements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.stevenw.customitems.CustomItems;
import org.stevenw.customitems.repair.RepairRequirement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RequirementXP extends RepairRequirement {
    private final int xp;
    private final CustomItems plugin;
    public RequirementXP(CustomItems plugin, int amount) {
        super(plugin);
        this.plugin = plugin;
        this.xp = amount;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getTotalExperience() >= xp;
    }

    @Override
    public String getName(Player player) {
        return String.valueOf(xp) + " XP Points";
    }

    @Override
    public Material getDefaultMaterial() {
        return Material.EXP_BOTTLE;
    }

    @Override
    public void repair(Player player) {
        setTotalExperience(player, getTotalExperience(player) - xp);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> desc = new ArrayList<>();
        desc.add("You have " + getTotalExperience(player) + " XP Points");
        return desc;
    }

    //Adapted from https://gist.github.com/RichardB122/8958201b54d90afbc6f0
    public int getTotalExperience(Player player) {
        int experience = 0;
        int level = player.getLevel();
        if(level >= 0 && level <= 15) {
            experience = (int) Math.ceil(Math.pow(level, 2) + (6 * level));
            int requiredExperience = 2 * level + 7;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        } else if(level > 15 && level <= 30) {
            experience = (int) Math.ceil((2.5 * Math.pow(level, 2) - (40.5 * level) + 360));
            int requiredExperience = 5 * level - 38;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        } else {
            experience = (int) Math.ceil(((4.5 * Math.pow(level, 2) - (162.5 * level) + 2220)));
            int requiredExperience = 9 * level - 158;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        }
    }

    public void setTotalExperience(Player player, int xp) {
        //Levels 0 through 15
        if(xp >= 0 && xp < 351) {
            //Calculate Everything
            int a = 1; int b = 6; int c = -xp;
            int level = (int) (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
            int xpForLevel = (int) (Math.pow(level, 2) + (6 * level));
            int remainder = xp - xpForLevel;
            int experienceNeeded = (2 * level) + 7;
            float experience = (float) remainder / (float) experienceNeeded;
            experience = round(experience, 2);
            //System.out.println("xpForLevel: " + xpForLevel);
           // System.out.println(experience);

            //Set Everything
            player.setLevel(level);
            player.setExp(experience);
            //Levels 16 through 30
        } else if(xp >= 352 && xp < 1507) {
            //Calculate Everything
            double a = 2.5; double b = -40.5; int c = -xp + 360;
            double dLevel = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
            int level = (int) Math.floor(dLevel);
            int xpForLevel = (int) (2.5 * Math.pow(level, 2) - (40.5 * level) + 360);
            int remainder = xp - xpForLevel;
            int experienceNeeded = (5 * level) - 38;
            float experience = (float) remainder / (float) experienceNeeded;
            experience = round(experience, 2);
          //  System.out.println("xpForLevel: " + xpForLevel);
            //System.out.println(experience);

            //Set Everything
            player.setLevel(level);
            player.setExp(experience);
            //Level 31 and greater
        } else {
            //Calculate Everything
            double a = 4.5; double b = -162.5; int c = -xp + 2220;
            double dLevel = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
            int level = (int) Math.floor(dLevel);
            int xpForLevel = (int) (4.5 * Math.pow(level, 2) - (162.5 * level) + 2220);
            int remainder = xp - xpForLevel;
            int experienceNeeded = (9 * level) - 158;
            float experience = (float) remainder / (float) experienceNeeded;
            experience = round(experience, 2);
            //System.out.println("xpForLevel: " + xpForLevel);
           // System.out.println(experience);

            //Set Everything
            player.setLevel(level);
            player.setExp(experience);
        }
    }

    private float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.floatValue();
    }
}
