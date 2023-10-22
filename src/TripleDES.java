import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
//J'étais pas obligé de refaire une classe pour triple DES mais ça changeait quelques méthodes et ça me dérangeait
public class TripleDES extends DES {

    //Attributs
    //Changement ici puisqu'on génère 3 clés au lieu d'une
    int[][] K =new int[3][64];
    //tab_cles devient un tableau de tableau de tableau puisque chaque tableau est le tableau de tableau de K1, K2 ou K3
    int[][][] tab_cles;

    public TripleDES(){
        genereKn();
        tab_cles=new int[3][NB_RONDE][48];
    }

     //Ici aussi ça change pour les mêmes raisons → 3 clés au lieu d'une
     public void genereKn() {
         for (int k = 0; k < 3; k++) {
             Random r = new Random();
             for (int i = 0; i < K[k].length; i++) {
                 int rdm = r.nextInt(2);
                 K[k][i] = rdm;
             }
         }
     }
     //Ou ici !
    public void génèreClé(int n,int nbDES){
        int[] kTemp=K[nbDES-1];
        int[] tempKey=permutation(PC1,kTemp);
        int[][] ensBloc=decoupage(tempKey,28);
        for(int i=0;i< ensBloc.length;i++){
            ensBloc[i]=decalle_gauche(ensBloc[i], TripleDES.TAB_DECALAGE[n]);
        }
        int[] blocPerm=recollage_bloc(ensBloc);
        int[] key=permutation(PC2,blocPerm);
        tab_cles[nbDES-1][n]=key;
//        System.out.println("clé finale K"+nbDES);
//        System.out.println(Arrays.toString(key));
    }
    //Problème ici !
    public int[] fonction_F(int[] unD,int n,int nbDES){
        int[] dPrime=permutation(E,unD);
        int[] dStar=xor(dPrime,tab_cles[nbDES-1][n]);
        int[][] ensdStar=decoupage(dStar,6);
        for(int i=0;i< ensdStar.length;i++){
            ensdStar[i]=fonctions_S(ensdStar[i],n);
        }
        int[] dStarRecolle=recollage_bloc(ensdStar);
        return permutation(P,dStarRecolle);
    }
     //On doit choisir quelle clé on utilise entre K1,K2 et K3 sinon à part ça, même algo qu'avant
    //genere me sert à savoir si je suis en train de decrypter ou non pour choisir si je génère ou non les sous clés
    public int[] crypte(String message_clair,int nbDes,boolean genere)
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
                if (genere) {
                        //Détermination d’une clé Kn
                        génèreClé(j, nbDes);
                }
                //Pour garder Dn
                oldD=D;
                //Dn+1 = Gn Xor F(Kn,Dn)
                D=xor(G,fonction_F(D,j,nbDes));
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
        System.out.println(nbDes+Arrays.deepToString(tab_cles[nbDes-1]));
        return recollage_bloc(decoupeBegin);
    }

    public String decrypte(int[] messageCodé,int nbDes,boolean genere)
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
                if(genere) {
                    //On génère ici une clé même au décryptage
                    génèreClé(j, nbDes);
                }
                //Toujours pour garder Dn
                oldD=D;
                //Dn+1 = Gn
                D=G;
                //Gn+1 = Dn XOR F(Kn, Dn+1)
                G=xor(oldD,fonction_F(D,j,nbDes));
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
        System.out.println(nbDes+Arrays.deepToString(tab_cles[nbDes-1]));
     return bitsToString(recollage_bloc(decoupeCodé));
    }

    public int[] crypteTripleDES(String messageClair){
        return crypte(decrypte(crypte(messageClair,1,true),2,true),3,true);
    }
    public String decrypteTripleDES(int[] messageCodé){
        return decrypte(crypte(decrypte(messageCodé,3,false),2,false),1,false);
    }
}
