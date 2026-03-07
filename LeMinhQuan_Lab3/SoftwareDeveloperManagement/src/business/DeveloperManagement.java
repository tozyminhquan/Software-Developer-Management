package business;

import java.io.*;
import java.util.*;
import models.Developer;
import tools.Acceptable;

public class DeveloperManagement implements Workable<Developer> {

    private static final String FILE_PATH = "developers.txt";
    private List<Developer> devList;

    public DeveloperManagement() {
        devList = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public void loadFromFile() {
        devList.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        int errorCount = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                int startBracket = line.indexOf('[');
                int endBracket = line.indexOf(']');
                if (startBracket == -1 || endBracket == -1) {
                    errorCount++;
                    continue;
                }

                String beforeBracket = line.substring(0, startBracket).trim();
                String insideBracket = line.substring(startBracket + 1, endBracket).trim();
                String afterBracket = line.substring(endBracket + 1).trim();

                String[] parts = beforeBracket.split(",");
                if (parts.length < 2) {
                    errorCount++;
                    continue;
                }
                String devID = parts[0].trim();
                String fullName = parts[1].trim();

                List<String> languages = new ArrayList<>();
                String[] langArr = insideBracket.split(",");
                for (int i = 0; i < langArr.length; i++) {
                    languages.add(langArr[i].trim());
                }

                String salaryStr = afterBracket;
                if (salaryStr.startsWith(",")) {
                    salaryStr = salaryStr.substring(1).trim();
                }

                int salary = 0;
                try {
                    salary = Integer.parseInt(salaryStr);
                } catch (NumberFormatException e) {
                    errorCount++;
                    continue;
                }

                devList.add(new Developer(devID, fullName, languages, salary));
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (errorCount > 0) {
            System.out
                    .println("Warning: File " + FILE_PATH + " has " + errorCount + " error line(s) that were skipped.");
        }
    }

    @Override
    public void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH));
            for (int i = 0; i < devList.size(); i++) {
                Developer d = devList.get(i);
                pw.println(d.getDevID() + ", " + d.getFullName() + ", "
                        + d.getProgrammingLanguages().toString() + ", " + d.getSalary());
            }
            pw.close();
            System.out.println("Saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public void add(Developer dev) {
        devList.add(dev);
    }

    public void addFromConsole() {
        System.out.println("\n===== ADD NEW DEVELOPER =====");

        String devID;
        while (true) {
            devID = Acceptable.checkRegex(
                    "Enter Developer ID (e.g. DEV001): ",
                    "DEV\\d{3}",
                    "Error: Developer ID must be a 6-character string, starting with \"DEV\" followed by 3 digits (e.g. DEV001)!");
            if (findById(devID) != null) {
                System.out.println("Error: This ID already exists, please enter a different ID.");
            } else {
                break;
            }
        }

        String fullName = Acceptable.checkString("Enter Full Name: ");

        String langInput = Acceptable.checkString("Enter Programming Languages (e.g. Java, C++): ");
        List<String> languages = new ArrayList<>();
        String[] langArr = langInput.split(",");
        for (int i = 0; i < langArr.length; i++) {
            String trimmed = langArr[i].trim();
            if (!trimmed.isEmpty()) {
                languages.add(trimmed);
            }
        }
        if (languages.isEmpty()) {
            languages.add("N/A");
        }

        int salary = Acceptable.checkInt("Enter Salary (USD, >= 1000): ", 1000, Integer.MAX_VALUE);

        devList.add(new Developer(devID, fullName, languages, salary));
        System.out.println("Added successfully!");
    }

    @Override
    public void update(String id) {
        Developer dev = findById(id);
        if (dev == null) {
            System.out.println("Developer ID does not exist!");
            return;
        }

        System.out.println("--- Update info (Press Enter to keep old data) ---");

        System.out.print("New Full Name [" + dev.getFullName() + "]: ");
        String newName = Acceptable.readOptionalLine();
        if (!newName.isEmpty()) {
            dev.setFullName(newName);
        }

        String currentLangs = "";
        List<String> langs = dev.getProgrammingLanguages();
        for (int i = 0; i < langs.size(); i++) {
            if (i > 0)
                currentLangs += ", ";
            currentLangs += langs.get(i);
        }
        System.out.print("New Languages [" + currentLangs + "]: ");
        String newLang = Acceptable.readOptionalLine();
        if (!newLang.isEmpty()) {
            List<String> listLang = new ArrayList<>();
            String[] arr = newLang.split(",");
            for (int i = 0; i < arr.length; i++) {
                listLang.add(arr[i].trim());
            }
            dev.setProgrammingLanguages(listLang);
        }

        System.out.print("New Salary [" + dev.getSalary() + "]: ");
        String newSalary = Acceptable.readOptionalLine();
        if (!newSalary.isEmpty()) {
            try {
                int salary = Integer.parseInt(newSalary);
                if (salary < 1000) {
                    System.out.println("Error: Salary must be at least 1000 USD. Keeping old salary.");
                } else {
                    dev.setSalary(salary);
                }
            } catch (Exception e) {
                System.out.println("Invalid salary format. Keep old salary.");
            }
        }
        System.out.println("Update successful!");
    }

    @Override
    public void delete(String id) {
        Developer dev = findById(id);
        if (dev == null) {
            System.out.println("Error: Developer not found with ID: " + id);
            return;
        }
        devList.remove(dev);
        System.out.println("Developer [" + id + "] deleted successfully.");
    }

    @Override
    public List<Developer> getAll() {
        return devList;
    }

    public Developer findById(String id) {
        for (int i = 0; i < devList.size(); i++) {
            if (devList.get(i).getDevID().equalsIgnoreCase(id)) {
                return devList.get(i);
            }
        }
        return null;
    }

    public void updateSalary(String id) {
        Developer dev = findById(id);
        if (dev == null) {
            System.out.println("Developer ID does not exist!");
            return;
        }
        System.out.println("Current salary of [" + id + "]: " + dev.getSalary() + " USD");
        int newSalary = Acceptable.checkInt("Enter new Salary (USD, >= 1000): ", 1000, Integer.MAX_VALUE);
        dev.setSalary(newSalary);
        System.out.println("Salary updated successfully!");
    }

    public List<Developer> filterByLanguage(String language) {
        List<Developer> result = new ArrayList<>();
        for (int i = 0; i < devList.size(); i++) {
            Developer d = devList.get(i);
            boolean found = false;
            List<String> devLangs = d.getProgrammingLanguages();
            for (int j = 0; j < devLangs.size(); j++) {
                if (devLangs.get(j).equalsIgnoreCase(language)) {
                    found = true;
                }
            }
            if (found) {
                result.add(d);
            }
        }
        return result;
    }

    public List<Developer> sortBySalaryAscending() {
        List<Developer> sorted = new ArrayList<>();
        for (int i = 0; i < devList.size(); i++) {
            sorted.add(devList.get(i));
        }
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - i - 1; j++) {
                if (sorted.get(j).getSalary() > sorted.get(j + 1).getSalary()) {
                    Developer temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }

    public void printHeader() {
        System.out.println("+----------+----------------------+---------------------------+----------+");
        System.out.println("| DevID    | Full Name            | Programming Languages     |   Salary |");
        System.out.println("+----------+----------------------+---------------------------+----------+");
    }

    public void printFooter() {
        System.out.println("+----------+----------------------+---------------------------+----------+");
    }

    public void displayAll() {
        if (devList.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }
        printHeader();
        for (int i = 0; i < devList.size(); i++) {
            System.out.println(devList.get(i));
        }
        printFooter();
    }

    public void displayList(List<Developer> list) {
        if (list.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        printHeader();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        printFooter();
    }

    public void handleListAll() {
        System.out.println("\n===== LIST ALL DEVELOPERS =====");
        displayAll();
    }

    public void handleSearchById() {
        System.out.println("\n===== SEARCH DEVELOPER BY ID =====");
        String searchId = Acceptable.checkString("Enter Developer ID: ").toUpperCase();
        Developer found = findById(searchId);
        if (found == null) {
            System.out.println("Developer ID does not exist!");
        } else {
            printHeader();
            System.out.println(found);
            printFooter();
        }
    }

    public void handleUpdateSalary() {
        System.out.println("\n===== UPDATE DEVELOPER SALARY =====");
        String salDevId = Acceptable.checkString("Enter Developer ID: ").toUpperCase();
        updateSalary(salDevId);
    }

    public void handleListByLanguage() {
        System.out.println("\n===== LIST DEVELOPERS BY LANGUAGE =====");
        String lang = Acceptable.checkString("Enter language (e.g. Java): ");
        List<Developer> filteredDevs = filterByLanguage(lang);
        System.out.println("Result for [" + lang + "]:");
        displayList(filteredDevs);
    }

    public boolean handleRemoveById(ProjectManagement projManager) {
        System.out.println("\n===== REMOVE DEVELOPER =====");
        String deleteDevId = Acceptable.checkString("Enter Developer ID to remove: ").toUpperCase();
        Developer a = findById(deleteDevId);
        if (a == null) {
            System.out.println("Developer ID does not exist!");
            return false;
        }
        if (projManager.hasProjectsByDevId(deleteDevId)) {
            System.out.println("Cannot delete: Developer is assigned to one or more projects.");
            return false;
        } else {
            delete(deleteDevId);
            return true;
        }
    }

    public void handleSortBySalary() {
        System.out.println("\n===== SORT DEVELOPERS BY SALARY =====");
        List<Developer> sorted = sortBySalaryAscending();
        displayList(sorted);
    }

    public void handleSave() {
        saveToFile();
    }
}
