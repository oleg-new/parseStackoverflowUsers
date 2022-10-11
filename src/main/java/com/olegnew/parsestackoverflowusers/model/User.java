package com.olegnew.parsestackoverflowusers.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class User {

    @SerializedName("items")
    private List<Items> items;
}
