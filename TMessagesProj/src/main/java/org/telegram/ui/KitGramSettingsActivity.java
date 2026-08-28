package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.KitGramSettings;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

import java.util.ArrayList;

public class KitGramSettingsActivity extends BaseFragment {
    private RecyclerListView listView;
    private ListAdapter listAdapter;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(getString(R.string.KitGramSettings));
        actionBar.setAllowOverlayTitle(true);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) finishFragment();
            }
        });

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listAdapter = new ListAdapter(context);
        listView.setAdapter(listAdapter);
        frameLayout.addView(listView, LayoutHelper.createFrame(-1, -1));
        listView.setOnItemClickListener((view, position) -> {
            if (position >= 1 && position <= 4) {
                String key = listAdapter.keys[position - 1];
                boolean enabled = !KitGramSettings.isEnabled(key);
                KitGramSettings.getPreferences().edit().putBoolean(key, enabled).apply();
                ((TextCheckCell) view).setChecked(enabled);
            }
        });
        fragmentView = frameLayout;
        return fragmentView;
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> descriptions = new ArrayList<>();
        descriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR,
                new Class[]{HeaderCell.class, TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        descriptions.add(new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, Theme.key_windowBackgroundGray));
        return descriptions;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context context;
        private final String[] keys = {KitGramSettings.KEEP_DELETED_MESSAGES, KitGramSettings.KEEP_EDITED_MESSAGES, KitGramSettings.HIDE_TYPING, KitGramSettings.HIDE_READ_TIME};

        ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : position == 5 ? 1 : 2;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getAdapterPosition() >= 1 && holder.getAdapterPosition() <= 4;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = viewType == 0 ? new HeaderCell(context) : viewType == 1 ? new TextInfoPrivacyCell(context) : new TextCheckCell(context);
            return new RecyclerView.ViewHolder(view) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                ((HeaderCell) holder.itemView).setText(getString(R.string.KitGramFeatures));
            } else if (position == 5) {
                ((TextInfoPrivacyCell) holder.itemView).setText(getString(R.string.KitGramSettingsInfo));
            } else {
                TextCheckCell cell = (TextCheckCell) holder.itemView;
                int title = position == 1 ? R.string.KitGramKeepDeleted : position == 2 ? R.string.KitGramKeepEdited : position == 3 ? R.string.KitGramHideTyping : R.string.KitGramHideReadTime;
                cell.setTextAndCheck(getString(title), KitGramSettings.isEnabled(keys[position - 1]), position != 4);
            }
        }
    }
}