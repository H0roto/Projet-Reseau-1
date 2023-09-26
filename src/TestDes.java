import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Random;

public class TestDes {
    public static String[] genereBloc(){
        Random r=new Random();
        String[] bloc=new String[8];
        for(int i=0;i<bloc.length;i++){
            bloc[i]="";
            for(int j=0;j<bloc.length;j++){
                bloc[i]+=r.nextInt(2);
            }
        }
        return bloc;
    }
    public static void main(String[] args) {
        //Initialisation du constructeur
        DES d=new DES();
        ///

        //Test de stringToBits et bitsToString
        String s="On sait depuis longtemps que travailler avec du texte lisible et contenant du sens est source de distractions," +
                " et empêche de se concentrer sur la mise en page elle-même." +
                " L'avantage du Lorem Ipsum sur un texte générique comme 'Du texte. Du texte. Du texte.'" +
                " est qu'il possède une distribution de lettres plus ou moins normale," +
                " et en tout cas comparable avec celle du français standard." +
                " De nombreuses suites logicielles de mise en page" +
                " ou éditeurs de sites Web ont fait du Lorem Ipsum leur faux texte par défaut," +
                " et une recherche pour 'Lorem Ipsum' vous conduira vers de nombreux sites qui n'en sont encore" +
                " qu'à leur phase de construction." +
                " Plusieurs versions sont apparues avec le temps, parfois par accident, souvent intentionnellement" +
                " (histoire d'y rajouter de petits clins d'oeil, voire des phrases embarassantes).";
        //Conservation de l'intégrité du message
        System.out.println("Le message initial est égal au message transformé: "+d.bitsToString(d.stringToBits(s)).equals(s));
        ///

        //Test visuel de genereMasterKey
        d.genereMasterKey();
        System.out.println(Arrays.toString(d.masterkey));
        System.out.println(d.masterkey.length);
        ///

        //Test visuel de genereBloc
        String[] blocTest=genereBloc();
        System.out.println(Arrays.toString(blocTest));
        System.out.println(blocTest.length);
        ///

        //Test de permutation et invPermutation
        System.out.println(Arrays.toString(d.permutation(DES.PERM_INITIALE, blocTest)));

    }
}
