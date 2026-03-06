package controller;

import business.DeveloperManagement;
import business.ProjectManagement;
import tools.Acceptable;

public class Main {

    public static void main(String[] args) {

        DeveloperManagement devManager = new DeveloperManagement();
        ProjectManagement projManager = new ProjectManagement(devManager);

        int choice;
        boolean isChanged = false;

        do {
            printMenu();
            choice = Acceptable.checkInt("\nEnter your choice (1-12): ", 1, 12);

            switch (choice) {

                case 1:
                    devManager.handleListAll();
                    break;

                case 2:
                    devManager.addFromConsole();
                    isChanged = true;
                    break;

                case 3:
                    devManager.handleSearchById();
                    break;

                case 4:
                    devManager.handleUpdateSalary();
                    isChanged = true;
                    break;

                case 5:
                    devManager.handleListByLanguage();
                    break;

                case 6:
                    projManager.addFromConsole();
                    isChanged = true;
                    break;

                case 7:
                    projManager.handleListGrouped();
                    break;

                case 8:
                    projManager.handleTotalExperience();
                    break;

                case 9:
                    boolean deleted = devManager.handleRemoveById(projManager);
                    if (deleted) {
                        isChanged = true;
                    }
                    break;

                case 10:
                    devManager.handleSortBySalary();
                    break;

                case 11:
                    devManager.handleSave();
                    projManager.handleSave();
                    isChanged = false;
                    break;

                case 12:
                    if (isChanged) {
                        boolean wantSave = Acceptable.checkBoolean("You have unsaved changes. Save before quitting?");
                        if (wantSave) {
                            devManager.handleSave();
                            projManager.handleSave();
                        }
                    }
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Please enter a number between 1 and 12.");
            }

        } while (choice != 12);
    }

    private static void printMenu() {
        System.out.println("\n===================================================");
        System.out.println(" Software Developer Management");
        System.out.println("====================================================");
        System.out.println("  1.  List all Developers");
        System.out.println("  2.  Add a new Developer");
        System.out.println("  3.  Search for a Developer by ID");
        System.out.println("  4.  Update a Developer's salary by ID");
        System.out.println("  5.  List all Developers by Language");
        System.out.println("  6.  Add a new Project");
        System.out.println("  7.  List all Projects by Developer (Grouped)");
        System.out.println("  8.  Calculate Total Experience by Dev ID");
        System.out.println("  9.  Remove a Developer by ID");
        System.out.println("  10. Sort Developers by Salary");
        System.out.println("  11. Save data to files");
        System.out.println("  12. Quit program");
    }
}
