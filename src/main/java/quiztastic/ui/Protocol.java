package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Player;
import quiztastic.domain.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Protocol{

    Quiztastic quiz = Quiztastic.getInstance();

    private Scanner in;
    private PrintWriter out;
    //private int counter = 1;
    private volatile int waiting;

    //private ArrayList<Player> players = new ArrayList<Player>();

    public Protocol(Scanner in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
    }

    //Her tager vi imod en String som bliver lavet om til en Integer.
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

    //Udskriver boardet
    public void displayBoard() {
        char categoryLetter = 'A';
        Game game = quiz.getCurrentGame();
        Board board = quiz.getBoard();
        List<Integer> scores = List.of(100,200,300,400,500);

        //Udskriver Categories med bogstav foran.
        for (Board.Group g : board.getGroups()) {
            out.print("[" + categoryLetter++ + "]" + " " + g.getCategory().getName() + " |");
        }
        out.println();

        //Udskrivning af den enkelte category's, spørgsmål i score
        for(int questionnumber = 0; questionnumber < 5; questionnumber++){
            out.print("|");
            for(int category = 0; category < 5; category++){
                out.print("     ");
                //Hvis kategorien er svaret på vil den udskrive --- istedet for scoren/spørgsmålet
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

    //Udskrivning af vores menu
    public void getHelpMsg() {
        out.println("******** Jepardy Menu *******");
        out.println("play: Asks everyone on the server to play the game with you");
        //out.println("createplayer: to make a new player");
        out.println("scoreboard: to show the current scoreboard");
        out.println("draw: draw the board");
        //out.println("answer A200: get the question for category A, question for 200 points");
        out.println("exit: exits the game");
        out.flush();
    }


    //Tager imod en player fra vores makePlayerAndRun();
    public void run(Player player) {
            int questionScore;
            int categoryNumber;
            int questionNumber;
            Game game = quiz.getCurrentGame();

            //laver String af vores player's navn
            String playerNavn = player.getPlayerName();
            out.println("Velkommen til Quiztasic " + playerNavn + ", du kan skrive help for hjælp");
            out.flush();
            String line;

            //Her starter vores switch case som styrer menu'en. Hvis personen ikke skriver exit vil han/hun aldrig ryge ud.
            while (!(line = in.next()).equals("exit")) {
                switch (line) {
                    /*case "createplayer":
                        makePlayerAndRun();
                        break;*/

                    //Når du skriver play vil den side og vente på at alle personerne i spillet har skrevet play.
                    case "play":
                        Player player1 = game.startGame();
                        if(player.equals(player1)){
                            displayBoard();
                            out.println("Du skal vælge et spørgsmål");
                            out.flush();
                            String question = in.next();
                            while(question.length() != 4){
                                out.print(question + ": Er ikke gyldig format, prøv igen");
                                out.print("\nEks: \"A400\"");
                                out.flush();
                                question = in.next();
                            }
                            String a = question.substring(0, 1).toUpperCase();
                            questionScore = Integer.parseInt(question.substring(1));
                            categoryNumber = chooseCategory2(a);
                            questionNumber = questionScore/100-1;
                            in.nextLine();
                            answeredQuestion(categoryNumber, questionNumber, player);
                            } else {
                            try {
                                out.println(player1 + " skal svare, venter på hans svar");
                                out.flush();
                                Game.Answer player1Answer = game.waitForAnswer();
                                out.println(player1Answer + " ");
                                out.flush();
                                while(answeredQuestion(player1Answer.getCategoryNumber(),player1Answer.getQuestionNumber(),player) == false){
                                    out.println("Og det var forkert");
                                    out.flush();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        out.flush();
                        break;
                    case "scoreboard":
                        showScoreBoard();
                        break;
                    case "help":
                        getHelpMsg();
                        break;
                    case "draw":
                        displayBoard();
                        break;
                    /*case "answer":
                        String question = in.next();
                        String a = question.substring(0, 1).toUpperCase();
                        int questionScore = Integer.parseInt(question.substring(1));
                        int categoryNumber = chooseCategory2(a);
                        int questionNumber = questionScore/100-1;
                        in.nextLine();
                        answeredQuestion(categoryNumber, questionNumber, player);

                        break;*/
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
        out.println("Indtast dit fornavn her: ");
        out.flush();
        String playerNavn = in.next();
        int playerScore = 0;
        int counter2 = game.makeCounter();
        Player player = new Player(counter2, playerNavn, playerScore);
        game.addPlayer(player);
        run(player);
    }


    public void showScoreBoard() {
        Game game = quiz.getCurrentGame();
        for (Player p: game.players) {
            out.println(p);
            out.println("-----------------------------------------------");
            out.flush();
        }
    }



    private boolean answeredQuestion(int categoryNumber, int questionNumber, Player player){
        Game game = quiz.getCurrentGame();
        String questionText = game.getQuestionText(categoryNumber, questionNumber);
        boolean rigtigt;
        int score = (questionNumber+1)*100;
        out.println(questionText);
        out.print("? ");
        out.flush();

        String answer = in.nextLine();
        String result = game.answerQuestion(categoryNumber,questionNumber, answer);
        if (result == null) {
            out.println(answer + " Was correct!");
            player.setPlayerScore(player.getPlayerScore() + score);
            rigtigt = true;
        } else {
            //Eventuelt fjern senere hvis flere spillere
            out.println(answer + " Was incorrect, the correct answer was: " + result);
            game.getRandomPlayer();
            rigtigt = false;
        }
        out.flush();
        return rigtigt;
    }

    public static void main(String[] args) throws IOException {
        Protocol protocol = new Protocol(new Scanner(System.in), new PrintWriter(System.out));
        protocol.makePlayerAndRun();
        //protocol.run();
    }
}