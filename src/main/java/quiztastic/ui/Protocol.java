package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Question;
import quiztastic.domain.Game;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Protocol {

    Quiztastic quiz = Quiztastic.getInstance();
    public void StartGame() throws IOException, ParseException {
        Scanner in = new Scanner(System.in);

        System.out.println("Write help for information about the game\n");

        String input = in.next();

        while(!input.equals("exit")) {
            switch(input) {
                case "help":
                    getHelpMsg();
                    break;
                case "draw":
                    displayBoard();
                    break;
                case "answer":
                    System.out.println("What question do you want to answer?");
                    String question = in.next();
                    String a = question.substring(0, 1).toLowerCase();
                    int questionScore = Integer.parseInt(question.substring(1));

                    chooseCategory(question);

                    System.out.println(chooseCategory(input2));


                    break;
                default:
                    System.out.println("Ugyldigt input");
                    break;
            }
            input = in.nextLine();
        }
    }

    private int chooseCategory(String cat){
        Map<String, Integer> options = Map.of("A", 0, "B", 1, "C", 2);
        Integer i = options.get(cat);
        if(i == null){
            System.out.println("Not a valid category: " + cat);
            i = -1;
        }
        return i;
    }

    private int chooseCategory2(String cat){
        switch(cat){
            case "A": return 0;
            case "B": return 1;
            case "C": return 2;
            case "D": return 3;
            case "E": return 4;
            default:
                return -1;
        }
    }

    public void displayBoard(){
        Game game = quiz.getCurrentGame();
        Board board = quiz.getBoard();
        List<Integer> scores = List.of(100,200,300,400,500);

        for (Board.Group g : board.getGroups()) {
            System.out.print(g.getCategory().getName() + " |");
        }
        System.out.println();

        for(int questionnumber = 0; questionnumber < 5; questionnumber++){
            System.out.print("|");
            for(int category = 0; category < 5; category++){
                System.out.print("     ");
                if(game.isAnswered(category, questionnumber)){
                    System.out.print("---");
                } else {
                    System.out.print(scores.get(questionnumber));
                }
                System.out.print("     |");
            }
            System.out.println();
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