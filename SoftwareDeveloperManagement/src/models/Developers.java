package models;

import java.io.Serializable;
import java.util.List;

public class Developers implements Serializable {

    private String devID;
    private String fullName;
    private List<String> programmingLanguages;
    private int salary;

    public Developers(String devID, String fullName, List<String> programmingLanguages, int salary) {
        this.devID = devID;
        this.fullName = fullName;
        this.programmingLanguages = programmingLanguages;
        this.salary = salary;
    }

    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getProgrammingLanguages() {
        return programmingLanguages;
    }

    public void setProgrammingLanguages(List<String> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("| %-8s | %-20s | %-25s | %8d |",devID, fullName, programmingLanguages.toString(), salary);
    }
}