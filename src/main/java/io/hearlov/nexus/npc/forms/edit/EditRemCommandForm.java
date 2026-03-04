package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.HModalForm;

public class EditRemCommandForm extends HModalForm {

    private final NexusEntity entity;
    private final String button;

    public EditRemCommandForm(NexusEntity entity, String button){
        super("Remove Command");
        form.content("Are you sure you want to remove this command: " + button);
        form.yesText("Yes");
        form.noText("No");

        this.entity = entity;
        this.button = button;
    }


    @Override
    protected void handleForm(Player player, boolean selected){
        if(selected){
            entity.removeCommand(button);
        }

        (new EditCommandsForm(entity)).sendForm(player);
        player.sendMessage("Command Removed: " + this.button);
        player.sendMessage("Don’t forget to save the world using the /save-all command after finishing your NPC editing.");
    }
}
