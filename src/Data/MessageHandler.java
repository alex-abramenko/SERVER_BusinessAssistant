package Data;

import Data.JDBC.*;
import Data.Wrapper.Command.*;
import Data.Wrapper.Essence.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageHandler {

    public String startHandling(String msg) throws IOException {
        Command answer;

        StringWriter writer = new StringWriter();
        try {
            StringReader reader = new StringReader(msg);
            Command command = new ObjectMapper().readValue(reader, Command.class);
            answer = commandExe(command);

        } catch (IOException e) {
            e.printStackTrace();
            answer = new Command();
            answer.nameCommand = Command.ERROR_WRONG_FORMAT_MSG;
        }

        new ObjectMapper().writeValue(writer, answer);
        return writer.toString();
    }

    private Command commandExe(Command comm) throws IOException {
        StringReader reader;
        Command ans = new Command();

        switch (comm.nameCommand) {

            /************ РЕГИСТРАЦИЯ ************/
            case (Command.COMM_REG):
                reader = new StringReader(comm.jsonParams);
                CommRegistration regParams = new ObjectMapper().readValue(reader, CommRegistration.class);
                try {
                    new RegisteredUsersJDBC().addUser(regParams);
                    ans.nameCommand = Command.OK_REG;
                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_REG;
                    e.printStackTrace();
                }
                return ans;


            /************ АВТОРИЗАЦИЯ ************/
            case (Command.COMM_AUTH):
                reader = new StringReader(comm.jsonParams);
                CommAuthorization authParams = new ObjectMapper().readValue(reader, CommAuthorization.class);
                try {
                    if(new RegisteredUsersJDBC().authorization(authParams))
                        ans.nameCommand = Command.OK_AUTH;
                    else
                        ans.nameCommand = Command.ERROR_AUTH;
                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_AUTH;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ ИНФО О ПОЛЬЗОВАТЕЛЕ ************/
            case (Command.COMM_GETUSERINFO):
                reader = new StringReader(comm.jsonParams);
                CommGetUserInfo paramsGetInfo = new ObjectMapper().readValue(reader, CommGetUserInfo.class);
                try {
                    User user = new RegisteredUsersJDBC().getUserInfo(paramsGetInfo);
                    ans.nameCommand = Command.OK_GETUSERINFO;

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, user);

                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETUSERINFO;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ СПИСКА ВСЕХ КАТЕГОРИЙ ************/
            case (Command.COMM_GETALLCAT):
                try {
                    ArrayList<Category> categories = new ShopsCategoriesJDBC().getShopsCategories();

                    ans.nameCommand = Command.OK_GETALLCAT;

                    CommGetCategories commGetCategories = new CommGetCategories();
                    commGetCategories.jsonCategories = new String[categories.size()];


                    for (int i = 0; i < categories.size(); i++) {
                        StringWriter writer = new StringWriter();
                        new ObjectMapper().writeValue(writer, categories.get(i));
                        commGetCategories.jsonCategories[i] = writer.toString();
                    }

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, commGetCategories);
                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETALLCAT;
                    e.printStackTrace();
                }
                return ans;


            /************ ДОБАВЛЕНИЕ МАГАЗИНА ************/
            case (Command.COMM_ADDSHOP):
                reader = new StringReader(comm.jsonParams);
                CommAddShop shopParams = new ObjectMapper().readValue(reader, CommAddShop.class);
                try {
                    new ShopsJDBC().addShop(shopParams);
                    int idShop = new ShopsJDBC().getShopByIdCreator(shopParams.ID_Creator).ID_Shop;

                    new TradesJDBC().createTable(idShop);
                    new CostsJDBC().createTable(idShop);
                    new PurchProductsJDBC().createTable(idShop);
                    new ProductsJDBC().createTable(idShop);

                    ans.nameCommand = Command.OK_ADDSHOP;

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_ADDSHOP;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ ИНФО О МАГАЗИНЕ ************/
            case (Command.COMM_GETSHOP):
                reader = new StringReader(comm.jsonParams);
                CommGetShop paramsGetShop = new ObjectMapper().readValue(reader, CommGetShop.class);
                try {
                    Shop shop = new ShopsJDBC().getShopByIdCreator(paramsGetShop.ID_Creator);
                    ans.nameCommand = Command.OK_GETSHOP;

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, shop);

                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETSHOP;
                    e.printStackTrace();
                }
                return ans;


            /************ ДОБАВЛЕНИЕ ТОВАРА ************/
            case (Command.COMM_ADDPRODUCT):
                reader = new StringReader(comm.jsonParams);
                CommAddProduct productParams = new ObjectMapper().readValue(reader, CommAddProduct.class);
                try {
                    new ProductsJDBC().addProduct(productParams);

                    ans.nameCommand = Command.OK_ADDPRODUCT;

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_ADDPRODUCT;
                    e.printStackTrace();
                }
                return ans;


            /************ ДОБАВЛЕНИЕ ЗАКУПКИ ТОВАРА ************/
            case (Command.COMM_ADDPURCHP):
                reader = new StringReader(comm.jsonParams);
                CommAddPurchP purchPParams = new ObjectMapper().readValue(reader, CommAddPurchP.class);
                try {
                    new PurchProductsJDBC().addPurchP(purchPParams);
                    new ProductsJDBC().incrementProduct(purchPParams.ID_Product, purchPParams.Quantity, purchPParams.ID_Shop);
                    ans.nameCommand = Command.OK_ADDPURCHP;

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_ADDPURCHP;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПРОДУКТОВ МАГАЗИНА ************/
            case (Command.COMM_GETALLPRODUCT):
                reader = new StringReader(comm.jsonParams);
                CommGetAllProducts getAllProductsParams = new ObjectMapper().readValue(reader, CommGetAllProducts.class);
                try {
                    ArrayList<Product> products = new ProductsJDBC().getAllProduct(getAllProductsParams);

                    ans.nameCommand = Command.OK_GETALLPRODUCT;

                    CommGetAllProducts commGetAllProducts = new CommGetAllProducts();
                    commGetAllProducts.jsonProducts = new String[products.size()];


                    for (int i = 0; i < products.size(); i++) {
                        StringWriter writer = new StringWriter();
                        new ObjectMapper().writeValue(writer, products.get(i));
                        commGetAllProducts.jsonProducts[i] = writer.toString();
                    }

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, commGetAllProducts);
                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETALLCAT;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ ЗАКУПОК ТОВАРА ************/
            case (Command.COMM_GETPURCHPR):
                reader = new StringReader(comm.jsonParams);
                CommGetPurchPr getGetPurchPrParams = new ObjectMapper().readValue(reader, CommGetPurchPr.class);
                try {
                    ArrayList<PurchProducts> purchProducts = new PurchProductsJDBC().getPurchPr(getGetPurchPrParams);

                    ans.nameCommand = Command.OK_GETPURCHPR;

                    CommGetPurchPr commGetPurchPr = new CommGetPurchPr();
                    commGetPurchPr.jsonPurchPr = new String[purchProducts.size()];


                    for (int i = 0; i < purchProducts.size(); i++) {
                        StringWriter writer = new StringWriter();
                        new ObjectMapper().writeValue(writer, purchProducts.get(i));
                        commGetPurchPr.jsonPurchPr[i] = writer.toString();
                    }

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, commGetPurchPr);
                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETPURCHPR;
                    e.printStackTrace();
                }
                return ans;


            /************ ДОБАВЛЕНИЕ ЗАТРАТ ************/
            case (Command.COMM_ADDCOST):
                reader = new StringReader(comm.jsonParams);
                CommAddCost addCostParams = new ObjectMapper().readValue(reader, CommAddCost.class);
                try {
                    new CostsJDBC().addCost(addCostParams);
                    ans.nameCommand = Command.OK_ADDCOST;

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_ADDCOST;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ ЗАТРАТ ************/
            case (Command.COMM_GETCOST):
                reader = new StringReader(comm.jsonParams);
                CommGetCosts getCostsParams = new ObjectMapper().readValue(reader, CommGetCosts.class);
                try {
                    ArrayList<Cost> costs = new CostsJDBC().getCosts(getCostsParams);

                    ans.nameCommand = Command.OK_GETCOST;

                    CommGetCosts commGetCosts = new CommGetCosts();
                    commGetCosts.jsonCosts = new String[costs.size()];


                    for (int i = 0; i < costs.size(); i++) {
                        StringWriter writer = new StringWriter();
                        new ObjectMapper().writeValue(writer, costs.get(i));
                        commGetCosts.jsonCosts[i] = writer.toString();
                    }

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, commGetCosts);
                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETCOST;
                    e.printStackTrace();
                }
                return ans;


            /************ ДОБАВЛЕНИЕ ТРЕЙДА ************/
            case (Command.COMM_ADDTRADE):
                reader = new StringReader(comm.jsonParams);
                CommAddTrade tradeParams = new ObjectMapper().readValue(reader, CommAddTrade.class);
                try {
                    new ProductsJDBC().decrementProduct(tradeParams.ID_Product, tradeParams.Quantity, tradeParams.ID_Shop);
                    new TradesJDBC().addTrade(tradeParams);
                    ans.nameCommand = Command.OK_ADDTRADE;

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_ADDTRADE;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ ТРЕЙДОВ ************/
            case (Command.COMM_GETTRADE):
                reader = new StringReader(comm.jsonParams);
                CommGetTrades getTradesParams = new ObjectMapper().readValue(reader, CommGetTrades.class);
                try {
                    ArrayList<Trade> trades = new TradesJDBC().getTrades(getTradesParams);

                    ans.nameCommand = Command.OK_GETTRADE;

                    CommGetTrades commGetTrades = new CommGetTrades();
                    commGetTrades.jsonTrades = new String[trades.size()];


                    for (int i = 0; i < trades.size(); i++) {
                        StringWriter writer = new StringWriter();
                        new ObjectMapper().writeValue(writer, trades.get(i));
                        commGetTrades.jsonTrades[i] = writer.toString();
                    }

                    StringWriter writer = new StringWriter();
                    new ObjectMapper().writeValue(writer, commGetTrades);
                    ans.jsonParams = writer.toString();

                } catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETTRADE;
                    e.printStackTrace();
                }
                return ans;


            /************ ПОЛУЧЕНИЕ ОТЧЕТОВ ************/
            case (Command.COMM_GETREPORT):
                reader = new StringReader(comm.jsonParams);
                CommGetReport getReportParams = new ObjectMapper().readValue(reader, CommGetReport.class);

                ans.nameCommand = Command.OK_GETREPORT;
                ArrayList<Product> products;

                try {
                    CommGetAllProducts commPr = new CommGetAllProducts();
                    commPr.ID_Shop = getReportParams.ID_Shop;
                    products = new ProductsJDBC().getAllProduct(commPr);

                }
                catch (SQLException | ClassNotFoundException e) {
                    ans.nameCommand = Command.ERROR_GETREPORT;
                    e.printStackTrace();
                    return ans;
                }

                Report report = new Report();

                report.idProducts = new int[products.size()];
                report.tittleProducts = new String[products.size()];
                report.detailProducts = new String[products.size()];
                for (int i = 0; i < products.size(); i++) {
                    report.idProducts[i] = products.get(i).ID_Product;
                    report.tittleProducts[i] = products.get(i).Tittle;
                    report.detailProducts[i] = products.get(i).Detail;
                }

                CommGetTrades commGetTrades = new CommGetTrades();
                commGetTrades.startDate = getReportParams.startDate;
                commGetTrades.endDate = getReportParams.endDate;
                commGetTrades.ID_Shop = getReportParams.ID_Shop;

                report.priceProducts = new float[products.size()];
                report.quanProducts = new int[products.size()];
                for (int i = 0; i < report.idProducts.length; i++) {
                    report.priceProducts[i] = 0f;
                    report.quanProducts[i] = 0;

                    try {
                        ArrayList<Trade> trades = new TradesJDBC().getTrades(commGetTrades, report.idProducts[i]);

                        for (int j = 0; j < trades.size(); j++) {
                            report.priceProducts[i] += trades.get(j).Price;
                            report.quanProducts[i] += trades.get(j).Quantity;
                        }
                    }
                    catch (SQLException | ClassNotFoundException e) { }
                }

                report.costs = 0;

                CommGetCosts commGetCosts = new CommGetCosts();
                commGetCosts.startDate = getReportParams.startDate;
                commGetCosts.endDate = getReportParams.endDate;;
                commGetCosts.ID_Shop = getReportParams.ID_Shop;

                try {
                    ArrayList<Cost> costs = new CostsJDBC().getCosts(commGetCosts);

                    for (int j = 0; j < costs.size(); j++) {
                        report.costs += costs.get(j).Price;
                    }
                }
                catch (SQLException | ClassNotFoundException e) { }

                CommGetPurchPr commGetPurchPr = new CommGetPurchPr();
                commGetPurchPr.startDate = getReportParams.startDate;
                commGetPurchPr.endDate = getReportParams.endDate;;
                commGetPurchPr.ID_Shop = getReportParams.ID_Shop;

                try {
                    ArrayList<PurchProducts> purchProducts = new PurchProductsJDBC().getPurchPr(commGetPurchPr);

                    for (int j = 0; j < purchProducts.size(); j++) {
                        report.costs += purchProducts.get(j).Price;
                    }
                }
                catch (SQLException | ClassNotFoundException e) { }

                report.revenue = 0;

                for (int i = 0; i < report.priceProducts.length; i++) {
                    report.revenue += report.priceProducts[i];
                }

                report.profit = report.revenue - report.costs;

                StringWriter writer = new StringWriter();
                new ObjectMapper().writeValue(writer, report);
                ans.jsonParams = writer.toString();

                return ans;



            /********* НЕИЗВЕСТНАЯ КОМАНДА *******/
            default:
                ans.nameCommand = Command.ERROR_UNKNOWN_COMMAND;
                return ans;
        }
    }
}

