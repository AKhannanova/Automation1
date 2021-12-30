import lib.CoreTestCase;
import lib.Platform;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import lib.ui.*;

import javax.swing.*;
import java.util.List;

public class Homeworks extends CoreTestCase {

    private static final String name_of_folder = "My articles";


    @Test
    public void testSaveTwoArticlesAndDeleteOne(){

        String search_value = "Java";
        String description1 = "Object-oriented programming language";
        String description2 = "Indonesian island";
        String description3 = "Island of Indonesia";

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);

        //Find first article
        SearchPageObject.initSearchInput(); //get the search field
        SearchPageObject.typeSearchLine(search_value);    //type "Java" to the search field
        SearchPageObject.clickByArticleWithSubstring(description1);   //click the article within the list

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);

        //wait until the article page is loaded
        String article_title1 = ArticlePageObject.getArticleTitle();

        //Save the first article (creating a new list for Android)
        if (Platform.getInstance().isAndroid()){
            ArticlePageObject.addArticleToNewList(name_of_folder);
        } else{
            ArticlePageObject.addArticleToMySaved();
        }

        //close the article
        ArticlePageObject.closeArticle();

        //Find the second article
        if (Platform.getInstance().isAndroid()){
            SearchPageObject.initSearchInput(); //get the search field
            SearchPageObject.typeSearchLine(search_value);    //type "Java" to the search field
            SearchPageObject.clickByArticleWithSubstring(description3);  //click the article within the list
        } else {
            SearchPageObject.clickByArticleWithSubstring(description2);  //click the article within the list
        }

        //wait until the second article page is loaded
        String article_title2 = ArticlePageObject.getArticleTitle();

        //Add article to existing list
        if (Platform.getInstance().isAndroid()){
            ArticlePageObject.addArticleToExistedList(name_of_folder);
        } else{
            ArticlePageObject.addArticleToMySaved();
        }

        //Close the second article
        ArticlePageObject.closeArticle();
        if (Platform.getInstance().isIOS()){
            SearchPageObject.waitForCancelButtonAndClick();
        }

        //Open the list of reading lists
        MyListPageObject MyListPageObject = MyListPageObjectFactory.get(driver);
        NavigationUI NavigationUI = NavigationUIFactory.get(driver);

        //Click the navigation button to get reading lists
        NavigationUI.clickMyList();

        //Open the reading list
        if (Platform.getInstance().isAndroid()){
            MyListPageObject.openFolderByName(name_of_folder);
        } else {
            MyListPageObject.waitAndClickCloseSyncButton();
        }

        //Delete the first article
        MyListPageObject.swipeByArticleToDelete(article_title1);

        MyListPageObject.waitForArticleToDisappearByTitle(article_title1);  //Check that the article is removed

        //Check that the second article is in the list

        if (Platform.getInstance().isAndroid()){
            assertEquals(
                    "Unexpected description",
                    description3.toLowerCase(),
                    MyListPageObject.getDescription(description3).getAttribute("text").toLowerCase()
            );
        } else {
            assertEquals(
                    "Unexpected description",
                    description2.toLowerCase(),
                    MyListPageObject.getDescription(description2).getAttribute("name").toLowerCase()
            );
        }

    }

    @Test
    public void testAssertElementPresent(){

        String search_value = "Java";
        String article_title = "Object-oriented programming language";

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);

        SearchPageObject.initSearchInput(); //get the search field

        SearchPageObject.typeSearchLine(search_value);    //type "Java" to the search field
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");   //click the article within the list

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);

        assertTrue(
                "The title is not found",
                ArticlePageObject.findArticleTitleWithoutWait() > 0
        );

    }

    @Test
    public void testCancelSearchingOfArticles(){

        String search_value = "Java";
        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);
        SearchPageObject.initSearchInput(); //get the search field
        SearchPageObject.typeSearchLine("Java");    //type "Java" to the search field
        SearchPageObject.getAmountOfFoundArticles();    //check that a few articles are found
        SearchPageObject.clickCancelSearch();   //click the cross to cancel search
        SearchPageObject.assertThereIsNoResultOfSearch();   //check that there is no search result

    }

    @Test
    public void testAllArticlesContainSearchValue(){
        String search_value = "Java";
        String search_value_low = search_value.toLowerCase();

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);

        SearchPageObject.initSearchInput(); //get the search field
        SearchPageObject.typeSearchLine(search_value);    //type "Java" to the search field

        SearchPageObject.waitForSearchResult(search_value); //the search result appears

        List titles_list = SearchPageObject.getArticlesTitlesList();    //get list of article titles

        //check that all articles titles contain the search value
        for (int i = 0; i < titles_list.size(); i++) {
                WebElement title = (WebElement) titles_list.get(i);
                if (Platform.getInstance().isAndroid()){
                    Assert.assertTrue(
                            "Does not contain " + search_value,
                            title.getAttribute("text").toLowerCase().contains(search_value_low)
                    );
                } else {
                    Assert.assertTrue(
                            "Does not contain " + search_value,
                            title.getAttribute("name").toLowerCase().contains(search_value_low)
                    );
                }
            }
        }

}





