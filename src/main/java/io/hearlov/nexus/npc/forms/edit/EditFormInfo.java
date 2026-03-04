package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import cn.nukkit.form.response.CustomResponse;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.HCustomForm;

public class EditFormInfo extends HCustomForm{

    public final NexusEntity entity;

    public EditFormInfo(NexusEntity entity){
        super("Create NPC");
        this.entity = entity;
        form.addInput("NPC Name Text", "Ex: Info", entity.getName());
        form.addToggle("Name Always Visible");
    }


    @Override
    protected void handleForm(Player player, CustomResponse response){
        String name = response.getInputResponse(0);
        boolean nameVisible = response.getToggleResponse(1);

        entity.setNameTag(name);
        entity.setNameTagAlwaysVisible(nameVisible);

        player.sendMessage("This NPC has been updated according to the configured information.");
        player.sendMessage("Don’t forget to save the world using the /save-all command after finishing your NPC editing.");
    }

}
