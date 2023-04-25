package hu.inf.unideb;

import java.io.IOException;

public interface SceneSwitcher {

    void switchSceneTo(String path) throws IOException;
}
