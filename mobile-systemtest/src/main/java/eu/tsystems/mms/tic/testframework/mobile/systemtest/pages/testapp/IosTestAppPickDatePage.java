package eu.tsystems.mms.tic.testframework.mobile.systemtest.pages.testapp;

import eu.tsystems.mms.tic.testframework.mobile.By;
import eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement.NativeMobileGuiElement;

/**
 * Created by nkfa on 31.01.2017.
 */
public class IosTestAppPickDatePage extends TestAppPickDatePage {

    /**
     * Default constructor.
     */
    public IosTestAppPickDatePage() {

        datePicker = new NativeMobileGuiElement(By.xPath("//*[@class='UIDatePicker']"));
        confirmButton = new NativeMobileGuiElement(By.accessibilityLabel("Ok"));
        checkPage();
    }
}
