import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Random;

public class TestDes {
    public static int[] genereBloc() { //C'est genere masterkey à part que la fonction renvoie un bloc à la place de modifier un tableau existant
        Random r = new Random();
        int[] blocGen = new int[DES.TAILLE_BLOC];
        for (int i = 0; i < DES.TAILLE_BLOC; i++) {
            int rdm = r.nextInt(2);
            blocGen[i] = rdm;
        }
        return blocGen;
    }

    public static void main(String[] args) {
        //Initialisation du constructeur
        DES d = new DES();
        ///

        //Test de stringToBits et bitsToString
        String s = "On sait depuis longtemps que travailler avec du texte lisible et contenant du sens est source de distractions," +
                " et empêche de se concentrer sur la mise en page elle-même." +
                " L'avantage du Lorem Ipsum sur un texte générique comme 'Du texte. Du texte. Du texte.'" +
                " est qu'il possède une distribution de lettres plus ou moins normale," +
                " et en tout cas comparable avec celle du français standard." +
                " De nombreuses suites logicielles de mise en page" +
                " ou éditeurs de sites Web ont fait du Lorem Ipsum leur faux texte par défaut," +
                " et une recherche pour 'Lorem Ipsum' vous conduira vers de nombreux sites qui n'en sont encore" +
                " qu'à leur phase de construction." +
                " Plusieurs versions sont apparues avec le temps, parfois par accident, souvent intentionnellement" +
                " (histoire d'y rajouter de petits clins d'oeil, voire des phrases embarassantes)";
        //Conservation de l'intégrité du message
        System.out.println("Le message initial est égal au message transformé: " + d.bitsToString(d.stringToBits(s)).equals(s));
        ///

        //Test visuel de genereMasterKey
        d.genereMasterKey();
        System.out.println(Arrays.toString(d.masterkey));
        System.out.println(d.masterkey.length);
        ///

        //Test visuel de genereBloc
        int[] blocTest = genereBloc();
        System.out.println(Arrays.toString(blocTest));
        System.out.println(blocTest.length);
        ///

        //Test de permutation et invPermutation
        System.out.println(Arrays.toString(d.permutation(DES.PERM_INITIALE, blocTest)));

        //Conservation de l'intégrité du bloc
        System.out.println("Le bloc initial est égal au bloc doublement permuté:" + Arrays.equals(d.invPermutation(DES.PERM_INITIALE, d.permutation(DES.PERM_INITIALE, blocTest)), blocTest));
        ///

        //Test de découpage/recollage pour une taille de bloc de 64 et 32
        int[] tailleBloc = {DES.TAILLE_BLOC, DES.TAILLE_SOUS_BLOC};
        int[] blocNonDecoupe = d.stringToBits(s);
        for (int taille : tailleBloc) {
            boolean sameData=true;
            int[][] test = d.decoupage(blocNonDecoupe, taille);
            for(int i=0;i< test.length;i++) {
                for (int j = 0; j < test[0].length; j++) {
                    int index = i * taille + j;
                    if (index < blocNonDecoupe.length && test[i][j] != blocNonDecoupe[index]) {
                        sameData = false;
                    }
                }
            }
            System.out.printf("Le bloc découpé contient les mêmes données que le bloc initial sans compter le bourrage (%d): %b \n",taille,sameData);
        }
        //Test de recollage
        System.out.println(Arrays.toString(blocNonDecoupe));
        System.out.println(Arrays.toString(d.recollage_bloc(d.decoupage(blocNonDecoupe, tailleBloc[0]))));
    }
}
