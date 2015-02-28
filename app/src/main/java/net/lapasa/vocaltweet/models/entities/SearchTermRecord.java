package net.lapasa.vocaltweet.models.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by mlapasa on 2/20/2015.
 */

@Table(name = "SEARCH_TERM_RECORD")
public class SearchTermRecord extends SugarRecord
{
    String term;

    public SearchTermRecord(){}

    public SearchTermRecord(String term)
    {
        this.term = term;
    }
}
