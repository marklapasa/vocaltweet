package net.lapasa.vocaltweet.models.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.Date;
import java.util.List;

@Table(name = "SEARCH_TERM_RECORD")
public class SearchTermRecord extends SugarRecord
{
    private long date;
    private String term;

    // Required empty constructor
    public SearchTermRecord(){}

    public SearchTermRecord(String term)
    {
        this.term = term;
        this.date = new Date().getTime();
    }

    public void setTerm(String term)
    {
        this.term = term;
    }


    public String getTerm()
    {
        return term;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }


    public static SearchTermRecord getRecord(String term)
    {
        List<SearchTermRecord> records = SearchTermRecord.findWithQuery(SearchTermRecord.class, "Select * from SEARCH_TERM_RECORD where term = ? ", term);

        if (!records.isEmpty())
        {
            return records.get(0);
        }

        return null;
    }
}
