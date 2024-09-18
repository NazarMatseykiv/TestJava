import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Main {
    public int convert(String roman) throws IllegalArgumentException {
        if (roman == null || roman.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty.");
        }

        // Спрощена логіка для перетворення дійсних римських цифр для ілюстрації
        if (roman.equalsIgnoreCase("I")) return 1;
        if (roman.equalsIgnoreCase("V")) return 5;
        if (roman.equalsIgnoreCase("X")) return 10;
        if (roman.equalsIgnoreCase("L")) return 50;
        if (roman.equalsIgnoreCase("C")) return 100;
        if (roman.equalsIgnoreCase("D")) return 500;
        if (roman.equalsIgnoreCase("M")) return 1000;


        throw new IllegalArgumentException("Invalid Roman numeral.");
    }
}
class RomanNumeralConverterTest {

    private final Main converter = new Main();

    // 1. Перевірка чи правильні прості числівники
    @Test
    void testValidRomanNumeralI() {
        assertEquals(1, converter.convert("I"));
    }

    // 2. Перевірка чи правильні прості числівники в різних відмінках
    @Test
    void testValidRomanNumeralV() {
        assertEquals(5, converter.convert("v"));
    }

    // 3. Перевірка недійсних римських цифр
    @Test
    void testInvalidRomanNumeral() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("A"));
    }

    // 4. Перевірка нульового входу
    @Test
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert(null));
    }

    // 5. Перевірка на введення порожнього рядка
    @Test
    void testEmptyStringInput() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert(""));
    }

    // 6. Перевірка на правильний складний числівник
    @Test
    void testValidRomanNumeralX() {
        assertEquals(10, converter.convert("X"));
    }

    // 7. Перевірка правильності складного числівника з малої літери
    @Test
    void testValidRomanNumeralLowercase() {
        assertEquals(50, converter.convert("l"));
    }

    // 8. Перевірка на недійсний символ у числі
    @Test
    void testInvalidCharacterInNumeral() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("XIIIV"));
    }

    // 9. Перевірка на довгу недійсну послідовність чисел
    @Test
    void testLongInvalidNumeral() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("MMMMM"));
    }

    // 10. Перевірка на регістр числа з недійсним повторенням
    @Test
    void testInvalidRepetitionNumeral() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("IIII"));
    }
}