package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

record Direction(String left, String right){}

public class Day8 {

    public static char[][] fileToCharMatrix(List<String> lines){
        char[][] matrix = new char[lines.size()][lines.get(0).length()];
        for(int i = 0; i < lines.size(); i++){
            matrix[i] = lines.get(i).toCharArray();
        }
        return matrix;
    }

    public static void main(String[] args) {
        Path path = Path.of("inputs/input.txt");
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)){
            List<String> lines = bufferedReader.lines().toList();
            System.out.println(part1(lines)); //11911
            System.out.println(part2(lines)); // 10151663816849
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static int part1(List<String> lines) {
        int noOfSteps = 0;
        // salvam prima linie cu steps care trebuies sa se repete pt a ajunge la final
        List<Character> directionsToFollow = new ArrayList<>();
        for(char ch : lines.get(0).toCharArray()){
            directionsToFollow.add(ch);
        }
        //parsam fiecare linie si salvam tuplul pozitie:directii intr-un HashMap
        // Direction este un tuplu de (left,right)
        Map<String, Direction> mapOfDirections = new HashMap<>();
        for(int i=2; i < lines.size(); i++){
            String line = lines.get(i);
            String[] twoParts = line.strip().split("=");
            String position = twoParts[0].strip();
            String[] directions = new String[2];
            Matcher regexMatcher = Pattern.compile("([A-Z]{3})").matcher(twoParts[1]);
            int index = 0;
            while(regexMatcher.find()){
                directions[index++] = regexMatcher.group();
            }
            mapOfDirections.put(position, new Direction(directions[0], directions[1]));
        }


        //folosim un boolean pt a vedea cand ajungem la final
        boolean gotToEnd = false;
        //pozitie de start este "AAA" si o sa o updatam pe parcursul programului
        String currentPosition = "AAA";
        while(!gotToEnd) {
            //la fiecare step vedem in ce directie trebuie sa mergem
            for (char step : directionsToFollow) {
                String directionToTake;
                Direction directionsOfCurrentPosition = mapOfDirections.get(currentPosition);
                if (step == 'L') {
                    directionToTake = directionsOfCurrentPosition.left();
                } else {
                    directionToTake = directionsOfCurrentPosition.right();
                }
                //incrementam step counter
                noOfSteps++;
                //daca am ajuns la final, iesim din loop
                if (directionToTake.equals("ZZZ")) {
                    gotToEnd = true;
                    break;
                }
                //updatam pozitia curenta cu noua pozitie descoperita la acest pas
                currentPosition = directionToTake;
            }
        }
        //returnam noOfSteps
        return noOfSteps;
    }

    private static long part2(List<String> lines) {
        int noOfSteps = 0;
        // salvam prima linie cu steps care trebuies sa se repete pt a ajunge la final
        List<Character> directionsToFollow = new ArrayList<>();
        // salvam pozitiile de start intr-o lista, pozitiile care se termina in 'A'
        List<String> startingPositions = new ArrayList<>();
        for(char ch : lines.get(0).toCharArray()){
            directionsToFollow.add(ch);
        }
        //aceasi parsare ca part1 cu o harta
        Map<String, Direction> mapOfDirections = new HashMap<>();
        for(int i=2; i < lines.size(); i++){
            String line = lines.get(i);
            String[] twoParts = line.strip().split("=");
            String position = twoParts[0].strip();
            String[] directions = new String[2];
            Matcher regexMatcher = Pattern.compile("([A-Z]{3})").matcher(twoParts[1]);
            int index = 0;
            while(regexMatcher.find()){
                directions[index++] = regexMatcher.group();
            }
            if(position.charAt(2) == 'A'){
                startingPositions.add(position);
            }
            mapOfDirections.put(position, new Direction(directions[0], directions[1]));
        }


        boolean gotToEnd = false;
        //facem o copie a pozitiilor de start pe care le vom updata la fiecare step
        //pastram Map-ul cu pozitiile initiale intacta deoarece o sa avem nevoie de ea pt a verifica ca fiecare
        //pozitie de start a facut un ciclu de rulare
        List<String> currentPositions = new ArrayList<>(List.of(startingPositions.toArray(new String[0])));
        //folosim un Map cu pozitiile initiale ca si chei si numarul de steps pt a ajunge la pozitia finala(care este si cea initiala)
        // , care se termina in 'Z'. Aici e o observatie pe care am facut-o
        // La inceput am gresit la inceput, incercam sa merg cu rularile in paralel dar executia nu se termina
        // fiecare pozitie de start are un ciclu pana cand ajunge la pozitia final si dupa o ia de la capat, directiile sunt aceleasi
        // am folosit Map isThereOffset si liniile comentate de cod care mi-au aratat asta. In acest cas o sa ne trebuiasca
        // Largest Common Multiplier de fiecare ciclu pt a gasi rezultatul
        Map<String, Integer> timesTookToHitZ = new HashMap<>();

        //liniile comentate cu acest Map sunt folosite pt a vedea ca nu exista offset intre cicluri, pozitia finala ==
        //pozitia initiala la fiecare ciclu
//        Map<String, Integer> isThereOffset = new HashMap<>();
        // initializam fiecare lungime de ciclu cu 0
        for(String startingPosition : startingPositions){
            timesTookToHitZ.put(startingPosition, 0);
//            isThereOffset.put(startingPosition, 1);
        }
        int countPositionsAtEnd= 0;
        while(!gotToEnd) {
            //executia step-urilor este identica, doar ca acum o sa iteram prin Map-ul de curent positions pt a updata
            //toate pozitiile, nu doar una
            for (char step : directionsToFollow) {
                String directionToTake;
                for(int i = 0; i < currentPositions.size(); i++) {
                    String currentPosition = currentPositions.get(i);
                    Direction directionsOfCurrentPosition = mapOfDirections.get(currentPosition);
                    if (step == 'L') {
                        directionToTake = directionsOfCurrentPosition.left();
                    } else {
                        directionToTake = directionsOfCurrentPosition.right();
                    }
                    currentPositions.set(i, directionToTake);
                }
                noOfSteps++;

                // daca una din pozitiile noi este finala (se termina in 'Z'), updatam valuarea pozitie initiale de la acelasi
                //index in harta timesTookToHitZ
                for(int i = 0; i < startingPositions.size(); i++) {
                    if (currentPositions.get(i).endsWith("Z")) {
                        String startingPosition = startingPositions.get(i);
                        timesTookToHitZ.put(startingPosition, noOfSteps);
                        //acest couner este folosit pt a ne da seama cand am updatat de Map-ul cu cicluri de numarul pozitiilor de start
                        //cand am facut asta, avem marimea ciclulilor pt fiecare starting position, putem face LCM
                        countPositionsAtEnd++;
                        // aceasta parte poate fi decomentata pentru a vedea ca nu exista offset intre ciclurile fiecarei pozitie de start
                        // in a gazi pozitia ei finale cu 'Z' la final
                        // used for verification and debugging
//                        if(countPositionsAtEnd == startingPositions.size()){
//                            isThereOffset = new HashMap<>(timesTookToHitZ);
//                        }
//                        if(countPositionsAtEnd % startingPositions.size() == 0 && countPositionsAtEnd != 0){
//                            for(Map.Entry<String,Integer> entry : isThereOffset.entrySet()) {
//                                System.out.println(entry.getKey() + " has achieved " + noOfSteps/entry.getValue() + " cycles of hitting Z node");
//                            }
//                        }
                    }
                }

                // daca am updatat Map-ul de cicluri de startingPositions.size() ori, putem sa ne oprim din a itera
                // acest bloc de cod poate fii comentat (impreuna cu decomentarea celorlalte linii) pentru a vedea cum nu exista offset intre cicluri
                // varianta brute-force nu functioneza, am incercat sa rulez prin iteratii 2 minute :))
                if(countPositionsAtEnd == startingPositions.size()){
                    gotToEnd = true;
                    break;
                }
            }
        }
        // folosim o functie simpla de lowestCommonMultiplier pt a gasi LCM de ciclurile gasite la pasul anterior
        long lcmOfCycles = lowestCommonMultiplier(timesTookToHitZ);

        //return the result, aici se vede ca numarul este prea mare pentru a putea itera de atatea ori cu toate pozitiile de start
        // 10151663816849
        // ar insemna ca programul sa ruleze de 10151663816849 * startingPositions.size() (care este 6).
        // asa ruleaza de ciclul maxim + gasirea LCM
        return lcmOfCycles;
    }

    private static long lowestCommonMultiplier(Map<String, Integer> timesTookToHitZ) {
        long lcm = 1;
        for(Map.Entry<String, Integer> entry : timesTookToHitZ.entrySet()){
            lcm = (entry.getValue() * lcm)/ greatestCommonDivisor(entry.getValue(), lcm);
        }
        return lcm;
    }

    private static long greatestCommonDivisor(long number1, long number2) {
        if(number2 == 0){
            return number1;
        }
        return greatestCommonDivisor(number2, number1 % number2);
    }


}
