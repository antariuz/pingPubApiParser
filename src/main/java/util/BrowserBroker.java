package util;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

public enum BrowserBroker {
    INSTANCE;

    Playwright playwright;


    BrowserBroker() {
        playwright = Playwright.create();
    }


    public Browser startBrowser(Type type, String wsEndPoint) {
        switch (type) {
            case FIREFOX:
                return playwright.firefox().connectOverCDP(wsEndPoint);
            case CHROMIUM:
            default:
                return playwright.chromium().connectOverCDP(wsEndPoint);

        }
    }

    public void close() {
        playwright.close();
    }

    public enum Type {
        CHROMIUM,
        FIREFOX,
    }
}
