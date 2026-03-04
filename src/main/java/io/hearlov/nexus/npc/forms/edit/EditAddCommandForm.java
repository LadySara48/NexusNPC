package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import cn.nukkit.form.response.CustomResponse;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.HCustomForm;

public class EditAddCommandForm extends HCustomForm{

    private final NexusEntity entity;

    public EditAddCommandForm(NexusEntity entity){
        super("Add New Command");
        form.addInput("Command", "Ex: /msg {player} Hello");

        this.entity = entity;
    }

    @Override
    protected void handleForm(Player player, CustomResponse response) {
        String cmd = response.getInputResponse(0);
        entity.addCommand(cmd);
        player.sendMessage("Command Added: " + cmd);
        player.sendMessage("Don’t forget to save the world using the /save-all command after finishing your NPC editing.");
        (new EditCommandsForm(entity)).sendForm(player);
    }
}
