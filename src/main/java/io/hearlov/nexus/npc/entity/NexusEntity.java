package io.hearlov.nexus.npc.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;
import io.hearlov.nexus.npc.NexusNPC;
import io.hearlov.nexus.npc.entity.pathfinder.PathfinderManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NexusEntity extends EntityHuman implements CustomEntity{

    public List<String> command;
    public NPCCommandSender commandSender;
    public boolean NameTagAlwaysVisible;
    public int movementType;

    public Vector3 vector3;

    public static final String IDENTIFIER = "minecraft:player";
    @Override public @NotNull String getIdentifier(){ return IDENTIFIER; }

    public NexusEntity(IChunk chunk, CompoundTag nbt){
        super(chunk, nbt);
    }

    @Override
    protected void initEntity(){
        super.initEntity();

        this.NameTagAlwaysVisible = this.namedTag.getBoolean("NTV");
        this.movementType = this.namedTag.getInt("movement");

        setNameTagVisible(true);
        if(this.NameTagAlwaysVisible) super.setNameTagAlwaysVisible(true);
        if(this.namedTag.containsString("NameTag")) super.setNameTag(namedTag.getString("NameTag"));

        List<StringTag> commands = namedTag.getList("commands", StringTag.class).getAll();
        List<String> cmnds = new ArrayList<>();
        for(StringTag tag : commands){
            cmnds.add(tag.data);
        }

        CompoundTag dTag = this.namedTag.getCompound("fpos");
        if(dTag.isEmpty()) this.vector3 = this.getPosition().getVector3();
        else this.vector3 = new Vector3(dTag.getInt("x"), dTag.getInt("y"), dTag.getInt("z"));

        this.command = cmnds;
        commandSender = new NPCCommandSender(this);

        this.serveMovement();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        CompoundTag dTag = new CompoundTag();
        dTag.putInt("x", this.vector3.getFloorX());
        dTag.putInt("y", this.vector3.getFloorY());
        dTag.putInt("z", this.vector3.getFloorZ());

        if(!this.command.isEmpty()){
            ListTag<StringTag> tag = new ListTag<>(ListTag.TAG_String);
            for(String cmnd : this.command){
                tag.add(new StringTag(cmnd));
            }
            this.namedTag.putList("commands", tag);
        }

        this.namedTag.putBoolean("NTV", this.NameTagAlwaysVisible);
        this.namedTag.putInt("movement", this.movementType);
        this.namedTag.putCompound("fpos", dTag);
    }

    @Override
    public void setNameTag(String name) {
        this.namedTag.putString("NameTag", name);
        super.setNameTag(name);
    }

    @Override
    public void setNameTagAlwaysVisible(boolean nameTagAlwaysVisible) {
        this.NameTagAlwaysVisible = nameTagAlwaysVisible;
        super.setNameTagAlwaysVisible(nameTagAlwaysVisible);
    }

    public void removeCommand(String cmd){
        this.command.remove(cmd);
    }

    public void addCommand(String cmd){
        if(this.command == null) this.command = new ArrayList<>();
        this.command.add(cmd);
    }

    public List<String> getCommands(){
        return this.command;
    }

    public void onAttackOrInteract(Player player){
        if(EntityListen.isInteractive(player)){
            EntityListen.onInteract(player, this);
            return;
        }
        for(String cmnd : command){
            String cmd = cmnd.replace("{player}", player.getName());
            player.getServer().executeCommand(commandSender, cmd);
        }
    }

    public void serveMovement(){
        if(this.vector3 == null || this.movementType == 0){
            if(PathfinderManager.getInstance().isRunning(this)) PathfinderManager.getInstance().stop(this);
            return;
        }

        if(!PathfinderManager.getInstance().isRunning(this)) PathfinderManager.getInstance().start(NexusNPC.getInstance(), this);
    }

    public Vector3 getFirstVector(){
        return this.vector3;
    }

    @Override
    public void setSkin(Skin skin){
        super.setSkin(skin);
    }

    @SuppressWarnings("unused")
    public static CustomEntityDefinition definition(){
        return CustomEntityDefinition.simpleBuilder(IDENTIFIER)
                .eid(IDENTIFIER)
                .hasSpawnEgg(false) // If true, a spawn egg of this entity will be generated and can be found in the creative inventory
                .isSummonable(false) // If false, you cannot spawn this entity by commands
                .originalName("Human") // A display name for the entity
                .maxHealth(220) // Define the max health
                .attack(0) // If the mob can attack you can define here its melee power
                .movement(0.1f, 7.0f) // Default movement speed
                .typeFamily("human") // Type family of the mob, useful for filterings on API and commands
                .collisionBox(2f, 2f) // Collision box of the entity, where you can hit/interact with it
                .knockbackResistance(0.6f) // The entity resistance against physical attacks
                .maxAutoStep(1.0625f, 1.0625f, 0.5625f) // You can define the max auto-step of the entity, making it move over blocks without need to jump
                .isPersistent(true) // If set true, the entity never despawns from distance
                .build();
    }
}