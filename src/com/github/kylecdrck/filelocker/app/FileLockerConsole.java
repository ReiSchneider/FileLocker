package com.github.kylecdrck.filelocker.app;

import com.github.kylecdrck.filelocker.FileLockDetail;
import com.github.kylecdrck.filelocker.FileLockers;
import com.github.kylecdrck.filelocker.RecursiveFileLocker;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Implementation of the FileLocker with a Console application
 *
 * @author Kyle Cancio
 */
public class FileLockerConsole {

    private static final Set<Integer> validOptions = Set.of(0, 1, 2);

    private static final RecursiveFileLocker recursiveFileLocker = FileLockers.newRecursiveFileLocker();

    private static final Supplier<String> LINE_READER;
    private static final Supplier<Integer> INT_READER;
    private static final Supplier<String> PASSWORD_READER;

    static {
        Scanner sc = new Scanner(System.in);
        LINE_READER = sc::nextLine;
        INT_READER = sc::nextInt;
        PASSWORD_READER = () -> System.console() != null ? new String(System.console().readPassword()) : sc.nextLine();
    }

    public static void main(String[] args) {
        System.out.println("*************************************************************");
        System.out.println("*\t\t\t\tWelcome to the File Locker App\t\t\t\t*");
        System.out.println("*************************************************************");

        int choice = -1;
        String filePath = null;
        do {
            try {
                System.out.print("""
                        \nSelect Option number to continue
                        [1] Lock File(s)
                        [2] Unlock File(s)
                        [0] Exit
                        
                        Input:\s""");

                choice = INT_READER.get();

                if (!validOptions.contains(choice)) {
                    throw new IllegalArgumentException();
                }

                if (choice == 0) {
                    System.out.println("File Locker will now exit");
                    break;
                }

                LINE_READER.get();

                filePath = inputPath(filePath);
                String password = inputPassword();
                String recursiveChoice = inputRecursiveChoice();


                boolean recursive = switch (recursiveChoice) {
                    case "y" -> true;
                    default -> false;
                };

                validatePathAndKey(filePath, password);

                FileLockDetail fileLockResult = switch (choice) {
                    case 1 -> recursiveFileLocker.lockFile(filePath, password, recursive ? Integer.MAX_VALUE : 1);
                    case 2 -> recursiveFileLocker.unlockFile(filePath, password, recursive ? Integer.MAX_VALUE : 1);
                    default -> null;
                };

                if (fileLockResult != null) {
                    System.out.println(fileLockResult.getMessage());
                } else {
                    System.out.println("File operation finished with errors!");
                }

            } catch (InputMismatchException | IllegalArgumentException iae) {
                if (iae instanceof InvalidParameterException) {
                    System.out.println(iae.getMessage());
                } else {
                    System.out.println("Invalid option");
                }
                LINE_READER.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            clearConsole();
        } while (!Objects.equals(0, choice));
    }
    private static void validatePathAndKey(String path, String key) {
        if (Objects.isNull(path) || path.isBlank() || Objects.isNull(key) || key.isBlank()) {
            throw new InvalidParameterException("Invalid path or password");
        }
    }

    private static void validatePassword(String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            throw new InvalidParameterException("Passwords do not match");
        }
    }

    private static String inputPath(String prevPath) {
        String path;
        if (Objects.isNull(prevPath) || prevPath.isBlank()){
            System.out.print("\nEnter file/folder path: ");
            path = LINE_READER.get();
        } else {
            System.out.printf("\nEnter file/folder path (Leave blank to use '%s' instead): ", prevPath);
            path = LINE_READER.get();
            path = path.isBlank() ? prevPath : path;
        }
        return path;
    }

    private static String inputPassword() {
        System.out.print("\nEnter password: ");
        String password = PASSWORD_READER.get();

        System.out.print("\nConfirm password: ");
        validatePassword(password, PASSWORD_READER.get());

        return password;
    }
    private static String inputRecursiveChoice() {
        System.out.print("\nInclude sub-folders? (y/n)? (Default=n): ");
        return LINE_READER.get().toLowerCase();
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {  
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ignored) {}
    }
}
