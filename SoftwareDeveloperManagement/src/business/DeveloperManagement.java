package business;

import java.io.*;
import java.util.*;
import models.Developers;
import tools.Acceptable;

public class DeveloperManagement implements Workable<Developers> {

    private static final String FILE_PATH = "developers.txt";
    private List<Developers> devList;

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
                    continue;
                }

                String beforeBracket = line.substring(0, startBracket).trim();
                String insideBracket = line.substring(startBracket + 1, endBracket).trim();
                String afterBracket = line.substring(endBracket + 1).trim();

                String[] parts = beforeBracket.split(",");
                if (parts.length < 2) {
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
                    System.out.println("Error reading salary at line: " + line);
                }

                devList.add(new Developers(devID, fullName, languages, salary));
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH));
            for (Developers d : devList) {
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
    public void add(Developers dev) {
        devList.add(dev);
    }

    public void addFromConsole() {
        System.out.println("\n===== ADD NEW DEVELOPER =====");

        String devID;
        while (true) {
            devID = Acceptable.checkRegex(
                    "Enter Developer ID (e.g. DEV001): ",
                    "DEV\\d{3}",
                    "Error: ID must be DEV followed by 3 digits (e.g. DEV001)!");
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

        Developers newDev = new Developers(devID, fullName, languages, salary);
        devList.add(newDev);
        System.out.println("Added successfully!");
    }

    @Override
    public void update(String id) {
        Developers dev = findById(id);
        if (dev == null) {
            System.out.println("Developer ID does not exist!");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("--- Update info (Press Enter to keep old data) ---");

        System.out.print("New Full Name [" + dev.getFullName() + "]: ");
        String newName = sc.nextLine().trim();
        if (!newName.isEmpty()) {
            dev.setFullName(newName);
        }

        System.out.print("New Languages [" + String.join(", ", dev.getProgrammingLanguages()) + "]: ");
        String newLang = sc.nextLine().trim();
        if (!newLang.isEmpty()) {
            List<String> listLang = new ArrayList<>();
            for (String l : newLang.split(",")) {
                listLang.add(l.trim());
            }
            dev.setProgrammingLanguages(listLang);
        }

        System.out.print("New Salary [" + dev.getSalary() + "]: ");
        String newSalary = sc.nextLine().trim();
        if (!newSalary.isEmpty()) {
            try {
                int salary = Integer.parseInt(newSalary);
                dev.setSalary(salary);
            } catch (Exception e) {
                System.out.println("Invalid salary format. Keep old salary.");
            }
        }
        System.out.println("Update successful!");
    }

    @Override
    public void delete(String id) {
        Developers dev = findById(id);
        if (dev == null) {
            System.out.println("Error: Developer not found with ID: " + id);
            return;
        }
        devList.remove(dev);
        System.out.println("Developer [" + id + "] deleted successfully.");
    }

    @Override
    public List<Developers> getAll() {
        return devList;
    }

    public Developers findById(String id) {
        for (Developers d : devList) {
            if (d.getDevID().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    public void updateSalary(String id) {
        Developers dev = findById(id);
        if (dev == null) {
            System.out.println("Developer ID does not exist!");
            return;
        }
        System.out.println("Current salary of [" + id + "]: " + dev.getSalary() + " USD");
        int newSalary = Acceptable.checkInt("Enter new Salary (USD, >= 1000): ", 1000, Integer.MAX_VALUE);
        dev.setSalary(newSalary);
        System.out.println("Salary updated successfully!");
    }

    public List<Developers> filterByLanguage(String language) {
        List<Developers> result = new ArrayList<>();
        for (Developers d : devList) {
            boolean found = false;
            for (int i = 0; i < d.getProgrammingLanguages().size(); i++) {
                if (d.getProgrammingLanguages().get(i).equalsIgnoreCase(language)) {
                    found = true;
                }
            }
            if (found) {
                result.add(d);
            }
        }
        return result;
    }

    public List<Developers> sortBySalaryAscending() {
        List<Developers> sorted = new ArrayList<>(devList);
        Collections.sort(sorted, new Comparator<Developers>() {
            @Override
            public int compare(Developers d1, Developers d2) {
                return Integer.compare(d1.getSalary(), d2.getSalary());
            }
        });
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
        for (Developers d : devList) {
            System.out.println(d);
        }
        printFooter();
    }

    public void displayList(List<Developers> list) {
        if (list.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        printHeader();
        for (Developers d : list) {
            System.out.println(d);
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
        Developers found = findById(searchId);
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
        List<Developers> filteredDevs = filterByLanguage(lang);
        System.out.println("Result for [" + lang + "]:");
        displayList(filteredDevs);
    }

    public boolean handleRemoveById(ProjectManagement projManager) {
        System.out.println("\n===== REMOVE DEVELOPER =====");
        String deleteDevId = Acceptable.checkString("Enter Developer ID to remove: ").toUpperCase();
        if (projManager.hasProjectsByDevId(deleteDevId)) {
            System.out.println("Cannot delete: Developer is assigned to a project.");
            return false;
        } else {
            delete(deleteDevId);
            return true;
        }
    }

    public void handleSortBySalary() {
        System.out.println("\n===== SORT DEVELOPERS BY SALARY (LOW TO HIGH) =====");
        List<Developers> sorted = sortBySalaryAscending();
        displayList(sorted);
    }

    public void handleSave() {
        saveToFile();
    }
}
