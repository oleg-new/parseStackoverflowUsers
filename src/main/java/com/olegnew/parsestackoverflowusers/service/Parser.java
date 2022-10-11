package com.olegnew.parsestackoverflowusers.service;

import com.google.gson.Gson;
import com.olegnew.parsestackoverflowusers.model.Collective;
import com.olegnew.parsestackoverflowusers.model.Collectives;
import com.olegnew.parsestackoverflowusers.model.Items;
import com.olegnew.parsestackoverflowusers.model.User;
import com.olegnew.parsestackoverflowusers.util.ConnectionUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.json.simple.parser.JSONParser;

@Data
public class Parser {
    private List<Items> users = new ArrayList<>();
    private JSONParser jsonParser = new JSONParser();
    private ConnectionUtil connectionUtil = new ConnectionUtil();

    public List<Items> getUserListFromApi(String usrUrl,
                                          List<String> locationList,
                                          List<String> tagList,
                                          String answerUrl,
                                          String questionUrl,
                                          int reputation) {
        String jsonString;
        int page = 1;
        while (true) {
            jsonString = connectionUtil.getStringFromUrl(usrUrl.replaceFirst(
                    "pageNumber", String.valueOf(page)));
            if (!jsonString.contains("items")) {
                break;
            }
            page++;
            getUsersListFromString(jsonString,
                    locationList,
                    tagList,
                    answerUrl,
                    questionUrl,
                    reputation);
        }
        return users;
    }

    public List<Items> getUsersListFromString(String usersString,
                                              List<String> locationList,
                                              List<String> tagList,
                                              String answerUrl,
                                              String questionUrl,
                                              int reputation) {
        Gson gson = new Gson();
        User user = gson.fromJson(usersString, User.class);
        for (Items currentUser:user.getItems()) {
            if (currentUser.getLocation() != null
                    && isCorrectLocation(currentUser, locationList)
                    && currentUser.getReputation() >= reputation) {
                if (currentUser.getCollectives()
                        != null && isTagInCollectives(currentUser,tagList)) {
                    currentUser.setAnswerCount(count(currentUser.getUserId(),
                            answerUrl, "answer"));
                    currentUser.setQuestionCount(count(currentUser.getUserId(),
                            questionUrl, "quest"));
                    users.add(currentUser);
                }
            }
        }
        return users;
    }

    private boolean isCorrectLocation(Items items, List<String> locationList) {
        return locationList
                .stream()
                .anyMatch(location -> items.getLocation().contains(location));
    }

    private boolean isTagInCollectives(Items items, List<String> tagList) {
        return tagList
                .stream()
                .anyMatch(t -> items.getCollectives()
                        .stream()
                        .map(Collectives::getCollective)
                        .map(Collective::getTags)
                        .flatMap(Collection::stream)
                        .collect(Collectors.joining(", "))
                        .contains(t));
    }

    public int count(Long userId, String requestedAddress, String target) {
        String stringUserId = userId.toString();
        String stringFromJson = connectionUtil.getStringFromUrl(requestedAddress.replaceFirst(
                "\\{ids}", stringUserId));
        return stringFromJson.split(target, -1).length - 1;
    }
}
