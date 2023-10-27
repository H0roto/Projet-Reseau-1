import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.*;

class DESTest extends DES {
    //Chaîne devant être cryptée/décryptée
    private static final String S = "On sait depuis longtemps que travailler avec du texte lisible et contenant du sens est source de distractions," +
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

    public static int[] genereBlocOrPerm(int taille, int bound) {
        //Bound=2 si on génère un bloc et taille sinon
        Random r = new Random();
        int[] blocGen = new int[taille];
        for (int i = 0; i < taille; i++) {
            int rdm = r.nextInt(bound);
            blocGen[i] = rdm;
        }
        return blocGen;
    }

    //Ici, on génère aussi une permutation, mais cette fois-ci, sans avoir 2 fois le même chiffre dans la table ni de chiffre manquant
    //Pour pouvoir faire l'opération inverse.
    public static int[] generePermAlea(int taille) {
        int[] tabPerm = new int[taille];
        HashSet<Integer> usedNumber = new HashSet<>();
        Random r = new Random();
        int i = 0;
        while (i < taille) {
            int number = r.nextInt(taille);
            if (!usedNumber.contains(number)) {
                tabPerm[i] = number;
                usedNumber.add(number);
                i++;
            }
        }
        return tabPerm;
    }

    public
    //Test de l'intégrité du message passé dans stringToBits et bitsToString
    @Test
    void testStringToBitsANDBitsToString() {
        assertEquals(S, bitsToString(stringToBits(S)));
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
            //ça ne sert pas à grand-chose puisque j'utilise le même algorithme pour tester que pour coder, mais bon je le laisse
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
        void valMinTest() {
            int[] perm = new int[]{0};
            int[] bloc = new int[]{1};
            int[] res = permutation(perm, bloc);
            assertEquals(bloc[perm[0]], res[0]);
        }

        @Test
        void valVideTest() {
            int[] perm = new int[]{};
            int[] bloc = new int[]{};
            int[] blocPasVide = new int[12];
            int[] permPasVide = {1, 0};
            int[] res = permutation(perm, bloc);
            int[] res2 = permutation(perm, blocPasVide);
            int[] res3 = permutation(permPasVide, bloc);
            //perm et blocs vides
            assertEquals(0, res.length);
            //perm vide mais pas bloc
            assertEquals(blocPasVide, res2);
            //perm pas vide mais bloc vide
            assertEquals(0, res3.length);
        }
    }

    @Nested
    class TestInvPermutation {
        @Test
        void testIfComplementaire() {
            Random r = new Random();
            int deb = r.nextInt(1, 50);
            int fin = r.nextInt(51, 100);
            for (int i = deb; i < fin; i++) {
                int[] perm = generePermAlea(i);
                int[] bloc = genereBlocOrPerm(i, 2);
                int[] res = invPermutation(perm, permutation(perm, bloc));
                assertArrayEquals(bloc, res);
            }
        }

        @Test
        void valMinTest() {
            int[] perm = new int[]{0};
            int[] bloc = new int[]{1};
            int[] res = invPermutation(perm, permutation(perm, bloc));
            assertArrayEquals(bloc, res);
        }

        @Test
        void valVideTest() {
            int[] perm = new int[]{};
            int[] bloc = new int[]{};
            int[] blocPasVide = new int[12];
            int[] permPasVide = {1, 0};
            int[] res = invPermutation(perm, permutation(perm, bloc));
            int[] res2 = invPermutation(perm, permutation(perm, blocPasVide));
            int[] res3 = invPermutation(perm, permutation(permPasVide, bloc));
            //perm et blocs vides
            assertEquals(0, res.length);
            //perm vide mais pas bloc
            System.out.println(Arrays.toString(res2));
            assertEquals(blocPasVide, res2);
            //perm pas vide mais bloc vide
            assertEquals(0, res3.length);
        }
    }

    @Nested
    class TestDecoupageEtRecollageBloc {
    @Test
    void testRandom() {
        Random r = new Random();
        for (int k = 1; k < 1000; k++) {
            int[] bloc = genereBlocOrPerm(k, 2);
            int taille = r.nextInt(1, k + 1);
            int[] res = recollage_bloc(decoupage(bloc, taille));
            //Je crée une copie du résultat faisant la taille de la liste initiale pour les comparer
            // sans avoir de problèmes de bourrage
            int[] sousListe = new int[bloc.length];
            System.arraycopy(res, 0, sousListe, 0, bloc.length);
            assertArrayEquals(bloc, sousListe);
        }
    }
    @Test
    void valMinTest(){
        int[] bloc={1};
        int [] res=recollage_bloc(decoupage(bloc,1));
        assertArrayEquals(bloc,res);
        int[] bloc2={};
        int[] res2=recollage_bloc(decoupage(bloc2,1));

    }
    @Test
    void testValInterdite(){
        int[] bloc={1};
        assertThrows(ArithmeticException.class,() -> recollage_bloc(decoupage(bloc,0)));
    }

}
    @Test
    void testDecalle_gauche() {
        int[] blocInitial=genereBlocOrPerm(100,33);
        System.out.println(Arrays.toString(blocInitial));
        int[] res=decalle_gauche(blocInitial,2);
        System.out.println(Arrays.toString(blocInitial));
        System.out.println(Arrays.toString(res));
        for(int i=0;i<blocInitial.length;i++){
            assertEquals(blocInitial[i+2],res[i]);
        }

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