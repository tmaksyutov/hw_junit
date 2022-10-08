package ru.itone;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ru.itone.data.Locale;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {
    @BeforeAll
    public static void setUp() {
        Configuration.holdBrowserOpen = true;
    }
    @ValueSource(strings = {"Ipad", "Ipad Air"})
    @ParameterizedTest(name = "Поиск Ipad в магазине {0}")
    void appleSearchTest(String testData) {
        open("https://www.apple.com/");
        $("#ac-gn-link-search").click();
        $("#ac-gn-searchform-input").setValue(testData);
        $("#ac-gn-searchform-input").setValue(testData).pressEnter();
        $("#exploreCurated")
                .shouldHave(text(testData));
    }
    @CsvSource( {
            "Ipad, iPad is a versatile and capable device",
            "Ipad Air, iPad Air offers an all-screen design"
    })
    @ParameterizedTest(name = "Поиск по описанию Ipad {0}")
    void appleSearchWithDescription(String searchQuery, String description){
        open("https://www.apple.com/");
        $("#ac-gn-link-search").click();
        $("#ac-gn-searchform-input").setValue(searchQuery);
        $("#ac-gn-searchform-input").setValue(searchQuery).pressEnter();
        $("#exploreCurated")
                .shouldHave(text(description));
    }

    static Stream<Arguments> selenideButtonsText(){
        return Stream.of(
                Arguments.of(Locale.EN, List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes")),
                Arguments.of(Locale.RU, List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"))
        );

    }
    @MethodSource
    @ParameterizedTest(name = "Проверка отображения названия кнопок для локали: {0}")
    void selenideButtonsText(Locale locale, List<String> buttonsTexts) {
    open("https://ru.selenide.org/");
        $$("#languages a").find(text(locale.name())).click();
        $$(".main-menu-pages a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }
    @EnumSource(Locale.class)
    @ParameterizedTest
    void checkLocaleTest(Locale locale) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).shouldBe(visible);
    }
}

