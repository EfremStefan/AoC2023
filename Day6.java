package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
        //pt fiecare timp din lista
        for(int i = 0; i < times.size(); i++){
            int currTime = times.get(i);
            int currMaxDistance = distances.get(i);
            int diffWaysToWin = 0;
            //verificam ce distanta parcurgem cu formula distanta = acceleratia * timpul, unde j este acceleratia
            // sau timpul petrecut accelerand
            for(int j = 0; j < currTime; j++){
                int distance = j * (currTime-j);
                if(distance > currMaxDistance){
                    diffWaysToWin++;
                }
            }
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
            // intr-un tabel de cifre si sa lucrez pe el, se pare ca nu a fost nevoie
            // o sa incerc sa vad daca pot face mai eficienta aceast parte
            if(twoParts[0].equals("Time")){
                time = Long.parseLong(sb.toString());
            } else {
                distance = Long.parseLong(sb.toString());
            }
        }
        //similar cu part1, to be improved if possible
        for(long j = 0; j < time; j++){
            long distanceToTravel = j * (time-j);
            if(distanceToTravel > distance){
                result++;
            }
        }
        return result;
    }


}
