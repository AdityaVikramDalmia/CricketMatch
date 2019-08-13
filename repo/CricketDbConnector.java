package repo;

import cricket.Player;
import cricket.Team;

public interface CricketDbConnector {
    void updateBallData(int ballNum, int match_id, Player batsmanOnStrike, Player otherBatsman, int currRun, boolean firstInningComplete, Player currBowler, int runsScored, int wicketsLost, int runSpecifyingIfOut);
    void updateSeriesData(int seriesId, Team matchWinner, Team team1, Team team2);
    int insertSeriesInfo(String team1, String team2, int numOfMatches);
    int updateMatchTossWinner(int seriesId, Team tossWinner, Team battingFirst, Team team1, Team team2, int overLimit);
    void updateMatchTargetSet(int matchId, int targetSet);
    void updatePlayerDataBatsman(int ballNum, int match_id, int currRun, int runSpecifyingIfOut, Player player);
    void updatePlayerDataBowler(int ballNum, int match_id, int currRun, int runSpecifyingIfOut, Player currBowler);
    void updateMatchWinner(int matchId, Team matchWinner, int wonByRuns, int wonByWickets);

    void updateSeriesWinner(int seriesId);
}
