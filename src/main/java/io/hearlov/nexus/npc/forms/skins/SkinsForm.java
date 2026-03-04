package io.hearlov.nexus.npc.forms.skins;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.form.response.SimpleResponse;
import io.hearlov.nexus.npc.skin.SkinCache;
import cn.nukkit.Player;

import io.hearlov.nexus.npc.forms.HFormAPI.BasicForm;

import java.util.Map;

public class SkinsForm extends BasicForm{

    public final Map<String, Skin> skins;

    public SkinsForm(){
        super("Skins");
        this.skins = SkinCache.getCachedSkins();

        for(String name : skins.keySet()){
            form.addButton(name);
        }
    }

    @Override
    protected void handleForm(Player player, SimpleResponse selected){
        String name = selected.button().text();

        if(!name.isEmpty()){
            Skin skin = skins.get(name);

            player.setSkin(skin);
        }
    }
}
