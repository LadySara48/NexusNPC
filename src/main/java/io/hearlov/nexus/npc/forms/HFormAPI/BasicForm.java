package io.hearlov.nexus.npc.forms.HFormAPI;

import cn.nukkit.Player;
import cn.nukkit.form.element.simple.ElementButton;
import cn.nukkit.form.response.SimpleResponse;
import cn.nukkit.form.window.SimpleForm;

public abstract class BasicForm{

    public final SimpleForm form;

    public BasicForm(String title){
        form = new SimpleForm(title)
            .onSubmit(this::handleForm)
            .onClose(this::onClose);
    }

    public void sendForm(Player player){
        form.send(player);
    }

    protected abstract void handleForm(Player player, SimpleResponse selected);

    public void onClose(Player player){

    }

}
