package lib.ui.android;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListPageObject;

public class AndroidMyListPageObject extends MyListPageObject {

    static {
                FOLDER_BY_NAME_TPL = "xpath://*[@text='{FOLDER_NAME}']";
                ARTICLE_BY_TITLE_TPL = "xpath://*[@text='{TITLE}']";
                DESCRIPTION = "id:org.wikipedia:id/page_list_item_description";
    }

    public AndroidMyListPageObject(AppiumDriver driver){

        super(driver);
    }
}
