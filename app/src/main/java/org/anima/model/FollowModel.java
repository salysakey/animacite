package org.anima.model;

import org.anima.entities.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BeugFallou on 09/07/15.
 */
public class FollowModel {
    private static FollowModel instance;
    public List<Food> lists;

    public FollowModel() {
        this.lists = new ArrayList<Food>();
    }

    public static FollowModel getInstance() {
        if (instance == null){
            // Create the instance
            instance = new FollowModel();
        }
        return instance;
    }

    public List<Food> getLists() {
        return lists;
    }

    public void addToList(Food newOne) {
        if (!lists.contains(newOne)) {
            this.lists.add(newOne);
        }
    }

    public void removeFromList(Food objectToRemove) {
        for(Food item : lists) {
            if (objectToRemove.getId() == item.getId()) {
                lists.remove(item);
                return;
            }
        }
    }
}
