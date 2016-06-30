package org.stevenw.customitems;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.stevenw.customitems.commands.GiveCommand;
import org.stevenw.customitems.commands.ItemCommand;
import org.stevenw.customitems.commands.RepairCommand;

public class CustomItems extends JavaPlugin {
    private ItemManager manager;
    private Economy econ = null;
    private Permission perms = null;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        setupEconomy();
        setupPermissions();
        manager = new ItemManager(this);
        manager.loadItems(getConfig().getConfigurationSection("Items"));
        getCommand("cigive").setExecutor(new GiveCommand());
        getCommand("ciitem").setExecutor(new ItemCommand(this));
        getCommand("cirepair").setExecutor(new RepairCommand(this, manager));
    }
    public ItemManager getItemManager() {
        return manager;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    public Economy getEconomy() {
        return this.econ;
    }
    public Permission getPermissions() {
        return this.perms;
    }

}
