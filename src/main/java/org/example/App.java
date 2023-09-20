package org.example;

import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Locale.setDefault(Locale.forLanguageTag("sv-SE"));
        Scanner scanner = new Scanner(System.in).useLocale(Locale.forLanguageTag("sv-SE"));
        String val;
        int[] elPris = new int[24];
        do {
            System.out.println("""
                    Elpriser
                    ========
                    1. Inmatning
                    2. Min, Max och Medel
                    3. Sortera
                    4. Bästa Laddningstid (4h)
                    5. Visualisering
                    e. Avsluta
                    """);
            val = scanner.nextLine().toLowerCase();
            switch (val) {
                case "1":
                    fyllaElprisArraye(elPris, scanner);
                    break;
                case "2":
                    int min = minstelPris(elPris);
                    int position = position(elPris, min);
                    int minToÖre = (int) min;
                    System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh\n", position, (position + 1), minToÖre);
                    float max = maxelPris(elPris);
                    int position1 = position(elPris, max);
                    int maxToÖre = (int) max;
                    System.out.printf("Högsta pris: %02d-%02d, %d öre/kWh\n", position1, (position1 + 1), maxToÖre);
                    medelPris(elPris);
                    break;
                case "3":
                    int[] tid = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
                    sorteraElpriser(elPris, tid);
                    for (int i = 0; i < elPris.length; i++) {
                        System.out.printf("%02d-%02d %d öre\n", tid[i], (tid[i] + 1),(int) elPris[i]);
                    }
                    break;
                case "4":
                    laddningsTid(elPris);
                    break;
                case "5":
                    double rang = (maxelPris(elPris) - minstelPris(elPris)) / 5.0;
                    int maxInput = (int) (maxelPris(elPris) );
                    int minInput = (int) (minstelPris(elPris));
                    diagram(elPris, rang, 6, maxInput,minInput);
                    for (int i = 5; i > 1; i--) {
                        diagram1(elPris, rang, i, maxInput,minInput);
                    }
                    diagram(elPris, rang, 1, minInput,minInput);
                    System.out.printf("   |------------------------------------------------------------------------\n");
                    System.out.printf("   | 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n");
                    break;
                case "e":
                    System.out.println("Programmet avslutar");
                    return;
                default:
            }
        }
        while (val != "e");
    }

    private static void fyllaElprisArraye(int[] elPris, Scanner scanner) {
        for (int i = 0; i < 24; i++) {
            elPris[i] = scanner.nextInt();
        }
        scanner.nextLine();
    }

    public static int minstelPris(int[] elPris) {
        int minstelPris = elPris[0];
        for (int i = 0; i < 24; i++) {
            if (elPris[i] < minstelPris)
                minstelPris = elPris[i];
        }
        return minstelPris;
    }

    public static int position(int[] elPris, float pris) {
        int position = 0;
        for (int j = 0; j < 24; j++) {
            if (elPris[j] == pris) position = j;
        }
        return position;
    }

    public static float maxelPris(int[] elPris) {
        int maxelPris = elPris[0];
        for (int i = 0; i < 24; i++) {
            if (elPris[i] > maxelPris)
                maxelPris = elPris[i];
        }
        return maxelPris;
    }

    public static void medelPris(int[] elPris) {
        float summa = 0;
        for (int i = 0; i < 24; i++) {
            summa = elPris[i] + summa;
        }
        float medelPris = (float) (summa/ 24.0);
        System.out.printf("Medelpris: %.2f öre/kWh\n", medelPris);
    }

    public static void sorteraElpriser(int[] elPriser, int[] tid) {
        for (int i = 23; i >=0; i--) {
            for (int j = 23; j >=0; j--) {
                if (elPriser[i] < elPriser[j]) {
                    int steg = elPriser[i];
                    elPriser[i] = elPriser[j];
                    elPriser[j] = steg;
                    int tider = tid[i];
                    tid[i] = tid[j];
                    tid[j] = tider;
                }
            }
        }
    }

    public static void laddningsTid(int[] el) {
        int position = 0;
        float medelminst = 0;
        int minstatal = Integer.MAX_VALUE;
        for (int i = 0; i < 21; i++) {
            int summa = el[i] + el[i + 1] + el[i + 2] + el[i + 3];
            if (summa < minstatal) {
                minstatal = summa;
                position = i;
            }
        }
        int summa1 = el[21] + el[22] + el[23] + el[0];
        int summa2 = el[22] + el[23] + el[0] + el[1];
        int summa3 = el[23] + el[0] + el[1] + el[2];
        int min = Math.min(Math.min(summa1, minstatal), Math.min(summa2, summa3));
        if (min == summa1) {
            minstatal = summa1;
            position = 21;
        }
        if (min == summa2) {
            minstatal = summa2;
            position = 22;
        }
        if (min == summa3) {
            minstatal = summa3;
            position = 23;
        }
        medelminst = (float) (minstatal / 4.0);
        System.out.printf("Påbörja laddning klockan %02d\nMedelpris 4h: %.1f öre/kWh\n", position, medelminst);
    }

    public static void diagram(int[] arraye, double rang, int counter, int input,float min) {
        String[] c = new String[24];
        for (int i = 0; i < 24; i++) {
            if (counter == 1) c[i] = "x";
            else if (arraye[i] <= input && arraye[i] >= ((int)((counter - 1) * rang))+ min) c[i] = "x";
            else c[i] = " ";
        }
        if (input<100&&input>0) {
            System.out.printf(" %d|  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s\n", input, c[0], c[1], c[2],
                    c[3], c[4], c[5], c[6], c[7], c[8], c[9], c[10], c[11], c[12], c[13], c[14], c[15], c[16], c[17], c[18], c[19], c[20], c[21], c[22], c[23]);
        }
        else {
            System.out.printf("%d|  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s\n", input, c[0], c[1], c[2],
                    c[3], c[4], c[5], c[6], c[7], c[8], c[9], c[10], c[11], c[12], c[13], c[14], c[15], c[16], c[17], c[18], c[19], c[20], c[21], c[22], c[23]);
        }
    }


    public static void diagram1(int[] arraye, double rang, int counter, int input,float min) {
        String[] c = new String[24];
        for (int i = 0; i < 24; i++) {
            if (counter == 1) c[i] = "x";
            else if (arraye[i] <= input && arraye[i] >= (int)((double)(counter - 1) * rang)+ min) c[i] = "x";
            else c[i] = " ";
        }
        System.out.printf("   |  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s  %s\n", c[0], c[1], c[2],
                c[3], c[4], c[5], c[6], c[7], c[8], c[9], c[10], c[11], c[12], c[13], c[14], c[15], c[16], c[17], c[18], c[19], c[20], c[21], c[22], c[23]);
    }
}
