package org.example;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;

public class Main extends JFrame {

    public JSONObject repositories = null;

    public JSONObject loadJSON(String fileName) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            return jsonObject;
        } catch (IOException | ParseException e) {
            System.err.println("Hiba történt a fájl olvasása során: " + e.getMessage());
            return null;
        }
    }

    Main() {
        super("Giter");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception ex) {
            System.out.println(ex);
        }

        SwingUtilities.invokeLater(() -> {
            JPanel main = new JPanel();
            main.setLayout(new GridBagLayout());

            repositories = loadJSON("repositories.json");
            if (repositories != null) {
                System.out.println(repositories);
            }

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 10, 0); // Üres hely a cím és a reponame között
            gbc.anchor = GridBagConstraints.CENTER;

            JLabel title = new JLabel("Local repositories");
            title.setFont(new Font("Arial", Font.BOLD, 24));
            main.add(title, gbc);

            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 0, 0); // Visszaállítjuk az alapértelmezett insetset
            gbc.anchor = GridBagConstraints.WEST;
            //gbc.fill = GridBagConstraints.BOTH;

            JPanel nav = new JPanel();
            JButton new_repo = new JButton("Új repository");
            new_repo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Ebben a blokkban hajthatja végre a gomb lenyomására reagáló műveleteket

                    Ablak ablak = new Ablak();
                    ablak.setVisible(true);
                    ablak.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            // Ide kerülhet az a kód, amit futtatni szeretnél, amikor a dialógusablak bezáródik
                            System.out.println("A dialógusablak bezárva.");
                        }
                    });

                }
            });
            nav.add(new_repo);
            main.add(nav,gbc);

            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 0, 0); // Visszaállítjuk az alapértelmezett insetset
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;

            JPanel repos_panel = new JPanel();
            repos_panel.setLayout(new BoxLayout(repos_panel, BoxLayout.Y_AXIS));

            if (repositories != null) {
                repositories.forEach((repositoryName, repositoryData) -> {
                    JSONObject repository = (JSONObject) repositoryData;
                    JLabel names = new JLabel(repositoryName.toString());
                    repos_panel.add(names);
                    System.out.println(repository);
                });
            }

            main.add(repos_panel, gbc);
            add(main);
            setVisible(true);
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
