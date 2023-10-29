import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.PSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.*;

class DESTest extends DES {
    //Fonction qui génère un bloc ou une permutation aléatoire
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

    //Ici, on génère aussi une permutation, mais cette fois-ci, sans avoir 2 fois le même chiffre dans la table ni de
    // chiffre manquant pour pouvoir faire l'opération inverse.
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
    //Fonction qui génère un message aléatoire
    public static String genereMessageAlea(int taille){
        Random r=new Random();
        char lettreAlea;
        StringBuilder message= new StringBuilder();
        for(int i=0;i<taille;i++){
            //C'est la plage des caractères ASCII imprimable + les caractères latins courants
            lettreAlea= (char) r.nextInt(32,256);
            message.append(lettreAlea);
        }
        return message.toString();
    }
    //Test de l'intégrité du message passé dans stringToBits et bitsToString
    @Nested class TestStringToBitsANDBitsToString {
        @Test void testRandom() {
            for (int i = 1; i < 1000; i++) {
                String message = genereMessageAlea(i);
                assertEquals(message, bitsToString(stringToBits(message)));
            }
        }
        @Test void testLimite(){
            String message = "";
            assertEquals(message,bitsToString(stringToBits(message)));
        }
    }
    //Test Visuel de GenereMasterKey parce que je ne peux pas faire grand-chose de mieux
    @Test
    void testGenereMasterKey() {
        genereMasterKey();
        System.out.println("///MasterKey Supposée Aléatoire///");
        System.out.println(Arrays.toString(masterkey));
        System.out.println("/////////////////////////////////\n");
    }
    //Remarque: la fonction invPermutation est analogue avec permutation que si tab_permutation contient tous les indices
    // de bloc et ce une et une seule fois.Vu que cette fonction n'est utilisée que pour inverser permutation initiale,
    // qui est une bijection, ce n'est pas gênant mais je tenais à le dire.
    @Nested
    class TestPermutationEtInvPermutation {
        @Test
        void testRandomIfComplementaire() {
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
    @Nested
    class TestDecalle_Gauche {
        @Test
        void testRandom() {
            Random r = new Random();
            for (int k = 1; k < 1000; k++) {
                int[] blocInitial = genereBlocOrPerm(k, 50);
                int decale_cran = r.nextInt(1, k + 1);
                int[] res = decalle_gauche(blocInitial, decale_cran);
                //Test du gros de la liste
                for (int i = decale_cran; i < blocInitial.length; i++) {
                    assertEquals(blocInitial[i], res[i - decale_cran]);
                }
                //Test de la fin de la liste
                for (int i = 0, j = blocInitial.length - decale_cran; i < decale_cran; i++, j++) {
                    assertEquals(blocInitial[i], res[j]);
                }
            }

        }
        @Test
        void testLimite(){
            int[] bloc={};
            int[] res=decalle_gauche(bloc,1);
            assertArrayEquals(bloc,res);
            int[] bloc2={1};
            int [] res2=decalle_gauche(bloc2,0);
            assertArrayEquals(bloc2,res2);
        }
    }

    @Nested
    class TestXor {
        //Là je teste pour des entiers entre 0 et 1 parce que c'est le plus logique pour le xor
        //Mais il faut savoir que ça fonctionne (bizarrement) pour tous les nombres même si je ne comprends pas trop son
        //Fonctionnement dans ce cas.
        @Test void testRandom() {
            Random r = new Random();
            for (int k = 1; k < 1000; k++) {
                int[] blocInitial = genereBlocOrPerm(k, 2);
                int[] res = xor(blocInitial,blocInitial);
                assertArrayEquals(res,new int[res.length]);
            }
        }
        @Test void testListeVide(){
            int[] bloc={};
            int[] res=xor(bloc,bloc);
            assertArrayEquals(res,bloc);
        }
        @Test void testInterdit(){
            int[] bloc={};
            int[] bloc2={1};
            assertThrows(IllegalArgumentException.class,()-> xor(bloc,bloc2));
        }
    }
    //Pareil cela ne sera qu'un test visuel
    @Test
    void testGénèreClé() {
        génèreClé(0);
        System.out.println("clé finale");
        System.out.println(Arrays.toString(tab_cles[0]));
    }
    //Bon j'aime bien quand c'est aléatoire mais là à part reproduire le même algorithme je vois pas comment
    //L'automatiser...
    @Nested class TestFonctionS{
            @Test void testDetermine() {
                assertArrayEquals(fonctions_S(new int[]{1,1,0,1,1,1},0),new int[]{1,1,1,0});
                assertArrayEquals(fonctions_S(new int[]{1,1,0,1,1,1},15),new int[]{0,0,0,0});
            }
            @Test void testInterdit(){
                assertThrows(IllegalArgumentException.class,()->fonctions_S(new int[]{},0));
            }
    }
    //Test visuel de F
    @Test
    void testFonction_F() {
        int[] D=genereBlocOrPerm(32,2);
        System.out.println("Résultat fonction F");
        System.out.println(Arrays.toString((fonction_F(D,0))));
    }

    @Nested class testCrypteDecrypte{
        @Test void testRandom() {
            for (int i = 1; i < 1000; i++) {
                String message = genereMessageAlea(i);
                assertEquals(message, decrypte(crypte(message)));
            }
        }
        @Test void testLimite(){
            String message="";
            assertEquals(message,decrypte(crypte(message)));
        }
    }
}