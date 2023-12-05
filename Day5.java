package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

record Interval(long start, long end) implements Comparable<Interval>{
    @Override
    public int compareTo(Interval o) {
        return (int) Math.min(this.start, o.start);
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
}
public class Day5 {

    public static void main(String[] args) {
        Path path = Path.of("inputs/input.txt");
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)){
            List<String> lines = bufferedReader.lines().toList();
            System.out.println(part1(lines)); // 173706076
            System.out.println(part2(lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long part1(List<String> lines) {
        List<Long> currentMappedNumbers = new ArrayList<>();
        List<Long> newMapping = new ArrayList<>();
        for(String line : lines){
            if(line.isEmpty()){
                continue;
            }
            String[] twoParts = line.strip().split(":");
            if(twoParts[0].contains("seeds")){
                String[] seeds = twoParts[1].strip().split(" ");
                for(String seed : seeds){
                    currentMappedNumbers.add(Long.parseLong(seed));
                }
            }
            if(twoParts[0].contains("-to-")){
                if(twoParts[0].contains("seed-to-soil")){
                    continue;
                }

                currentMappedNumbers.addAll(newMapping);
                newMapping.clear();
                //System.out.println(currentMappedNumbers);
            }
            if(Character.isDigit(line.charAt(0))){
                String[] threeNumbers = line.strip().split(" ");
                long destinationPosition = Long.parseLong(threeNumbers[0]);
                long startingPosition = Long.parseLong(threeNumbers[1]);
                long numberPositionsMapped = Long.parseLong(threeNumbers[2]);

                ListIterator<Long> iterator = currentMappedNumbers.listIterator();
                while(iterator.hasNext()){
                    long seed = iterator.next();
                    if(seed >= startingPosition && seed < startingPosition + numberPositionsMapped){
                        newMapping.add(destinationPosition + (seed - startingPosition));
                        iterator.remove();
                    }
                }
            }
        }
        currentMappedNumbers.addAll(newMapping);
        newMapping.clear();
        //System.out.println(currentMappedNumbers);
        return Collections.min(currentMappedNumbers);
    }



    private static long part2(List<String> lines) {
        Deque<Interval> currentMappedNumbers = new LinkedList<>();
        List<Interval> newMapping = new ArrayList<>();
        for(String line : lines){
            if(line.isEmpty()){
                continue;
            }
            String[] twoParts = line.strip().split(":");
            if(twoParts[0].contains("seeds")){
                String[] seeds = twoParts[1].strip().split(" ");
                for(int i = 0; i < seeds.length; i+=2){
                    long start = Long.parseLong(seeds[i]);
                    long end = Long.parseLong(seeds[i+1]);
                    Interval interval = new Interval(start, start + end);
                    currentMappedNumbers.add(interval);
                }
                //System.out.println("Start");
                //System.out.println(currentMappedNumbers);
            }
            if(twoParts[0].contains("-to-")){
                if(twoParts[0].contains("seed-to-soil")){
                    //System.out.println(twoParts[0]);
                    continue;
                }
                currentMappedNumbers.addAll(newMapping);
                newMapping.clear();
            }
            if(Character.isDigit(line.charAt(0))){
                String[] threeNumbers = line.strip().split(" ");
                long destinationPosition = Long.parseLong(threeNumbers[0]);
                long startingPosition = Long.parseLong(threeNumbers[1]);
                long numberPositionsMapped = Long.parseLong(threeNumbers[2]);
                long rowEndPosition = startingPosition + numberPositionsMapped;

                List<Interval> newRow = new ArrayList<>();
                while(!currentMappedNumbers.isEmpty()) {
                    Interval interval = currentMappedNumbers.pop();
                    Interval notHitBefore = new Interval(interval.start(), Math.min(interval.end(), startingPosition));
                    Interval intersection = new Interval(Math.max(interval.start(), startingPosition), Math.min(rowEndPosition, interval.end()));
                    Interval notHitAfter = new Interval(Math.max(rowEndPosition, interval.start()), interval.end());
                    if (notHitBefore.end() > notHitBefore.start()) {
                        newRow.add(notHitBefore);
                    }
                    if (notHitAfter.end() > notHitAfter.start()) {
                        newRow.add(notHitAfter);
                    }
                    if (intersection.end() > intersection.start()) {
                        newMapping.add(new Interval(destinationPosition + (intersection.start() - startingPosition), destinationPosition + (intersection.end() - startingPosition)));
                    }
                }
                currentMappedNumbers.addAll(newRow);
                //System.out.println(currentMappedNumbers);

            }
        }
        currentMappedNumbers.addAll(newMapping);
        newMapping.clear();

        long min = Long.MAX_VALUE;
        for(Interval interval : currentMappedNumbers){
            if(interval.start() < min){
                min = interval.start();
            }
        }
        return min;
    }


}
