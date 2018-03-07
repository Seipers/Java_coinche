import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;

/**
 * ClientKryo
 */

public class ClientKryo {

    private Client client;
    private GameClient game;

    public ClientKryo (String ip, String port) {
            this.client = new Client();
            this.client.start();
            
            Network.register(client);

            this.game = new GameClient(this.client);
            client.addListener(new Listener()
            {
                public void received (Connection connection, final Object object)
                {
                    if (object instanceof ServerRequest)
                    {
                        new Thread("HandleRequest")
                        {
                            public void run() {
                                game.HandleRequest((ServerRequest) object);
                            }
                        }.start();
                    }
                }

                public void disconnected (Connection connection) {
                    System.out.println("[-] Connection lost !");
                    System.exit(0);
                }
            });
            
            int p = 0;
            try{
            p = Integer.parseInt(port);
            }catch (NumberFormatException e) {
                System.out.println("Put a correct port");
            }

            try {
    			client.connect(5000, ip, p);
			} catch (IOException e) {
                System.out.println("Can't connect to server");
                System.exit(0);
            }
            this.run();
        }

    private synchronized void run()
    {
        try {
            wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}