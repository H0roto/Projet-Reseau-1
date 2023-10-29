import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GraphicDES extends JFrame {
    public static class CouplePanelText{
        JTextArea t;
        JPanel p;
        CouplePanelText(JPanel p,JTextArea t){
            this.p=p;
            this.t=t;
        }
        JPanel getPanel(){
            return p;
        }
        JTextArea getText(){
            return t;
        }
    }
    private boolean listenerAdded = false;
    JTextArea textCrypt;
    JTextArea textDecrypt;
    // Format de la date (pour avoir des noms de fichier uniques)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("SSSddHHmmMMyyyyss");
    //Je récupère la taille de la fenêtre
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        //Police par défaut
        int nouvelleTaille=30;
        Font policeDefaut = new Font("nomDeVotrePolice", Font.PLAIN, nouvelleTaille);
    ///
    //Fonction qui créé un couplePanelText
    private CouplePanelText createPanel(String labelText, ActionListener submitAction,ActionListener traitmentAction) {
    JPanel panel = new JPanel(new BorderLayout());
    // Zone de texte
    JTextArea textArea= new JTextArea();
    Dimension dimText = new Dimension(screenWidth / 2, screenHeight / 2);
    textArea.setFont(policeDefaut);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    textArea.setPreferredSize(dimText);
    textArea.setCaretColor(Color.GREEN);
    JPanel panelText = new JPanel();
    panelText.add(textArea);

    // Bouton d'envoi
    Dimension dimButton = new Dimension(screenWidth / 5, screenHeight / 17);
    JButton buttonSubmit = new JButton("Envoyer");
    buttonSubmit.setPreferredSize(dimButton);
    buttonSubmit.addActionListener(submitAction);
    JPanel panelButtonSub = new JPanel();
    panelButtonSub.add(buttonSubmit);
    buttonSubmit.setFont(policeDefaut);

    // Titre
    JLabel label = new JLabel(labelText);
    label.setFont(policeDefaut);
    JPanel labelPanel = new JPanel();
    labelPanel.add(label);
    // Bouton de choix du fichier
    JButton button = new JButton("Choisir un fichier");
    button.setFont(policeDefaut);
    button.addActionListener(traitmentAction);
    JPanel panelButton = new JPanel();
    panelButton.add(button);

    // Ajout des éléments au panel
    JPanel panelGroupText = new JPanel(new BorderLayout());
    panelGroupText.add(panelText, BorderLayout.NORTH);
    panelGroupText.add(panelButtonSub, BorderLayout.CENTER);

    panel.add(panelGroupText, BorderLayout.CENTER);
    panel.add(labelPanel, BorderLayout.NORTH);
    panel.add(panelButton, BorderLayout.SOUTH);

        return new CouplePanelText(panel, textArea);
}

    //Dico servant à retrouver le tableau de clé utilisé
//    HashMap<int[], int[][][]> dicoDecryptage=new HashMap<>();
    public GraphicDES() {


        // Titre de la fenêtre
        this.setTitle("DES");

        // Taille de la fenêtre occupant tout l'écran
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Layout
        this.setLayout(new BorderLayout());

        //Ajout barre d'onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        //couple JPanel et JTextArea de cryptage
        CouplePanelText coupleCryptage = createPanel(
                "Mettez ici le message devant être crypté ou choisissez le fichier devant l'être",
                //Fonction de cruptage en paramètre de la fonction pour créer un panel
                e -> {
            String messageFin="";
            TripleDES td=new TripleDES();
            System.out.println(textCrypt.getText());
            int[] messageCrypte=td.crypteTripleDES(textCrypt.getText());
            System.out.println(Arrays.toString(messageCrypte));
            LocalDateTime now = LocalDateTime.now();
            String nomduFichier=now.format(formatter)+".txt";
            try{
                File monFichier = new File(nomduFichier);
               String texteAMettre=Arrays.toString(messageCrypte)+";\n"+ Arrays.deepToString(td.tab_cles)+";\n"+Arrays.deepToString(td.K)+";";
                monFichier.createNewFile();
                Files.write(Paths.get(nomduFichier), texteAMettre.getBytes());
                messageFin="Le texte a bien été crypté";
                textCrypt.setForeground(Color.CYAN);
            } catch (IOException exception) {
            // Je gère les exceptions
            messageFin="Une erreur est survenue.";
            textCrypt.setForeground(Color.RED);
            exception.printStackTrace();
        }
            textCrypt.setText(messageFin);
            String finalMessageFin = messageFin;
            textCrypt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (finalMessageFin.equals(textCrypt.getText())) {
                    textCrypt.setText("");
                    textCrypt.setForeground(Color.GREEN);
                }
            }
                });
            },
                e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Fichier sélectionné: " + (selectedFile).getAbsolutePath());
                LocalDateTime now = LocalDateTime.now();
                String nomduFichier=now.format(formatter)+".txt";
                try {
                String aCrypter = new String(Files.readAllBytes(selectedFile.toPath()));
                System.out.println(aCrypter);
                TripleDES td=new TripleDES();
                int[] messageCrypte=td.crypteTripleDES(aCrypter);
                File monFichier = new File(nomduFichier);
                String texteAMettre=Arrays.toString(messageCrypte)+";\n"+ Arrays.deepToString(td.tab_cles)+";\n"+Arrays.deepToString(td.K)+";";
                monFichier.createNewFile();
                Files.write(Paths.get(nomduFichier), texteAMettre.getBytes());
                System.out.println(Arrays.toString(messageCrypte));
                UIManager.put("OptionPane.messageForeground", Color.CYAN);
                JOptionPane.showMessageDialog(null, "Le message a bien été crypté");
              } catch (IOException ex) {
                UIManager.put("OptionPane.messageForeground", Color.RED);
                JOptionPane.showMessageDialog(null, "Une erreur est survenue.");
                ex.printStackTrace();
            }
                UIManager.put("OptionPane.messageForeground", Color.RED);
            }
        });
        JPanel panelCryptage=coupleCryptage.getPanel();
        textCrypt=coupleCryptage.getText();

        //couple JPanel et JTextArea de décryptage
        CouplePanelText coupleDecryptage = createPanel("Mettez ici le message devant être décrypté ou choisissez le fichier devant l'être",
                //Fonction de cruptage en paramètre de la fonction pour créer un panel
                e -> {
                    System.out.println("HELLO WORLD");
        },
                e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Fichier sélectionné: " + (selectedFile).getAbsolutePath());
                String aDeCrypter = null;
                try {
                    aDeCrypter = new String(Files.readAllBytes(selectedFile.toPath()));
                    String[] tabSousChaines=new String[3];
                    int i=0;
                    System.out.println(aDeCrypter.length());
                    while (i<3 || aDeCrypter.contains(";")){
                        System.out.println(aDeCrypter);
                        int startIndex=0;
                        System.out.println(startIndex);
                        int endIndex=aDeCrypter.indexOf(";");
                        System.out.println(endIndex);
                        tabSousChaines[i]=aDeCrypter.substring(startIndex,endIndex);
                        aDeCrypter=aDeCrypter.substring(endIndex+1);
                        i++;
                    }
                    String[] crypté=tabSousChaines[0].replaceAll("\\[","")
                            .replaceAll("]","").split(",");
                    ArrayList<Integer> listnumber=new ArrayList<Integer>();
                    for (String s:crypté) {
                        listnumber.add(Integer.parseInt(s.replaceAll(" ","")));
                    }
                    int[] crypte=listnumber.stream().mapToInt(Integer::intValue).toArray();
                    FonctionAuxilliairePasDeMoi f=new FonctionAuxilliairePasDeMoi();
                    int[][][] tabCles=f.transformToNative3DArray(tabSousChaines[1]);
                    int[][] cleK=f.transformToNative2DArray(tabSousChaines[2]);
                    TripleDES td=new TripleDES();
                    td.tab_cles=tabCles;
                    td.K=cleK;
                    System.out.println(Arrays.deepToString(tabCles));
                    System.out.println(tabCles.length);
                    System.out.println(tabCles[0].length);
                    System.out.println(tabCles[0][0].length);
                    String messageFinal=td.decrypteTripleDES(crypte);
                    System.out.println(messageFinal);
                    textDecrypt.setForeground(Color.CYAN);
                    textDecrypt.setText(messageFinal);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JPanel panelDecryptage=coupleDecryptage.getPanel();
        textDecrypt=coupleCryptage.getText();
        //Liaison des panels aux onglets
        tabbedPane.addTab("Crypter", panelCryptage);
        tabbedPane.addTab("Décrypter", panelDecryptage);

        //Propriété des panels


        //Propriété des onglets
        tabbedPane.setFont(policeDefaut);
        tabbedPane.setForegroundAt(0, Color.BLACK); // Texte du premier onglet en noir
        tabbedPane.setForegroundAt(1, Color.GREEN); // Texte du deuxième onglet en vert
        tabbedPane.setForeground(Color.GREEN);  // Couleur du texte des onglets
        tabbedPane.setBackground(Color.BLACK);  // Couleur de fond des onglets non sélectionnés
        //Fonction qui fait en sorte que la couleur change quand on clique sur un onglet
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
        //Opération de fermeture par défaut
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        // Défini les couleurs par défaut
        UIManager.put("Panel.background", Color.BLACK);
        UIManager.put("Panel.foreground", Color.GREEN);
        UIManager.put("Label.background", Color.BLACK);
        UIManager.put("Label.foreground", Color.GREEN);
        UIManager.put("Button.background", Color.BLACK);
        UIManager.put("Button.foreground", Color.GREEN);
        UIManager.put("TabbedPane.selected", Color.GREEN);
        UIManager.put("TextArea.background",Color.BLACK);
        UIManager.put("TextArea.foreground",Color.GREEN);

        new GraphicDES();
    }
}
