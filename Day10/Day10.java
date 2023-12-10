package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10 {

    //reader of lines to char matrix
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
            char[][] matrix = fileToCharMatrix(lines);
            System.out.println(part1(matrix)); // 6927
            System.out.println(part2(matrix)); //TODO
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static int part1(char[][] matrix){
        //set up pipes
        Pipe vertical = new Pipe('|');
        Pipe horizontal = new Pipe('-');
        Pipe northEast = new Pipe('L');
        Pipe northWest = new Pipe('J');
        Pipe southWest = new Pipe('7');
        Pipe southEast = new Pipe('F');


        //set up conducte posibile pt fiecare directie
        List<Character> PossiblePipesN = List.of(southEast.getPipeType(), southWest.getPipeType(), vertical.getPipeType());
        List<Character> PossiblePipesW = List.of(northEast.getPipeType(), southEast.getPipeType(), horizontal.getPipeType());
        List<Character> PossiblePipesS = List.of(vertical.getPipeType(), northEast.getPipeType(), northWest.getPipeType());
        List<Character> PossiblePipesE = List.of(horizontal.getPipeType(), northWest.getPipeType(), southWest.getPipeType());


        // setam directiile posibile de conectare ale fiecarei conducte
        vertical.setPossibleDirections(List.of('N', 'S'));
        horizontal.setPossibleDirections(List.of('W', 'E'));
        northEast.setPossibleDirections(List.of('N', 'E'));
        northWest.setPossibleDirections(List.of('N', 'W'));
        southWest.setPossibleDirections(List.of('S', 'W'));
        southEast.setPossibleDirections(List.of('S', 'E'));

        // lista cu tipurile de conducte
        List<Pipe> listPipeTypes = new ArrayList<>(List.of(vertical, horizontal, northEast, northWest, southWest, southEast));

        // cautam pozitia initiala S
        int startingRow = -1;
        int startingCol = -1;
        boolean SFound = false;
        for(int i=0; i < matrix.length; i++){
            for(int j=0; j < matrix[0].length; j++){
                if(matrix[i][j] == 'S'){
                    startingRow = i;
                    startingCol = j;
                    SFound = true;
                    break;
                }
            }
            if(SFound){
                break;
            }
        }
        // verificam in ce directii putem pleca din S folosit listele de posibile conducte
        List<Character> startingDirections = new ArrayList<>(List.of('N', 'W', 'S', 'E'));
        Iterator<Character> iterator = startingDirections.iterator();
        while(iterator.hasNext()){
            Character direction = iterator.next();
            switch (direction){
                case 'N' -> {
                    if(startingRow == 0){
                        iterator.remove();
                        continue;
                    }
                    if(PossiblePipesN.contains(matrix[startingRow-1][startingCol])){
                        break;
                    }
                    iterator.remove();
                }
                case 'S' -> {
                    if(startingRow == matrix.length-1){
                        iterator.remove();
                        continue;
                    }
                    if(PossiblePipesS.contains(matrix[startingRow+1][startingCol])) {
                        break;
                    }
                    iterator.remove();
                }
                case 'W' -> {
                    if(startingCol == 0){
                        iterator.remove();
                        continue;
                    }
                    if(PossiblePipesW.contains(matrix[startingRow][startingCol-1])){
                        break;
                    }
                    iterator.remove();
                }
                case 'E' -> {
                    if(startingCol == matrix[0].length-1){
                        iterator.remove();
                        continue;
                    }
                    if(PossiblePipesE.contains(matrix[startingRow][startingCol+1])){
                        break;
                    }
                    iterator.remove();
                }
            }
        }
        //ramanem cu 2 directii valabile, asa ca o sa inlocuim S cu conducta care se afla de fapt la locatia respectiva
        if(startingDirections.size() == 2){
            for(Pipe pipe : listPipeTypes){
                // daca cele 2 liste, lista de directii ramase ale pozitiei de start si lista de directii a tipului de conducta
                // sunt egale, updatam pozitia initiala cu conducta din locul ei
                if(startingDirections.equals(pipe.getPossibleDirections())){
                    matrix[startingRow][startingCol] = pipe.getPipeType();
                }
            }
        }

        int steps = 0;
        int currRow = startingRow;
        int currCol = startingCol;
        char prevPosition = 0;

        //alegem directia de parcurgere a loopului ca fiind prima directie din lista de 2 posibile
        //in functie de directia de mers, updatam pozitia unde am fost,
        // folosim prevPosition pt a elimina acea varianta de mers din lista de posibile directii
        // a conductei in care ne aflam
        switch (startingDirections.get(0)){
            case 'N' ->  {
                prevPosition = 'S';
                currRow = startingRow-1;
            }
            case 'S' ->  {
                prevPosition = 'N';
                currRow = startingRow+1;
            }
            case 'W' ->  {
                prevPosition = 'E';
                currCol = startingCol-1;
            }
            case 'E' ->  {
                prevPosition = 'W';
                currCol = startingCol+1;
            }
        }

        //iteram pana ajungem la pozitia initiala
        while(true){
            // incrementam steps
            steps++;
            if(currRow == startingRow && currCol == startingCol){
                break;
            }

            // gasim pe ce tip de conducta de aflam
            Pipe currPipe = null;
            for(Pipe pipe : listPipeTypes){
                if(pipe.getPipeType().equals(matrix[currRow][currCol])){
                    currPipe = pipe;
                    break;
                }
            }
            // updatam urmatoarea directie prin a lua valoarea din lista de 2 directii care nu este egala
            // cu cea din care am venit
            char nextDirection = 0;
            for(Character possibleDirection : currPipe.getPossibleDirections()){
                if(!possibleDirection.equals(prevPosition)){
                    nextDirection = possibleDirection;
                }
            }

            // updatam coordonatele pozitiei urmatoare si updatam pozitia din care venim
            switch (nextDirection){
                case 'N' ->  {
                    prevPosition = 'S';
                    currRow = currRow-1;
                }
                case 'S' ->  {
                    prevPosition = 'N';
                    currRow = currRow+1;
                }
                case 'W' ->  {
                    prevPosition = 'E';
                    currCol = currCol-1;
                }
                case 'E' ->  {
                    prevPosition = 'W';
                    currCol = currCol+1;
                }
            }
        }
        //o sa luam jumate din steps deoarece acolo se afla pozitia de mijloc
        return steps/2;
    }

    private static int part2(char[][] matrix) {
        //TODO
        return 0;
    }


}
