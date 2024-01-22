package com.example.accommodiq.e2e.tests;

import com.example.accommodiq.e2e.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchAndFilterTest extends TestBase {

    @Test
    public void searchAndFilter() {
        SearchPage searchPage = new SearchPage(driver);

        Assert.assertTrue(searchPage.hasOpened());
    }
}
