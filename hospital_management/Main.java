import java.io.*;
import java.util.*;

class Patient {
    String id, name, history, password;

    public Patient(String id, String name, String history, String password) {
        this.id = id;
        this.name = name;
        this.history = history;
        this.password = password;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + history + "," + password;
    }
}

class Doctor {
    String id, name, specialization, password;

    public Doctor(String id, String name, String specialization, String password) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.password = password;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + specialization + "," + password;
    }
}

class Appointment {
    private static int appointmentCounter = 1; // Static counter for appointment IDs
    String id, patientId, doctorId, date;

    public Appointment(String patientId, String doctorId, String date) {
        this.id = String.valueOf(appointmentCounter++); // Use the counter as the ID
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
    }

    @Override
    public String toString() {
        return id + "," + patientId + "," + doctorId + "," + date;
    }
}


class FileManager {
    public static void saveToFile(String fileName, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<String> readFromFile(String fileName) {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return data;
    }

    public static void overwriteFile(String fileName, List<String> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}

class HospitalManagementSystem {
    private static final String PATIENT_FILE = "patients.txt";
    private static final String DOCTOR_FILE = "doctors.txt";
    private static final String APPOINTMENT_FILE = "appointments.txt";
    private static final String ADMIN_FILE = "admin.txt"; // File for storing admin credentials

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Check if admin exists, if not, register
        if (new File(ADMIN_FILE).length() == 0) {
            System.out.println("No admin found. Registering admin...");
            adminRegistration(scanner);
        }

        while (true) {
            System.out.println("\nHospital Management System");
            System.out.println("1. Admin Login");
            System.out.println("2. Patient Registration");
            System.out.println("3. Patient Login");
            System.out.println("4. Doctor Registration");
            System.out.println("5. Doctor Login");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminLogin(scanner);
                    break;
                case 2:
                    patientRegistration(scanner);
                    break;
                case 3:
                    patientLogin(scanner);
                    break;
                case 4:
                    doctorRegistration(scanner);
                    break;
                case 5:
                    doctorLogin(scanner);
                    break;
                case 6:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void doctorRegistration(Scanner scanner) {
        System.out.print("Enter Doctor ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        FileManager.saveToFile(DOCTOR_FILE, new Doctor(id, name, specialization, password).toString());
        System.out.println("Doctor registered successfully!");
    }

    private static void doctorLogin(Scanner scanner) {
        System.out.print("Enter Doctor ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        List<String> doctors = FileManager.readFromFile(DOCTOR_FILE);
        boolean found = false;

        for (String line : doctors) {
            String[] parts = line.split(",");
            if (parts[0].equals(id) && parts[3].equals(password)) {
                System.out.println("Doctor logged in successfully!");
                handleDoctorActions(scanner, id);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid Doctor Credentials.");
        }
    }

    private static void handleDoctorActions(Scanner scanner, String doctorId) {
        while (true) {
            System.out.println("\nDoctor Menu");
            System.out.println("1. View Appointments");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewDoctorAppointments(doctorId);
                    break;
                case 2:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void viewDoctorAppointments(String doctorId) {
        List<String> appointments = FileManager.readFromFile(APPOINTMENT_FILE);
        boolean found = false;
        System.out.println("\nAppointments for Doctor ID: " + doctorId);
        for (String line : appointments) {
            String[] parts = line.split(",");
            if (parts[2].equals(doctorId)) {
                System.out.println("Appointment ID: " + parts[0] + ", Patient ID: " + parts[1] + ", Date: " + parts[3]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No appointments found.");
        }
    }

    private static void adminRegistration(Scanner scanner) {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        // Save the admin credentials to file
        FileManager.saveToFile(ADMIN_FILE, username + "," + password);
        System.out.println("Admin registered successfully!");
    }

    private static void adminLogin(Scanner scanner) {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        List<String> adminData = FileManager.readFromFile(ADMIN_FILE);
        boolean found = false;

        for (String line : adminData) {
            String[] parts = line.split(",");
            if (parts[0].equals(username) && parts[1].equals(password)) {
                System.out.println("Admin logged in successfully!");
                handleAdminActions(scanner);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid Admin Credentials.");
        }
    }

    private static void handleAdminActions(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. View Patients");
            System.out.println("2. View Doctors");
            System.out.println("3. View Appointments");
            System.out.println("4. Edit Patient/Doctor/Appointment");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<String> patients = FileManager.readFromFile(PATIENT_FILE);
                    System.out.println("\nRegistered Patients:");
                    patients.forEach(System.out::println);
                    break;
                case 2:
                    viewDoctors();  // View doctor list
                    break;
                case 3:
                    List<String> appointments = FileManager.readFromFile(APPOINTMENT_FILE);
                    System.out.println("\nScheduled Appointments:");
                    appointments.forEach(System.out::println);
                    break;
                case 4:
                    editOptions(scanner); // Edit option for admin
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void editOptions(Scanner scanner) {
        System.out.println("\nEdit Options");
        System.out.println("1. Edit Patient");
        System.out.println("2. Edit Doctor");
        System.out.println("3. Edit Appointment");
        System.out.print("Enter your choice: ");
        int editChoice = scanner.nextInt();
        scanner.nextLine();

        switch (editChoice) {
            case 1:
                editPatient(scanner);
                break;
            case 2:
                editDoctor(scanner);
                break;
            case 3:
                editAppointment(scanner);
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }

    private static void editPatient(Scanner scanner) {
        System.out.print("Enter Patient ID to edit: ");
        String patientId = scanner.nextLine();
        List<String> patients = FileManager.readFromFile(PATIENT_FILE);
        boolean found = false;
        List<String> updatedPatients = new ArrayList<>();

        for (String line : patients) {
            String[] parts = line.split(",");
            if (parts[0].equals(patientId)) {
                found = true;
                System.out.print("Enter new Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new Medical History: ");
                String history = scanner.nextLine();
                updatedPatients.add(new Patient(patientId, name, history, parts[3]).toString());
            } else {
                updatedPatients.add(line);
            }
        }

        if (found) {
            FileManager.overwriteFile(PATIENT_FILE, updatedPatients);
            System.out.println("Patient details updated.");
        } else {
            System.out.println("Patient ID not found.");
        }
    }

    private static void editDoctor(Scanner scanner) {
        System.out.print("Enter Doctor ID to edit: ");
        String doctorId = scanner.nextLine();
        List<String> doctors = FileManager.readFromFile(DOCTOR_FILE);
        boolean found = false;
        List<String> updatedDoctors = new ArrayList<>();

        for (String line : doctors) {
            String[] parts = line.split(",");
            if (parts[0].equals(doctorId)) {
                found = true;
                System.out.print("Enter new Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new Specialization: ");
                String specialization = scanner.nextLine();
                updatedDoctors.add(new Doctor(doctorId, name, specialization, parts[3]).toString());
            } else {
                updatedDoctors.add(line);
            }
        }

        if (found) {
            FileManager.overwriteFile(DOCTOR_FILE, updatedDoctors);
            System.out.println("Doctor details updated.");
        } else {
            System.out.println("Doctor ID not found.");
        }
    }

    private static void editAppointment(Scanner scanner) {
        System.out.print("Enter Appointment ID to edit: ");
        String appointmentId = scanner.nextLine();
        List<String> appointments = FileManager.readFromFile(APPOINTMENT_FILE);
        boolean found = false;
        List<String> updatedAppointments = new ArrayList<>();

        for (String line : appointments) {
            String[] parts = line.split(",");
            if (parts[0].equals(appointmentId)) {
                found = true;
                System.out.print("Enter new Appointment Date (YYYY-MM-DD): ");
                String newDate = scanner.nextLine();
                // Updating the appointment with the same ID but new date
                updatedAppointments.add(new Appointment(parts[1], parts[2], newDate).toString());
            } else {
                updatedAppointments.add(line);
            }
        }

        if (found) {
            FileManager.overwriteFile(APPOINTMENT_FILE, updatedAppointments);
            System.out.println("Appointment updated successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }


    private static void viewDoctors() {
        List<String> doctors = FileManager.readFromFile(DOCTOR_FILE);
        System.out.println("\nList of Registered Doctors:");
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            doctors.forEach(System.out::println);
        }
    }

    private static void patientRegistration(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Medical History: ");
        String history = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        FileManager.saveToFile(PATIENT_FILE, new Patient(id, name, history, password).toString());
        System.out.println("Patient registered successfully!");
    }

    private static void patientLogin(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        List<String> patients = FileManager.readFromFile(PATIENT_FILE);
        boolean found = false;

        for (String line : patients) {
            String[] parts = line.split(",");
            if (parts[0].equals(id) && parts[3].equals(password)) {
                System.out.println("Patient logged in successfully!");
                handlePatientActions(scanner, id);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid Patient Credentials.");
        }
    }

    private static void handlePatientActions(Scanner scanner, String patientId) {
        while (true) {
            System.out.println("\nPatient Menu");
            System.out.println("1. View Doctors");
            System.out.println("2. Schedule Appointment");
            System.out.println("3. Edit Appointment Date");
            System.out.println("4. View Appointment History");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewDoctors();  // View doctor list
                    break;
                case 2:
                    scheduleAppointment(scanner, patientId);
                    break;
                case 3:
                    editPatientAppointment(scanner, patientId);
                    break;
                case 4:
                    viewAppointmentHistory(patientId);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void scheduleAppointment(Scanner scanner, String patientId) {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        Appointment appointment = new Appointment(patientId, doctorId, date);
        FileManager.saveToFile(APPOINTMENT_FILE, appointment.toString());
        System.out.println("Appointment scheduled successfully with ID: " + appointment.id);
    }


    private static void viewAppointmentHistory(String patientId) {
        List<String> appointments = FileManager.readFromFile(APPOINTMENT_FILE);
        boolean found = false;
        System.out.println("\nAppointment History:");
        for (String line : appointments) {
            String[] parts = line.split(",");
            if (parts[1].equals(patientId)) {
                System.out.println("Appointment ID: " + parts[0] + ", Doctor ID: " + parts[2] + ", Date: " + parts[3]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No appointments found.");
        }
    }

    private static void editPatientAppointment(Scanner scanner, String patientId) {
        System.out.print("Enter Appointment ID to edit: ");
        String appointmentId = scanner.nextLine();
        List<String> appointments = FileManager.readFromFile(APPOINTMENT_FILE);
        boolean found = false;
        List<String> updatedAppointments = new ArrayList<>();

        for (String line : appointments) {
            String[] parts = line.split(",");
            if (parts[0].equals(appointmentId) && parts[1].equals(patientId)) {
                found = true;
                System.out.print("Enter new Appointment Date: ");
                String newDate = scanner.nextLine();
                // Creating a new appointment with the same ID, patient ID, doctor ID, but a new date.
                updatedAppointments.add(new Appointment(parts[1], parts[2], newDate).toString());
            } else {
                updatedAppointments.add(line);
            }
        }

        if (found) {
            FileManager.overwriteFile(APPOINTMENT_FILE, updatedAppointments);
            System.out.println("Appointment updated successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }

}
