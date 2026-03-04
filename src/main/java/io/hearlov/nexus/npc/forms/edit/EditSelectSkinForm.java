package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.form.response.SimpleResponse;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.BasicForm;
import io.hearlov.nexus.npc.skin.SkinCache;

import java.util.Map;

public class EditSelectSkinForm extends BasicForm{

    public final NexusEntity entity;

    public EditSelectSkinForm(NexusEntity entity){
        super("Select Skin");
        this.entity = entity;

        Map<String, Skin> skins = SkinCache.getCachedSkins();
        for(String skinName : skins.keySet()){
            form.addButton(skinName);
        }
    }

    @Override
    protected void handleForm(Player player, SimpleResponse selected){
        String button = selected.button().text();

        Skin skin = SkinCache.getCachedSkins().get(button);
        entity.setSkin(skin);

        (new EditFormMain(entity)).sendForm(player);
        player.sendMessage("This NPC's skin has been updated again. (When you rejoin the server, your NPC will appear)");
        player.sendMessage("Don’t forget to save the world using the /save-all command after finishing your NPC editing.");
    }

    @Override
    public void onClose(Player player){
        if(player.isOnline()) (new EditFormMain(entity)).sendForm(player);
    }
}
