package nl.erikduisters.popularmovies.ui.activity.main_activity;

import java.util.List;

import nl.erikduisters.popularmovies.util.MyMenuItem;

/**
 * Created by Erik Duisters on 19-12-2017.
 */

final class MainActivityViewState {
    final List<MyMenuItem> optionsMenu;
    final boolean showAboutDialog;

    MainActivityViewState(List<MyMenuItem> optionsMenu, boolean showAboutDialog) {
        this.optionsMenu = optionsMenu;
        this.showAboutDialog = showAboutDialog;
    }
}
