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
            System.out.println(part2(lines)); // 11611182
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long part1(List<String> lines) {
        //List of the initial Seeds
        List<Long> initialSeeds = new ArrayList<>();
        // list of the number lines at every transformation part
        List<List<String>> listParts = new LinkedList<>();
        for(String line : lines){
            if(line.isEmpty()){
                continue;
            }
            if(Character.isDigit(line.charAt(0))){
                //number line added to current list
                listParts.get(listParts.size()-1).add(line);
            }
            if(line.contains("-to-")){
                //when we get to a new part, we create a new array list for the next lines
                listParts.add(new ArrayList<>());
            }
            String[] twoParts = line.strip().split(":");
            if(twoParts[0].contains("seeds")){
                String[] seedsString = twoParts[1].strip().split(" ");
                // add initial seeds
                for(String number : seedsString){
                    initialSeeds.add(Long.parseLong(number));
                }
            }
        }
        long min = Long.MAX_VALUE;
        // calculate every seed separately of one another
        // previously I did all of them at once and the code got really messy, see first commit
        for(long seed : initialSeeds){
            // go through every transformation part
            for(List<String> numberLines : listParts) {
                seed = parseChangeNumber(seed, numberLines);
            }
            // calculate min seed at the end
            if(seed < min){
                min = seed;
            }
        }
        return min;
    }

    //helper function to parse a seed through a transformation part
    private static Long parseChangeNumber(Long seed, List<String> numberLines) {
        for(String line : numberLines){
            String[] threeNumbers = line.strip().split(" ");
            // parse the information on each lone
            long destinationPosition = Long.parseLong(threeNumbers[0]);
            long startingPosition = Long.parseLong(threeNumbers[1]);
            long numberPositionsMapped = Long.parseLong(threeNumbers[2]);

            //if the seed is in the interval, transform it and return it
            // got caught here first, I thought a seed can be changed multiple times in a transformation part
            //but this is not the case (my bad) so we return once the seed is transformed once
            if(seed >= startingPosition && seed < startingPosition + numberPositionsMapped){
                return destinationPosition + seed - startingPosition;
            }
        }
        // if no transformation occurs, return seed
        return seed;
    }


    private static long part2(List<String> lines) {
        //using a list of intervals(a record of [start, end)) to store every interval of seeds
        List<Interval> initialIntervals = new ArrayList<>();
        List<List<String>> listParts = new LinkedList<>();
        for(String line : lines){
            if(line.isEmpty()){
                continue;
            }
            if(Character.isDigit(line.charAt(0))){
                listParts.get(listParts.size()-1).add(line);
            }
            if(line.contains("-to-")){
                listParts.add(new ArrayList<>());
            }
            String[] twoParts = line.strip().split(":");
            if(twoParts[0].contains("seeds")){
                String[] seeds = twoParts[1].strip().split(" ");
                for(int i = 0; i < seeds.length; i+=2){
                    //compared to part 1, we create intervals of 2 numbers and add them to initial Intervals
                    long start = Long.parseLong(seeds[i]);
                    long end = Long.parseLong(seeds[i+1]);
                    Interval interval = new Interval(start, start + end);
                    initialIntervals.add(interval);
                }
            }
        }
        long min = Long.MAX_VALUE;
        // parse through all transformations, one initial interval at the time
        for(Interval interval : initialIntervals){
            //compared to part1, we need a List of intervals because the initial interval will be split after any
            //modification
            Deque<Interval> intervalResults = new LinkedList<>();
            // we start with the initial interval
            intervalResults.add(interval);
            for(List<String> numberLines : listParts) {
                intervalResults = parseChangeIntervals(intervalResults, numberLines);
            }
            // calculate the minimum starting position from the final intervals
            for(Interval finalInterval : intervalResults){
                if(finalInterval.start() < min){
                    min = finalInterval.start();
                }
            }
        }
        return min;
    }

    private static Deque<Interval> parseChangeIntervals(Deque<Interval> intervalResults, List<String> numberLines) {
        // save the intervals that are changed by a transformation in a separate list that will be added
        // after all other transformations are made. This is because once an internal interval is transformed
        // from the initial interval, that part must not be transformed again in this section
        Deque<Interval> newMapping = new LinkedList<>();
        for(String line : numberLines){
            String[] threeNumbers = line.strip().split(" ");
            long destinationPosition = Long.parseLong(threeNumbers[0]);
            long startingPosition = Long.parseLong(threeNumbers[1]);
            long numberPositionsMapped = Long.parseLong(threeNumbers[2]);
            // save the endPosition of the transformation Interval for ease of use
            long rowEndPosition = startingPosition + numberPositionsMapped;

            // this list will save the part of the intervals that are not intersected with the transformation interval
            // needed some pen and paper for this one
            Deque<Interval> newRow = new LinkedList<>();
            while(!intervalResults.isEmpty()) {
                Interval interval = intervalResults.pop();
                // split into 3 intervals, the part that is not hit by the transformation interval that is before the intersection
                Interval notHitBefore = new Interval(interval.start(), Math.min(interval.end(), startingPosition));
                // the intersection between the interval to be changed and the transformation interval
                Interval intersection = new Interval(Math.max(interval.start(), startingPosition), Math.min(rowEndPosition, interval.end()));
                //the part that is not hit by the transformation interval that is after the intersection
                Interval notHitAfter = new Interval(Math.max(rowEndPosition, interval.start()), interval.end());
                // the parts not hit can be modified by the next lines, so we add them to the newRow list
                if (notHitBefore.end() > notHitBefore.start()) {
                    newRow.add(notHitBefore);
                }
                if (notHitAfter.end() > notHitAfter.start()) {
                    newRow.add(notHitAfter);
                }
                //the part that is the intersection will be added to the newMapping list as it must not be modified by further lines in this section
                if (intersection.end() > intersection.start()) {
                    newMapping.add(new Interval(destinationPosition + (intersection.start() - startingPosition), destinationPosition + (intersection.end() - startingPosition)));
                }
            }
            //the "not hit" intervals are added to our currently working list of intervals
            intervalResults = newRow;
        }
        //at the end add the newMapping interval list
        newMapping.addAll(intervalResults);
        return newMapping;
    }


}
