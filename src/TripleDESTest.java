import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TripleDESTest extends TripleDES {
    //Test Visuel de GenereKn parce que je ne peux pas faire grand-chose de mieux
    @Test
    void testGenereKn() {
        genereKn();
        System.out.println("///Kn Supposées Aléatoires///");
        for (int[] key : K) {
            System.out.println(Arrays.toString(key));
        }
        System.out.println("/////////////////////////////////\n");
    }

    //Pareil cela ne sera qu'un test visuel
    @Test
    void testGénèreClé() {
        génèreClé(0, 1);
        System.out.println("clé finale");
        System.out.println(Arrays.toString(tab_cles[0][0]));
    }

    //Idem donc pas grand intérêt
    @Test
    void testFonction_F() {
        int[] D = DESTest.genereBlocOrPerm(32, 2);
        System.out.println("Résultat fonction F");
        System.out.println(Arrays.toString((fonction_F(D, 0, 1))));
    }
    @Nested
    class testCrypteDecrypteTripleDES {
        @Test
        void testRandom() {
            for (int i = 1; i < 1000; i++) {
                String message = DESTest.genereMessageAlea(i);
                assertEquals(message,decrypteTripleDES(crypteTripleDES(message)));
            }
        }
        @Test
        void testLimite(){
            String message = "";
            assertEquals(message,decrypteTripleDES(crypteTripleDES(message)));
        }
    }
}