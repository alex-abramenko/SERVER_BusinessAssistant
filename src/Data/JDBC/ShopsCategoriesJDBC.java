package Data.JDBC;

import Data.Wrapper.Essence.Category;

import java.sql.SQLException;
import java.util.ArrayList;

public class ShopsCategoriesJDBC extends JDBC {
    private final String tableName = "ShopsCategories";

    public ShopsCategoriesJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public ArrayList<Category> getShopsCategories() throws SQLException {
        String sql = String.format("SELECT * FROM %s", tableName);
        resSet = statmt.executeQuery(sql);

        ArrayList<Category> categories = new ArrayList<>();
        while (resSet.next()) {
            Category cat = new Category();
            cat.id = resSet.getInt("ID_Category");
            cat.tittle = resSet.getString("Tittle");
            categories.add(cat);
        }
        resSet.close();
        return categories;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
