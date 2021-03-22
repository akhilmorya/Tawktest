package com.tawktest.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Avinash Kumar
 * @mail avisingh736@gmail.com
 */

@Entity(indices = [Index(value = ["id", "login"], unique = true)])
@Parcelize
data class User(
        @SerializedName("avatar_url")
        @ColumnInfo(name = "avatar_url")
        val avatarUrl: String,
        @SerializedName("events_url")
        @ColumnInfo(name = "events_url")
        val eventsUrl: String,
        @SerializedName("followers_url")
        @ColumnInfo(name = "followers_url")
        val followersUrl: String,
        @SerializedName("following_url")
        @ColumnInfo(name = "following_url")
        val followingUrl: String,
        @SerializedName("gists_url")
        @ColumnInfo(name = "gists_url")
        val gistsUrl: String,
        @SerializedName("gravatar_id")
        @ColumnInfo(name = "gravatar_id")
        val gravatarId: String,
        @SerializedName("html_url")
        @ColumnInfo(name = "html_url")
        val htmlUrl: String,
        @PrimaryKey
        @SerializedName("id")
        val id: Int,
        @SerializedName("login")
        @ColumnInfo(name = "login")
        val login: String,
        @SerializedName("node_id")
        @ColumnInfo(name = "node_id")
        val nodeId: String,
        @SerializedName("organizations_url")
        @ColumnInfo(name = "organizations_url")
        val organizationsUrl: String,
        @SerializedName("received_events_url")
        @ColumnInfo(name = "received_events_url")
        val receivedEventsUrl: String,
        @SerializedName("repos_url")
        @ColumnInfo(name = "repos_url")
        val reposUrl: String,
        @SerializedName("site_admin")
        @ColumnInfo(name = "site_admin")
        val siteAdmin: Boolean,
        @SerializedName("starred_url")
        @ColumnInfo(name = "starred_url")
        val starredUrl: String,
        @SerializedName("subscriptions_url")
        @ColumnInfo(name = "subscriptions_url")
        val subscriptionsUrl: String,
        @SerializedName("type")
        @ColumnInfo(name = "type")
        val type: String,
        @SerializedName("url")
        @ColumnInfo(name = "url")
        val url: String,
        @SerializedName("followers")
        @ColumnInfo(name = "followers")
        val followers: Int,
        @SerializedName("following")
        @ColumnInfo(name = "following")
        val following: Int,
        @SerializedName("notes")
        @ColumnInfo(name = "notes")
        var notes: String?
) : Parcelable