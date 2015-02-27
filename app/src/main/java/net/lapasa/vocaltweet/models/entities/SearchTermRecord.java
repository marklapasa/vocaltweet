package net.lapasa.vocaltweet.models.entities;

import com.orm.SugarRecord;

/**
 * Created by mlapasa on 2/20/2015.
 */
public class SearchTermRecord extends SugarRecord<SearchTermRecord>
{
    String term;

    public SearchTermRecord(){}

    public SearchTermRecord(String term)
    {
        this.term = term;
    }
}
