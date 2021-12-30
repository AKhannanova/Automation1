package lib.ui;
import lib.Platform;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

abstract public class SearchPageObject extends MainPageObject{

   protected static String
            SEARCH_INIT_ELEMENT,
            SEARCH_INPUT,
            SEARCH_CANCEL_BUTTON,
            SEARCH_RESULT_BY_SUBSTRING_TPL,
            SEARCH_RESULT_ELEMENT,
            SEARCH_EMPTY_RESULT_ELEMENT,
            SEARCH_RESULT_TITLE_AND_DESCRIPTION_BY_SUBSTRINGS_TPL,
            ARTICLE_TITLE,
            RESULT_LIST,
            ARTICLE_TPL,
            CANCEL_SEARCH_BUTTON;


    public SearchPageObject(AppiumDriver driver){
        super(driver);
    }

    /*TEMPLATE METHODS*/
    private static String getResultSearchElement(String substring){
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    private static String getResultTitleAndDescription(String substring1, String substring2){
        return SEARCH_RESULT_TITLE_AND_DESCRIPTION_BY_SUBSTRINGS_TPL.replace("{SUBSTRING1}", substring1).replace("{SUBSTRING2}", substring2);
    }

    private static String getArticleXPath(String i){
        return ARTICLE_TPL.replace("{i}", i);
    }
    /*TEMPLATE METHODS*/

    public void initSearchInput(){
        this.waitForElementPresent(SEARCH_INIT_ELEMENT, "Cannot find search input after clicking search init element");
        this.waitForElementAndClick(SEARCH_INIT_ELEMENT, "Cannot find and click search init element", 5);
    }

    public void waitForCancelButtonToAppear(){
        this.waitForElementPresent(SEARCH_CANCEL_BUTTON, "Cannot find search cancel button", 5);
    }

    public void waitForCancelButtonToDisappear(){
        this.waitForElementNotPresent(SEARCH_CANCEL_BUTTON, "Search cancel button is still presented", 5);
    }

    public void clickCancelSearch(){
        this.waitForElementAndClick(SEARCH_CANCEL_BUTTON, "Cannot find and click search cancel button", 5);
    }

    public void typeSearchLine(String search_line){
        this.waitForElementAndSendKeys(SEARCH_INPUT, search_line, "Cannot find and type into search input", 5);
    }

    public void waitForSearchResult(String substring){
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementPresent(search_result_xpath, "Cannot find search result with subscring " + substring, 5);
    }

    public void clickByArticleWithSubstring(String substring){
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementAndClick(search_result_xpath, "Cannot find and click search result with subsctring " + substring, 5);
    }

    public int getAmountOfFoundArticles(){
        this.waitForElementPresent(
                SEARCH_RESULT_ELEMENT,
                "Cannot find anything by the request",
                15
        );
        return this.getAmountsOfElements(SEARCH_RESULT_ELEMENT);
    }

    public void waitForEmptyResultLabel(){
        this.waitForElementPresent(
                SEARCH_EMPTY_RESULT_ELEMENT,
                "Cannot find empty result element",
                15
        );
    }

    public void assertThereIsNoResultOfSearch(){
        this.assertElementNotPresent(
                SEARCH_RESULT_ELEMENT,
                "We've found some results by request"
        );

    }

    public void waitForElementByTitleAndDescription(String title, String description){
        this.waitForElementPresent(
                getResultTitleAndDescription(title, description),
                "Cannot find article with title " + title + " and description " + description,
                15
        );
    }

    public void waitForCancelButtonAndClick(){
        this.waitForElementAndClick(
          CANCEL_SEARCH_BUTTON,
          "Cannot find search button",
          15
        );
    }

    public WebElement getSearchResultList(){
        return this.waitForElementPresent(
          RESULT_LIST,
          "No results",
          15
        );
    }

    public List getArticlesTitlesList(){
        if (Platform.getInstance().isAndroid()) {
            return getSearchResultList().findElements(By.id("org.wikipedia:id/page_list_item_title"));
        } else{
            return getSearchResultList().findElements(By.xpath("//XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeStaticText[1]"));
        }
    }


}
