package io.hearlov.nexus.npc.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;

import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import io.hearlov.nexus.npc.entity.NexusEntity;

public class EntityListener implements Listener{

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if(entity instanceof NexusEntity){
            if(damager instanceof Player){
                ((NexusEntity) entity).onAttackOrInteract((Player) damager);
            }
            event.setCancelled();
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof NexusEntity){
            event.setCancelled();
        }
    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent event){
        Entity entity = event.getEntity();
        Player player = event.getPlayer();
        if(entity instanceof NexusEntity){
            ((NexusEntity) entity).onAttackOrInteract(player);
        }
    }

}
