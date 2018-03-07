/**
 * Server
 */

public class ServerMain {


    public static void main(String[] args){
        if (args.length != 1)
        {
            System.out.println("Usage: java -jar jcoinche-server [port]");
            System.exit(0);
        }
        ServerKryo serv = new ServerKryo(args[0]);
    }
}