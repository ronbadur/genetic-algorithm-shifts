package final_project.benchmark;

import final_project.genetic.entities.Individual;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FilePrinter {
    private static FilePrinter ourInstance = new FilePrinter();

    public static FilePrinter getInstance() {
        return ourInstance;
    }

    private FilePrinter() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("GenerationFiles/Benchmark-bruteforce-optional-results.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        writer.close();
    }

//    public void print(ArrayList<ArrayList<BruteForceAlgorithm.DigitSpot>> combinations) {
//        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//                new FileOutputStream("GenerationFiles/Benchmark-results.txt"), "utf-8"))) {
//
//            AtomicInteger index = new AtomicInteger();
//            combinations.forEach(list -> {
//                try {
//                    index.incrementAndGet();
//                    writer.write("combination number: " + (index.get()) + "\n\n");
//                    list.forEach(object -> {
//                        try {
//                            writer.write(object.toString() + "\n");
//                        } catch (IOException e){
//                            System.out.printf(e.getMessage());
//                        }
//                    });
//                    writer.write("===================================" + "\n\n");
//
//                } catch (IOException e) {
//                    System.out.printf(e.getMessage());
//                }
//            });
//        } catch (IOException e) {
//            System.out.printf(e.getMessage());
//        }
//
//    }

    public void print(int[][][] constraints, String title) {

        StringBuilder text = new StringBuilder("==========  " + title + " ===========" + "\n\n");
        text.append(Individual.getPrintableMatrix(constraints));
        text.append("===================================" + "n.\n");

        try {
            Files.write(Paths.get("GenerationFiles/Benchmark-bruteforce-optional-results.txt"), text.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.printf(e.getMessage());
        }

    }

    public void print(ArrayList<int[][][]> optionalResults) {
        StringBuilder text = new StringBuilder();

        AtomicInteger index = new AtomicInteger();
        optionalResults.forEach(array -> {

            String printableMatrix = Individual.getPrintableMatrix(array);
            index.incrementAndGet();
            text.append("option number: " + (index.get()) + "\n\n");
            text.append(printableMatrix);
            text.append("===================================" + "\n\n");
        });

        try {
            Files.write(Paths.get("GenerationFiles/Benchmark-bruteforce-optional-results.txt"), text.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.printf(e.getMessage());
        }
    }
}
