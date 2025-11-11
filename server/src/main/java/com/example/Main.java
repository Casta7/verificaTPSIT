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
    enum Status{
        WELCOME
    }

    public static boolean isUsedUsername(String[] u, List <String> arr){
        for(int i = 0; i < arr.size(); i++){
            if(arr.get(i).equals(u[1])) return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        List<String> usernamesList = new ArrayList<>();
        List<Integer> disponibilita = new ArrayList<>();
        disponibilita.add(25); // Gold
        disponibilita.add(25); // Pit
        disponibilita.add(50); // Parterre
        final int PORTA = 3000;
        //CLIENT → (apre connessione sulla porta 3000)
        ServerSocket ss = new ServerSocket(PORTA);
        Socket s = ss.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        //SERVER → WELCOME
        out.println(Status.WELCOME);
        String[] username = {"", ""}; 
        do{
            //CLIENT → LOGIN <username>
            String input = in.readLine();
            username = input.split(" ", 2);  
            //SERVER → ERR USERINUSE se username già preso
            if(!username[0].equals("LOGIN") || username[1].isBlank() || isUsedUsername(username, usernamesList)) out.println("ERR");
        }while(!username[0].equals("LOGIN") || username[1].isBlank() || isUsedUsername(username, usernamesList));
        usernamesList.add(username[1]);
        //SERVER → OK 
        out.println("OK");

        while(true){
            String input = in.readLine();
            switch (input) {
                case "N":
                    out.println("AVAIL Gold:"+disponibilita.get(0)+" Pit:"+disponibilita.get(1)+" Parterre:"+disponibilita.get(2)+"");
                    break;
                case "BUY":
                    String[] tipo_qta = {"", ""}; 
                    int n = 0;
                    do{
                        String acquisto = in.readLine();
                        tipo_qta = acquisto.split(" ", 2);
                        if(!tipo_qta[0].equals("Gold") && !tipo_qta[0].equals("Pit") && !tipo_qta[0].equals("Parterre")){
                            out.println("KO");
                        }
                        else{          
                            if(tipo_qta[0].equals("Gold")) n = 0;
                            if(tipo_qta[0].equals("Pit")) n = 1;
                            if(tipo_qta[0].equals("Parterre")) n = 2;
                        } 

                    }while(!tipo_qta[0].equals("Gold") && !tipo_qta[0].equals("Pit") && !tipo_qta[0].equals("Parterre") || Integer.parseInt(tipo_qta[1]) > disponibilita.get(n));
                    out.println("OK");
                    disponibilita.set(n, disponibilita.get(n) - Integer.parseInt(tipo_qta[1]));
                    break;
                case "QUIT":
                    out.println("BYE");
                    ss.close();
                    s.close();
                    break;
                default:
                    break;
            }
        }

    }
}

