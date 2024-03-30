package com.mycompany.tournamentsprobability;

import java.util.ArrayList;

/**
 *
 * @author 20220791
 */
public class TournamentsProbability {
    
    static double wPercent = 0.75;
    static ArrayList<Double> sub;
    static int rounds;
    
    // Method to simulate a single match between two teams
    public static boolean simulateMatch(double skillTeam1, double skillTeam2) {
        double totalSkill = skillTeam1 + skillTeam2;
        double winProbability = skillTeam1 / totalSkill; // Probability team1 wins
        return Math.random() < winProbability; // Determine if team1 wins based on random draw
    }
    
        /**
     *
     * @param n number of teams
     * @param skills skill levels
     */
    public static void createTournament(int n, ArrayList<Double>skills){
        skills.add((double)10);
        for(int i = 1; i < n; i++){
            skills.add(10 - Math.random()*10);
        }
    }
    
    // Method to simulate a single round-robin tournament
    public static boolean simulateTournamentSingleRoundRobin(ArrayList<Double> skills) {
        int topTeamWins = 0; // Number of wins by the top team
        for (int i = 1; i < skills.size(); i++) {
            if (simulateMatch(skills.get(0), skills.get(i))) {
                topTeamWins++;
            }
        }
        // Check if top team won the majority of its matches
        return topTeamWins > skills.size() * wPercent;
    }
    
    public static boolean simulateTournamentDoubleRoundRobin(ArrayList<Double> skills) {
        int topTeamWins = 0; // Number of wins by the top team
        for (int i = 1; i < skills.size(); i++) {
            if (simulateMatch(skills.get(0), skills.get(i))) {
                topTeamWins++;
            }
            if (simulateMatch(skills.get(0), skills.get(i))) {
                topTeamWins++;
            }
        }
        // Check if top team won the majority of its matches
        return topTeamWins > skills.size() * 2 * wPercent;
    }
    
    public static boolean simulateTournamentKnockout(ArrayList<Double> skills) {
        sub = new ArrayList<>();
        rounds = calculateRounds(skills.size());
        for (int j = 0; j < rounds; j++) {
            simulateRound(skills, sub);
        }

        if (skills.get(0) == 10) {
            return true;
        }
        return false;
    }
    
    /**
     *
     * @param n
     * @param rounds
     * @return
     */
    public static int calculateRounds(int n) {
        int rounds = 0;
        while (n != 1) {
            n = n / 2;
            rounds++;
        }
        return rounds;
    }
    
    public static void simulateRound(ArrayList<Double> skills, ArrayList<Double> sub) {
        for (int i = 0; i < skills.size() - 1; i += 2) {
            if (simulateMatch(skills.get(i), skills.get(i + 1))) {
                sub.add(skills.get(i));
            } else {
                sub.add(skills.get(i + 1));
            }
        }
        skills.clear();
        for (int i = 0; i < sub.size(); i++) {
            skills.add(sub.get(i));
        }
        sub.clear();
    }
    
    public static void riggedKnouckoutFor(ArrayList<Double> skills) {
        ArrayList<Double> rigg = new ArrayList<>();
        int smallest = 1;
        for (int j = 0; j < (skills.size() / 2) - 1; j++) {
            for (int i = 1; i < skills.size(); i++) {
                if (skills.get(i) < skills.get(smallest)) {
                    smallest = i;
                }
            }
            rigg.add(skills.get(smallest));
            skills.remove(skills.get(smallest));
            smallest = 1;
        }
        for(int i = 1; i < skills.size(); i++) {
            rigg.add(skills.get(i));
            skills.remove(skills.get(i));
        }
        for(int i = 0; i < rigg.size(); i++) {
            skills.add(rigg.get(i));
        }

    }
    
    public static void riggedKnouckoutAgainst(ArrayList<Double> skills) {
        ArrayList<Double> rigg = new ArrayList<>();
        int largest = 0;
        while (!skills.isEmpty()) {
            for (int i = 0; i < skills.size(); i++) {
                if (skills.get(i) > skills.get(largest)) {
                    largest = i;
                }
                rigg.add(skills.get(largest));
                skills.remove(skills.get(largest));
                largest = 0;
            }
        }

        for (Double r : rigg) {
            skills.add(r);
        }

    }

    
    public static void threeTournamnetSimulation(int n, int simulations, ArrayList<Double> skills) {
        
        int topTeamWinsSingle = 0; // Number of simulations where the top team wins
        int topTeamWinsDouble = 0;
        int topTeamWinsKnockOut = 0;
        
        
        for (int i = 0; i < simulations; i++) {
            createTournament(n,skills);
            if (simulateTournamentSingleRoundRobin(skills)) {
                topTeamWinsSingle++;
            }
            if (simulateTournamentDoubleRoundRobin(skills)) {
                topTeamWinsDouble++;
            }
            if (simulateTournamentKnockout(skills)) {
                topTeamWinsKnockOut++;
            }
            skills.clear();
        }
        
        // Calculate and print the probability of the top team winning
        double probabilitySingle = (double) topTeamWinsSingle / simulations;
        double probabilityDouble = (double) topTeamWinsDouble / simulations;
        double probabilityKnockout = (double) topTeamWinsKnockOut / simulations;
        System.out.println("Probability of the top team winning single-round robin: " + probabilitySingle);
        System.out.println("Probability of the top team winning double-round robin: " + probabilityDouble);
        System.out.println("Probability of the top team winning knockout: " + probabilityKnockout);
    }
    
    public static void KnouckoutComparisonSimulation(int n, int simulations, ArrayList<Double> skills){
        int topTeamWinsKnockOut = 0;
        int topTeamWinsKnockOutFor = 0;
        int topTeamWinsKnockOutAgainst = 0;
        
        for (int i = 0; i < simulations; i++) {
            createTournament(n,skills);
            ArrayList<Double> clone = new ArrayList<>();
            ArrayList<Double> clone1 = new ArrayList<>();
            for(Double r: skills) {
                clone.add(r);
                clone1.add(r);
            }
            //ArrayList<Double> clone = (ArrayList<Double>) skills.clone();
            //ArrayList<Double> clone1 = (ArrayList<Double>) skills.clone();
            riggedKnouckoutFor(clone);
            if (simulateTournamentKnockout(clone)) {
                topTeamWinsKnockOutFor++;
            }
            
            
            riggedKnouckoutAgainst(skills);
            if (simulateTournamentKnockout(skills)) {
                topTeamWinsKnockOutAgainst++;
            }
            
            if (simulateTournamentKnockout(clone1)) {
                topTeamWinsKnockOut++;
            }
            
            
            
            
            
            skills.clear();
        }
        
        // Calculate and print the probability of the top team winning
        double probabilityNormal = (double) topTeamWinsKnockOut / simulations;
        double probabilityFor = (double) topTeamWinsKnockOutFor / simulations;
        double probabilityAgainst = (double) topTeamWinsKnockOutAgainst / simulations;
        System.out.println("Probability of the top team winning knockout: " + probabilityNormal);
        System.out.println("Probability of the top team winning knockout for: " + probabilityFor);
        System.out.println("Probability of the top team winning knockout against: " + probabilityAgainst);
    }
    
    public static void main(String[] args) {
        // Define skill levels for each team
        //double[] skills = {90, 70, 50, 30}; // Example skill levels for 4 teams
        ArrayList<Double>skills = new ArrayList<>();
        int n = 128;

        int simulations = 1000000; // Number of tournament simulations


        //threeTournamnetSimulation(n, simulations,skills);
        KnouckoutComparisonSimulation(n, simulations, skills);

        
    }
}
