package Prime;

import java.math.BigInteger;
import java.util.Scanner;

public class CalculateLargeNumbers {
    public static void main(String[] args) {
//        //creating big integers
//        BigInteger number1 = new BigInteger("123456789");
//        BigInteger number2 = new BigInteger("987654321");
//
//        //addition
//        BigInteger sum = number1.add(number2);
//        System.out.println(sum);
//
//        //subtraction
//        BigInteger difference = number2.subtract(number1);
//        System.out.println(difference);
//
//        //multiplication
//        BigInteger product = number1.multiply(number2);
//        System.out.println(product);
//
//        //division
//        BigInteger division = number2.divide(number1);
//        System.out.println(division);

        System.out.println("Enter an integer: ");
        Scanner sc = new Scanner(System.in);
        BigInteger number = new BigInteger(sc.nextLine());

        System.out.println("Enter an integer: ");
        BigInteger number2 = new BigInteger(sc.nextLine());

        System.out.println("Enter an operation you want to perform: add/subtract/multiply/divide/modulo");
        String operation = sc.nextLine();
        switch (operation) {
            case "add":
                number = number.add(number2);
                break;
                case "subtract":
                    number = number.subtract(number2);
                    break;
                    case "multiply":
                        number = number.multiply(number2);
                        break;
                        case "divide":
                            number = number.divide(number2);
                            break;
                            case "modulo":
                                number = number.mod(number2);
                                break;
                            default:
                                break;
        }
        System.out.println(number);
    }
}
