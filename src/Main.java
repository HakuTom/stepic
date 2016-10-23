
import org.omg.PortableInterceptor.INACTIVE;

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
        HashSet<String> s1 = new HashSet<>();
        s1.add("asd");
        s1.add("qwe");
        s1.add("zxc");

        HashSet<String> s2 = new HashSet<>();
        s2.add("asd");
        s2.add("qw");
        s2.add("zxc");

        symmetricDifference(s1,s2);
        System.out.println( symmetricDifference(s1,s2));
    }

    public static <T> Set<T> symmetricDifference(Set<? extends T> set1, Set<? extends T> set2) {
        HashSet<T> s1 = new HashSet<>(set1);
        HashSet<T> s2 = new HashSet<>(set2);
        s1.removeAll(set2);
        s2.removeAll(set1);
        s1.addAll(s2);
        return s1;
    }

    class Animal implements Serializable {
        private final String name;

        public Animal(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Animal) {
                return Objects.equals(name, ((Animal) obj).name);
            }
            return false;
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
    public static Animal[] deserializeAnimalArray(byte[] data) {
        try(ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(data))){
            int length = stream.readInt();
            Animal[] animals = new Animal[length];
            for (int i = 0; i < length; i++){
                Object object = stream.readObject();
                if(!(object instanceof Animal)) continue;
                animals[i] = (Animal)object;
            }
            return animals;
        }
        catch (Exception e){
            throw new IllegalArgumentException();
        }
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