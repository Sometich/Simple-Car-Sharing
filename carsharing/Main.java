package carsharing;

import org.h2.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main {
    public static Connection connection = null;
    private static Statement statement = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        Class<Driver> drive = Driver.class;
        try {
            connection = ConnectionManager.open();
            statement = connection.createStatement();
            boolean flag1 = true;

            statement.execute("CREATE TABLE IF NOT EXISTS company(id   INTEGER auto_increment primary key, name VARCHAR not null unique);");
            statement.execute("create table IF NOT EXISTS CAR(\n" +
                    "    id   INTEGER auto_increment primary key,\n" +
                    "    name VARCHAR not null unique,\n" +
                    "    company_id INTEGER not null,\n" +
                    "    CONSTRAINT ph_lol1 FOREIGN KEY (company_id) REFERENCES COMPANY(ID)\n" +
                    "                    );");
            statement.execute("create table IF NOT EXISTS CUSTOMER(\n" +
                    "    id   INTEGER auto_increment primary key,\n" +
                    "    name VARCHAR not null unique,\n" +
                    "    rented_car_id INTEGER NULL ,\n" +
                    "    CONSTRAINT ph_lol2 FOREIGN KEY (rented_car_id) REFERENCES CAR(ID)\n" +
                    "                    );");
            statement.execute("alter table company alter column id restart with 1;");
            statement.execute("alter table car alter column id restart with 1;");
            while (flag1) {
                System.out.println("1. Log in as a manager\n" +
                        "2. Log in as a customer\n" +
                        "3. Create a customer\n" +
                        "0. Exit");
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        menu1();
                        break;
                    case 2:
                        menu2();
                        break;
                    case 3:
                        menu3();
                        break;
                    case 0:
                        flag1 = false;
                        break;
                    default:
                        System.out.println("Not Correct statement");
                }

            }
        } finally {
            connection.close();
            statement.close();
        }
        // write your code here
    }









    public static void menu1() throws SQLException {
        boolean flag2 = true;
        while (flag2) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    var res = statement.executeQuery("SELECT id, name FROM company");
                    Map<Integer, String> companies = new HashMap<>();
                    while (res.next()) {
                        companies.put(res.getInt(1), res.getString(2));
                    }
                    if (companies.size() == 0) {
                        System.out.println("The company list is empty");
                    } else {
                        System.out.println("Choose the company:");
                        companies.forEach((a, b) -> System.out.println(a + ". " + b));
                        System.out.println("0. Back");
                        int chooseCompany = Integer.parseInt(scanner.nextLine());
                        if (companies.containsKey(chooseCompany)) {
                            System.out.println("'" + companies.get(chooseCompany) + "' company");
                            boolean flag3 = true;
                            while (flag3) {
                                System.out.println("1. Car list\n" +
                                        "2. Create a car\n" +
                                        "0. Back");
                                switch (Integer.parseInt(scanner.nextLine())) {
                                    case 1:
                                        String sql3 = "SELECT id, name FROM CAR WHERE company_id=" + chooseCompany + ";";
                                        var res1 = statement.executeQuery(sql3);
                                        List<String> cars = new ArrayList<>();
                                        while (res1.next()) {
                                            cars.add(res1.getString(2));
                                        }
                                        if (cars.size() == 0) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("Car list:");
                                            int count = 1;
                                            for (String e : cars) {
                                                System.out.println(count + ". " + e);
                                                count++;
                                            }
                                            System.out.println("0. Back");
                                        }
                                        break;
                                    case 2:
                                        System.out.println("Enter the car name:");
                                        String nameCar = scanner.nextLine();
                                        nameCar = "INSERT INTO CAR (name, company_id) VALUES ('" + nameCar + "', " + chooseCompany + ");";
                                        statement.execute(nameCar);
                                        System.out.println("The car was added!");
                                        break;
                                    case 0:
                                        flag3 = false;
                                }
                            }
                        }
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Enter the company name:");
                    String nameCompany = scanner.nextLine();
                    nameCompany = "INSERT INTO COMPANY (name) VALUES ('" + nameCompany + "');";
                    statement.execute(nameCompany);
                    System.out.println("The company was created!");
                    break;
                case 0:
                    flag2 = false;
                    break;
                default:
                    System.out.println("Not correct statement");
            }
        }
    }









    public static void menu2() throws SQLException {
        String sql3 = "SELECT id, name FROM CUSTOMER;";
        var res1 = statement.executeQuery(sql3);
        Map<Integer, String> customers = new HashMap();
        while (res1.next()) {
            customers.put(res1.getInt(1), res1.getString(2));
        }
        if (customers.size() == 0) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("The customer list:");
            customers.forEach((a, b) -> System.out.println(a + ". " + b));
            System.out.println("0. Back");
            int localSelect;
            switch (localSelect = Integer.parseInt(scanner.nextLine())) {
                case 0:
                    return;
                default:
                    boolean flag3 = true;
                    while (flag3) {
                        System.out.println("1. Rent a car\n" +
                                "2. Return a rented car\n" +
                                "3. My rented car\n" +
                                "0. Back");
                        switch (Integer.parseInt(scanner.nextLine())) {
                            case 1:
                                String sql11 = "SELECT rented_car_id FROM CUSTOMER WHERE id=" + localSelect + ";";
                                var try5 = statement.executeQuery(sql11);
                                try5.next();
                                int id_car5 = try5.getInt("rented_car_id");
                                if (id_car5 != 0) {
                                    System.out.println("You've already rented a car!");
                                    break;
                                }


                                int selectCompany;
                                var res = statement.executeQuery("SELECT id, name FROM company");
                                Map<Integer, String> companies = new HashMap<>();
                                while (res.next()) {
                                    companies.put(res.getInt(1), res.getString(2));
                                }
                                if (companies.size() == 0) {
                                    System.out.println("The company list is empty!");
                                    break;
                                } else {
                                    System.out.println("Choose the company:");
                                    companies.forEach((a, b) -> System.out.println(a + ". " + b));
                                    System.out.println("0. Back");
                                    selectCompany = Integer.parseInt(scanner.nextLine());
                                    if (selectCompany == 0) {
                                        break;
                                    }
                                }


                                int selectCar;
                                String nameCar;
                                String sql12 = "SELECT id, name FROM car WHERE id NOT IN (SELECT rented_car_id FROM customer WHERE rented_car_id <> 0) AND company_id=" + selectCompany + ";";
                                var res5 = statement.executeQuery(sql12);
                                Map<Integer, String> cars = new HashMap<>();
                                while (res5.next()) {
                                    cars.put(res5.getInt(1), res5.getString(2));
                                }
                                if (cars.size() == 0) {
                                    System.out.println("The car list is empty!");
                                    break;
                                } else {
                                    System.out.println("Choose a car:");
                                    cars.forEach((a, b) -> System.out.println(a + ". " + b));
                                    System.out.println("0. Back");
                                    selectCar = Integer.parseInt(scanner.nextLine());
                                    if (selectCar == 0) {
                                        break;
                                    }
                                    nameCar = cars.get(selectCar);
                                    System.out.println("You rented '" + nameCar + "'");
                                }

                                Map<Integer, String> carsTable = new HashMap<>();
                                String sql18 = "SELECT id, name FROM car WHERE company_id=" + selectCompany + ";";
                                var res8 = statement.executeQuery(sql18);
                                while (res8.next()) {
                                    carsTable.put(res8.getInt(1), res8.getString(2));
                                }
                                int localCar = 0;
                                for (Map.Entry<Integer, String> entry : carsTable.entrySet()) {
                                    if (entry.getValue().equals(nameCar)) {
                                        localCar = entry.getKey();
                                    }
                                }

                                String sql15 = "UPDATE customer SET rented_car_id=" + localCar + " WHERE id=" + localSelect + ";";
                                statement.execute(sql15);
                                break;
                            case 2:
                                String sql9 = "SELECT rented_car_id FROM CUSTOMER WHERE id=" + localSelect + ";";
                                var try2 = statement.executeQuery(sql9);
                                try2.next();
                                int id_car2 = try2.getInt("rented_car_id");
                                if (id_car2 == 0) {
                                    System.out.println("You didn't rent a car!");
                                    System.out.println();
                                    break;
                                }
                                String sql6 = "UPDATE customer SET rented_car_id=null WHERE id=" + localSelect + ";";
                                statement.execute(sql6);
                                System.out.println("You've returned a rented car!");
                                System.out.println();
                                break;
                            case 3:
                                String sql4 = "SELECT rented_car_id FROM CUSTOMER WHERE id=" + localSelect + ";";
                                var try1 = statement.executeQuery(sql4);
                                try1.next();
                                int id_car = try1.getInt("rented_car_id");
                                if (id_car == 0) {
                                    System.out.println("You didn't rent a car!");
                                    break;
                                }
                                String sql5 = "SELECT name, company_id FROM CAR WHERE id=" + id_car + ";";
                                var try17 = statement.executeQuery(sql5);
                                try17.next();
                                String nameOflocalCar = try17.getString("name");
                                int companylocalid = try17.getInt("company_id");

                                String sql55 = "SELECT name FROM company WHERE id=" + companylocalid + ";";
                                var try177 = statement.executeQuery(sql55);
                                try177.next();
                                String namecompanylocal = try177.getString("name");

                                System.out.println("Your rented car:\n" +
                                        nameOflocalCar + "\n" +
                                        "Company:\n" +
                                        namecompanylocal);
                                break;
                            case 0:
                                flag3 = false;
                        }
                    }

            }
        }
    }

    public static void menu3() throws SQLException {
        System.out.println("Enter the customer name:");
        String nameCustomer = scanner.nextLine();
        nameCustomer = "INSERT INTO customer (name) VALUES ('" + nameCustomer + "');";
        statement.execute(nameCustomer);
        System.out.println("The customer was added!");
        System.out.println();
    }

}