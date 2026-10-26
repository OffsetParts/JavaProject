import java.util.Scanner;
public class DentalRecords {

    /*

    Program to simulate A Floridian Dental Records application to:
        print dental records,
        extract a tooth,
        report the root canals,
        and exit nicely

    for a given family.

    */

    private static final Scanner keyboard = new Scanner(System.in);

    private static final char PRINT_COMMAND     = 'p';
    private static final char EXTRACT_COMMAND   = 'e';
    private static final char ROOT_COMMAND      = 'r';
    private static final char SENTINEL_COMMAND  = 'x';

    private static final char UPPER_COMMAND     = 'u';
    private static final char LOWER_COMMAND     = 'l';

    private static final int UPPER = 0;
    private static final int LOWER = 1;

    private static final int MAX_TEETH = 8;
    private static final int MAX_PEOPLE = 6;

    private static final char Incisor = 'I';
    private static final char Bicuspid = 'B';
    private static final char Missing = 'M';


    public static void main(String[] args) {

        int familySize;
        char command;
        String [] familyArray;
        char[][][] teethRecord;



        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");
        System.out.print("Please enter number of people in the family         : ");
        familySize = keyboard.nextInt();
        while (familySize < 1 || familySize > MAX_PEOPLE) {
            System.out.print("Invalid number of people, try again                 : ");
            familySize = keyboard.nextInt();
        }

        familyArray = new String[familySize];
        teethRecord = new char[familySize][2][];

        for (int index = 0; index < familySize; index++) {
            System.out.print("Please enter the family member " + (index + 1) + "  : ");
            familyArray[index] = keyboard.next();

            System.out.print("Please enter the uppers for " + familyArray[index] + " : ");
            teethRecord[index][UPPER] = teethRecorder();

            System.out.print("Please enter the lowers for "  + familyArray[index] + " : ");
            teethRecord[index][LOWER] = teethRecorder();
        }

        do {
            System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it                  : ");
            command = Character.toLowerCase(keyboard.next().charAt(0));
            while (command != PRINT_COMMAND && command != EXTRACT_COMMAND && command != ROOT_COMMAND && command != SENTINEL_COMMAND) {
                System.out.print("Invalid menu option, try again                    : ");
                command = Character.toLowerCase(keyboard.next().charAt(0));
            }

            switch (command) {
                case PRINT_COMMAND: printRecord(familyArray, teethRecord); break;
                case EXTRACT_COMMAND: extractCommand(familyArray, teethRecord); break;
                case ROOT_COMMAND: rootCommand(teethRecord); break;
            }

        } while (command != SENTINEL_COMMAND);

        System.out.println("\nExiting the Floridian Tooth Records :-)");
    }

    private static char[] teethRecorder() {
        String input = keyboard.next().toUpperCase();
        while (!validateType(input) || input.length() > MAX_TEETH) {
            if (!validateType(input)) {
                System.out.print("Invalid teeth types, try again                    : ");
            } else {
                System.out.print("Too many teeth, try again                         : ");
            }
            input = keyboard.next().toUpperCase();
        }

        return input.toCharArray();
    }

    private static boolean validateType(String input) {
        for (int index = 0; index < input.length(); index++) {
            char character = input.charAt(index);
            if (!(character == 'I' || character == 'B' || character == 'M')) {
                return false;
            }
        }
        return true;
    }

    private static void printRecord(String[] familyArray, char[][][] teethRecord) {
        System.out.println();
        for (int member = 0; member < familyArray.length; member++) {
            System.out.println(familyArray[member]);
            for (int layer = UPPER; layer <= LOWER; layer++) {
                System.out.print(layer == UPPER ? "  Uppers: " : "  Lowers: ");
                for (int tooth = 0; tooth < teethRecord[member][layer].length; tooth++) {
                    System.out.print(" " + (tooth + 1) + ":" + teethRecord[member][layer][tooth] + " ");
                }
                System.out.println();
            }
        }
    }

    private static void extractCommand(String[] familyArray, char[][][] teethRecord) {
        char layer;
        int memberPosition = -1;
        int layerPosition = -1;
        int toothNumber = -1;

        System.out.print("Which family member                           : ");
        memberPosition = findMember(keyboard.next(), familyArray);

        while (memberPosition < 0) {
            System.out.print("Invalid family member, try again    : ");
            memberPosition = findMember(keyboard.next(), familyArray);
        }

        System.out.print("Which tooth layer (U)pper or (L)ower      : ");
        layer = Character.toLowerCase(keyboard.next().charAt(0));
        while (layer != UPPER_COMMAND && layer != LOWER_COMMAND) {
            System.out.print("Invalid layer, try again                : ");
            layer = Character.toLowerCase(keyboard.next().charAt(0));
        }
        layerPosition = (layer == UPPER_COMMAND) ? UPPER : LOWER;

        char[] row = teethRecord[memberPosition][layerPosition];
        System.out.print("Which tooth number                          : ");
        toothNumber = keyboard.nextInt() - 1;
        while (toothNumber < 1 || toothNumber > row.length || row[toothNumber - 1] == Missing ) {
            if (toothNumber < 1 || toothNumber > row.length) {
                System.out.print("Invalid tooth number, try again             : ");
            } else {
                System.out.print("Missing tooth, try again                    : ");
            }
            toothNumber = keyboard.nextInt();

        }

        row[toothNumber - 1] = Missing ;

    }

    private static int findMember(String name, String[] familyArray) {
        for (int index = 0; index < familyArray.length; index++) {
            if (familyArray[index].equalsIgnoreCase(name)) {
                return index;
            }
        }
        return -1;
    }

    private static void rootCommand(char[][][] teethRecord) {
        int Incisors = 0;
        int Bicuspids = 0;
        int Missings = 0;

        for (int member = 0; member < teethRecord.length; member++) {
            for (int layer = UPPER; layer <= LOWER; layer++) {
                for (int tooth = 0; tooth < teethRecord[member][layer].length; tooth++) {
                    char type = teethRecord[member][layer][tooth];
                    if (type == Incisor) {
                        Incisors++;
                    } else if (type == Bicuspid) {
                        Bicuspids++;
                    } else {
                        Missings++;
                    }
                }
            }
        }

        // Ix^2 + Bx - M = 0
        double disc = Math.sqrt(Bicuspids * Bicuspids + 4 * Incisors * Missings);
        double root1 = (-Bicuspids + disc) / (2 * Bicuspids);
        double root2 = (-Bicuspids - disc) / (2 * Bicuspids);
        System.out.printf("One root canal at     %.2f%n", root1);
        System.out.printf("Another root canal at %.2f%n", root2);
    }
}
