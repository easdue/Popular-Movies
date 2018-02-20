package nl.erikduisters.popularmovies.ui.activity.main_activity;

import java.util.List;

import nl.erikduisters.popularmovies.util.MyMenuItem;

/**
 * Created by Erik Duisters on 19-12-2017.
 */

final class MainActivityViewState {
    final List<MyMenuItem> optionsMenu;

    MainActivityViewState(List<MyMenuItem> optionsMenu) {
        this.optionsMenu = optionsMenu;
    }
}
