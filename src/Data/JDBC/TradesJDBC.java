package Data.JDBC;

import Data.Wrapper.Command.CommAddTrade;
import Data.Wrapper.Command.CommGetTrades;
import Data.Wrapper.Essence.Trade;

import java.sql.SQLException;
import java.util.ArrayList;

public class TradesJDBC extends JDBC {
    private final String baseTableName = "_Trades";

    public TradesJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public void createTable(int idShop) throws SQLException {
        String sql = String.format("CREATE TABLE \'%s\' " +
                "(ID_Trade INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL," +
                "ID_Product INTEGER NOT NULL ON CONFLICT FAIL," +
                "Quantity INTEGER NOT NULL ON CONFLICT FAIL," +
                "Price DOUBLE NOT NULL ON CONFLICT FAIL," +
                "Date INTEGER NOT NULL ON CONFLICT FAIL);",
                String.format("%s%s",idShop,baseTableName));

        statmt.execute(sql);
    }

    public void addTrade(CommAddTrade d) throws SQLException {
        String sql = String.format(
                "INSERT INTO \'%s\' " +
                        "(\'ID_Product\', \'Quantity\', \'Price\', \'Date\')" +
                        "VALUES(\'%d\', \'%d\', \'%f\', \'%d\');",
                String.format("%s%s",d.ID_Shop,baseTableName), d.ID_Product, d.Quantity, d.Price, d.Date);
        statmt.execute(sql);
        System.out.println("Данные добавлены!");
    }

    public ArrayList<Trade> getTrades(CommGetTrades d) throws SQLException {
        String sql = String.format("SELECT * FROM \'%s\' WHERE Date >= %d AND Date <= %d",
                String.format("%s%s", d.ID_Shop,baseTableName), d.startDate, d.endDate);
        resSet = statmt.executeQuery(sql);

        ArrayList<Trade> trades = new ArrayList<>();
        while (resSet.next()) {
            Trade tr = new Trade();
            tr.ID_Trade = resSet.getInt("ID_Trade");
            tr.ID_Product = resSet.getInt("ID_Product");
            tr.Quantity = resSet.getInt("Quantity");
            tr.Price = (float) resSet.getDouble("Price");
            tr.Date = resSet.getLong("Date");
            trades.add(tr);
        }
        resSet.close();
        return trades;
    }

    public ArrayList<Trade> getTrades(CommGetTrades d, int id) throws SQLException {
        String sql = String.format("SELECT * FROM \'%s\' WHERE Date >= %d AND Date <= %d AND ID_Product=%d",
                String.format("%s%s", d.ID_Shop,baseTableName), d.startDate, d.endDate, id);
        resSet = statmt.executeQuery(sql);

        ArrayList<Trade> trades = new ArrayList<>();
        while (resSet.next()) {
            Trade tr = new Trade();
            tr.ID_Trade = resSet.getInt("ID_Trade");
            tr.ID_Product = resSet.getInt("ID_Product");
            tr.Quantity = resSet.getInt("Quantity");
            tr.Price = (float) resSet.getDouble("Price");
            tr.Date = resSet.getLong("Date");
            trades.add(tr);
        }
        resSet.close();
        return trades;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
