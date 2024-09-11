public class Main {

    // Метод конвертації цифри в римські числа
    public static String intToRoman(int num) {
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[num / 1000] +
                hundreds[(num % 1000) / 100] +
                tens[(num % 100) / 10] +
                ones[num % 10];
    }

    // Main для тестування
    public static void main(String[] args) {
        // 40 Тестів
        int[] testCases = {1, 2, 3, 4, 5, 9, 10, 14, 19, 20,
                24, 29, 34, 40, 44, 49, 50, 58, 60, 67,
                70, 79, 80, 89, 90, 98, 100, 150, 199, 250,
                300, 399, 400, 450, 500, 550, 600, 750, 900, 1000};

        for (int testCase : testCases) {
            System.out.println("Integer: " + testCase + " -> Roman Numeral: " + intToRoman(testCase));
        }
    }
}
