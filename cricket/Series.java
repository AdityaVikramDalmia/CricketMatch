package cricket;

import repo.CricketDbConnector;
import repo.SqlConnectorCricket;

public class Series {


    public static void conductSeries(int numOfMatches,String team1Name,String team2Name,int overLimit){
        CricketDbConnector connection=new SqlConnectorCricket();
        int seriesId=connection.insertSeriesInfo(team1Name,team2Name,numOfMatches);
        for (int i = 0; i <numOfMatches ; i++) {
            System.out.println(i);
            conductMatch(team1Name,team2Name,overLimit,seriesId,connection); }
        boolean setSeriesWinner=true;
        connection.updateSeriesWinner(seriesId);
    }
    public static void conductMatch(String team1Name,String team2Name,int overLimit,int seriesId,CricketDbConnector connection){
        Team team1=new Team(team1Name);
        Team team2=new Team(team2Name);
        team1.addPlayer(new Player("Virat Kohli",Player.PlayerType.BATSMAN));
        team1.addPlayer(new Player("Rohit Sharma",Player.PlayerType.BATSMAN));
        team1.addPlayer(new Player("Shikhar Dhawan",Player.PlayerType.BATSMAN));
        team1.addPlayer(new Player("MS Dhoni",Player.PlayerType.WICKETKEEPER));
        team1.addPlayer(new Player("Kedar Jadhav",Player.PlayerType.ALLROUNDER));
        team1.addPlayer(new Player("Jadeja",Player.PlayerType.ALLROUNDER));
        team1.addPlayer(new Player("Bhuveneshvar Kumar",Player.PlayerType.BOWLER));
        team1.addPlayer(new Player("Mohammed Shammi",Player.PlayerType.BOWLER));
        team1.addPlayer(new Player("Dinesh Karthik",Player.PlayerType.BOWLER));
        team1.addPlayer(new Player("Chahal",Player.PlayerType.BOWLER));
        team1.addPlayer(new Player("Jasprit Bumrah",Player.PlayerType.BOWLER));


        team2.addPlayer((new Player("Joe Root",Player.PlayerType.BATSMAN)));
        team2.addPlayer((new Player("Buttler",Player.PlayerType.WICKETKEEPER)));
        team2.addPlayer((new Player("Ben Stokes",Player.PlayerType.ALLROUNDER)));
        team2.addPlayer((new Player("Moeen Ali",Player.PlayerType.ALLROUNDER)));
        team2.addPlayer((new Player("Eoin Morgan",Player.PlayerType.BATSMAN)));
        team2.addPlayer((new Player("Johny Bairstrow",Player.PlayerType.WICKETKEEPER)));
        team2.addPlayer((new Player("Aadil Rashid",Player.PlayerType.BOWLER)));
        team2.addPlayer((new Player("Jofra Archer",Player.PlayerType.BOWLER)));
        team2.addPlayer((new Player("Jason Roy",Player.PlayerType.BOWLER)));
        team2.addPlayer((new Player("Chris Wokes",Player.PlayerType.BOWLER)));
        team2.addPlayer((new Player("Tom Curran",Player.PlayerType.ALLROUNDER)));

        Match match=new Match(team1,team2,overLimit,seriesId,connection);
        match.startMatch();
    }
}
