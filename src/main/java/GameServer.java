import com.esotericsoftware.kryonet.Connection;

import java.lang.reflect.Method;
import java.util.*;


public class GameServer {

    List<ClientSide>                waiting_room = new ArrayList<ClientSide>();
    Board                           board;
    Map<OpcodeEnum, Method>         OpHandler = new HashMap<>();

    public GameServer()
    {
        try 
        {
            OpHandler.put(OpcodeEnum.NAME, GameServer.class.getMethod("setName", ClientRequest.class, Connection.class));
            OpHandler.put(OpcodeEnum.BET, GameServer.class.getMethod("setBet", ClientRequest.class, Connection.class));
            OpHandler.put(OpcodeEnum.PLAY, GameServer.class.getMethod("player_played", ClientRequest.class, Connection.class));
        }
        catch (java.lang.NoSuchMethodException exp)
        {
            System.out.println(exp.getMessage());
        }
    }


    public void FirstConnect(Connection connection)
    {
        ClientSide  tmp = new ClientSide(connection);
        waiting_room.add(tmp);
        ServerRequest req = new ServerRequest();
        req.setOpcode(OpcodeEnum.WELCOME);
        connection.sendTCP(req);
        System.out.println("[+] Wecome sent!");
    }

    /* Function to handle setting name request from client*/
    public void setName(ClientRequest resp, Connection connection)
    {
        for(Iterator<ClientSide> iter = waiting_room.iterator(); iter.hasNext();){
            ClientSide tmp = iter.next();
            if (tmp.getSocket() == connection) {
                tmp.setName(resp.getName());
                System.out.println("[*] Name of new client : " + tmp.getName());
                if (this.board.addPlayer(tmp))
                    iter.remove();
                break;
            }
        }
    }

    public void setBet(ClientRequest resp, Connection connection)
    {
        board.setBet(resp);
    }

    /* Function to handle all requests from clients*/
    public void HandleRequest(ClientRequest req, Connection connection)
    {
       Method   tmp = OpHandler.get(req.getOpcode());

       try {
           tmp.invoke(this, req, connection);
       }catch (java.lang.IllegalAccessException exp){
            System.out.println(exp.getMessage());
        }catch (java.lang.reflect.InvocationTargetException exp){
           System.out.println(exp.getMessage());
       }
    }

    public void deleteFromWaitRoom(Connection connection)
    {
        for (Iterator<ClientSide> iter = waiting_room.iterator(); iter.hasNext();){
            ClientSide tmp = iter.next();
            if (tmp.getSocket() == connection) {
                System.out.println("[-] Client removed from waiting room");
                iter.remove();
                return;
            }
        }
        board.interrupt_game();
        board = new Board();
        waitingToBoard();
    }

    private void waitingToBoard(){
        ClientSide tmp;

        for(Iterator<ClientSide> iter = waiting_room.iterator(); iter.hasNext();){
            tmp = iter.next();
            if (tmp.getName() != ""){
                if ((this.board.addPlayer(tmp))) {
                    iter.remove();
                }
            }
        }
    }


    public void player_played(ClientRequest req, Connection connection){
        board.client_play(req);
    }

    public void run()
    {
        this.board = new Board();        
    }
}
