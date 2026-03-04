package io.hearlov.nexus.npc.forms.HFormAPI;

import cn.nukkit.Player;
import cn.nukkit.form.response.CustomResponse;
import cn.nukkit.form.window.CustomForm;

public abstract class HCustomForm{

    public final CustomForm form;

    public HCustomForm(String title){
        form = new CustomForm(title)
            .onSubmit(this::handleForm)
            .onClose(this::onClose);
    }

    public void sendForm(Player player){
        form.send(player);
    }

    protected abstract void handleForm(Player player, CustomResponse response);

    public void onClose(Player player){

    }

}
