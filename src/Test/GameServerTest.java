import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameServerTest extends TestCase {
    public void testFirstConnect() throws Exception {
        Object tmp = new Object();
        List<Object> waiting_room = new ArrayList<Object>();
        waiting_room.add(tmp);

        assertEquals(1, waiting_room.size());
    }

    public void testDeleteFromWaitRoom() throws Exception {
        Object tmp = new Object();
        List<Object> waiting_room = new ArrayList<Object>();
        waiting_room.add(tmp);

        waiting_room.remove(0);
        assertEquals(0, waiting_room.size());
    }

}