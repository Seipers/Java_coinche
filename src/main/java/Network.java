import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;
import java.util.List;

// This class is a convenient place to keep things common to both the client and server.
public class Network {

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(OpcodeEnum.class);
		kryo.register(CardEnum.class);
		kryo.register(CardNbEnum.class);
		kryo.register(Bet.class);
		kryo.register(List.class);
		kryo.register(ArrayList.class);
		kryo.register(Card.class);
		kryo.register(ServerRequest.class);
		kryo.register(ClientRequest.class);
	}
}