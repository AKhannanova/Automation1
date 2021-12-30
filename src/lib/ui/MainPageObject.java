package lib.ui;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import java.time.Duration;
import java.util.regex.Pattern;
import lib.Platform;

public class MainPageObject {
    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver){
        this.driver = driver;
    }

    public WebElement waitForElementPresent(String locator, String error_message, long timeoutInSeconds){
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return  wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    public WebElement waitForElementPresent(String locator, String error_message){
        return waitForElementPresent(locator, error_message, 5);
    }

    public WebElement waitForElementAndClick(String locator, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

     public boolean waitForElementNotPresent(String locator, String error_message, long timeoutInSeconds){
        By by = this.getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    public WebElement waitForElementAndClear(String locator, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public void swipeUp(int timeOfSwipe){
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(PointOption.point(x, start_y))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(timeOfSwipe)))
                .moveTo(PointOption.point(x, end_y))
                .release()
                .perform();
    }

    public void swipeUpQuick(){
        swipeUp(200);
    }

    public void swipeUpToFindElement(String locator, String error_message, int max_swipes){
        By by = this.getLocatorByString(locator);
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0){
            if (already_swiped >max_swipes){
                waitForElementPresent(locator, "Cannot find element by swipping up. \n" + error_message, 0);
                return;
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public boolean isElementLocatedOnScreen(String locator){
        int element_location_by_y = this.waitForElementPresent(locator, "Cannot find element by locator", 1).getLocation().getY();
        int screen_size_by_y = driver.manage().window().getSize().getHeight();
        return element_location_by_y < screen_size_by_y;
    }

    public void swipeUpTillElementAppear(String locator, String error_message, int max_swipes){
        int already_swiped = 0;
        while (!this.isElementLocatedOnScreen(locator)){
            if (already_swiped > max_swipes){
                Assert.assertTrue(error_message, this.isElementLocatedOnScreen(locator));
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public void clickElementToRightUpperCorner(String locator, String error_message){

        WebElement element = this.waitForElementPresent(locator + "/..", error_message);

        int right_x = element.getLocation().getX();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;
        int width = element.getSize().getWidth();

        int point_to_click_x = (right_x + width) - 3;
        int point_to_click_y = middle_y;

        System.out.println("3");

        TouchAction action = new TouchAction(driver);
        action.tap(PointOption.point(point_to_click_x, point_to_click_y)).perform();

    }

    public void swipeElementToLeft(String locator, String error_message){

        WebElement element = waitForElementPresent(
                locator,
                error_message,
                10
        );

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action.press(PointOption.point(right_x, middle_y));
        action.waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)));

        if (Platform.getInstance().isAndroid()){
            action.moveTo(PointOption.point(left_x, middle_y));
        } else {
            int offset_x = (-1 * element.getSize().getWidth());
            action.moveTo(PointOption.point(offset_x,0));
        }
        action.release();
        action.perform();
    }

    public int getAmountsOfElements(String locator){
        By by = this.getLocatorByString(locator);
        List elements = driver.findElements(by);
        return elements.size();
    }

    public void assertElementNotPresent(String locator, String error_message){
        int amount_of_elements = getAmountsOfElements(locator);
        if (amount_of_elements > 0){
            String default_message = "An element '" + locator.toString() + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    public void findArticles(String search_value){
        //click the 'Search Wikipedia'
        waitForElementAndClick(
                "//*[contains(@text,'Search Wikipedia')]",
                "Cannot find 'Search Wikipedia' input",
                5
        );

        //Enter the value for search - 'Java'
        waitForElementAndSendKeys(
                "//*[contains(@text,'Search…')]",
                search_value,
                "Cannot find search input",
                5
        );
    }

    public void openArticleAndSelectSaving(String article_title){
        //Find the article and click it to open
        waitForElementAndClick(
                "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + article_title + "']",
                "Cannot find " + article_title + " article",
                15
        );

        //Find the article title to make sure that the article is loaded
        waitForElementPresent(
                "org.wikipedia:id/view_page_title_text",
                "Cannot find article title",
                15
        );

        //Open the menu
        waitForElementAndClick(
                "//android.widget.ImageView[@content-desc='More options']",
                "Cannot find button to open article options",
                5
        );

        //Click the option 'Add to reading list'
        waitForElementPresent(
                "//android.widget.ListView",
                "Cannot find list",
                5
        );

        waitForElementAndClick(
                "//*[@text='Add to reading list']",
                "Cannot find option to add article to reading list",
                10
        );

    }

    private By getLocatorByString(String locator_with_type){

        String[] explorer_locator = locator_with_type.split(Pattern.quote(":"), 2);
        String by_type = explorer_locator[0];
        String locator = explorer_locator[1];

        if (by_type.equals("xpath")){
            return By.xpath(locator);
        } else if (by_type.equals("id")){
            return By.id(locator);
        } else {
            throw new IllegalArgumentException("Cannot get type of locator: " + locator_with_type);
        }
    }


}
