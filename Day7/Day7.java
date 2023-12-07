package days;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day7 {

    public static void main(String[] args) {
        Path path = Path.of("inputs/input.txt");
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)){
            List<String> lines = bufferedReader.lines().toList();
            System.out.println(part1(lines)); //253933213
            System.out.println(part2(lines)); //253473930 funnily enough we lose bid points with the Joker ability :)))
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static int part1(List<String> lines) {
        int result = 0;
        //map for card values
        Map<Character, Integer> cardValues = new HashMap<>(Map.of('2', 0, '3', 1, '4', 2, '5', 3, '6',
                4, '7', 5, '8', 6, '9', 7, 'T', 8
                , 'J', 9));
        cardValues.put('Q', 10);
        cardValues.put('K', 11);
        cardValues.put('A', 12);

        //hand evaluator to compare hand types
        HandEvaluator handEvaluator = new HandEvaluator(cardValues, false);
        //list of hands to be sorted by hand evaluator
        List<String> handResults = new LinkedList<>();
        //map to store each hand with its bid value
        Map<String, Integer> handBidMap = new HashMap<>();

        for(String line : lines) {
            String[] twoParts = line.strip().split(" ");
            //parse hand and bid
            String hand = twoParts[0];
            int bid = Integer.parseInt(twoParts[1]);
            //save hand for sorting
            handResults.add(hand);
            //save the <hand,bid> key, value pair
            handBidMap.put(hand, bid);
        }
        //Sort the hands with hand Evaluator as Comparator
        handResults.sort(handEvaluator);

        for(int i=0; i < handResults.size(); i++){
            //update the results with the rank * bid
            result += (i+1)* handBidMap.get(handResults.get(i));
        }
        return result;
    }

    private static int part2(List<String> lines) {
        int result = 0;
        Map<Character, Integer> cardValues = new HashMap<>(Map.of('2', 0, '3', 1, '4', 2, '5', 3, '6',
                4, '7', 5, '8', 6, '9', 7, 'T', 8
                , 'J', -1)); // put J at -1 as it is the lowest value card now
        cardValues.put('Q', 10);
        cardValues.put('K', 11);
        cardValues.put('A', 12);

        //hand evaluator to compare hand types
        HandEvaluator handEvaluator = new HandEvaluator(cardValues, true);
        //list of hands to be sorted by hand evaluator
        List<String> handResults = new LinkedList<>();
        //map to store each hand with its bid value
        Map<String, Integer> handBidMap = new HashMap<>();

        for(String line : lines) {
            String[] twoParts = line.strip().split(" ");
            //parse hand and bid
            String hand = twoParts[0];
            int bid = Integer.parseInt(twoParts[1]);
            //save hand for sorting
            handResults.add(hand);
            //save the <hand,bid> key, value pair
            handBidMap.put(hand, bid);
        }
        //Sort the hands with hand Evaluator as Comparator
        handResults.sort(handEvaluator);

        for(int i=0; i < handResults.size(); i++){
            //update the results with the rank * bid
            result += (i+1)* handBidMap.get(handResults.get(i));
        }
        return result;
    }

}
