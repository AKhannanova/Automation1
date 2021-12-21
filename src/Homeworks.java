import lib.CoreTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import lib.ui.*;

public class Homeworks extends CoreTestCase {


    @Test
    public void testSaveTwoArticlesAndDeleteOne(){

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        //Find first article
        SearchPageObject.initSearchInput(); //get the search field
        SearchPageObject.typeSearchLine("Java");    //type "Java" to the search field
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");   //click the article within the list

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);

        ArticlePageObject.waitForTitleElement();    //wait until the article page is loaded
        String article_title1 = ArticlePageObject.getArticleTitle();

        //Save the first article, creating a new list
        String name_of_folder = "My articles";
        ArticlePageObject.addArticleToNewList(name_of_folder);   //Add the article to the list
        ArticlePageObject.closeArticle();   //close the article

        //Find the second article
        SearchPageObject.initSearchInput(); //get the search field
        SearchPageObject.typeSearchLine("Java");    //type "Java" to the search field
        SearchPageObject.clickByArticleWithSubstring("Island of Indonesia");    //click the article within the list

        ArticlePageObject.waitForTitleElement();    //wait until the second article page is loaded
        String article_title2 = ArticlePageObject.getArticleTitle();

        //Add article to existing list
        ArticlePageObject.addArticleToExistedList(name_of_folder);
        ArticlePageObject.closeArticle();   //Close the second article

        //Open the list of reading lists
        MyListPageObject MyListPageObject = new MyListPageObject(driver);
        NavigationUI NavigationUI = new NavigationUI(driver);

        NavigationUI.clickMyList(); //Click the navigation button to get all reading lists
        MyListPageObject.openFolderByName(name_of_folder);  //Open the reading list

        //Delete the first article
        MyListPageObject.swipeByArticleToDelete(article_title1);    //Swipe to delete
        MyListPageObject.waitForArticleToDisappearByTitle(article_title1);  //Check that the article is removed

        //Open the second article and check the title
        MyListPageObject.waitForArticleAppearAndClickByTitle(article_title2);

        ArticlePageObject.waitForTitleElement();
        assertEquals(
                "Unexpected title",
                article_title2,
                ArticlePageObject.getArticleTitle()
        );

    }

    @Test
    public void testAssertElementPresent(){

        String search_value = "Java";
        String article_title = "Object-oriented programming language";

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput(); //get the search field

        SearchPageObject.typeSearchLine(search_value);    //type "Java" to the search field
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");   //click the article within the list

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);

        assertTrue(
                "The title is not found",
                ArticlePageObject.findArticleTitleWithoutWait() > 0
        );

    }

    @Test
    public void testCancelSearchingOfArticles(){

        String search_value = "Java";
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput(); //get the search field
        SearchPageObject.typeSearchLine("Java");    //type "Java" to the search field
        SearchPageObject.getAmountOfFoundArticles();    //check that a few articles are found
        SearchPageObject.clickCancelSearch();   //click the cross to cancel search
        SearchPageObject.assertThereIsNoResultOfSearch();   //check that there is no search result

    }


}
