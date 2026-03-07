package business;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import models.Developer;
import models.Project;
import tools.Acceptable;

public class ProjectManagement implements Workable<Project> {

    private static final String FILE_PATH = "projects.txt";
    private List<Project> projectList;
    private DeveloperManagement devManager;

    public ProjectManagement(DeveloperManagement devManager) {
        this.devManager = devManager;
        projectList = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public void loadFromFile() {
        projectList.clear();
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

                String[] parts = line.split(",", 5);
                if (parts.length < 5) {
                    errorCount++;
                    continue;
                }

                String projectID = parts[0].trim();
                String devID = parts[1].trim();
                String projectName = parts[2].trim();
                int duration = 0;
                try {
                    duration = Integer.parseInt(parts[3].trim());
                } catch (NumberFormatException e) {
                    errorCount++;
                    continue;
                }
                String startDate = parts[4].trim();

                projectList.add(new Project(projectID, devID, projectName, duration, startDate));
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
            for (int i = 0; i < projectList.size(); i++) {
                Project p = projectList.get(i);
                pw.println(p.getProjectID() + ", " + p.getDevID() + ", "
                        + p.getProjectName() + ", " + p.getDuration() + ", " + p.getStartDate());
            }
            pw.close();
            System.out.println("Saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    @Override
    public void add(Project p) {
        projectList.add(p);
    }

    public void addFromConsole() {
        System.out.println("\n===== ADD NEW PROJECT =====");

        String projectID;
        while (true) {
            projectID = Acceptable.checkRegex(
                    "Enter Project ID (e.g. PROJ01): ",
                    "PROJ\\d{2}",
                    "Error: ID must be PROJ followed by 2 digits (e.g. PROJ01)!");
            if (findById(projectID) != null) {
                System.out.println("Error: Project ID already exists!");
            } else {
                break;
            }
        }

        String devID;
        while (true) {
            devID = Acceptable.checkRegex(
                    "Enter Developer ID (must exist, e.g. DEV001): ",
                    "DEV\\d{3}",
                    "Error: ID must be DEV followed by 3 digits!");
            if (devManager.findById(devID) == null) {
                System.out.println("Error: Developer ID [" + devID + "] does not exist!");
            } else {
                break;
            }
        }

        String projectName = Acceptable.checkString("Enter Project Name: ");
        int duration = Acceptable.checkInt("Enter Duration (months, min 1): ", 1, 120);
        String startDate = Acceptable.checkFutureDate("Enter Start Date");

        projectList.add(new Project(projectID, devID, projectName, duration, startDate));
        System.out.println("Project added!");
    }

    @Override
    public void update(String id) {
        Project proj = findById(id);
        if (proj == null) {
            System.out.println("Error: Project not found with ID: " + id);
            return;
        }

        System.out.println("\n===== UPDATE PROJECT: " + id + " =====");
        System.out.println("(Press Enter to skip, keeping the current value)");

        System.out.print("DevID [" + proj.getDevID() + "]: ");
        String newDevID = Acceptable.readOptionalLine();
        if (!newDevID.isEmpty()) {
            if (devManager.findById(newDevID) == null) {
                System.out.println("Error: DevID does not exist, keeping current value.");
            } else {
                proj.setDevID(newDevID);
            }
        }

        System.out.print("Project Name [" + proj.getProjectName() + "]: ");
        String newName = Acceptable.readOptionalLine();
        if (!newName.isEmpty()) {
            proj.setProjectName(newName);
        }

        System.out.print("Duration [" + proj.getDuration() + " months]: ");
        String durInput = Acceptable.readOptionalLine();
        if (!durInput.isEmpty()) {
            try {
                int dur = Integer.parseInt(durInput);
                if (dur < 1 || dur > 120) {
                    System.out.println("Error: Duration must be between 1 and 120. Keeping current value.");
                } else {
                    proj.setDuration(dur);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input, keeping current value.");
            }
        }

        System.out.print("Start Date [" + proj.getStartDate() + "] (dd/MM/yyyy, ENTER to skip): ");
        String dateInput = Acceptable.readOptionalLine();
        if (!dateInput.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date parsed = sdf.parse(dateInput);
                if (!parsed.after(new Date())) {
                    System.out.println("Error: Date must be in the future. Keeping current value.");
                } else {
                    proj.setStartDate(dateInput);
                }
            } catch (ParseException e) {
                System.out.println("Error: Invalid date format. Keeping current value.");
            }
        }

        System.out.println("Update successful!");
    }

    @Override
    public void delete(String id) {
        Project proj = findById(id);
        if (proj == null) {
            System.out.println("Error: Project not found with ID: " + id);
            return;
        }
        projectList.remove(proj);
        System.out.println("Project [" + id + "] deleted successfully.");
    }

    @Override
    public List<Project> getAll() {
        return projectList;
    }

    public Project findById(String id) {
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getProjectID().equalsIgnoreCase(id)) {
                return projectList.get(i);
            }
        }
        return null;
    }

    public boolean hasProjectsByDevId(String devID) {
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getDevID().equalsIgnoreCase(devID)) {
                return true;
            }
        }
        return false;
    }

    public int calculateTotalExperience(String devID) {
        int total = 0;
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getDevID().equalsIgnoreCase(devID)) {
                total = total + projectList.get(i).getDuration();
            }
        }
        return total;
    }

    public List<Project> filterByDeveloperName(String devName) {
        List<Project> result = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            Project p = projectList.get(i);
            Developer dev = devManager.findById(p.getDevID());
            if (dev != null) {
                if (dev.getFullName().toLowerCase().contains(devName.toLowerCase())) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    public void printHeader() {
        System.out.println("+------------+----------+---------------------------+-----------------+--------------+");
        System.out.println("| ProjectID  | DevID    | Project Name              | Duration(months)| Start Date   |");
        System.out.println("+------------+----------+---------------------------+-----------------+--------------+");
    }

    public void printFooter() {
        System.out.println("+------------+----------+---------------------------+-----------------+--------------+");
    }

    public void displayAll() {
        if (projectList.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }
        printHeader();
        for (int i = 0; i < projectList.size(); i++) {
            System.out.println(projectList.get(i));
        }
        printFooter();
    }

    public void displayGroupedByDeveloper() {
        if (projectList.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }

        List<String> devIds = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            String dId = projectList.get(i).getDevID().toUpperCase();
            boolean exist = false;
            for (int j = 0; j < devIds.size(); j++) {
                if (devIds.get(j).equals(dId)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                devIds.add(dId);
            }
        }

        for (int i = 0; i < devIds.size(); i++) {
            String devId = devIds.get(i);
            Developer dev = devManager.findById(devId);
            String devName = (dev != null) ? dev.getFullName() : "Unknown";
            System.out.println("\n  Developer: " + devId + " - " + devName);
            printHeader();
            for (int j = 0; j < projectList.size(); j++) {
                if (projectList.get(j).getDevID().equalsIgnoreCase(devId)) {
                    System.out.println(projectList.get(j));
                }
            }
            printFooter();
        }
    }

    public void displayList(List<Project> list) {
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

    public void handleListGrouped() {
        System.out.println("\n===== ALL PROJECTS BY DEVELOPER (GROUPED) =====");
        displayGroupedByDeveloper();
    }

    public void handleTotalExperience() {
        System.out.println("\n===== CALCULATE TOTAL EXPERIENCE =====");
        String expDevId = Acceptable.checkString("Enter Developer ID: ").toUpperCase();
        Developer targetDev = devManager.findById(expDevId);
        if (targetDev == null) {
            System.out.println("Developer ID does not exist!");
        } else {
            int totalMonths = calculateTotalExperience(expDevId);
            System.out.println("Developer: " + targetDev.getDevID() + " - " + targetDev.getFullName());
            System.out.println("Total Experience: " + totalMonths + " months");
        }
    }

    public void handleSave() {
        saveToFile();
    }
}
