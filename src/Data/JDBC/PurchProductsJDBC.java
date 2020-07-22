package Data.JDBC;

import Data.Wrapper.Command.CommAddPurchP;
import Data.Wrapper.Command.CommGetPurchPr;
import Data.Wrapper.Essence.PurchProducts;

import java.sql.SQLException;
import java.util.ArrayList;

public class PurchProductsJDBC extends JDBC {
    private final String baseTableName = "_PurchProducts";

    public PurchProductsJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public void createTable(int idShop) throws SQLException {
        String sql = String.format("CREATE TABLE \'%s\' " +
                        "(ID_PurchProducts INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL," +
                        "ID_Product INTEGER NOT NULL ON CONFLICT FAIL," +
                        "Quantity INTEGER NOT NULL ON CONFLICT FAIL," +
                        "Price DOUBLE NOT NULL ON CONFLICT FAIL," +
                        "Date INTEGER NOT NULL ON CONFLICT FAIL);",
                String.format("%s%s",idShop,baseTableName));

        statmt.execute(sql);
    }

    public void addPurchP(CommAddPurchP d) throws SQLException {
        String sql = String.format(
                "INSERT INTO \'%s\' " +
                        "(\'ID_Product\', \'Quantity\', \'Price\', \'Date\')" +
                        "VALUES(\'%d\', \'%d\', \'%f\', \'%d\');",
                String.format("%s%s",d.ID_Shop,baseTableName), d.ID_Product, d.Quantity, d.Price, d.Date);
        statmt.execute(sql);
        System.out.println("Данные добавлены!");
    }

    public ArrayList<PurchProducts> getPurchPr(CommGetPurchPr d) throws SQLException {
        String sql = String.format("SELECT * FROM \'%s\' WHERE Date >= %d AND DATE <= %d",
                String.format("%s%s", d.ID_Shop,baseTableName), d.startDate, d.endDate);
        resSet = statmt.executeQuery(sql);

        ArrayList<PurchProducts> purch = new ArrayList<>();
        while (resSet.next()) {
            PurchProducts pr = new PurchProducts();
            pr.ID_PurchP = resSet.getInt("ID_PurchProducts");
            pr.ID_Product = resSet.getInt("ID_Product");
            pr.Quantity = resSet.getInt("Quantity");
            pr.Price = (float) resSet.getDouble("Price");
            pr.Date = resSet.getLong("Date");
            purch.add(pr);
        }
        resSet.close();
        return purch;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}

