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

package eu.tsystems.mms.tic.testframework.mobile.driver;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraSystemException;
import eu.tsystems.mms.tic.testframework.webdrivermanager.UnspecificWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverFactory;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileBrowserType;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Creates {@link WebDriver} sessions for {@link io.appium.java_client.AppiumDriver} based on {@link AppiumDriverRequest}
 * Date: 24.06.2020
 * Time: 13:16
 *
 * @author Eric Kubenka
 */
public class AppiumDriverFactory extends WebDriverFactory<AppiumDriverRequest> {

    private static final String GRID_ACCESS_KEY = PropertyManager.getProperty("tt.mobile.grid.access.key");
    private static final String GRID_URL = PropertyManager.getProperty("tt.mobile.grid.url");

    private static final String APPIUM_DEVICE_QUERY_IOS = PropertyManager.getProperty("tt.mobile.device.query.ios", "@os='ios' and @category='PHONE'");
    private static final String APPIUM_DEVICE_QUERY_ANDROID = PropertyManager.getProperty("tt.mobile.device.query.android", "@os='android' and @category='PHONE'");

    @Override
    protected AppiumDriverRequest buildRequest(WebDriverRequest webDriverRequest) {

        AppiumDriverRequest r;

        if (webDriverRequest instanceof AppiumDriverRequest) {
            r = (AppiumDriverRequest) webDriverRequest;
        } else if (webDriverRequest instanceof UnspecificWebDriverRequest) {
            r = new AppiumDriverRequest();
            r.copyFrom(webDriverRequest);
        } else {
            throw new TesterraSystemException(webDriverRequest.getClass().getSimpleName() + " is not allowed here");
        }

        return r;
    }

    @Override
    protected DesiredCapabilities buildCapabilities(DesiredCapabilities preSetCaps, AppiumDriverRequest request) {

        return preSetCaps;
    }

    @Override
    protected WebDriver getRawWebDriver(AppiumDriverRequest webDriverRequest, DesiredCapabilities desiredCapabilities) {

        // early exit.
        if (webDriverRequest.browser == null) {
            throw new TesterraRuntimeException("DriverRequest was null.");
        }

        // general caps
        desiredCapabilities.setCapability("testName", "Demo Tests"); // TODO here
        desiredCapabilities.setCapability("accessKey", GRID_ACCESS_KEY);

        switch (webDriverRequest.browser) {
            case MobileBrowsers.mobile_safari:

                desiredCapabilities.setCapability("deviceQuery", APPIUM_DEVICE_QUERY_IOS);
                desiredCapabilities.setBrowserName(MobileBrowserType.SAFARI);

                try {
                    final IOSDriver<IOSElement> driver = new IOSDriver<>(new URL(GRID_URL), desiredCapabilities);
                    final AppiumDeviceQuery appiumDeviceQuery = new AppiumDeviceQuery(driver.getCapabilities());
                    log().info("iOS Session created for: " + appiumDeviceQuery.toString());
                    return driver;
                } catch (MalformedURLException e) {
                    throw new SessionNotCreatedException("Could not create session, because URL invalid");
                }

            case MobileBrowsers.mobile_chrome:

                desiredCapabilities.setCapability("deviceQuery", APPIUM_DEVICE_QUERY_ANDROID);
                desiredCapabilities.setBrowserName(MobileBrowserType.CHROMIUM);

                try {
                    final AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL(GRID_URL), desiredCapabilities);
                    final AppiumDeviceQuery appiumDeviceQuery = new AppiumDeviceQuery(driver.getCapabilities());
                    log().info("Android Session created for: " + appiumDeviceQuery.toString());
                    return driver;
                } catch (MalformedURLException e) {
                    throw new SessionNotCreatedException("Could not create session, because URL invalid");
                }

            default:
                throw new TesterraRuntimeException("Mobile Browser not supported.");
        }
    }

    @Override
    protected void setupSession(EventFiringWebDriver eventFiringWebDriver, AppiumDriverRequest request) {

    }
}