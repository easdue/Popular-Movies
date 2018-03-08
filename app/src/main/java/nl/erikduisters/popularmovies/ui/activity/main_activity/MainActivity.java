package nl.erikduisters.popularmovies.ui.activity.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.ui.BaseActivity;
import nl.erikduisters.popularmovies.ui.dialog.AboutDialog;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragment;
import nl.erikduisters.popularmovies.util.MenuUtil;
import nl.erikduisters.popularmovies.util.MyMenuItem;

public class MainActivity extends BaseActivity<MainActivityViewModel> implements AboutDialog.DismissListener {
    private static final String TAG_MOVIE_LIST_FRAGMENT = "MovieListFragment";
    private static final String TAG_ABOUT_DIALOG = "AboutDialog";

    @BindView(R.id.toolbar) Toolbar toolbar;

    private List<MyMenuItem> optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_MOVIE_LIST_FRAGMENT);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentPlaceholder, MovieListFragment.newInstance(), TAG_MOVIE_LIST_FRAGMENT)
                    .commit();
        }

        viewModel.getViewState().observe(this, this::render);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<MainActivityViewModel> getViewModelClass() {
        return MainActivityViewModel.class;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuUtil.updateMenu(menu, optionsMenu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                viewModel.onMenuItemClicked(item.getItemId());
                return true;
        }

        return false;
    }

    private void render(@Nullable MainActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        optionsMenu = viewState.optionsMenu;

        invalidateOptionsMenu();

        if (viewState.showAboutDialog) {
            showAboutDialog();
        }
    }

    private void showAboutDialog() {
        AboutDialog dialog = (AboutDialog) getSupportFragmentManager().findFragmentByTag(TAG_ABOUT_DIALOG);

        if (dialog == null) {
            dialog = AboutDialog.newInstance();
            dialog.show(getSupportFragmentManager(), TAG_ABOUT_DIALOG);
        }

        dialog.setListener(this);
    }

    @Override
    public void onDismiss() {
        viewModel.onAboutDialogDismissed();
    }
}
