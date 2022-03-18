package com.itt.tds.distributor;

import com.itt.tds.distributor.db.exceptions.DBException;
import java.io.IOException;
import java.net.Socket;

public class Practice {

    public static void main(String args[]) throws DBException, IOException, ClassNotFoundException {

        Socket socket = new Socket("10.0.2.16", 1500);

    }

}

class Product {

    int id;
    String name;
    float price;

    public Product(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
