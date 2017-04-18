package com.mattaniah.wisechildhalacha.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.activities.MainActivity;
import com.mattaniah.wisechildhalacha.bookmarking.BookmarkManager;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;
import com.mattaniah.wisechildhalacha.models.SeifModel;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Beezy Works Studios on 2/18/2016.
 */
public class BookmarkRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private Book book;
    private RecyclerView recyclerView;

    HebrewDateFormatter hebrewDateFormatter = new HebrewDateFormatter();


    public BookmarkRecyclerAdapter(Context context, Book book, RecyclerView recyclerView) {
        this.context = context;
        this.book = book;
        this.recyclerView = recyclerView;
        hebrewDateFormatter.setHebrewFormat(true);
        hebrewDateFormatter.setUseGershGershayim(false);
        createDataSet();
    }

    List<Object> dataSet = new ArrayList<>();

    private void createDataSet() {
        dataSet.clear();
        Map<Sections, Map<Integer, Map<Integer, String>>> bookmarkedSections = new TreeMap<>();
        for (Sections sections : Sections.values()) {
            Map<Integer, Map<Integer, String>> markedSimanim = BookmarkManager.getInstance().getBookmarksForSection(sections, book);
            if (!markedSimanim.isEmpty())
                bookmarkedSections.put(sections, markedSimanim);
        }
        for (Sections section : bookmarkedSections.keySet()) {
            Map<Integer, Map<Integer, String>> bookmarkedSimanim = new TreeMap<>(bookmarkedSections.get(section));
            dataSet.add(section);
            for (Integer siman : bookmarkedSimanim.keySet()) {
                Map<Integer, String> seifim = new TreeMap<>(bookmarkedSimanim.get(siman));
                dataSet.add(siman);
                for (Integer sief : seifim.keySet()) {
                    dataSet.add(new SeifModel(section, book, siman, sief, seifim.get(sief)));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    private static final int SECTION_HEADER_TYPE = 0;
    private static final int SIMAN_HEADER_TYPE = 1;
    private static final int SEIF_TYPE = 2;
    private static final int SPACE_TYPE = 3;

    @Override
    public int getItemViewType(int position) {
        if (position == dataSet.size())
            return SPACE_TYPE;
        if (dataSet.get(position) instanceof Sections)
            return SECTION_HEADER_TYPE;
        if (dataSet.get(position) instanceof Integer)
            return SIMAN_HEADER_TYPE;
        if (dataSet.get(position) instanceof SeifModel)
            return SEIF_TYPE;

        return SPACE_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView baseTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.bookmark_textview, parent, false);
        switch (viewType) {
            case SECTION_HEADER_TYPE:
                return new SectionHeaderHolder(baseTextView);
            case SIMAN_HEADER_TYPE:
                return new SimanHeaderHolder(baseTextView);
            case SEIF_TYPE:
                return new BookmarkHolder(LayoutInflater.from(context).inflate(R.layout.bookmark_row, parent, false));
            case SPACE_TYPE:
                return new SpaceViewHolder(baseTextView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionHeaderHolder)
            ((SectionHeaderHolder) holder).setSection((Sections) dataSet.get(position));
        if (holder instanceof SimanHeaderHolder)
            ((SimanHeaderHolder) holder).setSiman((Integer) dataSet.get(position));
        if (holder instanceof BookmarkHolder)
            ((BookmarkHolder) holder).setSeif((SeifModel) dataSet.get(position));
    }


    class SectionHeaderHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SectionHeaderHolder(TextView textView) {
            super(textView);
            this.textView = textView;
            int style = R.style.BookmarkText_SectionHeader;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.textView.setTextAppearance(style);
            } else {
                //noinspection deprecation
                this.textView.setTextAppearance(context, style);
            }
        }

        public void setSection(Sections section) {
            textView.setText(section.getName());
        }
    }

    class SimanHeaderHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SimanHeaderHolder(TextView textView) {
            super(textView);
            this.textView = textView;
            int style = R.style.BookmarkText_SimanHeader;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.textView.setTextAppearance(style);
            } else {
                //noinspection deprecation
                this.textView.setTextAppearance(context, style);
            }
        }

        public void setSiman(int siman) {
            textView.setText(String.format("%s %s", "סימן", hebrewDateFormatter.formatHebrewNumber(siman)));
        }
    }

    class BookmarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button textView;
        SeifModel seif;

        public BookmarkHolder(View itemView) {
            super(itemView);
            this.textView = (Button) itemView.findViewById(R.id.textView);
            itemView.findViewById(R.id.deleteButton).setOnClickListener(this);
            this.textView.setOnClickListener(this);
            int style = R.style.BookmarkText_Bookmark;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.textView.setTextAppearance(style);
            } else {
                //noinspection deprecation
                this.textView.setTextAppearance(context, style);
            }
        }

        public void setSeif(SeifModel bookmarkModel) {
            textView.setText((String.format("%s %s", "סעיף", hebrewDateFormatter.formatHebrewNumber(bookmarkModel.getSeif() + 1))));
            this.seif = bookmarkModel;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.deleteButton:
                    deleteSeif();
                    break;
                case R.id.textView:
                    startSeif();
                    break;
            }
        }

        private void startSeif(){
            SettingsUtil settingsUtil = new SettingsUtil(context);
            book.saveAsDefault(settingsUtil);
            settingsUtil.saveDefaultSection(seif.getSection());
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(MainActivity.Extra_Set_Siman, seif.getSiman());
            intent.putExtra(MainActivity.Extra_Set_Seif, seif.getSeif());
            intent.putExtra(MainActivity.Extra_Set_Book, seif.getBook().ordinal());
            intent.putExtra(MainActivity.Extra_Set_Section, seif.getSection().ordinal());
            context.startActivity(intent);
        }

        private void deleteSeif(){
            if (seif == null)
                return;
            final int position = dataSet.indexOf(seif);
            BookmarkManager.getInstance().removeBookmark(seif.getSection(), book, seif.getSiman(), seif.getSeif());
            dataSet.remove(position);
            notifyItemRemoved(position);
            Snackbar.make(recyclerView, R.string.bookmarkDeleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyItemInserted(position);
                            dataSet.add(position, seif);
                            BookmarkManager.getInstance().addBookmark(seif.getSection(), book, seif.getSiman(), seif.getSeif(), seif.getBookmark());
                        }
                    }).show();
        }
    }

    class SpaceViewHolder extends RecyclerView.ViewHolder {

        public SpaceViewHolder(View itemView) {
            super(itemView);
            itemView.setMinimumHeight((int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material) * 2);
        }
    }

    public void deleteAll() {
        final List<Object> removedData = new ArrayList<>(dataSet);
        dataSet.clear();
        BookmarkManager.getInstance().remove(book);
        notifyDataSetChanged();
        Snackbar.make(recyclerView, R.string.allBookmarksDeleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataSet.addAll(removedData);
                        for (Object object :removedData){
                            if (object instanceof SeifModel)
                                BookmarkManager.getInstance().addBookmark((SeifModel)object);
                        }
                        notifyDataSetChanged();
                    }
                }).show();
    }
}
