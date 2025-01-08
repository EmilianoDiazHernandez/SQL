package dao;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Table {

    public String name;
    private final List<List<String>> content;

    public Table(String name, String filePath) throws IOException {
        this.name = name;
        this.content = loadDataFromCSV(filePath);
        loadDataFromCSV(filePath);
    }

    private List<List<String>> loadDataFromCSV(String filePath) throws IOException {
        List<List<String>> tableContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<String> row = new ArrayList<>(Arrays.asList(values));
                tableContent.add(row);
            }
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
        try {
            Table table = new Table("Users", "db/t1.csv");
            table.printTable();
        } catch (IOException e) {
            System.out.println("Table not found");
        }
    }
}
