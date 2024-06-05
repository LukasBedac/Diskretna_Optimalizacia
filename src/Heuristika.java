import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Heuristika {
    private Scanner scanner;
    private BonusovaUloha vymennaHeuristika;
    private ArrayList<Float> a; //hmotnost
    private ArrayList<Float> c; //cena
    private final static int POCET_PREDMETOV = 500;

    public Heuristika() {
        this.a = new ArrayList<>();
        this.c = new ArrayList<>();
        this.nacitanieZoSuboru();
        this.usporiadaj();
        this.vypocet();
    }

    private void vypocet() {
        var Z = new ArrayList<Integer>();
        var ucelovaF = 0;
        var K_hmotnost = 0; //hmotnost
        var r_pocet = 0; //predmety v batohu
        for (int i = 0; i < POCET_PREDMETOV; i++) {
            Z.add(1);
            K_hmotnost += this.a.get(i);
            r_pocet += 1;
            ucelovaF += (int)(this.c.get(i) * Z.get(i));
        }

        int i = 0;
        while (K_hmotnost > 11000 || r_pocet > 300) {
            r_pocet -= 1;
            K_hmotnost -= this.a.get(i);
            Z.set(i, 0);
            ucelovaF -= (this.c.get(i).intValue());
            i++;
            System.out.println("Pocet predmetov v batohu je: " + r_pocet);
            System.out.println("Hmotnost batohu s poctom predmetov " + r_pocet + " je: " + K_hmotnost);
            System.out.println("");
        }
        System.out.println();
        System.out.println("Ucelova funkcia: " + ucelovaF);
        System.out.println("Celkova hmotnost batohu po splneni podmienok je: " + K_hmotnost);
        System.out.println("Celkovy pocet predmetov v batohu je: " + r_pocet);
        System.out.println("Ulohu o batohu mozeme povazovat za pripustne riesenie s poctom predmetov v batohu "
                + r_pocet + " a s celkovou hmotnostou: " + K_hmotnost);
        try {
            this.vypisDoSuboru(ucelovaF, K_hmotnost, r_pocet, Z);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.vymennaHeuristika = new BonusovaUloha(ucelovaF, K_hmotnost, r_pocet, Z, this.a, this.c);
    }


    private void nacitanieZoSuboru() {
        try (var subor = ClassLoader.getSystemResourceAsStream("H3_a.txt")){
            if (subor != null) {
                this.scanner = new Scanner(subor);
            }
            if (this.scanner != null) {
                while (this.scanner.hasNext()) {
                    this.a.add(this.scanner.nextFloat());
                }
                try (var subor2 = ClassLoader.getSystemResourceAsStream("H3_c.txt")) {
                    if (subor2 != null) {
                        this.scanner = new Scanner(subor2);
                    }
                    while (this.scanner.hasNext()) {
                        this.c.add(this.scanner.nextFloat());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void usporiadaj() {
        for (int i = 0; i < this.a.size(); i++) {
            for (int j = i + 1; j < this.a.size(); j++) {
                if (this.a.get(i) < this.a.get(j)) {
                    var tempC = this.c.get(i);
                    this.c.set(i, this.c.get(j));
                    this.c.set(j, tempC);
                    var tempA = this.a.get(i);
                    this.a.set(i, this.a.get(j));
                    this.a.set(j, tempA);
                }
            }
        }
    }
    private void vypisDoSuboru(int uF, int hmotnost, int predmeetyVBatohu, ArrayList<Integer> vlozenePredmety) throws FileNotFoundException {
        File vyhodnotenie = new File("vyhodnotenie.txt");
            var writer = new PrintWriter(vyhodnotenie);
        writer.println("Vysledok heuristiky po usporiadani predmetov vopred v aplikacii je nasledovny:");
            writer.println("Ucelova funkcia: " + uF);
            writer.println("Hmotnost batohu: " + hmotnost);
            writer.println("Pocet predmetov v batohu: " + predmeetyVBatohu);
            writer.print("Vlozene predmety poporade: " + vlozenePredmety);
            writer.close();
    }
}
