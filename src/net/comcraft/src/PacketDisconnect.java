package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketDisconnect extends Packet {

    private String reason;
    private EntityPlayer player;

    public PacketDisconnect() {
    }

    public PacketDisconnect(EntityPlayer player) {
        this.player = player;
    }

    public void writeData(DataOutputStream dos) throws IOException {
        if (player != null) {
            dos.writeFloat(player.rotationPitch);
            dos.writeFloat(player.rotationYaw);
        }
    }

    public void readData(DataInputStream dis) throws IOException {
        this.reason = dis.readUTF();
    }

    public void process(ServerGame handler) {
        handler.handleDisconnect(reason, this);
    }

}
