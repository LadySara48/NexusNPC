package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import cn.nukkit.form.response.SimpleResponse;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.BasicForm;

public class EditFormMain extends BasicForm{

    public final NexusEntity entity;

    public EditFormMain(NexusEntity entity){
        super("Editing NPC");
        form.addButton("Edit NPC Info");
        form.addButton("Reload Skin (Set your skin)");
        form.addButton("Select Skin");
        form.addButton("NPC Commands");
        form.addButton("NPC Movement");
        this.entity = entity;
    }


    @Override
    protected void handleForm(Player player, SimpleResponse selected){
        switch (selected.buttonId()){
            case 0:
                (new EditFormInfo(entity)).sendForm(player);
                break;
            case 1:
                entity.setSkin(player.getSkin());
                form.send(player);
                player.sendMessage("This NPC's skin has been updated again. (When you rejoin the server, your NPC will appear)");
                player.sendMessage("Don’t forget to save the world using the /save-all command after finishing your NPC editing.");
                break;
            case 2:
                (new EditSelectSkinForm(entity)).sendForm(player);
                break;
            case 3:
                (new EditCommandsForm(entity)).sendForm(player);
                break;
            case 4:
                (new EditMovementForm(entity)).sendForm(player);
                break;
        }
    }
}
