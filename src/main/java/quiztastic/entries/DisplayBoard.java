package quiztastic.entries;

import quiztastic.app.Quiztastic;

public class DisplayBoard {
    static char A = 'A';
    public static void main(String[] args) {
        //A+=1;
        //System.out.println(A);
        Quiztastic quiz = Quiztastic.getInstance();
        System.out.println(quiz.getBoard());
    }
}
