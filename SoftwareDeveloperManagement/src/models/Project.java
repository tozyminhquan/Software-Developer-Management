package models;

import java.io.Serializable;

public class Project implements Serializable {

    private String projectID;
    private String devID;
    private String projectName;
    private int duration;
    private String startDate;

    public Project(String projectID, String devID, String projectName, int duration, String startDate) {
        this.projectID = projectID;
        this.devID = devID;
        this.projectName = projectName;
        this.duration = duration;
        this.startDate = startDate;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-8s | %-25s | %-15d | %-12s |",
                projectID, devID, projectName, duration, startDate);
    }
}
