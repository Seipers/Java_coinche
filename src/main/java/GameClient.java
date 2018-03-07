import com.esotericsoftware.kryonet.Client;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javax.net.ssl.ExtendedSSLSession;

/**
 * GameClient
 */
public class GameClient {
    
    private Client client;
    private List<Card>  cards;
    private List<Card>  hand = new ArrayList<Card>();
    private Map<OpcodeEnum, Method> ReqContener = new HashMap<>();
    private int maxbet = 80;
    private boolean coinche = false;
    private int pass = 0;
    Scanner scan;

    public GameClient(Client client)
    {
        this.client = client;
        try {
			this.ReqContener.put(OpcodeEnum.WELCOME, GameClient.class.getMethod("WelcomeFuction", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.DEAL, GameClient.class.getMethod("getCard", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.ASKBET, GameClient.class.getMethod("askBet", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.OTHERBET, GameClient.class.getMethod("getOtherBet", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.DEALER, GameClient.class.getMethod("dealer_info", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.ASKPLAY, GameClient.class.getMethod("play_card", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.OTHERPLAY, GameClient.class.getMethod("otherPlay", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.WINDRAW, GameClient.class.getMethod("getWinHand", ServerRequest.class));
            this.ReqContener.put(OpcodeEnum.SCORE, GameClient.class.getMethod("Score", ServerRequest.class));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    }


    public void HandleRequest(ServerRequest req)
    {
        Method tmp = ReqContener.get(req.getOpcode());

        try {
			tmp.invoke(this, req);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }

    public void WelcomeFuction(ServerRequest req)
    {
        ClientRequest tmp = new ClientRequest();
        tmp.setOpcode(OpcodeEnum.NAME);
        scan = new Scanner(System.in);
        System.out.print("What's your name ? ");
        String str = "";
        while (str == "")
        {
            try{
            str = scan.nextLine();
            }catch(NoSuchElementException e){
                scan = new Scanner(System.in);
                str = "";
            }
        }
        tmp.setName(str);
        this.client.sendTCP(tmp);
    }

    public void askBet(ServerRequest req)
    {
        System.out.println("\nWhat's your bet ?");        
        ClientRequest resp = new ClientRequest();
        Bet mybet = new Bet();
        int choice = 0;
        while (choice < 1 || choice > 4)
        {
            System.out.println("Choose ?\n1. Play | 2. Pass | 3. Coinche | 4. SurCoinche");
            try {
            choice = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                choice = 0;
            } catch (NoSuchElementException e){
                scan = new Scanner(System.in);
                choice = 0;
            }
            if (choice == 3 && (coinche || pass > 0 || maxbet == 80))
            {
                System.out.println("Can't coinche!");
                choice = 0;
            }
            if (choice == 4 && (!coinche || pass % 2 == 1))
            {
                System.out.println("Can't surcoinche!");
                choice = 0;
            }
            if (maxbet >= 160 && choice == 1) {
                System.out.println("Max bet already reached");
                choice = 0;
            }
        }
        if (choice == 1)
            mybet = this.Bet(mybet);
        else if (choice == 2)
            mybet.pass = true;
        else if (choice == 3)
            mybet.coinche = true;
        else
            mybet.surcoinche = true;
        resp.setOpcode(OpcodeEnum.BET);
        resp.setMybet(mybet);
        client.sendTCP(resp);            
    }

    public Bet Bet(Bet mybet)
    {
        int color = 0;
        while (color < 1 || color > 4)
        {
            System.out.println("Which ?\n1. Diamond | 2. Heart | 3. Club | 4. Spade");
            try {
            color = Integer.parseInt(scan.nextLine());
            }catch (NumberFormatException e) {
            color = 0;
            }catch (NoSuchElementException e){
                color = 0;
                scan = new Scanner(System.in);
            }
        }
        mybet.setColor(color);
        int bet = 0;
        while (bet < maxbet || bet > 160)
        {
            int i = this.maxbet;
            System.out.println("How many ?");
            while (i <= 160)
            {
                System.out.print(" " + i);
                if (i <= 150)
                    System.out.print(" |");
                else
                    System.out.println();
                i += 10;
            }
            try {
            bet = Integer.parseInt(scan.nextLine());
            }catch (NumberFormatException e){
                bet = 0;
            }catch (NoSuchElementException e){
                bet = 0;
                scan = new Scanner(System.in);
            }
            if (bet % 10 != 0)
                bet = 0;
        }
        mybet.setBet(bet);
        return (mybet);
    } 

    public void getOtherBet(ServerRequest req)
    {
        System.out.print(req.getTeam() + " ");
        Bet other = req.getMybet();

        if (other.pass)
            pass++;
        else
            pass = 0;
        if (other.pass)
            System.out.println(" pass");
        else if (other.coinche) {
            System.out.println(" coinche");
            coinche = true;
        }
        else if (other.surcoinche)
            System.out.println(" surcoinche");
        else
        {
            this.maxbet = other.getBet() + 10;
            System.out.println("bet " + other.getColor().getName() + " " + other.getBet());
        }
    }

    public void getCard(ServerRequest req)
    {
        this.cards = req.getCards();
        this.printCard();
    }

    private void printCard()
    {
        for (Iterator it = this.cards.iterator(); it.hasNext();)
        {
            Card tmp = (Card) it.next();
            System.out.print(tmp.getColor().getCode());
            System.out.print(tmp.getNumber().getCode());
            if (it.hasNext())
                System.out.print(" | ");
        }
        System.out.println();
    }

    public void dealer_info(ServerRequest req){
        System.out.println(req.getTeam() + " win this round with " + req.getMybet().getColor().getName() + " bet : " + req.getMybet().getBet());
    }

    public void play_card(ServerRequest req){
        boolean turn_end = false;
        int     index = -1;

        while (!turn_end) {
            printCard();
            System.out.print("It's your turn, play card (Type index of your card) !\n=>");
            try{
                index = Integer.parseInt(scan.nextLine());
            }catch (NumberFormatException exp){
                index = -1;
            }catch (NoSuchElementException e){
                index = -1;
                scan = new Scanner(System.in);
            }
            if (index < 0 || index > cards.size() - 1)
                System.out.println("Invalid Index !");
            else if (!can_play(index))
                System.out.println("Don't play that !");
            else
                turn_end = true;
        }
        ClientRequest resp = new ClientRequest();
        resp.setMycard(cards.get(index));
        cards.remove(index);
        resp.setOpcode(OpcodeEnum.PLAY);
        client.sendTCP(resp);
    }

    private boolean can_play(int index) {
        if (hand.size() > 0) {
            if (cards.get(index).getColor() != hand.get(0).getColor()) {
                for (Card tmp : cards) {
                    if (tmp.getColor() == hand.get(0).getColor())
                        return false;
                }
            }
        }
        return true;
    }

    public void otherPlay(ServerRequest req)
    {
        hand.add(req.getCard());
        System.out.println(req.getTeam() + " " + req.getCard().getColor().getName() + " " + req.getCard().getNumber().getName());
    }

    public void getWinHand(ServerRequest req)
    {
        hand.clear();
        System.out.println(req.getTeam() + " score " + req.getPoints());
    }

    public void Score(ServerRequest req)
    {
        System.out.println("Ally :" + req.getPoints() + " point(s)");
        System.out.println("Enemy :" + req.getEnnemy_points() + " point(s)");
        maxbet = 80;
    }
}