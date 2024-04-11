package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;

public class Ablak extends JFrame {


    public void GitClone(String url, String path){

        String repositoryUrl = url;
        String destinationFolder = path;

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
            JLabel label = new JLabel("Új repository");
            label.setPreferredSize(new Dimension(380,18));

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
