package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Table {

    public String name;
    public final List<List<String>> content;

    public Table(String name, String filePath) {
        this.name = name;
        this.content = loadDataFromCSV(filePath);
        loadDataFromCSV(filePath);
    }

    private List<List<String>> loadDataFromCSV(String filePath) {
        List<List<String>> tableContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<String> row = new ArrayList<>(Arrays.asList(values));
                tableContent.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return tableContent;
    }

    public void printTable() {
        System.out.println(name);
        for (List<String> row : content) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Table table = new Table("Users", "db/t1.csv");
        table.printTable();
    }
}
