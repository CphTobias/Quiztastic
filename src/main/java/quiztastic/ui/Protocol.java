package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Protocol{

    Quiztastic quiz = Quiztastic.getInstance();

    private final Scanner in;
    private final PrintWriter out;
    private int counter = 1;
    //private ArrayList<Player> players = new ArrayList<Player>();

    public Protocol(Scanner in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
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

    public void displayBoard() {
        char categoryLetter = 'A';
        Game game = quiz.getCurrentGame();
        Board board = quiz.getBoard();
        List<Integer> scores = List.of(100,200,300,400,500);

        for (Board.Group g : board.getGroups()) {
            out.print("[" + categoryLetter++ + "]" + " " + g.getCategory().getName() + " |");
        }
        out.println();

        for(int questionnumber = 0; questionnumber < 5; questionnumber++){
            out.print("|");
            for(int category = 0; category < 5; category++){
                out.print("     ");
                if(game.isAnswered(category, questionnumber)){
                    out.print("---");
                } else {
                    out.print(scores.get(questionnumber));
                }
                out.print("     |");
            }
            out.println();
            out.flush();
        }
    }

    public void getHelpMsg() {
        out.println("******** Jepardy Menu *******");
        out.println("createplayer: to make a new player");
        out.println("scoreboard: to show the current scoreboard");
        out.println("draw: draw the board");
        out.println("answer A200: get the question for category A, question for 200 points");
        out.println("exit: exits the game");
        out.flush();
    }



    public void run(Player player) {
            Game game = quiz.getCurrentGame();
            String playerNavn = player.getPlayerName();
            out.println("Velkommen til Quiztasic " + playerNavn + ", du kan skrive help for hjælp");
            out.flush();
            String line = null;
            while (!(line = in.next()).equals("exit")) {
                switch (line) {
                    case "createplayer":
                        makePlayerAndRun();
                        break;
                    case "scoreboard":
                        game.showScoreBoard();
                        break;
                    case "help":
                        getHelpMsg();
                        break;
                    case "draw":
                        displayBoard();
                        break;
                    case "answer":
                        String question = in.next();
                        String a = question.substring(0, 1).toUpperCase();
                        int questionScore = Integer.parseInt(question.substring(1));
                        int categoryNumber = chooseCategory2(a);
                        int questionNumber = questionScore/100-1;
                        in.nextLine();
                        answeredQuestion(categoryNumber, questionNumber, player);

                        break;
                    default:
                        out.println("Ugyldigt input");
                        out.flush();
                        break;
                }
            }
                line = in.nextLine();
        }


    public void makePlayerAndRun(){
        Game game = quiz.getCurrentGame();
        out.println("Velkommen til Quiztasic, her kan du vælge dit flotte navn :)");
        out.flush();
        out.println("VIS DU ER HER!");
        out.flush();
        in.nextLine();
        out.println("Indtast dit fornavn her: ");
        out.flush();
        String playerNavn = in.next();
        int playerScore = 0;
        Player player = new Player(counter, playerNavn, playerScore);
        game.addPlayer(player);
        run(player);
    }

    public synchronized int makeCounter(){
        counter++;
        return counter;
    }

    /*private void addPlayer(Player p){
        players.add(p);
    }
    public void showScoreBoard() {
        for (Player p: players) {
            out.println(p);
            out.flush();
        }
    }*/

    private void answeredQuestion(int categoryNumber, int questionNumber, Player player){
        Game game = quiz.getCurrentGame();
        String questionText = game.getQuestionText(categoryNumber, questionNumber);
        int score = (questionNumber+1)*100;
        out.println(questionText);
        out.print("? ");
        out.flush();

        String answer = in.nextLine();
        String result = game.answerQuestion(categoryNumber,questionNumber, answer);
        if (result == null) {
            out.println(answer + " Was correct!");
            player.setPlayerScore(player.getPlayerScore() + score);
        } else {
            //Eventuelt fjern senere hvis flere spillere
            out.println(answer + " Was incorrect, the correct answer was: " + result);
        }
        out.flush();

    }

    public static void main(String[] args) throws IOException {
        Protocol protocol = new Protocol(new Scanner(System.in), new PrintWriter(System.out));
        protocol.makePlayerAndRun();
        //protocol.run();
    }
}