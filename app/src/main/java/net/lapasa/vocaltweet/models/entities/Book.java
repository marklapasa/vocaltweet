package net.lapasa.vocaltweet.models.entities;

import com.orm.SugarRecord;

/**
 * Created by mlapasa on 2/24/2015.
 */
public class Book extends SugarRecord
{
    String title;

    public Book()
    {
    }

    public Book(String title)
    {
        this.title = title;
    }
}
