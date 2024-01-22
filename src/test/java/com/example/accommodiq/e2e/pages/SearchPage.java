package com.example.accommodiq.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@ActiveProfiles("test")
public class SearchPage {
    private WebDriver driver;
    private static String PAGE_URL = "http://localhost:4200/search";

    @FindBy(how = How.CSS, using = "h1")
    private WebElement heading;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }

    public boolean hasOpened() {
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(heading, "stays"));
    }
}
