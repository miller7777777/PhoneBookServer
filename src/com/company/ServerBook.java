package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by vladimir on 15.05.15.
 */
public class ServerBook {
    private static final int PORT = 7001;
    private BufferedReader reader;
    private ServerSocket serverSocket;
    private ArrayList<Contact> book;
    private String allContacts = "";
    private PrintWriter writer;

    public void start() {
        book = new ArrayList<Contact>();
        try {
            serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String query;
            writer = new PrintWriter(clientSocket.getOutputStream());

            while (true) {
                query = reader.readLine();
                if (query != null) {
                    String[] data = query.split(":");
                    if (data[0].equals("add")) {
                        addContact(data[1], data[2], data[3]);
                    } else if (data[0].equals("showall")){
                        showall();
                        writer.println(allContacts);
                        writer.flush();
                        allContacts = "";
                    } else if (data[0].equals("removecontact")){
                        String message = removeContact(data[1]);
                        writer.println(message);
                        writer.flush();
                    }
                    else if (data[0].equals("stop")) {
                        break;
                    }
                }
            }

            reader.close();
            writer.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removeContact(String s) {

        String message = null;

        for (int i = 0; i < book.size(); i++) {

            if (book.get(i).getName().equals(s)){
                book.remove(i);
                message = "OK";

            } else {message = "Не найдено!";}

        }
        return message;
    }

    private String showall() {

        for (int i = 0; i < book.size(); i++) {


            allContacts = allContacts + book.get(i).getName() + ":" + book.get(i).getPhone() + ":" + book.get(i).getEmail() + ":";

        }

        System.out.println(allContacts);

        return allContacts;






    }

    private void addContact(String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        contact.print();
        book.add(contact);
    }
}
