import javax.swing.*;
import java.awt.*;

public class GraphicDES extends JFrame {

    public GraphicDES() {
        // Configurer le titre de la fenêtre
        setTitle("DES");

        // Configurer la taille de la fenêtre pour qu'elle occupe tout l'écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Configurer la couleur de fond et la couleur de texte par défaut
        getContentPane().setBackground(Color.BLACK);

        // Configurer la mise en page (layout)
        setLayout(new BorderLayout());

        // Créer le JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.NORTH);

        // Ajouter le premier onglet
        JPanel panel1 = createPanel();
        tabbedPane.addTab("Crypter", panel1);
        tabbedPane.setForeground(Color.GREEN);  // Couleur du texte des onglets
        tabbedPane.setBackground(Color.BLACK);  // Couleur de fond des onglets non sélectionnés
        tabbedPane.addChangeListener(e -> {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JPanel tabComponent = (JPanel) tabbedPane.getComponentAt(i);
            if (i == tabbedPane.getSelectedIndex()) {
                tabbedPane.setForegroundAt(i, Color.BLACK);
            } else {
                tabbedPane.setForegroundAt(i, Color.GREEN);
                tabComponent.setBackground(Color.BLACK);
            }
        }
    });
        // Ajouter le deuxième onglet
        JPanel panel2 = createPanel();
//        panel2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 0));
        tabbedPane.addTab("Décrypter", panel2);

        tabbedPane.setForegroundAt(0, Color.BLACK); // Premier onglet en noir
        tabbedPane.setForegroundAt(1, Color.GREEN); // Deuxième onglet en vert
        int nouvelleTaille=30;
        Font newFont = new Font("nomDeVotrePolice", Font.PLAIN, nouvelleTaille);
        tabbedPane.setFont(newFont);
        // Créer et configurer le panneau (boîte de dialogue)
        JPanel dialogPanel = new JPanel();
        dialogPanel.setBackground(Color.BLACK);
        dialogPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        add(dialogPanel, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        // Ajouter des composants au panneau
        JLabel label = new JLabel("Texte en vert");
        label.setForeground(Color.GREEN);
        dialogPanel.add(label);

        // Configurer l'opération de fermeture par défaut
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Rendre la fenêtre visible
        setVisible(true);
    }
    public static void main(String[] args) {
        // Définir la couleur de fond de l'onglet sélectionné
        UIManager.put("TabbedPane.selected", Color.GREEN);
//        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
        new GraphicDES();
    }
}
