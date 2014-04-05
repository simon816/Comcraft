package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerData extends Packet {

    private static final int ACTION_JOIN = 1;
    private static final int ACTION_QUIT = 2;
    private static final int ACTION_MOVE = 3;
    private static final int ACTION_INVENTORY = 4;
    private EntityPlayer player = null;

    private int action;
    private int pId;
    private Object data = null;
    private int itmIndex;

    public PacketPlayerData() {
    }

    public PacketPlayerData(EntityPlayer player) {
        this.player = player;
    }

    public PacketPlayerData(int id, Vec3D position) {
        data = position;
        pId = id;
    }

    public PacketPlayerData(int index, InvItemStack itemstack) {
        data = itemstack;
        itmIndex = index;
    }

    public void writeData(DataOutputStream dos) throws IOException {
        if (player != null) {
            dos.write(ACTION_JOIN);
            player.writeToDataOutputStream(dos);
        } else if (data instanceof Vec3D) {
            dos.write(ACTION_MOVE);
            dos.writeInt(pId);
            dos.writeFloat(((Vec3D) data).x);
            dos.writeFloat(((Vec3D) data).y);
            dos.writeFloat(((Vec3D) data).z);
        } else if (data instanceof InvItemStack) {
            dos.write(ACTION_INVENTORY);
            dos.write(itmIndex);
            dos.writeShort(((InvItemStack) data).itemID);
            dos.write(((InvItemStack) data).stackSize);
        }
    }

    public void readData(DataInputStream dis) throws IOException {
        action = dis.read() & 0xFF;
        pId = dis.readInt();
        switch (action) {
        case ACTION_JOIN:
        case ACTION_MOVE:
            data = new Vec3D(dis.readFloat(), dis.readFloat(), dis.readFloat());
            break;
        case ACTION_QUIT:
            break;
        case ACTION_INVENTORY:
            itmIndex = dis.read();
            data = new InvItemStack(dis.readShort(), dis.read());
            break;
        default:
            break;
        }
    }

    public void process(ServerGame handler) {
        if (action == ACTION_MOVE) {
            handler.handlePlayerMove(pId, (Vec3D) data);
        } else if (action == ACTION_JOIN) {
            handler.handleNewPlayer(pId, (Vec3D) data);
        } else if (action == ACTION_QUIT) {
            handler.handlePlayerQuit(pId);
        } else if (action == ACTION_INVENTORY) {
            handler.handleInventoryChange(itmIndex, (InvItemStack) data);
        }
    }
}
