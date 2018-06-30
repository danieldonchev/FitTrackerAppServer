package tracker;

import org.hsqldb.Server;

public class HSQlDBServer {

    public HSQlDBServer(){
        Server server = new Server();
        server.setDatabaseName(0, "mainDB");
        server.setDatabasePath(0, "mem:mainDB");
        server.setPort(9091);
        server.start();
    }


}
