package io.hearlov.nexus.npc.forms.edit;

import cn.nukkit.Player;
import cn.nukkit.form.response.SimpleResponse;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.HFormAPI.BasicForm;

public class EditCommandsForm extends BasicForm{

    private final NexusEntity entity;
    private final int LastButton;

    public EditCommandsForm(NexusEntity entity){
        super("NPC Commands");
        this.entity = entity;
        int last = 0;
        for(String cmd : entity.getCommands()){
            form.addButton(cmd);
            last++;
        }

        form.addButton("Add New Command");
        LastButton = last;
    }

    @Override
    protected void handleForm(Player player, SimpleResponse selected) {
        if(selected.buttonId() == LastButton){
            (new EditAddCommandForm(entity)).sendForm(player);
        }else if(selected.button().text() != null){
            (new EditRemCommandForm(entity, selected.button().text())).sendForm(player);
        }
    }
}
