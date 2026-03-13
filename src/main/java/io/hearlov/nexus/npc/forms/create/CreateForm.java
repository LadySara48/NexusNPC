package io.hearlov.nexus.npc.forms.create;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.response.CustomResponse;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.HCustomForm;

public class CreateForm extends HCustomForm{

    public CreateForm(){
        super("Create NPC");
        form.addInput("NPC Name Text", "Ex: Info");
        form.addToggle("Your Equality");
        form.addToggle("Name Always Visible");
    }

    @Override
    protected void handleForm(Player player, CustomResponse response){
        String name = response.getInputResponse(0);
        boolean equality = response.getToggleResponse(1);
        boolean nameVisible = response.getToggleResponse(2);

        CompoundTag nbt = Entity.getDefaultNBT(player);
        IChunk chunk = player.getChunk();
        NexusEntity npc = new NexusEntity(chunk, nbt);
        npc.setNameTag(name);
        npc.setSkin(player.getSkin());
        npc.setNameTagAlwaysVisible(nameVisible);

        if(equality){
            Item[] items = player.getInventory().getArmorContents();
            npc.getInventory().setArmorContents(items);
            Item item = player.getInventory().getItemInHand();
            npc.getInventory().setItemInHand(item);
        }

        npc.spawnToAll();
        player.sendMessage("NPC Created: " + npc.getId());
        player.sendMessage("You can type '/npc edit' and click on the NPC to edit it.");
    }

}
