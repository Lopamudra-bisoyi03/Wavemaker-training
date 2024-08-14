package Prime;

import java.util.Scanner;

public class GuessGame {
    public static void main(String[] args) {
        int rand = (int)(Math.random() * 100);
        while (true) {
            System.out.println("Guess a number between 0 and 100");
            Scanner sc = new Scanner(System.in);
            int guess = sc.nextInt();

            if (guess == rand) {
                System.out.println("Congratulations! You win!");
                break;
            } else {
                if (guess < rand) {
                    System.out.println("You went low. Dream big!");
                } else {
                    System.out.println("You went high. Come back!");
                }

            }
        }
    }
}

