package raf.projekat_back.dto;

public class UpdateStatusDto {

    private Integer userID;
    private Integer id;
    private String action;
    private String restartPhase;

    public Integer getUserID() {
        return userID;
    }

    public String getRestartPhase() {
        return restartPhase;
    }

    public void setRestartPhase(String restartPhase) {
        this.restartPhase = restartPhase;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
