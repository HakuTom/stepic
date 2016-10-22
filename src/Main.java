
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;

/**
 * Created by dokgo on 16.10.16.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ls", "-lh")
                .directory(new File("/home/dokgo/Git"))
                .redirectInput(ProcessBuilder.Redirect.from(new File("/dev/null")))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = processBuilder.start();
        try( BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))){
            reader.lines().forEach(System.out::println);
        }

        int exitValue = process.waitFor();
        if (exitValue != 0) {
            System.err.println("Subprocess terminated abmormaly");
        }

    }



    public static String readAsString(InputStream inputStream, Charset charset) throws IOException {

        Reader reader = new InputStreamReader(inputStream, charset);
        int ch;
        String res = "";
        while ((ch = reader.read()) != -1){
            res += new String (Character.toChars(ch));
        }
        return res;

        //System.out.println(Arrays.toString(arr));

    }
    public static void checkSumOfStream(InputStream inputStream) throws IOException {
        int curr;
        int tmp;
        while((curr = inputStream.read()) != -1){
            if(curr == 13 && (tmp = inputStream.read()) == 10){
                curr = tmp;
            }
            System.out.println((byte)curr);
        }
    }


    public static List<Double> parseFile(String pathString) {
        Path path = Paths.get(pathString);
        String[] list = new String[0]; //.replaceAll("[\\]\\[]","")
        try {
            list = Files.readAllLines(path).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Double> numbers = new ArrayList<>();

        for(int i = 0; i < list.length; i++) {
            String s = list[i].replace(",", ".");
            if(!s.equals(""))
                numbers.add(Double.parseDouble(s));
        }
        return numbers;
    }

    public static double mean(String path) {
        List<Double> numbers = parseFile(path);
        double sum = 0;
        for(double d : numbers){
            sum += d;
        }
        return  sum/numbers.size();
    }

}