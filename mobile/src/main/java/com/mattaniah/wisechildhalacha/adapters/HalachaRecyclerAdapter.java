package com.mattaniah.wisechildhalacha.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.bookmarking.BookmarkManager;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.HostActivity;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.mattaniah.wisechildhalacha.helpers.ViewUtil;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mattaniah on 7/20/2015.
 */
public class HalachaRecyclerAdapter extends RecyclerView.Adapter<HalachaRecyclerAdapter.HalachaViewHolder> {
    private HostActivity hostActivity=null;
    private final Sections section;
    private final Book book;

    JSONArray simanim;
    ViewUtil viewUtil;

    List<Object> dataSet = new ArrayList<>();
    List<SimanHeader> simanHeaders = new ArrayList<>();
    List<SeifHeader> seifHeaders = new ArrayList<>();

    HebrewDateFormatter hFat = new HebrewDateFormatter();
    String[] simanNames;

    final int SIMAN_HEADER = 0;
    final int SEIF_HEADER = 1;
    final int BODY = 2;


    public HalachaRecyclerAdapter(Activity context, Sections section, Book book) {
        if (context instanceof HostActivity)
            this.hostActivity = (HostActivity) context;
        this.section = section;
        this.book = book;
        this.viewUtil = new ViewUtil(context);
        simanim = section.getJSONArray(context, book);
        hFat.setUseGershGershayim(false);
        simanNames = context.getResources().getStringArray(R.array.siman_names);
        Map<String, Map> sectionBookmarks = BookmarkManager.getInstance().getBookmarksForSection(section, book);

        try {
            for (int i = 0; i < simanim.length(); i++) {
                SimanHeader simanHeader = new SimanHeader(i + section.getFirstSiman());
                dataSet.add(simanHeader);
                simanHeaders.add(simanHeader);
                JSONArray simanArray = simanim.getJSONArray(i);
                Map simanBookmarks = sectionBookmarks.get(BookmarkManager.getSimanKey(i + section.getFirstSiman()));
                for (int j = 0; j < simanArray.length(); j++) {
                    SeifHeader seifHeader = new SeifHeader(i + section.getFirstSiman(), j);
                    seifHeader.isBookmarked = simanBookmarks != null && simanBookmarks.containsKey(BookmarkManager.getSeifKey(j));
                    seifHeaders.add(seifHeader);
                    dataSet.add(seifHeader);
                    dataSet.add(simanArray.getString(j));
                    simanHeader.addSeif(seifHeader);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*@param the relative number of the siman in the section. E.g. if the section starts with 28, the 30th siman would have
    * a relative position of 3*/
    public int getPositionForSiman(int relativePosition) {
        return dataSet.indexOf(simanHeaders.get(relativePosition));
    }

    public int getPositionForSeif(int relativePositionofSiman, int seifNumber) {
        SimanHeader simanHeader = simanHeaders.get(relativePositionofSiman);
        return dataSet.indexOf(simanHeader.getSeifim().get(seifNumber));
    }

    public List<SeifHeader> getSeifimForSiman(int relativeSimanPosition) {
        SimanHeader simanHeader = simanHeaders.get(relativeSimanPosition);
        return simanHeader.getSeifim();
    }

    public int getNumberOfSimanim() {
        return simanim.length();
    }


    @Override
    public int getItemViewType(int position) {
        Object itemAtPosition = dataSet.get(position);
        if (itemAtPosition instanceof String)
            return BODY;
        if (itemAtPosition instanceof SimanHeader)
            return SIMAN_HEADER;
        if (itemAtPosition instanceof SeifHeader)
            return SEIF_HEADER;
        return BODY;
    }

    public abstract class HalachaViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public HalachaViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class SimanHeaderViewHolder extends HalachaViewHolder implements View.OnClickListener {
        TextView textView;
        TextView seifCount;
        ImageView divider;
        SimanHeader simanHeader;

        public SimanHeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.siman_textview);
            divider = (ImageView) itemView.findViewById(R.id.divider);
            seifCount = (TextView) itemView.findViewById(R.id.seif_count);
            itemView.setOnClickListener(this);
            divider.setColorFilter(viewUtil.getDividerColor());
            textView.setTextColor(viewUtil.getSimanHeaderTextColor());
            seifCount.setTextColor(viewUtil.getSimanSeifCountColor());
        }

        public void setSiman(SimanHeader simanHeader) {
            this.simanHeader = simanHeader;
            textView.setText("סימן " + hFat.formatHebrewNumber(simanHeader.getSiman()));
            seifCount.setText(seifCount());
        }

        public void setDividerVisibility(boolean visibility) {
            divider.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        }

        private String seifCount() {
            return "ובו " + hFat.formatHebrewNumber(simanHeader.getSize()) + " סעיפים";
        }

        @Override
        public void onClick(View v) {
            if (hostActivity!=null&&simanHeader!=null)
                hostActivity.showSeifPicker(simanHeaders.indexOf(simanHeader));
        }
    }

    private class SeifHeaderViewHolder extends HalachaViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView bookmarkButton;
        SeifHeader seif;

        public SeifHeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.seif_textview);
            bookmarkButton = (ImageView) itemView.findViewById(R.id.seif_bookmark);

            int color = viewUtil.getSeifHeaderTextColor();
            textView.setTextColor(color);
            bookmarkButton.setColorFilter(color);
        }

