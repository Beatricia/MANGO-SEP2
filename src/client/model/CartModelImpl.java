package client.model;

import client.networking.Client;

public class CartModelImpl implements CartModel{
    private Client client;

    public CartModelImpl(Client client){
        this.client = client;
    }

}
