package net.lapasa.vocaltweet.models;

import android.database.sqlite.SQLiteException;

import net.lapasa.vocaltweet.models.entities.SearchTermRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class SearchTermsModel extends Observable
{
    public static final int SEARCH_TERMS_LOADED = 50;
    private static SearchTermsModel _instance;
    private List<SearchTermRecord> terms = new ArrayList<>();


    // Singleton
    private SearchTermsModel() {};

    public static SearchTermsModel getInstance()
    {
        if (_instance == null)
        {
            _instance = new SearchTermsModel();
            _instance.loadRecentSearchTerms();
        }
        return _instance;
    }

    public List<SearchTermRecord> getTerms()
    {
        return terms;
    }

    public void clearCache()
    {
        SearchTermRecord.deleteAll(SearchTermRecord.class);
        loadRecentSearchTerms();
    }

    /**
     * Query Search Terms table for terms entered submited by the user
     */
    public void loadRecentSearchTerms()
    {
        terms.clear();

        try
        {
            List<SearchTermRecord> results = SearchTermRecord.findWithQuery(SearchTermRecord.class, "Select * from SEARCH_TERM_RECORD order by date desc");

            for (SearchTermRecord record : results)
            {
                terms.add(record);
            }
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }

        notifyObservers(SEARCH_TERMS_LOADED);
    }

    @Override
    public void notifyObservers(Object data)
    {
        setChanged();
        super.notifyObservers(data);
    }
}