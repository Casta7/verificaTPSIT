package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(3000);
        List<String> usernamesList = new ArrayList<>();
        List<Integer> disponibilita = new ArrayList<>();
        disponibilita.add(25); // Gold
        disponibilita.add(25); // Pit
        disponibilita.add(50); // Parterre
        do {
            Socket socket = server.accept();
            MioThread t = new MioThread(socket, usernamesList, disponibilita);
            t.start();
        } while (true);
    }
}  

