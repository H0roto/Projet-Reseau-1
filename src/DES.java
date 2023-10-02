import java.util.ArrayList;
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
    static final int E[]={31,0,1,2,3,4,
                          3,4,5,6,7,8,
                          7,8,9,10,11,12,
                          11,12,13,14,15,16,
                          15,16,17,18,19,20,
                          19,20,21,22,23,24,
                          23,24,25,26,27,28,
                          27,28,29,30,31,0};
    static final int[] PC1={56,48,40,32,24,16,8,62,54,46,38,30,22,14,
                            0,57,49,41,33,25,17,6,61,53,45,37,29,21,
                            9,1,58,50,42,34,26,13,5,60,52,44,36,28,
                            18,10,2,59,51,43,35,20,12,4,27,19,11,3};
    static final int[] PC2 = {13, 16, 10, 23, 0, 4,
                              2, 27, 14, 5, 20, 9,
                             22, 18, 11, 3, 25, 7,
                             15, 6, 26, 19, 12, 1,
                             40, 51, 30, 36, 46, 54,
                             29, 39, 50, 44, 32, 47,
                             43, 48, 38, 55, 33, 52,
                             45, 41, 49, 35, 28, 31};

    static final int[] P = {
            5, 6, 19, 20, 28, 11, 27, 16,
            0, 14, 22, 25, 4, 17, 30, 9,
            1, 7, 23, 13, 31, 26, 2, 8,
            18, 12, 29, 5, 21, 10, 3, 24
        };
    int[] masterkey =new int[64];
    int[][] tab_cles;

    public DES(){
        genereMasterKey();
        tab_cles=new int[NB_RONDE][48];
    }
    public int[] stringToBits(String message){
        char[] tabChar=message.toCharArray();
        StringBuilder sBit= new StringBuilder();
        for(int i=0;i<message.length();i++){
                int num= tabChar[i];
                String so=Integer.toBinaryString(num);
                //Je veux des octets donc je demande à remplir de 0 ce qui reste pour arriver à 8
                sBit.append(String.format("%08d", Integer.parseInt(so)));
        }
                int[] tabBit=new int[sBit.length()];
        for(int j=0;j<sBit.length();j++){
            tabBit[j]=Character.getNumericValue(sBit.charAt(j));
        }
        return tabBit;
    }

    public String bitsToString(int[] blocs){
        ArrayList<Character> listChar= new ArrayList<>();
        StringBuilder tempoSBits= new StringBuilder();
        for(int i=0;i<blocs.length;i++){
            tempoSBits.append(blocs[i]);
            // Si nous avons 8 bits ou si nous sommes à la fin du tableau 'blocs'
            if (tempoSBits.length()==8 || i== blocs.length-1){
                int ascii=(Integer.parseInt(tempoSBits.toString(),2));
                listChar.add((char)ascii);
                tempoSBits = new StringBuilder();
            }
        }
        StringBuilder exit = new StringBuilder();
        for (char c:listChar
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
    public int[] permutation(int[] tab_permutation,int[] bloc){
        int[] tempo=new int[tab_permutation.length];
        for(int i=0;i<tab_permutation.length;i++){
            tempo[i]=bloc[tab_permutation[i]];
        }
        return tempo;
         }
    public int[] invPermutation(int[] tab_permutation,int[] bloc){
        int[] tempo=new int[TAILLE_BLOC];
        for(int i=0;i<tab_permutation.length;i++){
            tempo[tab_permutation[i]]=bloc[i];
        }
        return tempo;
    }
    public int[][] decoupage(int[] bloc,int tailleBlocs){
        //je fais ça pour avoir assez de blocs pour stocker tous les éléments du tableau bloc
        //Si je ne fais pas ça, la division entière risque de ne pas donner assez de blocs pour stocker le nombre d'éléments voulus
        int tailleTab=(bloc.length+tailleBlocs-1)/tailleBlocs;
        int[][] tabTempo=new int[tailleTab][tailleBlocs];
        for(int i=0;i<tailleTab;i++){
            for (int j=0;j<tailleBlocs;j++){
                int index=i*tailleBlocs+j;
                if(index<bloc.length){
                    tabTempo[i][j]=bloc[index];
                }
//                else{
//                    tabTempo[i][j]=2;
//                }
            }
        }
        return tabTempo;
    }
    public int[] recollage_bloc(int[][] blocs){
        System.out.println(blocs.length);
        int taille=blocs[0].length;
        int[] exit=new int[taille* blocs.length];
         for(int i=0;i< blocs.length;i++) {
                for (int j = 0; j < taille; j++) {
                    int index= i * taille + j;
                    exit[index]=blocs[i][j];
                }
            }
         return exit;
    }
    public int[] decalle_gauche(int[] bloc,int nbCran){
        int n=bloc.length;
        //tableau temporaire pour les éléments tout à gauche
        int[] tempoTab=new int[nbCran];
        //stockage de ces éléments dans le tableau temporaire
        for(int i=0;i<nbCran;i++){
            tempoTab[i]=bloc[i];
        }
        //traitement des autres entiers
        for(int i=0;i<n-nbCran;i++){
            bloc[i]=bloc[i+nbCran];
        }

        //traitement des éléments du tableau temporaire
        //Je ne savais pas qu'on pouvait initialiser plusieurs variables dans une simple boucle for.
        //Ce fut bien pratique dans ce cas puisque j'avais réellement besoin de ces 2 variables
        for(int i=n-nbCran,j=0;i<n;i++,j++){
            bloc[i]=tempoTab[j];
        }
        return bloc;
    }
    public int[] xor(int[] tab1,int[] tab2){
        int[] exit=new int[tab1.length];
        for(int i=0;i<tab1.length;i++){
            exit[i]=tab1[i]^tab2[i];
        }
        return exit;
    }
    public void génèreClé(int n){
        System.out.println("MASTER KEY:");
        System.out.println(Arrays.toString(masterkey));
        int[] tempKey=permutation(PC1,masterkey);
        int[][] ensBloc=decoupage(tempKey,28);
        System.out.println("Ens Bloc perm");
        System.out.println(Arrays.deepToString(ensBloc));
        for(int i=0;i< ensBloc.length;i++){
            ensBloc[i]=decalle_gauche(ensBloc[i],DES.TAB_DECALAGE[n]);
        }
        System.out.println("Ens Bloc perm+ decalle:");
        System.out.println(Arrays.deepToString(ensBloc));
        int[] blocPerm=recollage_bloc(ensBloc);
        System.out.println("Recollage bloc:");
        System.out.println(Arrays.toString(blocPerm));
        int[] key=permutation(PC2,blocPerm);
        tab_cles[n]=key;
        System.out.println("clé finale");
        System.out.println(Arrays.toString(key));



    }
    public int[] fonctions_S(int[] tab){
        int ligne=tab[0]*2+tab[5];
        int colonne=tab[1]*8+tab[2]*4+tab[3]*2+tab[4];
        int res=DES.S[ligne][colonne];
        String sRes=Integer.toBinaryString(res);
        int[] tabBit=new int[sRes.length()];
        for(int i=0;i<tabBit.length;i++){
            tabBit[i]=Character.getNumericValue(sRes.charAt(i));
        }
        return tabBit;
    }
    public int[] fonction_F(int[] unD,int n){
        int[] dPrime=permutation(DES.E,unD);
        int[] dStar=xor(dPrime,tab_cles[n]);
        int[][] ensdStar=decoupage(dStar,6);
        for(int i=0;i< ensdStar.length;i++){
            ensdStar[i]=fonctions_S(ensdStar[i]);
        }
        int[] dStarRecolle=recollage_bloc(ensdStar);
        return permutation(P,dStarRecolle);
    }
    public int[] crypte(String message_clair)
    {
        int[] begin=stringToBits(message_clair);
        int[][] decoupeBegin=decoupage(begin,TAILLE_BLOC);
        for (int i=0;i<decoupeBegin.length;i++) {
            int[] blocPerm=permutation(PERM_INITIALE,decoupeBegin[i]);
            int[][] sousBlocPerm=decoupage(blocPerm,TAILLE_SOUS_BLOC);
            int[] D=sousBlocPerm[1];
            int[] G=sousBlocPerm[0];
            int[] oldD=D;
            for(int j=0;j<16;j++){
                génèreClé(j);
                oldD=D;
                D=xor(G,fonction_F(D,j));
                G=oldD;
            }
            int[][] finRonde=new int [sousBlocPerm.length][2];
            int[] end=invPermutation(PERM_INITIALE,recollage_bloc(finRonde));
            decoupeBegin[i]=end;
        }
        return recollage_bloc(decoupeBegin);
    }

    public String decrypte(int[] messageCodé){
     return "hello";
    }
}