        public void setSeif(SeifHeader seif) {
            this.seif = seif;
            textView.setText(seif.getSeifName());
            bookmarkButton.setOnClickListener(this);
            setIsBookmarked(seif.isBookmarked);
        }

        @Override
        public void onClick(View v) {
            bookmarkButton.setImageResource(R.drawable.ic_bookmark);
            bookmarkButton.setColorFilter(viewUtil.getBookmarkedColor());
            setIsBookmarked(!seif.isBookmarked);
            toggleBookmarked(!seif.isBookmarked);
        }

        private void setIsBookmarked(boolean isBookmarked) {
            bookmarkButton.setColorFilter(isBookmarked ? viewUtil.getBookmarkedColor() : viewUtil.getSeifHeaderTextColor());
            bookmarkButton.setImageResource(isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
        }

        private void toggleBookmarked(boolean isBookmarked) {
            if (isBookmarked)
                seif.addBookmark();
            else
                seif.removeBookmark();
        }
    }

    private class BodyViewHolder extends HalachaViewHolder {
        TextView textView;

        public BodyViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView;
            textView.setTextColor(viewUtil.getTextColor());
            textView.setTextSize(viewUtil.getTextSize());
            textView.setTypeface(viewUtil.getTypeface());
            textView.setLineSpacing(1f, viewUtil.getLineSpacing());
        }

        public void setText(String text) {
            textView.setText(Html.fromHtml(text));
        }
    }

    @Override
    public HalachaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SIMAN_HEADER:
                return new SimanHeaderViewHolder(viewUtil.getLayoutInflater().inflate(R.layout.siman_header, parent, false));
            case SEIF_HEADER:
                return new SeifHeaderViewHolder(viewUtil.getLayoutInflater().inflate(R.layout.seif_header, parent, false));
            case BODY:
                return new BodyViewHolder(viewUtil.getLayoutInflater().inflate(R.layout.text_component_simple, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(HalachaViewHolder holder, int position) {
        Object itemAtPosition = dataSet.get(position);

        if (itemAtPosition instanceof String) {
            ((BodyViewHolder) holder).setText((String) itemAtPosition);
            return;
        }

        if (itemAtPosition instanceof SimanHeader) {
            ((SimanHeaderViewHolder) holder).setSiman(((SimanHeader) itemAtPosition));
            ((SimanHeaderViewHolder) holder).setDividerVisibility(position != 0);
            return;
        }

        if (itemAtPosition instanceof SeifHeader) {
            ((SeifHeaderViewHolder) holder).setSeif(((SeifHeader) itemAtPosition));
            return;
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class SimanHeader {
        int siman;

        private List<SeifHeader> seifim;

        public int getSiman() {
            return siman;
        }

        public SimanHeader(int siman) {
            this.siman = siman;
            this.seifim = new ArrayList<>();
        }

        public int getSize() {
            return seifim.size();
        }

        public List<SeifHeader> getSeifim() {
            return seifim;
        }

        public void addSeif(SeifHeader seif) {
            this.seifim.add(seif);
        }
    }

    public class SeifHeader {
        int seif;
        boolean isBookmarked;
        int siman;

        public SeifHeader(int siman, int seif) {
            this.seif = seif;
            this.siman = siman;
        }

        public String getSeifName() {
            return "סעיף " + hFat.formatHebrewNumber(seif + 1);
        }

        public void addBookmark() {
            this.isBookmarked = true;
            BookmarkManager.getInstance().addBookmark(section, book, siman, seif, " ");
        }

        public void removeBookmark() {
            this.isBookmarked = false;
            BookmarkManager.getInstance().removeBookmark(section, book, siman, seif);
        }


    }
}
