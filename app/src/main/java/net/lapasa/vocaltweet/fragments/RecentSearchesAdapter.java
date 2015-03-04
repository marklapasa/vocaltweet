package net.lapasa.vocaltweet.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.models.SearchTermsModel;

public class RecentSearchesAdapter extends BaseAdapter
{
    private final LayoutInflater inflator;
    private SearchTermsModel model = SearchTermsModel.getInstance();

    public RecentSearchesAdapter(LayoutInflater inflator)
    {
        this.inflator = inflator;
    }

    @Override
    public int getCount()
    {
        return model.getTerms().size();
    }

    @Override
    public Object getItem(int position)
    {
        String text = model.getTerms().get(position).getTerm();
        return text;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String text = model.getTerms().get(position).getTerm();

        TextView tv = (TextView) convertView;
        if (tv == null)
        {
            tv = (TextView) inflator.inflate(R.layout.recent_search_term_text_view, null);
        }

        tv.setText(text);
        return tv;
    }


}
