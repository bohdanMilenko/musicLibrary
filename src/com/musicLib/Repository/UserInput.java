package com.musicLib.Repository;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static com.musicLib.Repository.MetaData.*;

public class UserInput {

    private static Scanner scanner = new Scanner((System.in));
    static int orderResultSet( ){
        System.out.println("How would you like the query sorted?\n" +
                "1 - Ascending\n" +
                "2 - Descending\n" +
                "3 - No Sorting");
        int order;
        int option = 0;
        try {
            int i=0;
            do{
                option = scanner.nextInt();
                scanner.nextLine();
            }while (option <0 && option >=3);
            System.out.println(option);

        } catch (InputMismatchException e) {
            System.out.println("Please type in 1,2 or 3!");
            scanner.nextLine();
            orderResultSet();
        }
        switch (option){
            case 1:
                order = ORDER_ASC;
                break;
            case 2:
                order = ORDER_DESC;
                break;
            default:
                order = ORDER_NONE;
        }

        return order;
    }


    public static String getUserInput() {
        String songName = "";
        try {
            int i = 0;
            songName = scanner.nextLine();
            while (!(Pattern.matches("[a-zA-z]+", songName))){
                System.out.println("Please enter a string!");
                songName = scanner.nextLine();
                i++;
                if(i==2) break;
            }
            return songName;
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid input");
            return songName;
        }
    }
}
