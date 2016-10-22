
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * Created by dokgo on 16.10.16.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        Client originalClient = new Client();
        originalClient.setID(1);
        originalClient.setName("Chuck Norris");
        originalClient.setBirthDate(LocalDate.of(1940, 3, 10));

        Path path = Paths.get("object.bin");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(originalClient);
        }

        Client deserializedClient;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            deserializedClient = (Client) ois.readObject();
        }

        System.out.printf("%-15s %-30s\n", "ID", deserializedClient.getID());
        System.out.printf("%-15s %-30s\n", "Name", deserializedClient.getName());
        System.out.printf("%-15s %-30s\n", "Date of Birth", deserializedClient.getBirthDate());
        System.out.printf("%-15s %-30s\n", "Age", deserializedClient.getAgeInYears());

    }
    public static class Client implements Serializable {
        private long id;
        private String name;
        private LocalDate birthDate;
        private transient int ageInYears;

        public long getID() {
            return id;
        }

        public void setID(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public int getAgeInYears() {
            if (ageInYears == 0) {
                ageInYears = birthDate.until(LocalDate.now()).getYears();
            }
            return ageInYears;
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