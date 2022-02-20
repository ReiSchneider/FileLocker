import factory.FileLockerFactory;
import service.FileLocker;

import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * Sample implementation of the FileLocker with a Console application
 *
 * @author Kyle Cancio
 */
public class FileLockerConsole {

    private static final Set<Integer> validOptions = Set.of(0, 1, 2);
    private static final FileLocker fileLocker = FileLockerFactory.getFileLocker();

    public static void main(String[] args) {
        System.out.println("*************************************************************");
        System.out.println("*\t\t\t\tWelcome to the File Locker App\t\t\t\t*");
        System.out.println("*************************************************************");

        Scanner sc = new Scanner(System.in);

        int choice = 0;

        do {
            try {
                System.out.println("""
                        \nSelect Option to continue
                        [1] Encrypt
                        [2] Decrypt;
                        [0] Exit""");

                choice = sc.nextInt();

                if (!validOptions.contains(choice)) {
                    throw new Exception();
                }

                if (choice == 0) {
                    System.out.println("File Locker will now exit");
                    break;
                }

                sc.nextLine();

                System.out.println("\n\nEnter file/directory path");
                String filePath = sc.nextLine();

                boolean success = switch (choice) {
                    case 1 -> fileLocker.encryptFile(filePath, null);
                    case 2 -> fileLocker.decryptFile(filePath, null);
                    default -> false;
                };

                if (success) {
                    System.out.println(choice == 1 ? "Encryption success!" : "Decryption success!");
                } else {
                    System.out.println(choice == 1 ? "Encryption failed!" : "Decryption failed!");
                }

            } catch (Exception e) {
                System.out.printf("Invalid option: %s", choice);
            }

        } while (!Objects.equals(0, choice));
    }
}
