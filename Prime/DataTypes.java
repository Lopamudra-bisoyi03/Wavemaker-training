public class DataTypes {
    public static void main(String[] args) {
        boolean isPassed = false;
        int count = 12;
        byte marks = 96;
        char letter = 'L';
        int bigNumber = 99999;
        long hugeNumber = 1234566756L;
        float decimalNumber = 1.23F;
        double precision = 3.14;
        String name = "Lopa";
        int[] array = {6,7,8,9};


        System.out.println(count);
        System.out.println(marks);
        System.out.println(isPassed);
        System.out.println(marks);
        System.out.println(letter);
        System.out.println(bigNumber);
        System.out.println(hugeNumber);
        System.out.println(decimalNumber);
        System.out.println(precision);
        System.out.println(name);
        for (int i = 0; i < array.length; i++)
            System.out.println(array[i]+ " ");
    }
}
