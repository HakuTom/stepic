import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * Created by dokgo on 16.10.16.
 */

public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler h3 = new ConsoleHandler();
        h3.setFormatter(new XMLFormatter());
        LOGGER.log(Level.FINE,"started with{0}", Arrays.toString(args));
        int i = 29;
        LOGGER.log(Level.INFO,"im logging i {0}", i);
        LOGGER.severe("warningLog{0}");
        System.out.println("lol");

        int[] arr = {1};
        for(int j = 1; j < arr.length; j++){
            System.out.println(arr[j]);
        }
    }
}