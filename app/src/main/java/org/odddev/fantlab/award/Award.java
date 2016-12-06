package org.odddev.fantlab.award;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author kenrube
 * @since 07.12.16
 */

public class Award {

    @SerializedName("award_close")
    @Expose
    public String awardClose;

    @SerializedName("award_id")
    @Expose
    public String awardId;

    @SerializedName("award_type")
    @Expose
    public String awardType;

    @SerializedName("comment")
    @Expose
    public String comment;

    @SerializedName("compiler")
    @Expose
    public String compiler;

    @SerializedName("contests")
    @Expose
    public List<Contest> contests;

    @SerializedName("copyright")
    @Expose
    public String copyright;

    @SerializedName("copyright_link")
    @Expose
    public String copyrightLink;

    @SerializedName("country_id")
    @Expose
    public String countryId;

    @SerializedName("country_name")
    @Expose
    public String countryName;

    @SerializedName("curator_id")
    @Expose
    public String curatorId;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("homepage")
    @Expose
    public String homepage;

    @SerializedName("is_opened")
    @Expose
    public Integer isOpened;

    @SerializedName("lang_id")
    @Expose
    public String langId;

    @SerializedName("max_date")
    @Expose
    public String maxDate;

    @SerializedName("min_date")
    @Expose
    public String minDate;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("non_fantastic")
    @Expose
    public String nonFantastic;

    @SerializedName("notes")
    @Expose
    public String notes;

    @SerializedName("process_status")
    @Expose
    public String processStatus;

    @SerializedName("rusname")
    @Expose
    public String rusname;

    @SerializedName("show_in_list")
    @Expose
    public String showInList;
}
