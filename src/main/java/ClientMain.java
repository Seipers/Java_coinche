/**
 * Client
 */

public class ClientMain {

    public static void main(String[] args) {
        if (args.length != 2)
        {
            System.out.println("Usage: java -jar jcoinche-client.jar [ip] [port]");
            System.exit(0);
        }
        ClientKryo client = new ClientKryo(args[0], args[1]);
    }
}