package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day4 {
    public static char[][] fileToCharMatrix(List<String> lines) {
        char[][] matrix = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            matrix[i] = lines.get(i).toCharArray();
        }
        return matrix;
    }

    public static void main(String[] args) {
        Path path = Path.of("inputs/input.txt");
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)){
            List<String> lines = bufferedReader.lines().toList();
            char[][] matrix = fileToCharMatrix(lines);
            System.out.println(part1(lines)); //21821
            System.out.println(part2(lines)); //5539496
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static int part1(List<String> lines) {
        int result = 0;
        for(String line : lines){
            int countNumbers = 0;
            String cardLine = line.strip().split(":")[1];
            String[] twoParts = cardLine.split("[|]");
            // Set pentru a salva numerele castigatoare, acestea sunt diferite
            Set<Integer> set = new HashSet<>();
            String[] winningNumbers = twoParts[0].strip().split("[ ]+");
            String[] numbersOwned = twoParts[1].strip().split("[ ]+");
            // Salvez winningNumbers in Set (prima parte a definitiei)
            for(String number : winningNumbers){
                set.add(Integer.parseInt(number));
            }
            //Verific existenta numereleor castigatoare in partea a doua a liniei/numbersOwned
            for(String number: numbersOwned){
                if (set.contains(Integer.parseInt(number))) {
                    // Acest remove l-am folsit in cazul in care aveam numere owned identice, nu a fost cazul
                    set.remove(0);
                    countNumbers++;
                }
            }
            // calculam rezultatul
            if(countNumbers != 0) {
                result += Math.pow(2, countNumbers - 1);
            }

        }
        return result;
    }

    private static int part2(List<String> lines) {
        int result = 0;
        //Folosesc un HashMap pentru a pastra tuplul (nr card, numar de cards castigate de acel numar).
        //Asa reusesc sa trec prin toate cartile castigate de la un numar
        // Am folosit un TreeMap original, crezand ca o sa am nevoie de ordinea key-lor cand fac trecerea prin
        // scrach cards castigate dar am modificat la HashMap realizand ca asta nu este cazul
        Map<Integer, Integer> wonScratchCards = new HashMap<>();
        for(String line : lines){
            String[] cardNumberSection = line.strip().split(":")[0].split(" ");
            String cardNumber = cardNumberSection[cardNumberSection.length-1];
            int presentCardNumber = Integer.parseInt(cardNumber);
            //Functie ajutatoare pentru cautarea scratchCardurilor dintr-o linie
            p2CardParser(wonScratchCards, line);
            // crestem rezultatul pentru cartea prezenta pe care o avem deja
            result++;
            int numberPresentCardsWon = wonScratchCards.getOrDefault(presentCardNumber, 0);
            // crestem rezultatul pentru numarul de carti castigate de acest numar (castigate in trecut la cartile anterioare)
            result += numberPresentCardsWon;
            // aflarea rezultatului dureaza putin din cauza numarului pare de apeluri pentru cartile castigate
            while (numberPresentCardsWon > 0){
                p2CardParser(wonScratchCards, line);
                numberPresentCardsWon--;
            }
            wonScratchCards.remove(presentCardNumber);


        }
        return result;
    }

    private static void p2CardParser(Map<Integer, Integer> wonScratchCards, String line){
        //Prima parte e similara cu p1, doar ca acum salvam si numarul cartii in presentCardNumber
        String cardLine = line.strip().split(":")[1];
        String[] cardNumberSection = line.strip().split(":")[0].split(" ");
        String cardNumber = cardNumberSection[cardNumberSection.length-1];
        int presentCardNumber = Integer.parseInt(cardNumber);

        String[] twoParts = cardLine.split("[|]");
        Set<Integer> set = new HashSet<>();
        String[] winningNumbers = twoParts[0].strip().split("[ ]+");
        String[] numbersOwned = twoParts[1].strip().split("[ ]+");
        for(String number : winningNumbers){
            set.add(Integer.parseInt(number));
        }
        int countScratchWonForCard = 0;
        for(String number: numbersOwned){
            if (set.contains(Integer.parseInt(number))) {
                set.remove(0);
                countScratchWonForCard++;
                //cream o intrare in HashMap daca nu am mai castigat aceasta noua carte sau
                // ii crestem numarul de carti castigate
                wonScratchCards.merge(presentCardNumber + countScratchWonForCard, 1, Integer::sum);
            }
        }
    }
}
