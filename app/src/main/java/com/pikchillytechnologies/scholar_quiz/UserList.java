package com.pikchillytechnologies.scholar_quiz;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserList {

    private String AdminFlag;
    private String EmailId;
    private String ModeratorFlag;
    private String Name;
    private String SlackId;
    private String UserId;
    private String channelId;
    private String channelName;

    public UserList() {
        /*Blank default constructor essential for Firebase*/
    }

    public UserList(String AdminFlag, String EmailId, String ModeratorFlag, String Name, String SlackId, String UserId, String channelId, String channelName) {

        this.AdminFlag = AdminFlag;
        this.EmailId = EmailId;
        this.ModeratorFlag = ModeratorFlag;
        this.Name = Name;
        this.SlackId = SlackId;
        this.UserId = UserId;
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public String getAdminFlag() {
        return AdminFlag;
    }

    public void setAdminFlag(String adminFlag) {
        AdminFlag = adminFlag;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getModeratorFlag() {
        return ModeratorFlag;
    }

    public void setModeratorFlag(String moderatorFlag) {
        ModeratorFlag = moderatorFlag;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSlackId() {
        return SlackId;
    }

    public void setSlackId(String slackId) {
        SlackId = slackId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
