package client.networking;

import transferobjects.ErrorMessage;
import transferobjects.LoginRequest;
import transferobjects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 *
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;
    private final SocketClient client;

    //lets decide about try/catch or throwing exceptions

    /**
     * Initializes a Socket object, with all the required streams.
     * @param client The SocketClient belonging to this ClientHandler
     * @throws IOException if the Server is unavailable, or if any other network error occurs
     */
    public ClientHandler(SocketClient client) throws IOException {
        socket = new Socket("localhost", 1111);
        fromServer = new ObjectInputStream(socket.getInputStream());
        toServer = new ObjectOutputStream(socket.getOutputStream());
        this.client = client;
    }

    /**
     * This method is put on a separate thread when the SocketClient is created,
     * the main input from the server
     */
    @Override
    public void run() {
        try {
            while(true){
                Object obj = fromServer.readObject();

                if(obj instanceof User){
                    client.userReceivedFromServer((User)obj);
                }
                else if(obj instanceof ErrorMessage){
                    client.errorReceivedFromServer(obj);
                }
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Sends a Serializable object to the server
     * @param object the object to be sent to the server
     */
    public void send(Serializable object) {
        try {
            toServer.writeObject(object);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
