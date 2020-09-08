package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Board;
import quiztastic.core.Question;
import quiztastic.domain.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Protocol implements Runnable {

    Quiztastic quiz = Quiztastic.getInstance();
    private final Socket socket;

    public Protocol(Socket socket) {
        this.socket = socket;
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

    public void displayBoard() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        Game game = quiz.getCurrentGame();
        Board board = quiz.getBoard();
        List<Integer> scores = List.of(100,200,300,400,500);

        for (Board.Group g : board.getGroups()) {
            out.print(g.getCategory().getName() + " |");
        }
        out.println();

        for(int questionnumber = 0; questionnumber < 5; questionnumber++){
            out.println("|");
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

    public void getHelpMsg() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("******** Jepardy Menu *******\n" +
                "draw: draw the board\n" +
                "answer A200: get the question for category A, question for 200 points\n" +
                "exit: exits the game");
        out.flush();
    }


    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            out.println("Velkommen til Quiztasic, du kan skrive help for hjælp");

            String line = null;
            while (!(line = in.nextLine()).equals("exit")) {
                switch (line) {
                    case "help":
                        getHelpMsg();
                        break;
                    case "draw":
                        displayBoard();
                        break;
                    case "answer":
                        out.println("What question do you want to answer?");
                        out.flush();
                        String question = in.next();
                        String a = question.substring(0, 1).toLowerCase();
                        int questionScore = Integer.parseInt(question.substring(1));

                        chooseCategory(question);

                        //System.out.println(chooseCategory(input2));


                        break;
                    default:
                        System.out.println("Ugyldigt input");
                        break;
                }
            }
                line = in.nextLine();
                socket.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }


    public static void main(String[] args) throws IOException, ParseException {
        final int port = 6060;
        final ServerSocket serverSocket = new ServerSocket(port);

        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("[CONNECTED]" + socket.getInetAddress() + " port " + socket.getPort());

            Thread thread = new Thread(new Protocol(socket));
            thread.start();

        }
    }
}