package lib.ui.ios;

import io.appium.java_client.AppiumDriver;
import lib.ui.MyListPageObject;

public class IOSMyListPageObject extends MyListPageObject {

    static {
        ARTICLE_BY_TITLE_TPL = "id:{TITLE}";//"xpath://XCUIElementTypeLink[contains(@name='{TITLE}')]";
        ARTICLE_BY_DESCRIPTION_TPL = "id:{DESCRIPTION}";
        DESCRIPTION = "id:Indonesian island";
        CLOSE_SYNC_BUTTON = "id:Close";
        DELETE_BUTTON = "id:swipe action delete";
    }

    public IOSMyListPageObject(AppiumDriver driver){

        super(driver);
    }
}
