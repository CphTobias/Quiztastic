package quiztastic.ui;

import quiztastic.app.Quiztastic;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Protocol {


    public void StartGame() throws IOException, ParseException {
        Quiztastic quiz = Quiztastic.getInstance();
        Scanner in = new Scanner(System.in);

        System.out.println("Write help for information about the game\n");

        String input = in.nextLine();

        while(!input.equals("exit")) {
            switch(input) {
                case "help":
                    getHelpMsg();
                    break;
                case "draw":
                    System.out.println(quiz.getBoard());
                    break;
                case "answer":
                    System.out.println("What question do you want to answer?");
                    String input2 = in.nextLine();

                    break;
                default:
                    System.out.println("Ugyldigt input");
                    break;
            }
            input = in.nextLine();
        }
    }

    public void getHelpMsg(){
        System.out.println("******** Jepardy Menu *******\n" +
                "draw: draw the board\n" +
                "answer A200: get the question for category A, question for 200 points\n" +
                "exit: exits the game");
    }

    public static void main(String[] args) throws IOException, ParseException {
        new Protocol().StartGame();
    }
}