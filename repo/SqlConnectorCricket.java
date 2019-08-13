package repo;

import cricket.DbConnector;
import cricket.Player;
import cricket.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConnectorCricket implements CricketDbConnector {

    private static PreparedStatement preparedStatement=null;
    private static ResultSet resultSet=null;


    @Override
    public void updateBallData(int ballNum, int match_id, Player batsmanOnStrike, Player otherBatsman, int currRun, boolean firstInningComplete, Player currBowler, int runsScored, int wicketsLost, int runSpecifyingIfOut) {
            try{
                Connection cn=DbConnector.getConnection();
                int inningNumber=firstInningComplete?2:1;
                int totalWickets=currRun==runSpecifyingIfOut?wicketsLost+1:wicketsLost;
                int runs_scored_this_ball=currRun==runSpecifyingIfOut?0:currRun;
                int total_runs_scored_till_now=runs_scored_this_ball+runsScored;
                int wicketLostThisball=currRun==runSpecifyingIfOut?1:0;
                String query=" insert into ball_data (match_id,ball_no,inning_no,bowler,batsman1,batsman2,wicket,runs_scored,total_runs_scored,total_wickets) values("+match_id+","+ballNum+","+inningNumber+",'"+currBowler+"','"+batsmanOnStrike+"','"+otherBatsman+"',"+wicketLostThisball+","+runs_scored_this_ball+","+total_runs_scored_till_now+","+totalWickets+")";
                preparedStatement=cn.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
            catch(SQLException se){
                se.printStackTrace();
            }
    }

    @Override
    public void updateSeriesData(int seriesId, Team matchWinner, Team team1, Team team2) {
        try{
            if(matchWinner==null){
                return;
            }
            int colToUpdate=matchWinner.toString().equals(team1.toString())?1:2;
            Connection cn=DbConnector.getConnection();
            String query="select match_won_team1,match_won_team2 from series_data where series_id="+seriesId;

            preparedStatement=cn.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
            int team1MatchWon=0;
            int team2MatchWon=0;
            while (resultSet.next()){
                team1MatchWon=resultSet.getInt(1);
                team2MatchWon=resultSet.getInt(2);
            }
            int updatedValue=colToUpdate==1?team1MatchWon+1:team2MatchWon+1;
            query="update series_data set match_won_team"+colToUpdate+"="+updatedValue+" where series_id="+seriesId;
            preparedStatement=cn.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (SQLException se){
            se.printStackTrace();
        }
    }

    @Override
    public int insertSeriesInfo(String team1, String team2, int numOfMatches) {
        try{
           Connection cn= DbConnector.getConnection();
           preparedStatement=cn.prepareStatement("insert into series_data (team1,team2,num_matches_in_series) values('"+ team1 +"','"+team2+"',"+numOfMatches+")");
           preparedStatement.executeUpdate();

           String query="Select series_id from series_data order by series_id desc limit 1";
           preparedStatement=cn.prepareStatement(query);
           resultSet=preparedStatement.executeQuery();
           int id=-1;
           while(resultSet.next()){
               id=resultSet.getInt(1);
           }
           return id;

        }
        catch (SQLException se){
            se.printStackTrace();

        }

        return -1;
    }

    @Override
    public int updateMatchTossWinner(int seriesId, Team tossWinner, Team battingFirst, Team team1, Team team2, int overLimit) {
        try{
            Connection cn= DbConnector.getConnection();
            System.out.println("insert into match_data(series_id,team1,team2,toss_winner,over_limit,battingFirst) values ("+seriesId+",'"+team1+"','"+team2+"','"+tossWinner+"',"+overLimit+",'"+battingFirst+"'");
            preparedStatement=cn.prepareStatement("insert into match_data(series_id,team1,team2,toss_winner,over_limit,battingFirst,match_complete) values ("+seriesId+",'"+team1+"','"+team2+"','"+tossWinner+"',"+overLimit+",'"+battingFirst+"','n')");
            preparedStatement.executeUpdate();
            String query="Select match_id from match_data order by match_id desc limit 1";
            preparedStatement=cn.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
            int id=-1;
            while(resultSet.next()){
                id=resultSet.getInt(1);
            }
            return id;


        }
        catch (SQLException se){
            se.printStackTrace();

        }
        return -1;
    }

    @Override
    public void updateMatchTargetSet(int matchId, int targetSet) {
        try{
            Connection cn= DbConnector.getConnection();
            preparedStatement=cn.prepareStatement("update match_data set target_set="+targetSet+" where match_id="+matchId);
            preparedStatement.executeUpdate();
        }
        catch(SQLException se){
            se.printStackTrace();
        }

    }

    @Override
    public void updatePlayerDataBatsman(int ballNum, int match_id, int currRun, int runSpecifyingIfOut, Player player) {
        try{
            Connection cn=DbConnector.getConnection();
            if(!isPlayerPresentInPlayerPerformance(match_id,player)){
                int runs_scored=currRun==runSpecifyingIfOut?0:currRun;
                int four_hit=currRun==4?1:0;
                int six_hit=currRun==6?1:0;
                String is_out;
                if(currRun==runSpecifyingIfOut){
                    is_out="y";
                }
                else{
                    is_out="n";
                }
                String query="insert into player_performance (match_id,runs_scored,balls_played,player_name,is_out,four_hit,six_hit) values("+match_id+","+ runs_scored+",1,'"+player+"','"+is_out+"',"+four_hit+","+six_hit+")";
                preparedStatement=cn.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
            else{
                String query="select unique_id,runs_scored,balls_played,four_hit,six_hit from player_performance where match_id="+match_id+" and player_name='"+player+"'";
                preparedStatement=cn.prepareStatement(query);
                resultSet=preparedStatement.executeQuery();
                int runsScoredTillNow=0;
                int fourHitTillNow=0;
                int sixHitTillNow=0;
                int ballsPlayedTillNow=0;
                int unique_id=0;
                String is_out;
                if(currRun==runSpecifyingIfOut){
                    is_out="y";
                }
                else{
                    is_out="n";
                }
                while (resultSet.next()){
                    unique_id=resultSet.getInt(1);
                    fourHitTillNow=resultSet.getInt(4);
                    sixHitTillNow=resultSet.getInt(5);
                    runSpecifyingIfOut=resultSet.getInt(2);
                    ballsPlayedTillNow=resultSet.getInt(3);
                }
                int runs_scored=currRun==runSpecifyingIfOut?runsScoredTillNow:runsScoredTillNow+currRun;
                int four_hit=currRun==4?fourHitTillNow+1:fourHitTillNow;
                int six_hit=currRun==6?sixHitTillNow+1:sixHitTillNow;
                int balls_played=ballsPlayedTillNow+1;

                query="update player_performance set four_hit="+four_hit+", six_hit="+six_hit+",runs_scored="+runs_scored+",balls_played="+balls_played+",is_out='"+is_out+"' where unique_id="+unique_id;
                preparedStatement=cn.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }


    }

    @Override
    public void updatePlayerDataBowler(int ballNum, int match_id, int currRun, int runSpecifyingIfOut, Player currBowler) {
        try{
            Connection cn=DbConnector.getConnection();
            if(!isPlayerPresentInPlayerPerformance(match_id,currBowler)){
                int runsGivenThisBall=currRun==runSpecifyingIfOut?0:currRun;
                int wicket_taken_in_total=currRun==runSpecifyingIfOut?1:0;
                String query="insert into player_performance (match_id,balls_delivered,runs_given,wickets_taken,player_name) values ("+match_id+",1,"+runsGivenThisBall+","+wicket_taken_in_total+",'"+currBowler+"')";
                preparedStatement=cn.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
            else{
                String query="select balls_delivered,runs_given,wickets_taken from player_performance where match_id="+match_id+" and player_name='"+currBowler+"'";
                preparedStatement=cn.prepareStatement(query);
                resultSet=preparedStatement.executeQuery();
                int runsGivenTillNow=0;
                int wicketsTakenTillNow=0;
                int ballsDeliveredTillNow=0;

                while(resultSet.next()){
                    runsGivenTillNow=resultSet.getInt(2);
                    ballsDeliveredTillNow=resultSet.getInt(1);
                    wicketsTakenTillNow=resultSet.getInt(3);
                }
                int runsGivenThisBall=currRun==runSpecifyingIfOut?0:currRun;
                int wicket_taken_in_total=currRun==runSpecifyingIfOut?1+wicketsTakenTillNow:0+wicketsTakenTillNow;
                int runsGivenTotal=runsGivenThisBall+runsGivenTillNow;
                int totalBallsDelivered=ballsDeliveredTillNow+1;
                query="update player_performance set balls_delivered="+totalBallsDelivered+",runs_given="+runsGivenTotal+",wickets_taken="+wicket_taken_in_total+" where match_id="+match_id+" and player_name='"+currBowler+"'";
                preparedStatement=cn.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException se){
            se.printStackTrace();
        }

    }

    @Override
    public void updateMatchWinner(int matchId, Team matchWinner, int wonByRuns, int wonByWickets) {
        try{
            Connection cn=DbConnector.getConnection();
            String query="update match_data set match_winner='"+matchWinner+"',won_by_runs="+wonByRuns+",won_by_wickets="+wonByWickets+",match_complete='y' where match_id="+matchId;
            preparedStatement=cn.prepareStatement(query);
            preparedStatement.executeUpdate();
        }
        catch(SQLException se){
            se.printStackTrace();;
        }

    }

    @Override
    public void updateSeriesWinner(int seriesId) {
        try{
            Connection cn= DbConnector.getConnection();
            String query="select team1,team2,match_won_team1,match_won_team2 from series_data where series_id="+seriesId;
            preparedStatement=cn.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
            String team1Name="";
            String team2Name="";
            int team1Wins=0;
            int team2Wins=0;
            while(resultSet.next()){
                team1Name=resultSet.getString(1);
                team2Name=resultSet.getString(2);
                team1Wins=resultSet.getInt(3);
                team2Wins=resultSet.getInt(4);
            }

            String match_winner=team1Wins>team2Wins?team1Name:team1Wins==team2Wins?"draw":team2Name;

            preparedStatement=cn.prepareStatement("update series_data set series_winner='"+match_winner+"' where series_id="+seriesId);
            preparedStatement.executeUpdate();
        }
        catch(SQLException se){
            se.printStackTrace();
        }

    }
    private boolean isPlayerPresentInPlayerPerformance(int matchId,Player player){
        try{
            Connection cn=DbConnector.getConnection();
            String query="select unique_id from player_performance where match_id="+matchId+" and player_name='"+player+"'";
            preparedStatement=cn.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
            int i=0;
            while(resultSet.next()){
                i++;
            }
            if(i!=0){
                return true;
            }
            return false;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return false;
    }
}
