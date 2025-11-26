package domain;

import java.util.List;

/**
 * Curated list of POIs.
 */
public class POIList {
    private String listId;
    private String title;
    private List<POI> pois;
    private String curatorId;
    private boolean published;

    public POIList() {
    }

    public POIList(String listId, String title, List<POI> pois, String curatorId, boolean published) {
        this.listId = listId;
        this.title = title;
        this.pois = pois;
        this.curatorId = curatorId;
        this.published = published;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<POI> getPois() {
        return pois;
    }

    public void setPois(List<POI> pois) {
        this.pois = pois;
    }

    public String getCuratorId() {
        return curatorId;
    }

    public void setCuratorId(String curatorId) {
        this.curatorId = curatorId;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
