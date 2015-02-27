package net.lapasa.vocaltweet.models.entities;

import android.database.sqlite.SQLiteException;

import com.orm.SugarRecord;
import com.twitter.sdk.android.core.models.MediaEntity;

import java.util.ArrayList;
import java.util.List;

public class MediaEntityRecord extends SugarRecord<MediaEntityRecord>
{
    TweetRecord owner;
    String url;
    String expandedUrl;
    String displayUrl;
    int start;
    int end;
    long mediaEntityId;
    String mediaEntityIdStr;
    String mediaUrl;
    String mediaUrlHttps;
    long sourceStatusId;
    String sourceStatusIdStr;
    String type;

    public MediaEntityRecord()
    {
        // Empty constuctor required
    }

    public MediaEntityRecord(TweetRecord owner, MediaEntity entity)
    {
        this.owner = owner;

        this.url = entity.url;
        this.expandedUrl = entity.expandedUrl;
        this.displayUrl = entity.displayUrl;
        this.start = entity.getStart();
        this.end = entity.getEnd();
        this.mediaEntityId = entity.id;
        this.mediaEntityIdStr = entity.idStr;
        this.mediaUrl = entity.mediaUrl;
        this.mediaUrlHttps = entity.mediaUrlHttps;
        this.sourceStatusId = entity.sourceStatusId;
        this.sourceStatusIdStr = entity.sourceStatusIdStr;
        this.type = entity.type;
    }

    public static void setMediaEntryList(TweetRecord owner, List<MediaEntity> entities)
    {
        for (MediaEntity entity : entities)
        {
            MediaEntityRecord record = new MediaEntityRecord(owner, entity);
            record.save();
        }
    }

    /**
     * Return a list of MediaEntity objects associated to this TweetRecord
     *
     * @param tweetRecord
     * @return
     */
    public static List<MediaEntity> getMediaEntityList(final TweetRecord tweetRecord)
    {
        String tweetId = String.valueOf(tweetRecord.getId());
        List<MediaEntity> entities = null;

        try
        {
            List<MediaEntityRecord> records = MediaEntityRecord.find(MediaEntityRecord.class, "owner = ?", tweetId);

            if (records != null && !records.isEmpty())
            {
                entities = new ArrayList<MediaEntity>();

                for (MediaEntityRecord record : records)
                {
                    entities.add(record.getMediaEntity());
                }
            }
        }
        catch (SQLiteException e)
        {
            return null;
        }

        return entities;
    }

    private MediaEntity getMediaEntity()
    {
        return new MediaEntity(url, expandedUrl, displayUrl, start, end, mediaEntityId, mediaEntityIdStr, mediaUrl, mediaUrlHttps, getSizes(), sourceStatusId, sourceStatusIdStr, type);
    }

    private MediaEntity.Sizes getSizes()
    {
        return null;
    }
}
