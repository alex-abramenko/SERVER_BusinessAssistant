package Data.JDBC;

import Data.Wrapper.Command.CommAddShop;
import Data.Wrapper.Essence.Shop;

import java.sql.SQLException;

public class ShopsJDBC extends JDBC {
    private final String tableName = "Shops";

    public ShopsJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public void addShop(CommAddShop d) throws SQLException {
//        String sql = String.format(
//                        "INSERT INTO \'%s\' " +
//                        "(\'Tittle\', \'ID_Creator\', \'TypeShop\', \'Town\', \'Address\', " +
//                        "\'isCategory_1\', \'isCategory_2\', \'isCategory_3\', \'isCategory_4\'" +
//                        "\'isCategory_5\', \'isCategory_6\', \'isCategory_7\', \'isCategory_8\'" +
//                        "\'isCategory_9\', \'isCategory_10\', \'isCategory_11\', \'isCategory_12\'" +
//                        "\'isCategory_13\', \'isCategory_14\', \'isCategory_15\', \'isCategory_16\') " +
//                        "VALUES(\'%s\', %d, \'%s\', \'%s\', \'%s\', " +
//                        "\'%d\', \'%d\', \'%d\', \'%d\', \'%d\', \'%d\', \'%d\', \'%d\', \'%d\', " +
//                        "\'%d\', \'%d\', \'%d\', \'%d\', \'%d\', \'%d\', \'%d\');",
//                        tableName, d.Tittle, d.ID_Creator, d.TypeShop, d.Town, d.Address,
//                d.isCategory[0], d.isCategory[1], d.isCategory[2], d.isCategory[3], d.isCategory[4],
//                d.isCategory[5], d.isCategory[6], d.isCategory[7], d.isCategory[8], d.isCategory[9],
//                d.isCategory[10], d.isCategory[11], d.isCategory[12], d.isCategory[13], d.isCategory[14],
//                d.isCategory[15]);

        String sql = String.format("INSERT INTO \'%s\' " +
                "(\'Tittle\', \'ID_Creator\', \'TypeShop\', \'Town\', \'Address\')" +
                "VALUES(\'%s\', \'%d\', \'%s\', \'%s\', \'%s\');"
                ,tableName, d.Tittle, d.ID_Creator, d.TypeShop, d.Town, d.Address);
        // TODO: 20.02.2020 ДОБАВИТЬ ДОБАВЛЕНИЕ isCategory
        statmt.execute(sql);

        System.out.println("Данные добавлены!");
    }

    public Shop getShopByIdCreator(int idCr) throws SQLException {
        Shop s = new Shop();
        String sql;
        sql = String.format("SELECT * FROM %s WHERE ID_Creator=%d", tableName, idCr);

        resSet = statmt.executeQuery(sql);
        resSet.next();

        s.Address = resSet.getString("Address");
        s.ID_Creator = resSet.getInt("ID_Creator");
        s.ID_Shop = resSet.getInt("ID_Shop");
        s.TypeShop = resSet.getString("TypeShop");
        s.Town = resSet.getString("Town");
        s.Tittle = resSet.getString("Tittle");
        for (int i = 0; i < s.isCategory.length; i++) {
            s.isCategory[i] = resSet.getInt("isCategory_" + (i+1));
        }

        resSet.close();

        return s;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
