package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.WebElement;

abstract public class MyListPageObject extends MainPageObject {

    protected static String
            FOLDER_BY_NAME_TPL,
            ARTICLE_BY_TITLE_TPL,
            ARTICLE_BY_DESCRIPTION_TPL,
            DESCRIPTION,
            CLOSE_SYNC_BUTTON,
            DELETE_BUTTON;

    /*TEMPLATE METHODS*/
    private static String getFolderXpathByName(String name_of_folder) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }

    private static String getSaveArticleXpathByTitle(String article_title) {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", article_title);
    }

    private static  String getSavedArticleDescriptionXpath(String description){
        return  ARTICLE_BY_DESCRIPTION_TPL.replace("{DESCRIPTION}",description);
    }
    /*TEMPLATE METHODS*/

    public MyListPageObject(AppiumDriver driver) {
        super(driver);
    }

    //Open the reading list with articles
    public void openFolderByName(String name_of_folder) {
        String folder_name_xpath = getFolderXpathByName(name_of_folder);

        this.waitForElementPresent(
                folder_name_xpath,
                "Cannot find folder by name " + name_of_folder,
                15
        );

        this.waitForElementAndClick(
                folder_name_xpath,
                "Cannot find folder by name " + name_of_folder,
                15
        );
    }

    //Open the article clicking the title within the reading list
    public void waitForArticleAppearByTitle(String article_title) {
        String article_xpath = getSaveArticleXpathByTitle(article_title);
        this.waitForElementPresent(
                article_xpath,
                "cannot find saved article by title " + article_title,
                15
        );
    }

    //Click the article with the title
    public void waitForArticleAppearAndClickByTitle(String article_title) {
        String article_xpath = getSaveArticleXpathByTitle(article_title);
        this.waitForElementAndClick(
                article_xpath,
                "cannot find saved article by title " + article_title,
                5
        );
    }

    //Check that the article is removed from the reading list
    public void waitForArticleToDisappearByTitle(String article_title) {
        String article_xpath = getSaveArticleXpathByTitle(article_title);
        this.waitForElementNotPresent(
                article_xpath,
                "Saved article still present with title " + article_title,
                5
        );
    }

    //Delete the article from the reading list
    public void swipeByArticleToDelete(String article_title) {
        this.waitForArticleAppearByTitle(article_title);
        String article_xpath = getSaveArticleXpathByTitle(article_title);
        this.swipeElementToLeft(
                article_xpath,
                "Cannot find saved article"
        );

        if (Platform.getInstance().isIOS()) {
            this.waitDeleteButtonAndClick();
            //this.clickElementToRightUpperCorner(article_title, "Cannot find saved article");
        }

        this.waitForArticleToDisappearByTitle(article_title);
    }

    public void waitAndClickCloseSyncButton() {
        this.waitForElementAndClick(
                CLOSE_SYNC_BUTTON,
                "Cannot find close button on the SYNC window",
                10
        );
    }

    public void waitDeleteButtonAndClick() {
        this.waitForElementAndClick(
                DELETE_BUTTON,
                "Cannot find delete button",
                10
        );
    }

    public WebElement getDescription(String description) {
        if (Platform.getInstance().isAndroid()) {
            return this.waitForElementPresent(
              DESCRIPTION,
              "Cannot find description",
              10
            );
        } else {
            return this.waitForElementPresent(
                    getSavedArticleDescriptionXpath(description),
                    "Cannot find description",
                    10
            );
        }
    }
}

