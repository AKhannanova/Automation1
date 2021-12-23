package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class SearchPageObject extends MainPageObject{

    private static final String
            SEARCH_INIT_ELEMENT = "xpath://*[contains(@text,'Search Wikipedia')]",
            SEARCH_INPUT = "xpath://*[contains(@text,'Search…')]",
            SEARCH_CANCEL_BUTTON = "id:org.wikipedia:id/search_close_btn",
            SEARCH_RESULT_BY_SUBSTRING_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
            SEARCH_RESULT_ELEMENT = "xpath://*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']",
            SEARCH_EMPTY_RESULT_ELEMENT = "xpath://*[@text='No results found']",
            SEARCH_RESULT_TITLE_AND_DESCRIPTION_BY_SUBSTRINGS_TPL = "xpath://*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING1}' or @text='{SUBSTRING2}']";


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


}
