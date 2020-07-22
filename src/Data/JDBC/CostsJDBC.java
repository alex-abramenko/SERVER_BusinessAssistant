package Data.JDBC;

import Data.Wrapper.Command.CommAddCost;
import Data.Wrapper.Command.CommGetCosts;
import Data.Wrapper.Essence.Cost;

import java.sql.SQLException;
import java.util.ArrayList;

public class CostsJDBC extends JDBC {
    private final String baseTableName = "_Costs";

    public CostsJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public void createTable(int idShop) throws SQLException {
        String sql = String.format("CREATE TABLE \'%s\' " +
                        "(ID_Cost INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL," +
                        "Tittle STRING  NOT NULL ON CONFLICT FAIL," +
                        "Price DOUBLE NOT NULL ON CONFLICT FAIL," +
                        "Date INTEGER NOT NULL ON CONFLICT FAIL);",
                String.format("%s%s",idShop,baseTableName));

        statmt.execute(sql);
    }

    public void addCost(CommAddCost d) throws SQLException {
        String sql = String.format(
                "INSERT INTO \'%s\' " +
                        "(\'Tittle\', \'Price\', \'Date\')" +
                        "VALUES(\'%s\', \'%f\', \'%d\');",
                String.format("%s%s",d.ID_Shop,baseTableName), d.Tittle, d.Price, d.Date);
        statmt.execute(sql);
        System.out.println("Данные добавлены!");
    }

    public ArrayList<Cost> getCosts(CommGetCosts d) throws SQLException {
        String sql = String.format("SELECT * FROM \'%s\' WHERE Date >= %d AND DATE <= %d",
                String.format("%s%s", d.ID_Shop,baseTableName), d.startDate, d.endDate);
        resSet = statmt.executeQuery(sql);

        ArrayList<Cost> costs = new ArrayList<>();
        while (resSet.next()) {
            Cost pr = new Cost();
            pr.ID_Cost = resSet.getInt("ID_Cost");
            pr.Tittle = resSet.getString("Tittle");
            pr.Price = (float) resSet.getDouble("Price");
            pr.Date = resSet.getLong("Date");
            costs.add(pr);
        }
        resSet.close();
        return costs;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
