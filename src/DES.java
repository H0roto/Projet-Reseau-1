import javax.crypto.spec.PSource;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DES {
    //Constantes
    static final int TAILLE_BLOC=64;
    static final int TAILLE_SOUS_BLOC=32;
    static final int NB_RONDE=16;
    static final int TAB_DECALAGE[]={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    static final int PERM_INITIALE[]={57,49,41,33,25,17,9,1,
                                  59,51,43,35,27,19,11,3,
                                  61,53,45,37,29,21,13,5,
                                  63,55,47,39,31,23,15,7,
                                  56,48,40,32,24,16,8,0,
                                  58,50,42,34,26,18,10,2,
                                  60,52,44,36,28,20,12,4,
                                  62,54,46,38,30,22,14,6};

    //Vous avez demandé un tableau de tableau mais je trouvais plus logique de faire un tableau de tableau de tableau
    //notamment pour l'accès direct d'un terme à partir des lignes et des colonnes.
                                //S1
    static final int S[][][]= {{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                                {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                                {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                                {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
                                //S2
                               {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                                {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                                {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                                {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
                                //S3
                               {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                                {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                                {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                                {1, 10, 13, 0, 6, 9, 8, 7, 15, 14, 3, 11, 5, 2, 12, 4}},
                                //S4
                               {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                                {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                                {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                                {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
                                //S5
                               {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                                {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                                {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                                {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
                                //S6
                               {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                                {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                                {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                                {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
                                //S7
                               {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                                {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                                {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                                {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
                                //S8
                               {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                                {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                                {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                                {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};
    static final int E[]={31,0,1,2,3,4,
                          3,4,5,6,7,8,
                          7,8,9,10,11,12,
                          11,12,13,14,15,16,
                          15,16,17,18,19,20,
                          19,20,21,22,23,24,
                          23,24,25,26,27,28,
                          27,28,29,30,31,0};
    static final int[] PC1={56,48,40,32,24,16,8,
                            0,57,49,41,33,25,17,
                            9,1,58,50,42,34,26,
                            18,10,2,59,51,43,35,
                            62,54,46,38,30,22,14,
                            6,61,53,45,37,29,21,
                            13,5,60,52,44,36,28,
                            20,12,4,27,19,11,3};


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

    ///

    //Attributs
    int[] masterkey =new int[64];
    int[][] tab_cles;
    ///

    //Constructeur de la Classe DES
    //génère la masterkey et initialise le tableau des clés
    public DES(){
        genereMasterKey();
        tab_cles=new int[NB_RONDE][48];
    }


    //Méthode qui transforme le String passé en entrée en un tableau d'entiers
    public int[] stringToBits(String message){
        //transformation de la chaîne en un tableau de caractères
        char[] tabChar=message.toCharArray();
        //Utilisation d'un StringBuilder pour éviter le réadressage de sBit
        StringBuilder sBit= new StringBuilder();
        for(int i=0;i<message.length();i++){
                int num= tabChar[i];
                String so=Integer.toBinaryString(num);
                //Je veux des octets donc je demande à remplir de 0 ce qui reste pour arriver à 8
                sBit.append(String.format("%08d", Integer.parseInt(so)));
        }
                int[] tabBit=new int[sBit.length()];
        for(int j=0;j<sBit.length();j++){
            //pour transformer les caractères en entiers
            tabBit[j]=Character.getNumericValue(sBit.charAt(j));
        }
        return tabBit;
    }

    //Méthode complémentaire à stringToBits qui transforme un tableau d'entiers passé en paramètre en String
    public String bitsToString(int[] blocs){
        ArrayList<Character> listChar= new ArrayList<>();
        StringBuilder tempoSBits= new StringBuilder();
        for(int i=0;i<blocs.length;i++){
            tempoSBits.append(blocs[i]);
            // Si nous avons 8 bits ou si nous sommes à la fin du tableau 'blocs'
            if (tempoSBits.length()==8 || i== blocs.length-1){
                //Si le caractère devant être encodé est nul -> bourrage donc inutile à transmettre
                if(!tempoSBits.toString().equals("00000000")) {
                    int ascii = (Integer.parseInt(tempoSBits.toString(), 2));
                    listChar.add((char) ascii);
                }
                tempoSBits = new StringBuilder();
            }
        }
        StringBuilder exit = new StringBuilder();
        //On ajoute enfin tous les caractères qu'on vient de former pour retrouver la chaîne initiale
        for (char c:listChar
             ) {
            exit.append(c);
        }
        return exit.toString();
     }
     //Méthode qui génère la MasterKey
     public void genereMasterKey() {
         Random r = new Random();
         for (int i = 0; i < masterkey.length; i++) {
             int rdm = r.nextInt(2);
             masterkey[i] = rdm;
         }
     }

     //Méthode qui permute un bloc grâce à une table de permutation
    public int[] permutation(int[] tab_permutation,int[] bloc){
        //Je traite le cas où aucune permutation n'est appliquée
        //Et le cas où le bloc est vide
        if (tab_permutation.length==0 || bloc.length==0){
            return bloc;
        }

        int[] tempo=new int[tab_permutation.length];
        for(int i=0;i<tab_permutation.length;i++){
            tempo[i]=bloc[tab_permutation[i]];
        }
        return tempo;
         }

     //Méthode complémentaire à permutation qui réalise la permutation inverse d'une table de permutation, à un bloc.
    public int[] invPermutation(int[] tab_permutation,int[] bloc){
        int[] tempo=new int[tab_permutation.length];
        for(int i=0;i<tab_permutation.length;i++){
            tempo[tab_permutation[i]]=bloc[i];
        }
        return tempo;
    }

    //Méthode qui découpe un bloc en sous blocs d'une taille prédéfinie
    // -> pour ne pas avoir de problèmes de taille, on fait du bourrage
    // (qui se fait par défaut avec des tableaux donc on bourre avec des 0)
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
            }
        }
        return tabTempo;
    }

    //Méthode complémentaire à decoupage qui recolle les blocs précédemment découpés
    public int[] recollage_bloc(int[][] blocs){
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

    //Méthode qui permet de décaller tous les éléments d'une liste vers la gauche, d'un certain cran
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
    //Méthode qui réalise le xor entre 2 tableaux de même taille
    public int[] xor(int[] tab1,int[] tab2){
        int[] exit=new int[tab1.length];
        for(int i=0;i<tab1.length;i++){
            //J'utilise le xor intégré de java pour éviter les ifs inutiles
            exit[i]=tab1[i]^tab2[i];
        }
        return exit;
    }

    //Méthode qui génère une clé et la place dans tab_cles: application à la lettre de l'algorithme du diapo (diapo 28)
    public void génèreClé(int n){
        //Permutation masterkey
        int[] tempKey=permutation(PC1,masterkey);
        //Decoupage en 2 blocs de 28 bits
        int[][] ensBloc=decoupage(tempKey,28);
        //Décallage à gauche de tous les blocs
        for(int i=0;i< ensBloc.length;i++){
            ensBloc[i]=decalle_gauche(ensBloc[i],DES.TAB_DECALAGE[n]);
        }
        //Recollage des blocs
        int[] blocPerm=recollage_bloc(ensBloc);
        //Nouvelle permutation avec PC2
        int[] key=permutation(PC2,blocPerm);
        System.out.println("clé finale");
        System.out.println(Arrays.toString(key));
        tab_cles[n]=key;
    }

    //Méthode qui transforme un tableau en un autre tableau aui représente une donnée sous forme binaire
    //d'un tableau Sn dont on connait l'indice.
    //En résumé, on applique la fonction S du diaporama ^^' (diapo 31)
    public int[] fonctions_S(int[] tab,int n){
        //Les bits 1 et 6 permettent de trouver la ligne
        int ligne=tab[0]*2+tab[5];
        //Les bits 2,3,4,5 servent à trouver la colonne
        int colonne=tab[1]*8+tab[2]*4+tab[3]*2+tab[4];
        //n%8 puisqu'on n'a que 8 tableaux de S
        //On trouve une valeur dans S à l'aide de la ligne et de la colonne précédemment calculée
        int res=DES.S[n%8][ligne][colonne];
        String sRes=Integer.toBinaryString(res);
        //Petite technique pas forcément optimale qui permet de toujours avoir un tableau de taille 4
        //Sinon j'avais des tableaux de taille variable ce qui n'est pas folichon
        int[] tabBit=new int[sRes.length()];
        for(int i=0;i<tabBit.length;i++){
            tabBit[i]=Character.getNumericValue(sRes.charAt(i));
        }
        int[] exit=new int[4];
        for (int i = 0, j = 4-tabBit.length; j<exit.length && i< tabBit.length; i++,j++) {
            exit[j]=tabBit[i];
        }
        return exit;
    }

    //Méthode qui réalise la fonction F du diapo (diapo 31)
    public int[] fonction_F(int[] unD,int n){
        //Extension de Dn en un bloc D'n grâce à E
        int[] dPrime=permutation(DES.E,unD);
        //Xor entre D'n et Kn
        int[] dStar=xor(dPrime,tab_cles[n]);
        //Découpage des blocs dstar en bloc de 8 bits
        int[][] ensdStar=decoupage(dStar,6);
        //Fonction S pour chacun de ces blocs
        for(int i=0;i< ensdStar.length;i++){
            ensdStar[i]=fonctions_S(ensdStar[i],n);
        }
        //Recollage des blocs
        int[] dStarRecolle=recollage_bloc(ensdStar);
        //Application de la permutation P
        return permutation(P,dStarRecolle);
    }

    //Méthode qui crypte un message passé en paramètre en suivant l'algorithme DES expliqué sur le diapo (diapo 25)
    public int[] crypte(String message_clair)
    {
        int[] begin=stringToBits(message_clair);
        // Le texte est fractionné en blocs de 64 bits.
        int[][] decoupeBegin=decoupage(begin,TAILLE_BLOC);
        //Pour chaque bloc on fait :
        for (int i=0;i<decoupeBegin.length;i++) {
            //une permutation initiale
            int[] blocPerm=permutation(PERM_INITIALE,decoupeBegin[i]);
            //un découpage en deux parties G0 et D0 de 32 bits
            int[][] sousBlocPerm=decoupage(blocPerm,TAILLE_SOUS_BLOC);
            int[] D=sousBlocPerm[1];
            int[] G=sousBlocPerm[0];
            int[] oldD;
            //pour chaque paire (Dn,Gn) faire NB_RONDE fois :
            for(int j=0;j<NB_RONDE;j++){
                //Détermination d’une clé Kn
                génèreClé(j);
                //Pour garder Dn
                oldD=D;
                //Dn+1 = Gn Xor F(Kn,Dn)
                D=xor(G,fonction_F(D,j));
                //Gn+1 = Dn
                G=oldD;
            }
            int[][] finRonde=new int [sousBlocPerm.length][2];
            finRonde[0]=G;
            finRonde[1]=D;
            // les deux parties G15 et D15 sont recollées + permutation inverse de la première
            int[] end=invPermutation(PERM_INITIALE,recollage_bloc(finRonde));
            decoupeBegin[i]=end;
            //System.out.println(Arrays.toString(recollage_bloc(decoupeBegin)));
        }
        return recollage_bloc(decoupeBegin);
    }
    //Méthode qui décrypte un message à l'aide d'un tableau d'entier
    public String decrypte(int[] messageCodé)
    {
        // Le texte est fractionné en blocs de 64 bits.
        int[][] decoupeCodé=decoupage(messageCodé,TAILLE_BLOC);
        //Pour chaque bloc on fait :
        for(int i=0;i<decoupeCodé.length;i++){
            //une permutation initiale
            int[] blocPerm=permutation(PERM_INITIALE,decoupeCodé[i]);
            //un découpage en deux parties G0 et D0 de 32 bits
            int[][] sousBlocPerm=decoupage(blocPerm,TAILLE_SOUS_BLOC);
            int[] D=sousBlocPerm[1];
            int[] G=sousBlocPerm[0];
            int[] oldD;
            //pour chaque paire (Dn,Gn) faire NB_RONDE fois (en partant de 16 pour aller à 0)
            for (int j=NB_RONDE-1;j>=0;j--){
                //Toujours pour garder Dn
                oldD=D;
                //Dn+1 = Gn
                D=G;
                //Gn+1 = Dn XOR F(Kn, Dn+1)
                G=xor(oldD,fonction_F(D,j));
            }
            int[][] finRonde=new int[sousBlocPerm.length][2];
            finRonde[0]=G;
            finRonde[1]=D;
            //// les deux parties G0 et D0 sont recollées + permutation inverse de la première
            int[] end=invPermutation(PERM_INITIALE,recollage_bloc(finRonde));
            decoupeCodé[i]=end;
            //System.out.println(Arrays.toString(recollage_bloc(decoupeCodé)));
        }
        //On transforme le tableau obtenu en un texte lisible
     return bitsToString(recollage_bloc(decoupeCodé));
    }
}
