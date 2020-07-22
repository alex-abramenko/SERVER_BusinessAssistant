package Data.JDBC;

import Data.Wrapper.Command.CommAddProduct;
import Data.Wrapper.Command.CommGetAllProducts;
import Data.Wrapper.Essence.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductsJDBC extends JDBC {
    private final String baseTableName = "_Products";

    public ProductsJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public void createTable(int idShop) throws SQLException {
        String sql = String.format("CREATE TABLE \'%s\' " +
                        "(ID_Product INTEGER PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL," +
                        "Tittle STRING  NOT NULL ON CONFLICT FAIL," +
                        "Detail STRING  NOT NULL ON CONFLICT FAIL," +
                        "Price DOUBLE NOT NULL ON CONFLICT FAIL," +
                        "Quantity INTEGER DEFAULT (0));",
                String.format("%s%s",idShop,baseTableName));


        statmt.execute(sql);
    }

    public void addProduct(CommAddProduct d) throws SQLException {
        String sql = String.format(
                "INSERT INTO \'%s\' " +
                        "(\'Tittle\', \'Detail\', \'Price\')" +
                        "VALUES(\'%s\', \'%s\', \'%f\');",
                String.format("%s%s",d.ID_Shop,baseTableName), d.Tittle, d.Detail, d.Price);
        statmt.execute(sql);
        System.out.println("Данные добавлены!");
    }

    public void incrementProduct(int id, int inc, int ID_SHOP) throws SQLException {
        String sql = String.format(
                "SELECT Quantity FROM \'%s\' WHERE ID_Product=\"%d\"", String.format("%s%s",ID_SHOP,baseTableName),
                id);
        resSet = statmt.executeQuery(sql);
        resSet.next();

        int q = resSet.getInt("Quantity");
        q += inc;

        sql = String.format("UPDATE \'%s\'  SET Quantity = \'%d\' WHERE ID_Product = \'%d\';", String.format("%s%s",ID_SHOP,baseTableName),
                q, id);
        statmt.execute(sql);
        resSet.close();
    }

    public void decrementProduct(int id, int dec, int ID_SHOP) throws SQLException {
        String sql = String.format(
                "SELECT Quantity FROM \'%s\' WHERE ID_Product=\"%d\"", String.format("%s%s",ID_SHOP,baseTableName),
                id);
        resSet = statmt.executeQuery(sql);
        resSet.next();

        int q = resSet.getInt("Quantity");
        q -= dec;

        if(q < 0)
            statmt.execute("ERROR");

        sql = String.format("UPDATE \'%s\'  SET Quantity = \'%d\' WHERE ID_Product = \'%d\';", String.format("%s%s",ID_SHOP,baseTableName),
                q, id);
        statmt.execute(sql);
        resSet.close();
    }

    public ArrayList<Product> getAllProduct(CommGetAllProducts d) throws SQLException {
        String sql = String.format("SELECT * FROM \'%s\'", String.format("%s%s", d.ID_Shop,baseTableName));
        resSet = statmt.executeQuery(sql);

        ArrayList<Product> products = new ArrayList<>();
        while (resSet.next()) {
            Product pr = new Product();
            pr.ID_Product = resSet.getInt("ID_Product");
            pr.Tittle = resSet.getString("Tittle");
            pr.Detail = resSet.getString("Detail");
            pr.Quantity = resSet.getInt("Quantity");
            pr.Price = (float) resSet.getDouble("Price");
            products.add(pr);
        }
        resSet.close();
        return products;
    }

    public Product getProduct(int id, int ID_Shop) throws SQLException {
        String sql = String.format("SELECT * FROM \'%s\' WHERE ID_Product = %d",
                String.format("%s%s", ID_Shop,baseTableName), id);
        resSet = statmt.executeQuery(sql);

        resSet.next();
        Product pr = new Product();
        pr.ID_Product = resSet.getInt("ID_Product");
        pr.Tittle = resSet.getString("Tittle");
        pr.Detail = resSet.getString("Detail");
        pr.Quantity = resSet.getInt("Quantity");
        pr.Price = (float) resSet.getDouble("Price");

        resSet.close();
        return pr;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
