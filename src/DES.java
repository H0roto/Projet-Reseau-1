import java.util.Arrays;
import java.util.Random;

public class DES {
    static final int TAILLE_BLOC=64;
    static final int TAILLE_SOUS_BLOC=32;
    static final int NB_RONDE=1;
    static final int TAB_DECALAGE[]={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    static final int PERM_INITIALE[]={57,49,41,33,25,17,9,1,
                                  59,51,43,35,27,19,11,3,
                                  61,53,45,37,29,21,13,5,
                                  63,55,47,39,31,23,15,7,
                                  56,48,40,32,24,16,8,0,
                                  58,50,42,34,26,18,10,2,
                                  60,52,44,36,28,20,12,4,
                                  62,54,46,38,30,22,14,6};
    static final int S[][]= {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
                       {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
                       {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
                       {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}};
    static final int E[][]={{31,0,1,2,3,4},
                      {3,4,5,6,7,8},
                      {7,8,9,10,11,12},
                      {11,12,13,14,15,16},
                      {15,16,17,18,19,20},
                      {19,20,21,22,23,24},
                      {23,24,25,26,27,28},
                      {27,28,29,30,31,0}};
    int[] masterkey =new int[64];
    int[] tab_cles;

    public DES(){
        genereMasterKey();
        tab_cles=new int[64]; //TODO JSP bien la taille de tab_cles
    }

    public int[] crypte(String message_clair){
        return new int[1];
    }

    public String decrypte(int[] messageCodé){
     return "hello";
    }
    public String[] stringToBits(String message){
        char[] tabChar=message.toCharArray();
        String[] tabBit=new String[tabChar.length];
        for(int i=0;i<tabBit.length;i++){
                int num= tabChar[i];
                String so=Integer.toBinaryString(num);
                tabBit[i]= so;
        }
        return tabBit;
    }

    public String bitsToString(String[] blocs){ //TODO Changer le String[] en int mais pas encore x)
        char[] tabChar=new char[blocs.length];
        for(int i=0;i<tabChar.length;i++){
            int number=Integer.parseInt(blocs[i],2);
            tabChar[i]=(char) number;
        }
        StringBuilder exit = new StringBuilder();
        for (char c:tabChar
             ) {
            exit.append(c);
        }
        return exit.toString();
     }
     public void genereMasterKey() {
         Random r = new Random();
         for (int i = 0; i < masterkey.length; i++) {
             int rdm = r.nextInt(2);
             masterkey[i] = rdm;
         }
     }
    public String[] permutation(int[] tab_permutation,String[] bloc){
        StringBuilder sbloc = new StringBuilder();
        for(int i=0;i<TAILLE_BLOC;i++){
         sbloc.append(bloc[i]);
        }

            return tempo;
         }
    public String[] invPermutation(int[] tab_permutation,String[] bloc){
        String[] tempo=new String[64];
        for(int i=0;i<bloc.length;i++){
            tempo[i]=bloc[tab_permutation[i]];
        }
        return tempo;
    }
    public int[][] decoupage(int[] bloc,int nbBlocs){
        return new int[3][2];
    }
    public int[] recollage_bloc(int[][] blocs){
        return new int[2];
    }
    public void génèreClé(int n){

    }
    public int[] decalle_gauche(int[] bloc,int nbCran){
        return new int[2];
    }
    public int[] xor(int[] tab1,int[] tab2){
        return new int[2];
    }
    public int[] fonctions_S(int[] tab){
        return new  int[2];
    }
    public int[] fonction_F(int[] uneCle,int[] unD){
        return new int[3];
    }
}
