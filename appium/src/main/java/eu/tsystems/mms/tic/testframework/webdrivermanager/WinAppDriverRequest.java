/*
 * Testerra
 *
 * (C) 2021, Mike Reiche, T-Systems MMS GmbH, Deutsche Telekom AG
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
 */

package eu.tsystems.mms.tic.testframework.webdrivermanager;

import eu.tsystems.mms.tic.testframework.appium.Browsers;
import eu.tsystems.mms.tic.testframework.appium.WinAppDriverFactory;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class WinAppDriverRequest extends AbstractWebDriverRequest implements Loggable {
    public static final String TOP_LEVEL_WINDOW="appTopLevelWindow";
    public static final String DEVICE_NAME="deviceName";
    public static final String APP_ID="app";
    public static final String WORKING_DIR="appWorkingDir";
    public static final String APP_ARGUMENTS ="appArguments";
    public static final String APP_ID_DESKTOP="Root";
    public static final String WAIT_FOR_APP_LAUNCH="ms:waitForAppLaunch";

    private String reuseApplicationByWindowTitle = null;
    private String appId = null;
    private int startupTimeoutSeconds = 0;
    private int reuseTimeoutSeconds = 0;

    public WinAppDriverRequest() {
        super();
        setBrowser(Browsers.windows);
        setStartupTimeoutSeconds(WinAppDriverFactory.Properties.STARTUP_TIMEOUT_SECONDS.asLong().intValue());
        setReuseTimeoutSeconds(WinAppDriverFactory.Properties.REUSE_TIMEOUT_SECONDS.asLong().intValue());
    }

    public void setDesktopApplication() {
        this.setApplication(APP_ID_DESKTOP);
    }

    /**
     * Tries to reuse a driver by an given application window title.
     * @param applicationTitle
     */
    public void reuseApplicationByWindowTitle(String applicationTitle) {
        this.reuseApplicationByWindowTitle = applicationTitle;
        if (DEFAULT_SESSION_KEY.equals(this.getSessionKey())) {
            this.setSessionKey(applicationTitle);
        }
    }

    public Optional<String> getReusableApplicationWindowTitle() {
        return Optional.ofNullable(this.reuseApplicationByWindowTitle);
    }

    public void setApplicationPath(String applicationPath) {
        log().info("application path string: " + applicationPath);
        log().info("application path: " + Paths.get(applicationPath));
        log().info("application path parent: " + Paths.get(applicationPath).getParent());
        this.setApplicationPath(Paths.get(applicationPath));
    }

    public void setApplicationPath(Path applicationPath) {
//        if (!Files.exists(applicationPath)) {
//            throw new RuntimeException("Application not found: " + applicationPath);
//        }
        this.setApplication(applicationPath.toString());
        this.setWorkingDir(applicationPath.getParent().toString());
    }

    /**
     * Sets the application id and the session key.
     */
    public void setApplication(String applicationId) {
        this.appId = applicationId;
        if (applicationId != null) {
            this.setSessionKey(applicationId);
        }
    }

    /**
     * Removes the application id
     */
    public void unsetApplication() {
        this.appId = null;
    }

    public Optional<String> getApplicationId() {
        return Optional.ofNullable(this.appId);
    }

    public void setWorkingDir(String workingDir) {
        this.getDesiredCapabilities().setCapability(WORKING_DIR, workingDir);
    }

    public void setApplicationArguments(String ... argv) {
        this.getDesiredCapabilities().setCapability(APP_ARGUMENTS, String.join(" ", argv));
    }

    @Override
    public Optional<URL> getServerUrl() {
        if (!super.getServerUrl().isPresent()) {
            try {
                setServerUrl(WinAppDriverFactory.Properties.WINAPP_SERVER_URL.asString());
            } catch (MalformedURLException e) {
                log().error("Invalid value for property: " + WinAppDriverFactory.Properties.WINAPP_SERVER_URL.toString());
            }
        }
        return super.getServerUrl();
    }

    public int getStartupTimeoutSeconds() {
        return this.startupTimeoutSeconds;
    }

    public void setStartupTimeoutSeconds(int startupTimeoutSeconds) {
        this.startupTimeoutSeconds = startupTimeoutSeconds;
        // Setting the app launch timeout is useless, because it waits
        // even when the application is already started
        // this.getDesiredCapabilities().setCapability(WAIT_FOR_APP_LAUNCH, startupTimeoutSeconds);
    }

    public int getReuseTimeoutSeconds() {
        return reuseTimeoutSeconds;
    }

    public void setReuseTimeoutSeconds(int reuseTimeoutSeconds) {
        this.reuseTimeoutSeconds = reuseTimeoutSeconds;
    }
}
