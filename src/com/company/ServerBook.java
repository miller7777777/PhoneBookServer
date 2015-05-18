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
    private String message = "";
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
                        message = addContact(data[1], data[2], data[3]);

                        sendMessage(message);
                    } else if (data[0].equals("showall")) {
                        showall();

                        sendMessage(allContacts);
                        allContacts = "";
                    } else if (data[0].equals("removecontact")) {
                        message = removeContact(data[1]);

                        sendMessage(message);
                    } else if (data[0].equals("stop")) {
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


        for (int i = 0; i < book.size(); i++) {

            if (book.get(i).getName().equals(s)) {


                book.remove(i);

                message = "OK. Контакт удален. Contact removed.";

            } else {
                message = "Не найдено! Not Found!";
            }


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

    private String addContact(String name, String phone, String email) {
        Contact contact = new Contact(name, phone, email);
        contact.print();
        book.add(contact);

        return "OK. Контакт добавлен. Contact added!";
    }

    private void sendMessage(String message) {

        writer.println(message);
        writer.flush();
    }
}
