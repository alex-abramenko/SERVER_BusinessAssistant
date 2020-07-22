package Data.JDBC;

import Data.Wrapper.Command.CommAuthorization;
import Data.Wrapper.Command.CommGetUserInfo;
import Data.Wrapper.Command.CommRegistration;
import Data.Wrapper.Essence.User;

import java.sql.SQLException;

public class RegisteredUsersJDBC extends JDBC {
    private final String tableName = "RegisteredUsers";

    public RegisteredUsersJDBC() throws SQLException, ClassNotFoundException {
        connection();
    }

    public void addUser(CommRegistration d) throws SQLException {
        String sql = String.format(
                        "INSERT INTO \'%s\' " +
                        "(\'Login\', \'Password\', \'Phone\', \'Name\', \'TypeAccount\')" +
                        "VALUES(\'%s\', \'%s\', \'%s\', \'%s\', \'%s\');",
                        tableName, d.Login, d.Password, d.Phone, d.Name, d.TypeAccount);
        statmt.execute(sql);
        System.out.println("Данные добавлены!");
    }

    public boolean authorization(CommAuthorization d) throws SQLException {
        String sql = String.format("SELECT Password FROM %s WHERE Login=\"%s\"", tableName, d.Login);
        resSet = statmt.executeQuery(sql);
        resSet.next();
        String pass = resSet.getString("Password");
        resSet.close();

        if (d.Password.equals(pass))
            return true;
        return false;
    }

    public User getUserInfo(CommGetUserInfo d) throws SQLException {
        User u = new User();
        String sql;
        if (d.id != 0)
            sql = String.format("SELECT * FROM %s WHERE ID_User=%d", tableName, d.id);
        else
            sql = String.format("SELECT * FROM %s WHERE Login=\"%s\"", tableName, d.login);

        resSet = statmt.executeQuery(sql);
        resSet.next();

        u.id = resSet.getInt("ID_User");
        u.login = resSet.getString("Login");
        u.phone = resSet.getString("Phone");
        u.name = resSet.getString("Name");
        u.typeAcc = resSet.getString("TypeAccount");

        resSet.close();

        return u;
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
