package cz.basicland.stattrack;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MacePlungeEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final double baseDmg, bonusDmg, fallDistance;
    private final ItemStack mace;

    public MacePlungeEvent(@NotNull Player player, ItemStack mace, double baseDmg, double bonusDmg, double fallDistance) {
        super(player);
        this.baseDmg = baseDmg;
        this.bonusDmg = bonusDmg;
        this.mace = mace;
        this.fallDistance = fallDistance;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public double getBaseDmg() {
        return baseDmg;
    }

    public double getBonusDmg() {
        return bonusDmg;
    }

    public double getFallDistance() {
        return fallDistance;
    }

    public ItemStack getMace() {
        return mace;
    }

}
