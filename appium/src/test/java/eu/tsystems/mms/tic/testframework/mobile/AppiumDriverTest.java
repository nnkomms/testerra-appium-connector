/*
 * Testerra
 *
 * (C) 2020, Eric Kubenka, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package eu.tsystems.mms.tic.testframework.mobile;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileBrowserType;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Date: 24.06.2020
 * Time: 12:26
 *
 * @author Eric Kubenka
 */
public class AppiumDriverTest {

    private final String accessKey = PropertyManager.getProperty("tt.mobile.grid.access.key");
    //    protected IOSDriver<IOSElement> driver = null;
    protected AndroidDriver<AndroidElement> driver = null;

    @BeforeMethod
    public void setUp() throws MalformedURLException {

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("testName", "Demo Tests");
        dc.setCapability("accessKey", accessKey);
        //        dc.setCapability("deviceQuery", "@os='ios' and @category='PHONE'");
        dc.setCapability("deviceQuery", "@os='android' and @category='PHONE'");
        //        dc.setBrowserName(MobileBrowserType.SAFARI);
        dc.setBrowserName(MobileBrowserType.CHROMIUM);
        //        driver = new IOSDriver<>(new URL("https://mobiledevicecloud.t-systems-mms.eu/wd/hub"), dc);
        driver = new AndroidDriver<AndroidElement>(new URL("https://mobiledevicecloud.t-systems-mms.eu/wd/hub"), dc);
    }

    @Test
    public void testT01_DoGoogleSearch() {

        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.get("https://www.google.com");
        new WebDriverWait(driver, 10).until(driver1 -> {
            ExpectedCondition<WebElement> q = ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='q']"));
            return q;
        });

        File screenshotAs = driver.getScreenshotAs(OutputType.FILE);
        System.out.println(screenshotAs.getAbsolutePath());
        WebElement searchBar = driver.findElement(By.xpath("//input[@name='q']"));
        searchBar.sendKeys("Experitest");
    }

    @AfterMethod
    public void tearDown() {

        System.out.println("Report URL: " + driver.getCapabilities().getCapability("reportUrl"));
        driver.quit();
    }
}
