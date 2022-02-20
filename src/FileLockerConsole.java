import factory.FileLockerFactory;
import service.FileLocker;

import java.security.InvalidParameterException;
import java.util.InputMismatchException;
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
    private static final FileLocker fileLocker = FileLockerFactory.getSecuredFileLocker();

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
                        [2] Decrypt
                        [0] Exit""");

                choice = sc.nextInt();

                if (!validOptions.contains(choice)) {
                    throw new IllegalArgumentException();
                }

                if (choice == 0) {
                    System.out.println("File Locker will now exit");
                    break;
                }

                sc.nextLine();

                System.out.println("\n\nEnter file/directory path");
                String filePath = sc.nextLine();

                System.out.println("\n\nEnter password");
                String key = sc.nextLine();

                validatePathAndKey(filePath, key);

                boolean success = switch (choice) {
                    case 1 -> fileLocker.encryptFile(filePath, key);
                    case 2 -> fileLocker.decryptFile(filePath, key);
                    default -> false;
                };

                if (success) {
                    System.out.println(choice == 1 ? "Encryption finished!" : "Decryption finished!");
                } else {
                    System.out.println(choice == 1 ? "Encryption finished with errors!" : "Decryption finished with errors!");
                }

            } catch (InputMismatchException | IllegalArgumentException iae) {
                if (iae instanceof InvalidParameterException) {
                    System.out.println("Invalid path or password!");
                } else {
                    System.out.printf("Invalid option: %s", choice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (!Objects.equals(0, choice));
    }

    private static void validatePathAndKey(String path, String key) {
        if (Objects.isNull(path) || path.isBlank() || Objects.isNull(key) || key.isBlank()) {
            throw new InvalidParameterException();
        }
    }
}
