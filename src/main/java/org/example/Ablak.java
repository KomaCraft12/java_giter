package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;

public class Ablak extends JFrame {

    public String repo_name;
    public String repo_url;
    public String repo_local_dir;


    public void register(){

        String fileName = "repositories.json"; // JSON fájl elérési útvonala és neve
        JSONObject jsonObject = new JSONObject(); // JSON objektum inicializálása

        // JSON parser inicializálása
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // JSON fájl beolvasása és parse-olása
            jsonObject = (JSONObject) parser.parse(reader);

            // Az új modpack hozzáadása
            JSONObject repo = new JSONObject();
            repo.put("repo", repo_url);
            repo.put("local_dir",repo_local_dir);

            jsonObject.put(repo_name, repo); // Új modpack hozzáadása a jsonObject-hez

            // Fájl tartalmának frissítése az új JSON objektummal
            try (FileWriter file = new FileWriter(fileName)) {
                // Gson inicializálása a szép formázás céljából
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String prettyJsonString = gson.toJson(jsonObject); // Szépen formázott JSON sztring generálása
                file.write(prettyJsonString); // Szépen formázott JSON sztring kiírása a fájlba
            } catch (IOException e) {
                System.err.println("Hiba történt a fájl írása során: " + e.getMessage());
            }

        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
        }


    }

    public static String getRepoName(String url) {
        try {
            URI uri = new URI(url);
            String[] segments = uri.getPath().split("/");
            if (segments.length >= 2) {
                return segments[segments.length - 1].replaceAll("\\.git$", "");
            } else {
                return "Nem található repository név a megadott URL-ben.";
            }
        } catch (URISyntaxException e) {
            return "Hibás URL formátum: " + e.getMessage();
        }
    }

    public void GitClone(String url, String path){

        repo_name = getRepoName(url);
        repo_url = url;
        repo_local_dir = path + "\\" + repo_name;

        String repositoryUrl = url;
        String destinationFolder = path + "\\" + repo_name;

        // Parancs létrehozása
        String[] command = {"git", "clone", repositoryUrl, destinationFolder};

        // ProcessBuilder létrehozása a parancs futtatásához
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            // Parancs futtatása
            Process process = processBuilder.start();

            // Parancs végrehajtásának figyelése
            int exitCode = process.waitFor();

            // Ellenőrizd, hogy a parancs sikeresen futott-e
            if (exitCode == 0) {
                System.out.println("A git clone parancs sikeresen lefutott.");
                register();
            } else {
                System.err.println("Hiba történt a git clone parancs futtatása közben.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    Ablak() {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Giter - CLONE");
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            JLabel label = new JLabel("Új repository", SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(380,25));
            label.setFont(new Font("Arial",Font.BOLD,18));

            JButton path_btn = new JButton();
            path_btn.setText("Utvonal");

            JLabel path = new JLabel("");
            path.setPreferredSize(new Dimension(380,18));

            path_btn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile);
                    path.setText(selectedFile.toString());
                    //textField.setText(selectedFile.getAbsolutePath());
                }
            });

            JTextArea url = new JTextArea();
            url.setPreferredSize(new Dimension(380,18));

            JButton button = new JButton();
            button.setText("CLONE");

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    System.out.println("Clone: "+url.getText());

                    GitClone(url.getText(),path.getText());

                }
            });

            panel.add(label);
            panel.add(path_btn);
            panel.add(path);
            panel.add(url);
            panel.add(button);
            frame.add(panel);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

        });


    }

    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex){
            System.out.println(ex);
        }
        new Ablak();
    }

}
