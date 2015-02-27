package net.lapasa.vocaltweet.models.entities;

import android.database.sqlite.SQLiteException;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.models.UserEntities;

import java.util.List;

public class UserRecord extends SugarRecord<UserRecord>
{
    boolean contributorsEnabled;
    String createdAt;
    boolean defaultProfile;
    boolean defaultProfileImage;
    String description;
    String email;

    int favouritesCount;
    boolean followRequestSent;
    int followersCount;
    int friendsCount;
    boolean geoEnabled;
    long userId;
    String userIdStr;
    boolean isTranslator;
    String lang;
    int listedCount;
    String profileBackgroundImageUrlHttps;
    String location;
    String name;
    String profileBackgroundColor;
    String profileBackgroundImageUrl;
    boolean profileBackgroundTile;
    String profileBannerUrl;
    String profileImageUrl;
    String profileImageUrlHttps;
    String profileLinkColor;
    String profileSidebarBorderColor;
    String profileSidebarFillColor;
    String profileTextColor;
    boolean profileUseBackgroundImage;
    boolean protectedUser;
    String screenName;
    boolean showAllInlineMedia;

    int statusesCount;
    String timeZone;
    String url;
    int utcOffset;
    boolean verified;
    String withheldInCountries;
    String withheldScope;


    @Ignore
    Tweet status;

    @Ignore
    UserEntities entities;


    public UserRecord()
    {
        // Empty Constructor required
    }

    public UserRecord(User user)
    {
        this.contributorsEnabled = user.contributorsEnabled;
        this.createdAt = user.createdAt;
        this.defaultProfile = user.defaultProfile;
        this.defaultProfileImage = user.defaultProfileImage;
        this.description = user.description;
        this.email = user.email;
        this.entities = user.entities;
        this.favouritesCount = user.favouritesCount;
        this.followRequestSent = user.followRequestSent;
        this.followersCount = user.followersCount;
        this.friendsCount = user.friendsCount;
        this.geoEnabled = user.geoEnabled;
        this.userId = user.id;
        this.userIdStr = user.idStr;
        this.isTranslator = user.isTranslator;
        this.lang = user.lang;
        this.listedCount = user.listedCount;
        this.location = user.location;
        this.name = user.name;
        this.profileBackgroundColor = user.profileBackgroundColor;
        this.profileBackgroundImageUrl = user.profileBackgroundImageUrl;
        this.profileBackgroundImageUrlHttps = user.profileBackgroundImageUrlHttps;
        this.profileBackgroundTile = user.profileBackgroundTile;
        this.profileBannerUrl = user.profileBannerUrl;
        this.profileImageUrl = user.profileImageUrl;
        this.profileImageUrlHttps = user.profileImageUrlHttps;
        this.profileLinkColor = user.profileLinkColor;
        this.profileSidebarBorderColor = user.profileSidebarBorderColor;
        this.profileSidebarFillColor = user.profileSidebarFillColor;
        this.profileTextColor = user.profileTextColor;
        this.profileUseBackgroundImage = user.profileUseBackgroundImage;
        this.protectedUser = user.protectedUser;
        this.screenName = user.screenName;
        this.showAllInlineMedia = user.showAllInlineMedia;
        this.status = user.status;
        this.statusesCount = user.statusesCount;
        this.timeZone = user.timeZone;
        this.url = user.url;
        this.utcOffset = user.utcOffset;
        this.verified = user.verified;
        this.withheldInCountries = user.withheldInCountries;
        this.withheldScope = user.withheldScope;


    }

    public User getUser()
    {
        return new User(contributorsEnabled, createdAt, defaultProfile, defaultProfileImage, description, email, entities, favouritesCount, followRequestSent, followersCount, friendsCount, geoEnabled, userId, userIdStr, isTranslator, lang, listedCount, location, name, profileBackgroundColor, profileBackgroundImageUrl, profileBackgroundImageUrlHttps, profileBackgroundTile, profileBannerUrl, profileImageUrl, profileImageUrlHttps, profileLinkColor, profileSidebarBorderColor, profileSidebarFillColor, profileTextColor, profileUseBackgroundImage, protectedUser, screenName, showAllInlineMedia, status, statusesCount, timeZone, url, utcOffset, verified, withheldInCountries, withheldScope);
    }

    public static UserRecord findExistingUserRecord(long userId)
    {
        try
        {
            List<UserRecord> userRecordList = UserRecord.find(UserRecord.class, "userId = " + userId, null);
            if (userRecordList != null && !userRecordList.isEmpty())
            {
                return userRecordList.get(0);
            }
        }
        catch (SQLiteException e)
        {
            return null;
        }

        return null;
    }
}
