package net.comcraft.src;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketWorldInfo extends Packet {

    private DataInputStream dis;
    private Object[][] existingPlayers;

    public PacketWorldInfo() {
    }

    public void writeData(DataOutputStream dos) throws IOException {
    }

    public void readData(DataInputStream dis) throws IOException {
        byte[] data = new byte[dis.readInt()];
        System.out.println("length = " + data.length);
        dis.read(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        this.dis = new DataInputStream(bais);

        int players = dis.read() & 0xFF;
        System.out.println("players = " + players);
        existingPlayers = new Object[players][2];
        for (int i = 0; i < players; i++) {
            int pId = dis.readInt();
            Vec3D v = new Vec3D(dis.readFloat(), dis.readFloat(), dis.readFloat());
            existingPlayers[i] = new Object[] { new Integer(pId), v };
        }
    }

    public void process(ServerGame handler) {
        handler.handleWorldInfo(dis);
        for (int i = 0; i < existingPlayers.length; i++) {
            handler.handleNewPlayer(((Integer) existingPlayers[i][0]).intValue(), (Vec3D) existingPlayers[i][1]);
        }
    }

}
