package com.olegnew.parsestackoverflowusers;

import com.olegnew.parsestackoverflowusers.model.Items;
import com.olegnew.parsestackoverflowusers.service.Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final int reputation = 223;
    private static final List<String> tagList = new ArrayList<>(
            Arrays.asList("java", ".net", "docker", "C#"));
    private static final List<String> locationList = new ArrayList<>(
            Arrays.asList("Moldova","Romania"));
    private static final String stringUserUrl = "https://api.stackexchange.com"
            + "/2.3/users?page=pageNumber&pagesize=100&order=desc&"
            + "sort=reputation&site=stackoverflow";
    private static final String stringAnswerUrl = "https://api.stackexchange.com"
            + "/2.3/users/{ids}/answers?order=desc&sort=activity&site=stackoverflow";
    private static final String stringQuestionUrl = "https://api.stackexchange.com"
            + "/2.3/users/{ids}/questions?order=desc&sort=activity&site=stackoverflow";

    private static final Parser parser = new Parser();

    public static void main(String[] args) {
        List<Items> usersList = parser.getUserListFromApi(stringUserUrl,
                locationList, tagList, stringAnswerUrl, stringQuestionUrl, reputation);
        System.out.println("====================================================================================================");
        System.out.println("===============list of users with tags : \"java\", \".net\", \"docker\", \"C#\"=============================");
        System.out.println(usersList);
        usersList.clear();
        tagList.add("google");
        usersList = parser.getUserListFromApi(stringUserUrl,
                locationList, tagList, stringAnswerUrl, stringQuestionUrl, reputation);
        System.out.println("====================================================================================================");
        System.out.println("===============list of users with tags : \"java\", \".net\", \"docker\", \"C#\" , \"google\"==================");
        System.out.println(usersList);

    }
}
