import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * ServerKryo
 */

public class ServerKryo {

    GameServer          game = new GameServer();

    public ServerKryo (String port) {
        try {
            System.out.println("[+] Server Started");
            Server server = new Server();
            server.start();
            int p = 0;

            try{
                p = Integer.parseInt(port);
            }catch (NumberFormatException e) {
                System.out.println("Put a valid port!");
                System.exit(0);
            }
            System.out.println("[+] Port : " + p);
            
            server.bind(p);

            Network.register(server);

            server.addListener(new Listener() {

                public void received (Connection connection, Object object) {
                    if (object instanceof ClientRequest) {
                        game.HandleRequest((ClientRequest)object, connection);
                    }
                }

                public void connected (Connection connection) {
                    System.out.println("[+] New Client");
                    game.FirstConnect(connection);
                }

                public void disconnected (Connection connection) {
                    game.deleteFromWaitRoom(connection);
                    System.out.println("[-] Client Disconnected");
                }

            });
        }
        catch (java.io.IOException exp){
            System.out.println(exp.getMessage());
        }
        game.run();
    }
}