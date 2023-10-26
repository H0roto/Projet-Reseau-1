import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.*;

class DESTest extends DES {
    //Chaîne devant être cryptée/décryptée
    private static final String S="On sait depuis longtemps que travailler avec du texte lisible et contenant du sens est source de distractions," +
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
     public static int[] genereBlocOrPerm(int taille,int bound) {
         //Bound=2 si on génère un bloc et taille sinon
        Random r = new Random();
        int[] blocGen = new int[taille];
        for (int i = 0; i < taille; i++) {
            int rdm = r.nextInt(bound);
            blocGen[i] = rdm;
        }
        return blocGen;
    }

//    public static int[] generePerm

    public
    //Test de l'intégrité du message passé dans stringToBits et bitsToString
    @Test
    void testStringToBitsANDBitsToString() {
         assertEquals(S,bitsToString(stringToBits(S)));
    }
    @Test
    void testGenereMasterKey() {
         genereMasterKey();
         System.out.println("///MasterKey Supposée Aléatoire///");
         System.out.println(Arrays.toString(masterkey));
         System.out.println(masterkey.length);
         System.out.println("/////////////////////////////////\n");
    }
    //Remarque: la fonction invPermutation est analogue avec permutation que si tab_permutation contient tous les indices de bloc et ce une et une seule fois.
    //Vu que cette fonction n'est utilisée que pour inverser permutation initiale, qui est une bijection, ce n'est pas gênant mais je tenais à le dire.
    @Nested
    class testPermutation {
         @Test
         void randomTest() {
            Random r = new Random();
            int deb = r.nextInt(1, 50);
            int fin = r.nextInt(51, 100);
            for (int i = deb; i < fin; i++) {
                int[] perm = genereBlocOrPerm(i, i);
                int[] bloc = genereBlocOrPerm(i, 2);
                int[] res = permutation(perm, bloc);
                for (int j = 0; j < bloc.length; j++) {
                    assertEquals(bloc[perm[j]], res[j]);
                }
            }
         }
         @Test
        void valMinTest(){
             int[] perm=new int[]{0};
             int[] bloc=new int[]{1};
             int[] res=permutation(perm,bloc);
             assertEquals(bloc[perm[0]],res[0]);
         }
         @Test
        void valVideTest(){
             int[] perm=new int []{};
             int [] bloc=new int[]{};
             int [] blocPasVide=new int[12];
             int[] permPasVide={1,0};
             int[] res=permutation(perm,bloc);
             int[] res2=permutation(perm,blocPasVide);
             int[] res3=permutation(permPasVide,bloc);
             //perm et blocs vides
             assertEquals(0, res.length);
             //perm vide mais pas bloc
             assertEquals(blocPasVide,res2);
             //perm pas vide mais bloc vide
             assertEquals(0,res3.length);
         }
    }

    @Nested
    class TestInvPermutation{
         @Test
         void testIfComplementaire() {

    }
    }
    @Test
    void testDecoupage() {
    }

    @Test
    void testRecollage_bloc() {
    }

    @Test
    void testDecalle_gauche() {
    }

    @Test
    void testXor() {
    }

    @Test
    void testGénèreClé() {
    }

    @Test
    void testFonctions_S() {
    }

    @Test
    void testFonction_F() {
    }

    @Test
    void testCrypte() {
    }

    @Test
    void testDecrypte() {
    }
}