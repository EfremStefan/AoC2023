package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    public static void main(String[] args) {
        Path path = Path.of("inputs/input.txt");
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)){
            List<String> lines = bufferedReader.lines().toList();
            System.out.println(part1(lines)); // 1195150
            System.out.println(part2(lines)); // 42550411
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static int part1(List<String> lines) {
        List<Integer> times = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        int result = 1;
        //parsez inputul
        for(String line : lines){
            String[] twoParts = line.strip().split(":");
            String[] racesTime = twoParts[1].strip().split("[ ]+");
            if(twoParts[0].equals("Time")){
                for(String time : racesTime){
                    times.add(Integer.parseInt(time));
                }
            } else {
                for (String distance : racesTime) {
                    distances.add(Integer.parseInt(distance));
                }
            }
        }
        //pt fiecare timp din lista folosim functia calculateTimeSpanDistance care face un binary search
        for(int i = 0; i < times.size(); i++){
            int currTime = times.get(i);
            int currMaxDistance = distances.get(i);
            int diffWaysToWin = calculateTimeSpanDistance(currTime, currMaxDistance);
            result *= diffWaysToWin;
        }

        return result;
    }

    private static long part2(List<String> lines) {
        long distance = 0, time = 0;
        long result = 0;
        for(String line : lines){
            String[] twoParts = line.strip().split(":");
            String[] racesInformation = twoParts[1].strip().split("[ ]+");
            StringBuilder sb = new StringBuilder();
            for(String races : racesInformation){
                sb.append(races);
            }
            // credeam ca inputul va fii prea mare si va trebui sa schimb din long
            if(twoParts[0].equals("Time")){
                time = Long.parseLong(sb.toString());
            } else {
                distance = Long.parseLong(sb.toString());
            }
        }
        //finally improved O(t) to O(log(t))
        result = calculateTimeSpanDistance(time, distance);
        return result;
    }

    // functie ajutatoare care face un dublu binary search
    // Ne intereseaza primul timp care este mai mare decat cel maxim din input si al ultimul
    //fiecare se afla in cele doua jumatati ale timpilor, unde maximum este mereu la mijloc
    //astfel facem un binary search pe prima jumatate ca sa aflam primul timp care trece de record-ul anterior
    // si un binary search invers, deoarece a doua parte de array este descrescatoare, ca sa aflam al doilea
    private static int calculateTimeSpanDistance(long currTime, long currMaxDistance) {
        // classic binary search pt prima jumatate
        long left = 0;
        long right = currTime/2;
        while(left + 1 < right){
            long mid = (left + right)/2;
            long distance = distanceTraveledForAcceleration(mid, currTime);
            if(distance == currMaxDistance){
                break;
            }
            if( distance > currMaxDistance){
                right = mid;
            } else {
                left = mid;
            }
        }
        long firstMaxPosition = right;

        // al doilea binary search este facut invers deoarece a doua jumatate este descrescatoare
        left = currTime/2 + 1;
        right = currTime;
        while(left + 1 < right){
            long mid = (left + right)/2;
            long distance = distanceTraveledForAcceleration(mid, currTime);
            if(distance == currMaxDistance){
                break;
            }
            if(distance >= currMaxDistance){
                left = mid;
            } else {
                right = mid;
            }
        }
        long secondMaxPosition = left;
        //returnam diferenta si acesta este rezultatul pe aceasta cursa
        return (int) (secondMaxPosition - firstMaxPosition + 1);
    }

    // functie ajutatoare pt calcularea distantei
    private static long distanceTraveledForAcceleration(long acceleration, long allTime){
        return acceleration * (allTime - acceleration);
    }


}
