package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MioThread extends Thread
{
    Socket s;
    List<String> usernamesList;
    List<Integer> disponibilita;

    public MioThread(Socket s, List<String> usernamesList, List<Integer> disponibilita) throws IOException{
       this.s = s;
       this.usernamesList = usernamesList;
       this.disponibilita = disponibilita;
    }

    enum Status{
        WELCOME
    }

    public static boolean isUsedUsername(String[] u, List <String> arr){
        for(int i = 0; i < arr.size(); i++){
            if(arr.get(i).equals(u[1])) return true;
        }
        return false;
    }
    
    public static String translInt(int n){
        if(n == 0) return "Gold";
        if(n == 1) return "Pit";
        if(n == 2) return "Parterre";
        return null;
    }

    public static int translStr(String[] str){
        if(str[0].equals("Gold")) return  0;
        if(str[0].equals("Pit")) return  1;
        if(str[0].equals("Parterre")) return 2;
        return -1;
    }

    @Override
    public void run() {
        try{
            
            boolean login = false;
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
                if(!username[0].equals("LOGIN") || username[1].isBlank()) out.println("ERR SYNTAX");
                if(isUsedUsername(username, usernamesList)) out.println("ERR USERINUSE");
            }while(!username[0].equals("LOGIN") || username[1].isBlank() || isUsedUsername(username, usernamesList));
            usernamesList.add(username[1]);
            login = true;
            //SERVER → OK 
            out.println("OK");
            
            //
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
                                out.println("ERR UNKNOWNTYPE");
                            }
                            else{          
                                n = translStr(tipo_qta);
                            }
                            if(Integer.parseInt(tipo_qta[1]) > disponibilita.get(n)){
                                out.println("KO");
                            }
                            if(tipo_qta[0].isBlank() || tipo_qta[1].isBlank()) out.println("ERR SYNTAX");


    
                        }while(!tipo_qta[0].equals("Gold") && !tipo_qta[0].equals("Pit") && !tipo_qta[0].equals("Parterre") || Integer.parseInt(tipo_qta[1]) > disponibilita.get(n));
                        out.println("OK");
                        disponibilita.set(n, disponibilita.get(n) - Integer.parseInt(tipo_qta[1]));
                        if(disponibilita.get(n) == 0) out.println("NOTIFY SoldOut "+translInt(n));
                        break;
                    case "QUIT":
                        out.println("BYE");
                        s.close();
                        break;
                    default:
                        out.println("ERR UNKNOWNCMD");
                        break;
                }
            }
        
        }catch(Exception e){

        }

    }
}

