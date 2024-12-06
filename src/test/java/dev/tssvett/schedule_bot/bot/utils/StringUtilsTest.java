package dev.tssvett.schedule_bot.bot.utils;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StringUtilsTest {

    @ParameterizedTest
    @MethodSource("provideCapitalizeFirstLetterData")
    void capitalizeFirstLetter(String input, String expected) {
        assertEquals(expected, StringUtils.capitalizeFirstLetter(input));
    }

    private static Stream<Arguments> provideCapitalizeFirstLetterData() {
        return Stream.of(
                Arguments.of("hello", "Hello"),
                Arguments.of("", ""),
                Arguments.of(null, null),
                Arguments.of("a", "A"),
                Arguments.of("Hello", "Hello")
        );
    }

    @ParameterizedTest
    @MethodSource("provideExtractUnifiedTimeData")
    void extractUnifiedTime(String[] timeParts, String expected) {
        assertEquals(expected, StringUtils.extractUnifiedTime(timeParts));
    }

    private static Stream<Arguments> provideExtractUnifiedTimeData() {
        return Stream.of(
                Arguments.of(new String[]{"8.00", "9.45"}, "8.00 - 9.45"),
                Arguments.of(new String[]{"", ""}, " - "),
                Arguments.of(new String[]{"10:00", "11:30"}, "10:00 - 11:30")
        );
    }

    @ParameterizedTest
    @MethodSource("provideExtractNotUnifiedTimeData")
    void extractNotUnifiedTime(String[] timeParts, String expected) {
        assertEquals(expected, StringUtils.extractNotUnifiedTime(timeParts));
    }

    private static Stream<Arguments> provideExtractNotUnifiedTimeData() {
        return Stream.of(
                Arguments.of(new String[]{"Пн-Пт", "8.00", "9.45", "Сб", "8.00", "9.45"},
                        "Пн-Пт 8.00 - 9.45, Сб 8.00 - 9.45"),
                Arguments.of(new String[]{"", "", "", "", "", ""},
                        "  - ,   - "),
                Arguments.of(new String[]{"Пн-Пт", "10:00", "11:30", "Сб", "12:00", "13:30"},
                        "Пн-Пт 10:00 - 11:30, Сб 12:00 - 13:30")
        );
    }
}