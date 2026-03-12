package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import cn.nukkit.form.response.SimpleResponse;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.BasicForm;

public class EditMovementForm extends BasicForm{

    public NexusEntity entity;

    public EditMovementForm(NexusEntity entity){
        super("Edit Movement");

        this.form.addButton("Idle")
                .addButton("Pathfinder");

        this.entity = entity;
    }

    @Override
    protected void handleForm(Player player, SimpleResponse selected) {
        this.entity.movementType = selected.buttonId();
        this.entity.serveMovement();

        player.sendMessage("NPC movement type has been updated.");
    }
}
