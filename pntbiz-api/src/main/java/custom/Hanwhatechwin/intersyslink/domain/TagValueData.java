package custom.Hanwhatechwin.intersyslink.domain;

/**
 * Created by jhlee on 2017-09-20.
 */

public class TagValueData {
    private String tagId;
    private String tagName;
    private String tagValue;
    private String changeTagValue;
    private String serverId;
    private String readTime;
    private String categoryCode1;
    private String quality;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getChangeTagValue() {
        return changeTagValue;
    }

    public void setChangeTagValue(String changeTagValue) {
        this.changeTagValue = changeTagValue;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getCategoryCode1() {
        return categoryCode1;
    }

    public void setCategoryCode1(String categoryCode1) {
        this.categoryCode1 = categoryCode1;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}