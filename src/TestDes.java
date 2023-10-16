import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Random;

public class TestDes {
    public static int[] genereBloc(int taille) { //C'est genere masterkey à part que la fonction renvoie un bloc à la place de modifier un tableau existant
        Random r = new Random();
        int[] blocGen = new int[taille];
        for (int i = 0; i < taille; i++) {
            int rdm = r.nextInt(2);
            blocGen[i] = rdm;
        }
        return blocGen;
    }

    public static void main(String[] args) {
        //Initialisation du constructeur
        DES d = new DES();
        ///

        //Message devant être codé
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
        ///

        //Test de conservation de l'intégrité du message pour les méthodes analogues bitsToString et stringToBits
        System.out.println("Le message initial est égal au message transformé: " + d.bitsToString(d.stringToBits(s)).equals(s));
        ///

        //Test visuel de genereMasterKey
        d.genereMasterKey();
        System.out.println("///MasterKey Supposée Aléatoire///");
        System.out.println(Arrays.toString(d.masterkey));
        System.out.println(d.masterkey.length);
        System.out.println("/////////////////////////////////\n");
        ///

        //Test visuel de genereBloc
        int[] blocTest = genereBloc(64);
        System.out.println("///Bloc Supposé Aléatoire///");
        System.out.println(Arrays.toString(blocTest));
        System.out.println(blocTest.length);
        System.out.println("///////////////////////////\n");
        ///

        //Test de conservation de l'intégrité du bloc lors d'une double permutation réciproque
        System.out.println("Le bloc initial est égal au bloc doublement permuté:" + Arrays.equals(d.invPermutation(DES.PERM_INITIALE, d.permutation(DES.PERM_INITIALE, blocTest)), blocTest));
        ///

        //Test de découpage/recollage pour une taille de bloc de 64 et 32
        int[] tailleBloc = {DES.TAILLE_BLOC, DES.TAILLE_SOUS_BLOC};
        int[] blocNonDecoupe = d.stringToBits(s);
        for (int taille : tailleBloc) {
            //Je fais un tableau qui a la même taille que blocNonDecoupe pour éviter de compter le bourrage dans la comparaison
            int[] test = Arrays.copyOf(d.recollage_bloc(d.decoupage(blocNonDecoupe, tailleBloc[0])),blocNonDecoupe.length);
            boolean sameData= Arrays.equals(test, blocNonDecoupe);
            System.out.printf("Le bloc découpé contient les mêmes données que le bloc initial sans compter le bourrage (%d): %b \n",taille,sameData);
        }
        ///

        //Test Visuel de décallage gauche TODO faire un test plus perfectionné
        System.out.println(Arrays.toString(blocTest));
        System.out.println(Arrays.toString(d.decalle_gauche(blocTest, 2))+"\n\n");
        ///

        //TODO Test de xor faire un test plus perfectionné
        System.out.println(Arrays.toString(blocTest));
        int[] blocTest2=genereBloc(64);
        System.out.println(Arrays.toString(blocTest2));
        System.out.println(Arrays.toString(d.xor(blocTest, blocTest2)));
        ///
        // TODO Test de S mieux également
        d.génèreClé(0);
        System.out.println(Arrays.toString(d.fonctions_S(new int[]{0,1,0,1,1,1})));
        ///

        //TODO Test de F plus perfecionné
        d.génèreClé(0);
        int[] blocTest3=genereBloc(64);
        int[] D = d.decoupage(blocTest3,32)[1];
        System.out.println(Arrays.toString((d.fonction_F(D,0))));

        //TODO  Test de crypte:
        System.out.println(Arrays.toString(d.crypte(s)));
        System.out.println(d.decrypte(d.crypte(s)));
        System.out.println(s);

    }
}
