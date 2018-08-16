import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    private static String input_path= "1.txt";
    private static String output_path = "2.txt";

    // dans la maison
    private static long subTotal = strToMilliSec("01:37:37,280");
    private static long actualTotal = strToMilliSec("01:41:39,000");

    private static double delayPerMinute = 60 * (actualTotal - subTotal + 0.0) / subTotal;
    // 只是算着看看

    private static long strToMilliSec(String time) {
        return Integer.parseInt(time.substring(0, 2)) * 60 * 60 * 1000
                + Integer.parseInt(time.substring(3, 5)) * 60 * 1000
                + Integer.parseInt(time.substring(6, 8)) * 1000
                + Integer.parseInt(time.substring(9, 12));
    }

    private static String milliSecToString(long time) {
        long hour = time / (60 * 60 * 1000);
        long minute = (time % (60 * 60 * 1000)) / (60 * 1000);
        long second = (time % (60 * 1000)) / 1000;
        long milliSec = time % 1000;
        return f2(hour) + ":" +
                f2(minute) + ":" +
                f2(second) + "," +
                String.format("%03d", milliSec);
    }

    private static String f2(long i) {
        return String.format("%02d", i);
    }

    private static long shouldBe(long time) {
        return (long) (time * (actualTotal + 0.0) / subTotal);
    }

    private static  String processSubtitleTime(String operand) {
        return milliSecToString(shouldBe(strToMilliSec(operand.substring(0, 12))));
    }

    private static String processLine(String line) {
        return processSubtitleTime(line.substring(0, 12))
                + " --> "
                + processSubtitleTime(line.substring(17, 17 + 12));
    }

    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(output_path),true));

        int i = 1;
        boolean ready = false;
        Scanner scanner = new Scanner(new File(input_path));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (ready) {
                // process and write
                pw.println(processLine(line));

                ready = false;
                i++;
                continue;
            }


            try {
                ready =  (Integer.parseInt(line) == i);
            } catch (NumberFormatException e) {
                ready = false;
            }

            // write directly
            pw.println(line);
        }

        pw.flush();
        System.out.println("seems all right");
    }
}
