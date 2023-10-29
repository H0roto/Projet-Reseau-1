import java.util.ArrayList;
import java.util.Arrays;
//J'utilise cette fonction pour remplacer la bibliothèque JSON
public class FonctionAuxilliairePasDeMoi {
public static ArrayList<String> splitArray(String input, String openBracket, String closeBracket) {
    ArrayList<String> result = new ArrayList<>();
    int depth = 0;
    StringBuilder current = new StringBuilder();

    for (int i = 0; i < input.length(); i++) {
        if (input.startsWith(openBracket, i)) {
            if (depth == 0) {
                current = new StringBuilder();
            }
            depth++;
        } else if (input.startsWith(closeBracket, i)) {
            depth--;
            if (depth == 0) {
                result.add(current.toString().trim());
            }
        } else if (depth > 0) {
            current.append(input.charAt(i));
        }
    }

    return result;
}






public int[][][] transformToNative3DArray(String input) {
    ArrayList<ArrayList<ArrayList<Integer>>> list3D = new ArrayList<>();

    // Divisez la chaîne en tableaux de niveau 2
    ArrayList<String> level2Arrays = splitArray(input, "[[", "]]");

    for (String level2Array : level2Arrays) {
        ArrayList<ArrayList<Integer>> list2D = new ArrayList<>();

        // Ici, nous utilisons trim pour enlever les crochets supplémentaires
        ArrayList<String> level1Arrays = splitArray(level2Array.trim(), "[", "]");

        for (String level1Array : level1Arrays) {
            ArrayList<Integer> list1D = new ArrayList<>();

            String[] numbers = level1Array.split(",");
            for (String number : numbers) {
                list1D.add(Integer.parseInt(number.trim()));
            }

            list2D.add(list1D);
        }

        list3D.add(list2D);
    }

    // Convertir ArrayList en tableau natif
    int[][][] nativeArray = new int[list3D.size()][][];

    for (int i = 0; i < list3D.size(); i++) {
        nativeArray[i] = new int[list3D.get(i).size()][];

        for (int j = 0; j < list3D.get(i).size(); j++) {
            nativeArray[i][j] = new int[list3D.get(i).get(j).size()];

            for (int k = 0; k < list3D.get(i).get(j).size(); k++) {
                nativeArray[i][j][k] = list3D.get(i).get(j).get(k);
            }
        }
    }

    return nativeArray;
}



public int[][] transformToNative2DArray(String input) {
    ArrayList<ArrayList<Integer>> list2D = new ArrayList<>();

    // Supprimez les crochets externes et divisez la chaîne en tableaux de niveau 1
    String trimmedInput = input.substring(1, input.length() - 1);
    ArrayList<String> level1Arrays = splitArray(trimmedInput, "[", "]");

    for (String level1Array : level1Arrays) {
        ArrayList<Integer> list1D = new ArrayList<>();

        String[] numbers = level1Array.split(",");
        for (String number : numbers) {
            list1D.add(Integer.parseInt(number.trim()));
        }

        list2D.add(list1D);
    }

    // Convertir ArrayList en tableau natif
    int[][] nativeArray = new int[list2D.size()][];

    for (int i = 0; i < list2D.size(); i++) {
        nativeArray[i] = new int[list2D.get(i).size()];

        for (int j = 0; j < list2D.get(i).size(); j++) {
            nativeArray[i][j] = list2D.get(i).get(j);
        }
    }

    return nativeArray;
}





    public static void main(String[] args) {
        FonctionAuxilliairePasDeMoi f=new FonctionAuxilliairePasDeMoi();
        String input = "[[[1,2,3],[4,5,6]],[[7,8,9],[10,11,12]],[[1,2,3],[4,5,6]],[[7,8,9],[10,11,12]]]";
        System.out.println(Arrays.deepToString(f.transformToNative3DArray(input)));
        String saucisse="[[1,2,3],[4,5,6]]";
        System.out.println(Arrays.deepToString(f.transformToNative2DArray(saucisse)));
    }
}
