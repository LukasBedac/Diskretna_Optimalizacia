import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BonusovaUloha {

    private int ucelovaF;
    private int k_hmotnost;
    private int r_pocet;
    private ArrayList<Integer> Z;
    private ArrayList<Float> a;
    private ArrayList<Float> c;

    public BonusovaUloha(int ucelovaF, int k, int r, ArrayList<Integer> Z, ArrayList<Float> a, ArrayList<Float> c) {
        this.ucelovaF = ucelovaF;
        this.k_hmotnost = k;
        this.r_pocet = r;
        this.Z = Z;
        this.a = a;
        this.c = c;
        this.rozdelenieZ();


    }
    public void rozdelenieZ() {
        ArrayList<Float> nevlozenePredmety = new ArrayList<>();
        ArrayList<Float> vlozenePredmety = new ArrayList<>();
        for (int i = 0; i < this.Z.size(); i++) {
             if (this.Z.get(i) == 1) {
                 vlozenePredmety.add(this.c.get(i));
             } else {
                 nevlozenePredmety.add(this.c.get(i));
             }
        }
        //System.out.println(vlozenePredmety);
        //System.out.println(nevlozenePredmety);
        this.vymenaKoeficientov(vlozenePredmety, nevlozenePredmety);
    }
    public void vymenaKoeficientov(ArrayList<Float> vlozene, ArrayList<Float> nevlozene) {
        int staraUcelovaF = this.ucelovaF;
        int staraHmotnost = this.k_hmotnost;
            for (int i = 0; i < nevlozene.size(); i++) {
                if (nevlozene.get(i) > vlozene.get(i)) {
                    if (this.k_hmotnost + this.a.get(i) - this.a.get(i + nevlozene.size()) > 11000) {
                        break;
                    }
                    this.Z.set(i, 1);
                    this.Z.set((i) + vlozene.size(), 0);
                    this.k_hmotnost += this.a.get(i);
                    this.k_hmotnost -= this.a.get(i + nevlozene.size());
                    this.ucelovaF += this.c.get(i);
                    this.ucelovaF -= this.c.get(i + nevlozene.size());
                    System.out.println("Vymiename predmety s cenami: " + nevlozene.get(i) + " a: " + vlozene.get(i));
                    System.out.println("Hmotnost batohu po vymeneni predmetov: " + this.k_hmotnost);
                    System.out.println("Ucelova funkcia batohu po vymeneni: " + this.ucelovaF);
                    System.out.println("");
                }
        }
        System.out.println();
        System.out.println("Hmotnost batohu po vymeneni predmetov: " + this.k_hmotnost);
        System.out.println("Ucelova funkcia sa zmenila na: " + this.ucelovaF);
        if (staraUcelovaF < this.ucelovaF) {
            System.out.println("Ucelova funkcia sa zlepsila o: " + (this.ucelovaF - staraUcelovaF));
            if (staraHmotnost < this.k_hmotnost) {
                System.out.println("Hmotnost sa zvysila o: " + (this.k_hmotnost - staraHmotnost));
            } else {
                System.out.println("Hmotnost sa zmensila o: " + (staraHmotnost - this.k_hmotnost));
            }
        } else {
            System.out.println("Ucelova funkcia sa zhorsila o: " + (staraUcelovaF - this.ucelovaF));
            if (staraHmotnost < this.k_hmotnost) {
                System.out.println("Hmotnost sa zvysila o: " + (this.k_hmotnost - staraHmotnost));
            } else {
                System.out.println("Hmotnost sa zmensila o: " + (staraHmotnost - this.k_hmotnost));
            }
        }
        try {
            this.zapisDoSuboruBonus(this.k_hmotnost, this.ucelovaF, this.Z);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void zapisDoSuboruBonus(int kapacita, int ucelova, ArrayList<Integer> vlozenePredmety) throws FileNotFoundException {
        File vyhodnotenie = new File("BonusVyhodnotenie.txt");
        var writer = new PrintWriter(vyhodnotenie);
        writer.println("Vyhodnotenie bonusovej heuristky za pouzitia kriteria: vyber predmet z doposial nevlozenych" +
                " predmetov a vloz ho do batohu miesto prvej " +
                "mensej najdenej ceny, sa ucelova funkcia a kapacita zmenila nasledovne: ");
        writer.println("Ucelova funkcia: " + ucelova);
        writer.println("Hmotnost batohu: " + kapacita);
        writer.println("Predmety v batohu ostavaju na pocte: " + this.r_pocet);
        writer.print("Vlozene predmety poporade: " + vlozenePredmety);
        writer.close();
    }
}
