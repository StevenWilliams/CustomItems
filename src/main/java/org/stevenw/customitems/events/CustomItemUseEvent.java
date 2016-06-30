package org.stevenw.customitems.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.stevenw.customitems.CustomItem;

public class CustomItemUseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private CustomItem itemType;
    private ItemStack item;
    private Player player;
    private Event parentEvent;

    public CustomItemUseEvent(Event parentEvent, CustomItem itemType, ItemStack item, Player player) {
        this.parentEvent = parentEvent;
        this.itemType = itemType;
        this.item = item;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public CustomItem getItemType() {
        return itemType;
    }

    public ItemStack getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public Event getParentEvent() {
        return parentEvent;
    }
}
