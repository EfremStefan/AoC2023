package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {

    public static void main(String[] args) {
        Path path = Path.of("inputs/input.txt");
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)){
            List<String> lines = bufferedReader.lines().toList();
            System.out.println(part1(lines)); // 1992273652
            System.out.println(part2(lines)); // 1012
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static int part1(List<String> lines) {
        int result = 0;
        for(String line : lines){
            // salvam numerele intr-o lista firstSequence
            String[] numbers = line.strip().split(" ");
            List<Integer> firstSequence = new ArrayList<>();
            for(String number : numbers){
                firstSequence.add(Integer.parseInt(number));
            }

            //folosim o Lista de Liste pentru a salva fiecare linie creata sub linia initiala
            List<List<Integer>> sequences = new LinkedList<>();
            sequences.add(firstSequence);
            //creeam noi linii din diferentele intre numere pana gasim linia doar cu zero-uri
            boolean allZeroes = false;
            while(!allZeroes){
                int countZeroes = 0;
                List<Integer> intermediateSequence = new ArrayList<>();
                //ultima secventa din lista este cea cu cea mai putine elemente, adica cea de la care trebuie sa cream noua lista
                List<Integer> lastSequence = sequences.get(sequences.size()-1);
                for(int i=0; i < lastSequence.size()-1; i++){
                    int nextNumber = lastSequence.get(i+1);
                    int currNumber = lastSequence.get(i);
                    int resultToBeSaved = nextNumber-currNumber;
                    //daca vedem zerouri, incrementam counterul
                    if(resultToBeSaved == 0){
                        countZeroes++;
                    }
                    intermediateSequence.add(resultToBeSaved);
                }
                //adaugam noua secventa la lista de secvente
                sequences.add(intermediateSequence);
                //daca counterul de zerouri ne arata ca toata linia e plina de zerouri, terminam crearea de secvente
                if(countZeroes == intermediateSequence.size()){
                    allZeroes = true;
                }
            }

            //incepem cu un previous Line Element de 0 si il modificam cu noul element de la fiecare linie
            int previousLineElement = 0;
            // iteram prin fiecare linie in ordine inversa ca sa adaugam ultimul element la fiecare secventa
            // elementul extrapolat
            for(int i = sequences.size()-1; i > - 1; i--){
                List<Integer> currSequence = sequences.get(i);
                int lastSequenceElement = currSequence.get(currSequence.size()-1);
                previousLineElement += lastSequenceElement;
                currSequence.add(previousLineElement);
            }
            //luam ultimul element din prima lista, acesta este raspunsul de la aceasta linie, valoarea extrapolata
            int nextElementInSequence = firstSequence.get(firstSequence.size()-1);
            // adaugam elementul la rezultat
            result += nextElementInSequence;
        }

        return result;
    }

    // foarte similara cu part1, doar o sa va arat ce am modificat la final
    private static int part2(List<String> lines) {
        int result = 0;
        for(String line : lines){
            String[] numbers = line.strip().split(" ");
            List<Integer> firstSequence = new LinkedList<>();
            for(String number : numbers){
                firstSequence.add(Integer.parseInt(number));
            }

            List<List<Integer>> sequences = new LinkedList<>();
            sequences.add(firstSequence);
            boolean allZeroes = false;
            while(!allZeroes){
                int countZeroes = 0;
                List<Integer> intermediateSequence = new LinkedList<>();
                List<Integer> lastSequence = sequences.get(sequences.size()-1);
                for(int i=0; i < lastSequence.size()-1; i++){
                    int nextNumber = lastSequence.get(i+1);
                    int currNumber = lastSequence.get(i);
                    int resultToBeSaved = nextNumber-currNumber;
                    if(resultToBeSaved == 0){
                        countZeroes++;
                    }
                    intermediateSequence.add(resultToBeSaved);
                }
                sequences.add(intermediateSequence);
                if(countZeroes == intermediateSequence.size()){
                    allZeroes = true;
                }
            }

            // singura parte modificata, acum avem un firstElement of Previous line deoarece trebuie sa extrapolam in trecut
            int firstElementPreviousLine = 0;
            for(int i = sequences.size()-1; i > - 1; i--){
                List<Integer> currSequence = sequences.get(i);
                int firstSequenceElement = currSequence.get(0);
                //inversam ecuatia de la partea intai  y z
                // daca din                             x   aveam inainte z = x + y, acum o sa avem y = z - x
                //repetam acelasi proces ca la part1 doar ca adaugam la inceputu listei
                // am modificat implementarea cu LinkedList la part 2 care sunt o implementare de Deque
                // ca sa fie mai rapida adaugarea la inceputul coadei
                firstElementPreviousLine = firstSequenceElement - firstElementPreviousLine;
                currSequence.add(0, firstElementPreviousLine);
            }
            // luam primul element al primei secvente, acesta e raspunsul de la aceasta linie si il adaugam la result
            int nextElementInSequence = firstSequence.get(0);
            result += nextElementInSequence;
        }

        return result;
    }


}
