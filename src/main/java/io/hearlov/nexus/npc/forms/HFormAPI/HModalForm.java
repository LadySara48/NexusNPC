package io.hearlov.nexus.npc.forms.HFormAPI;

import cn.nukkit.Player;
import cn.nukkit.form.window.ModalForm;

public abstract class HModalForm{

    public final ModalForm form;

    public HModalForm(String title){
        form = new ModalForm(title)
                .onSubmit((player, response) -> handleForm(player, response.yes()))
                .onClose(this::onClose);
    }

    public void sendForm(Player player){
        form.send(player);
    }

    protected abstract void handleForm(Player player, boolean selected);

    public void onClose(Player player){

    }

}
