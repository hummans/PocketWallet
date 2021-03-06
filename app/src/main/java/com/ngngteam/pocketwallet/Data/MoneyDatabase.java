package com.ngngteam.pocketwallet.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ngngteam.pocketwallet.Model.ExpenseItem;
import com.ngngteam.pocketwallet.Model.IncomeItem;
import com.ngngteam.pocketwallet.Model.RecurrentTransaction;
import com.ngngteam.pocketwallet.Utils.SharedPrefsManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikos on 7/26/2014.
 */
public class MoneyDatabase extends SQLiteOpenHelper {

    private static final int Database_Version = 3;
    private static final String Database_Name = "MoneyDatabase";

    private static final String Table_Expense = "Expense";
    private static final String Table_Income = "Income";
    private static final String TABLE_RECURRENT = "recurrent_transactions";

    //Table Expense Columns
    private static final String Key_EId = "_id";
    private static final String Key_ECategory = "category";
    private static final String Key_EDate = "date";
    private static final String Key_EPrice = "price";
    private static final String Key_ENotes = "notes";

    //Table Income Columns
    private static final String Key_Iid = "_id";
    private static final String Key_IAmount = "amount";
    private static final String Key_ISource = "source";
    private static final String Key_IDate = "date";
    private static final String Key_INotes = "notes";

    //Table Recurrent Columns
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";
    private static final String KEY_FREQ = "frequency";
    private static final String KEY_INTERVAL = "interval";
    private static final String KEY_DAY = "day";
    private static final String KEY_EXPIRATION = "expiration";
    private static final String KEY_NEXT_DATE = "nextDate";
    private static final String KEY_ISEXPENSE = "isExpense";
    private static final String KEY_ISVALID = "isValid";

    private SQLiteDatabase mydb;

    private static final String Create_Expense_Table = "CREATE TABLE IF NOT EXISTS " + Table_Expense + "(" + Key_EId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Key_ECategory + " TEXT NOT NULL," + Key_EDate + " TEXT NOT NULL," + Key_EPrice + " DOUBLE," + Key_ENotes + " TEXT)";

    private static final String Create_Income_Table = "CREATE TABLE IF NOT EXISTS " + Table_Income + "(" + Key_Iid + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Key_IAmount + " DOUBLE," + Key_ISource + " TEXT NOT NULL," + Key_IDate + " TEXT NOT NULL," + Key_ENotes + " TEXT" + ")";

    private static final String Create_Expense_Recurrent = "CREATE TABLE IF NOT EXISTS " + TABLE_RECURRENT
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_NAME + " TEXT NOT NULL," + KEY_AMOUNT + " DOUBLE NOT NULL," + KEY_CATEGORY + " TEXT NOT NULL,"
            + KEY_DATE + " TEXT NOT NULL," + KEY_FREQ + " INTEGER NOT NULL," + KEY_INTERVAL + " TEXT NOT NULL,"
            + KEY_DAY + " INTEGER," + KEY_EXPIRATION + " TEXT," + KEY_NEXT_DATE + " TEXT NOT NULL,"
            + KEY_ISEXPENSE + " INTEGER," + KEY_ISVALID + " INTEGER);";

    private Context context;

    public MoneyDatabase(Context context) {
        super(context, Database_Name, null, Database_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Expense_Table);
        sqLiteDatabase.execSQL(Create_Income_Table);
        sqLiteDatabase.execSQL(Create_Expense_Recurrent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 1:
                sqLiteDatabase.execSQL("ALTER TABLE " + Table_Income + " ADD COLUMN " + Key_INotes + " TEXT");
            case 2:
                sqLiteDatabase.execSQL(Create_Expense_Recurrent);
        }

        onCreate(sqLiteDatabase);
    }

    public void openDatabase() throws SQLException {
        mydb = this.getWritableDatabase();
    }

    //we take from parameter expense all the attributes we create a tuple in table Expense
    public void insertExpense(ExpenseItem expense) {

        ContentValues values = new ContentValues();
        values.put(Key_ECategory, expense.getCategory());
        values.put(Key_EDate, expense.getDate());
        values.put(Key_EPrice, expense.getPrice());
        values.put(Key_ENotes, expense.getNotes());

        mydb.insert(Table_Expense, null, values);

    }

    //we take from parameter income all the attributes we create a tuple in table Income
    public void insertIncome(IncomeItem income) {

        ContentValues values = new ContentValues();
        values.put(Key_IAmount, income.getAmount());
        values.put(Key_ISource, income.getSource());
        values.put(Key_IDate, income.getDate());
        values.put(Key_INotes, income.getNotes());

        mydb.insert(Table_Income, null, values);

    }

    public void insertRecurrent(RecurrentTransaction item) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_AMOUNT, item.getAmount());
        values.put(KEY_CATEGORY, item.getCategory());
        values.put(KEY_DATE, item.getDate());
        values.put(KEY_FREQ, item.getFreq());
        values.put(KEY_INTERVAL, item.getInterval());
        values.put(KEY_DAY, item.getDay());
        values.put(KEY_EXPIRATION, item.getExpiration());
        values.put(KEY_NEXT_DATE, item.getNextDate());
        values.put(KEY_ISEXPENSE, item.getIsExpense());
        values.put(KEY_ISVALID, item.getIsValid());

        getWritableDatabase().insert(TABLE_RECURRENT, null, values);
    }

    public void updateExpense(ExpenseItem expense) {

        ContentValues values = new ContentValues();
        values.put(Key_ECategory, expense.getCategory());
        values.put(Key_EDate, expense.getDate());
        values.put(Key_EPrice, expense.getPrice());
        values.put(Key_ENotes, expense.getNotes());

        getReadableDatabase().update(Table_Expense, values, Key_EId + " = " + expense.getId(), null);

    }

    public void updateIncome(IncomeItem income) {
        ContentValues values = new ContentValues();
        values.put(Key_ISource, income.getSource());
        values.put(Key_IAmount, income.getAmount());
        values.put(Key_IDate, income.getDate());
        values.put(Key_INotes, income.getNotes());

        getReadableDatabase().update(Table_Income, values, Key_Iid + " = " + income.getId(), null);
    }

    public void updateRecurrent(RecurrentTransaction item) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_AMOUNT, item.getAmount());
        values.put(KEY_CATEGORY, item.getCategory());
        values.put(KEY_DATE, item.getDate());
        values.put(KEY_FREQ, item.getFreq());
        values.put(KEY_INTERVAL, item.getInterval());
        values.put(KEY_DAY, item.getDay());
        values.put(KEY_EXPIRATION, item.getExpiration());
        values.put(KEY_NEXT_DATE, item.getNextDate());
        values.put(KEY_ISEXPENSE, item.getIsExpense());
        values.put(KEY_ISVALID, item.getIsValid());

        getReadableDatabase().update(TABLE_RECURRENT, values, KEY_ID + " = " + item.getId(), null);

    }

    public void updateCategory(String oldCategoryName, String newCategoryName) {
        ContentValues newValues = new ContentValues();
        newValues.put(Key_ECategory, newCategoryName);

        getWritableDatabase().update(Table_Expense, newValues, Key_ECategory + "='" + oldCategoryName + "'", null);
    }

    public void updateSource(String oldSource, String newSource) {
        ContentValues newValues = new ContentValues();
        newValues.put(Key_ISource, newSource);

        getWritableDatabase().update(Table_Income, newValues, Key_ISource + "='" + oldSource + "'", null);

    }

    public void deleteTuplesDependedOnCategory(String category, boolean expense) {

        if (expense)
            getReadableDatabase().delete(Table_Expense, Key_ECategory + "=" + "'" + category + "'", null);
        else
            getReadableDatabase().delete(Table_Income, Key_ISource + "=" + "'" + category + "'", null);

    }

    public void updateTuplesDependedOnCategory(String category, boolean expense, String newCategory) {

        ContentValues values = new ContentValues();
        if (expense) {
            values.put(Key_ECategory, newCategory);
            getReadableDatabase().update(Table_Expense, values, Key_ECategory + "=" + "'" + category + "'", null);
        } else {
            values.put(Key_ISource, newCategory);
            getReadableDatabase().update(Table_Income, values, Key_ISource + "=" + "'" + category + "'", null);
        }
    }

    public void deleteExpense(int id) {
        getReadableDatabase().delete(Table_Expense, Key_EId + "=" + id, null);
    }

    public void deleteIncome(int id) {
        getReadableDatabase().delete(Table_Income, Key_Iid + "=" + id, null);
    }

    public void deleteRecurrent(int id) {
        getReadableDatabase().delete(TABLE_RECURRENT, KEY_ID + "=" + id, null);
    }

    public void deleteAllIncome() {
        getWritableDatabase().delete(Table_Income, null, null);
    }

    public void deleteAllExpense() {
        getWritableDatabase().delete(Table_Expense, null, null);
    }

    public void deleteAllRecurrents() {
        getWritableDatabase().delete(TABLE_RECURRENT, null, null);
    }


    //return a cursor which contains the whole table expense (select *)
    public Cursor getAllExpenses() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense,
                null);
    }

    //return a cursor which contains the whole table recurrents (select *)
    public Cursor getAllRecurrents() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_RECURRENT
                        + " ORDER BY " + KEY_ISVALID + " DESC," + KEY_NEXT_DATE + " ASC",
                null);
    }

    public ArrayList<RecurrentTransaction> getRecurrentsNotification() {
        //get all the transactions tha are either for today or pending (previous date that are not done)
        //and are  valid , meaning they are not complete
        Date today = Calendar.getInstance().getTime();
        String sToday = new SimpleDateFormat("yyyy-MM-dd").format(today);

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_RECURRENT
                        + " WHERE " + KEY_NEXT_DATE + " <= '" + sToday + "' AND " + KEY_ISVALID + "='1'"
                        + " ORDER BY " + KEY_NEXT_DATE,
                null);

        ArrayList<RecurrentTransaction> list = new ArrayList();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            RecurrentTransaction t = new RecurrentTransaction(c);

            list.add(t);

        }
        return list;
    }

    //return a cursor which contains the tuples with Category equal to the parameter category of table Expenses
    public Cursor getExpensesByCategory(String category) {

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " WHERE " + Key_ECategory + " LIKE " + "'" + category +
                        "' ORDER BY " + Key_EDate + " DESC, " + Key_EId + " DESC",
                null);
    }

    //return a cursor which contains the table expense order by price depended on variable asc (ASC-DESC)
    public Cursor getExpensesByPriceOrder(boolean asc) {
        String order = " ASC";
        if (!asc) {
            order = " DESC";
        }
        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " ORDER BY " + Key_EPrice + order + " , " + Key_EDate + " DESC", null);

    }

    //return a cursor which contains the tuples of table expense with Date equal to parameter date
    public Cursor getExpensesByDate(String date) {

        String dateTokens[] = date.split("-");
        String reformedDate = dateTokens[2] + "-" + dateTokens[1] + "-" + dateTokens[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " WHERE " + Key_EDate + " LIKE " + "'" + reformedDate + "' ORDER BY " + Key_EId + " DESC",
                null);
    }

    //return a cursor which contains the tuples of table expense with Date equal to parameter date
    public Cursor getExpensesByDate(String date, String category) {

        String dateTokens[] = date.split("-");
        String reformedDate = dateTokens[2] + "-" + dateTokens[1] + "-" + dateTokens[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense +
                " WHERE " + Key_EDate + " LIKE " + "'" + reformedDate + "'" +
                " AND " + Key_ECategory + "='" + category +
                "' ORDER BY " + Key_EDate + " DESC, " + Key_EId + " DESC", null);
    }


    //return a cursor which contains the tuples of table expense with date between of parameter date1 and parameter date2
    public Cursor getExpensesByDateToDate(String date1, String date2) {

        String DateFrom[] = date1.split("-");
        String DateTo[] = date2.split("-");
        String reformedDateFrom = DateFrom[2] + "-" + DateFrom[1] + "-" + DateFrom[0];
        String reformedDateTo = DateTo[2] + "-" + DateTo[1] + "-" + DateTo[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " WHERE " + Key_EDate + ">=" + "'" + reformedDateFrom + "'" +
                " AND " + Key_EDate + "<=" + "'" + reformedDateTo + "' ORDER BY " + Key_EDate + " DESC, " + Key_EId + " DESC", null);

    }

    //overload
    public Cursor getExpensesByDateToDate(String date1, String date2, String category) {
        String DateFrom[] = date1.split("-");
        String DateTo[] = date2.split("-");
        String reformedDateFrom = DateFrom[2] + "-" + DateFrom[1] + "-" + DateFrom[0];
        String reformedDateTo = DateTo[2] + "-" + DateTo[1] + "-" + DateTo[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense +
                " WHERE " + Key_EDate + ">=" + "'" + reformedDateFrom + "'" +
                " AND " + Key_EDate + "<=" + "'" + reformedDateTo +
                "' AND " + Key_ECategory + "='" + category +
                "' ORDER BY " + Key_EDate + " DESC, " + Key_EId + " DESC", null);

    }

    //return a cursor which contains the tuples of table expense order by the date
    public Cursor getExpensesFromNewestToOldest() {

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " ORDER BY " + Key_EDate + " DESC , " + Key_EId + " DESC"
                , null);
    }

    //return a cursor which contains all the tuples of table income
    public Cursor getAllIncomes() {
        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income,
                null);
    }

    //return a cursor which contains all the tuples of table income with Source = to parameter source
    public Cursor getIncomesBySource(String source) {
        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income +
                " WHERE " + Key_ISource + "=" + "'" + source + "' ORDER BY " + Key_IDate + " DESC, " + Key_Iid + " DESC", null);
    }


    //return a cursor which contains the tuples of table income order by the date
    public Cursor getIncomesByNewestToOldest() {

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income + " ORDER BY " + Key_IDate + " DESC , " + Key_Iid + " DESC"
                , null);
    }

    public Cursor getIncomesByAmountOrder(boolean asc) {
        String order = " ASC";
        if (!asc) {
            order = " DESC";
        }
        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income + " ORDER BY " + Key_IAmount + order + " , " + Key_IDate + " DESC", null);
    }

    //return a cursor which contains the tuples of table income with Date equal to parameter date
    public Cursor getIncomesByDate(String date) {

        String dateTokens[] = date.split("-");
        String reformedDate = dateTokens[2] + "-" + dateTokens[1] + "-" + dateTokens[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income + " WHERE " + Key_IDate + " LIKE " + "'" + reformedDate + "' ORDER BY " + Key_Iid + " DESC",
                null);
    }

    //return a cursor which contains the tuples of table income with Date equal to parameter date
    public Cursor getIncomesByDate(String date, String category) {

        String dateTokens[] = date.split("-");
        String reformedDate = dateTokens[2] + "-" + dateTokens[1] + "-" + dateTokens[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income +
                " WHERE " + Key_IDate + ">=" + "'" + reformedDate + "'" +
                " AND " + Key_ISource + "='" + category +
                "' ORDER BY " + Key_IDate + " DESC , " + Key_Iid + " DESC", null);
    }

    //return a cursor which contains the tuples of table income with date between of parameter date1 and parameter date2
    public Cursor getIncomesByDateToDate(String date1, String date2) {

        String DateFrom[] = date1.split("-");
        String DateTo[] = date2.split("-");
        String reformedDateFrom = DateFrom[2] + "-" + DateFrom[1] + "-" + DateFrom[0];
        String reformedDateTo = DateTo[2] + "-" + DateTo[1] + "-" + DateTo[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income + " WHERE " + Key_IDate + ">=" + "'" + reformedDateFrom + "'" +
                " AND " + Key_IDate + "<=" + "'" + reformedDateTo + "' ORDER BY " + Key_IDate + " DESC , " + Key_Iid + " DESC", null);
    }

    //overload
    //return a cursor which contains the tuples of table income with date between of parameter date1 and parameter date2
    public Cursor getIncomesByDateToDate(String date1, String date2, String category) {

        String DateFrom[] = date1.split("-");
        String DateTo[] = date2.split("-");
        String reformedDateFrom = DateFrom[2] + "-" + DateFrom[1] + "-" + DateFrom[0];
        String reformedDateTo = DateTo[2] + "-" + DateTo[1] + "-" + DateTo[0];

        return getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income +
                " WHERE " + Key_IDate + ">=" + "'" + reformedDateFrom + "'" +
                " AND " + Key_IDate + "<=" + "'" + reformedDateTo + "'" +
                " AND " + Key_ISource + "='" + category +
                "' ORDER BY " + Key_IDate + " DESC , " + Key_Iid + " DESC", null);
    }

    public boolean categoryHasItems(String category, boolean expense) {

        Cursor c;

        if (expense) {
            c = getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " WHERE " + Key_ECategory + "=" + "'" + category + "'", null);
        } else {
            c = getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income + " WHERE " + Key_ISource + "=" + "'" + category + "'", null);
        }

        if (c.getCount() == 0) {
            return false;
        }

        return true;

    }


    //used to determine savings value in OverviewActivity
    //total expense amount of all time
    public double getTotal(boolean isExpense) {

        Cursor cursor;
        double total = 0;

        if (isExpense) {
            cursor = this.getAllExpenses();

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total = Double.parseDouble(cursor.getString(3)) + total;
                }

            }
        } else {
            cursor = this.getAllIncomes();

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total = Double.parseDouble(cursor.getString(1)) + total;
                }

            }
        }


        return total;
    }

    public double getTotal(boolean isExpense, ArrayList<String> categoryFilter) {

        Cursor cursor;
        double total = 0;

        if (isExpense) {
            cursor = this.getAllExpenses();

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilter.contains(cursor.getString(1)))
                        total = Double.parseDouble(cursor.getString(3)) + total;
                }

            }
        } else {
            cursor = this.getAllIncomes();

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilter.contains(cursor.getString(2)))
                        total = Double.parseDouble(cursor.getString(1)) + total;
                }

            }
        }


        return total;
    }

    public double getTotalForCategory(String category, boolean isExpense) {

        Cursor cursor;
        double total;

        if (isExpense) {
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + Table_Expense + " WHERE " + Key_ECategory + " LIKE " + "'" + category +
                    "'", null);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(3));
                }
            }
            Log.i("nikos", "Total expense for " + category + " : " + total);
        } else {
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + Table_Income +
                    " WHERE " + Key_ISource + "=" + "'" + category + "'", null);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(1));
                }
            }

        }


        return total;
    }

    //used for current month in OverviewActivity
    public double getTotalForCurrentMonth(boolean isExpense) {

        Cursor cursor;
        double total = 0;
        Calendar c = Calendar.getInstance();

        int days[] = {1, 5, 10, 15, 20, 25};
        int firstDay = days[new SharedPrefsManager(context).getPrefsDayStart()];

        c.set(Calendar.DAY_OF_MONTH, firstDay);

        String firstOfMonth = new SimpleDateFormat("dd-MM-yyyy").format(new Date(c.getTimeInMillis()));

        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);

        String lastOfMonth = new SimpleDateFormat("dd-MM-yyyy").format(new Date(c.getTimeInMillis()));

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(firstOfMonth, lastOfMonth);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total = Double.parseDouble(cursor.getString(3)) + total;
                }

            }
        } else {
            cursor = this.getIncomesByDateToDate(firstOfMonth, lastOfMonth);

            if (cursor.getCount() > 0) {

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total = Double.parseDouble(cursor.getString(1)) + total;
                }

            }
        }


        return total;
    }

    //used for current week in OverviewActivity
    //parameter is a calendar constant
    public double getTotalForCurrentWeek(int firstDayOfWeek, boolean isExpense) {

        Cursor cursor;
        double total = 0;

        Calendar c = Calendar.getInstance();

        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int endDay = firstDayOfWeek;

        Date startDate, endDate;

        if (currentDay == endDay) {
            startDate = c.getTime();
            c.add(Calendar.DAY_OF_YEAR, 6);
            endDate = c.getTime();
        } else {
            while (currentDay != endDay) {
                c.add(Calendar.DATE, 1);
                currentDay = c.get(Calendar.DAY_OF_WEEK);
            }
            c.add(Calendar.DAY_OF_YEAR, -1);
            endDate = c.getTime();

            c.add(Calendar.DAY_OF_YEAR, -6);
            startDate = c.getTime();

        }

        c.setTime(startDate);

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String firstOfWeek = day + "-" + month + "-" + c.get(Calendar.YEAR);
        c.setTime(endDate);

        day = c.get(Calendar.DAY_OF_MONTH) + "";
        month = (c.get(Calendar.MONTH) + 1) + "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String lastOfWeek = day + "-" + month + "-" + c.get(Calendar.YEAR);

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(firstOfWeek, lastOfWeek);

            if (cursor.getCount() > 0) {

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total = Double.parseDouble(cursor.getString(3)) + total;
                }

            }
        } else {
            cursor = this.getIncomesByDateToDate(firstOfWeek, lastOfWeek);

            if (cursor.getCount() > 0) {

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total = Double.parseDouble(cursor.getString(1)) + total;
                }

            }
        }


        return total;

    }

    //used for every category in a month in PieDistributionActivity
    public double getMonthTotalForCategory(String category, boolean isExpense) {

        Cursor cursor;
        double total;
        Calendar c = Calendar.getInstance();

        int days[] = {1, 5, 10, 15, 20, 25};
        int firstDay = days[new SharedPrefsManager(context).getPrefsDayStart()];

        c.set(Calendar.DAY_OF_MONTH, firstDay);

        String firstOfMonth = new SimpleDateFormat("dd-MM-yyyy").format(new Date(c.getTimeInMillis()));

        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_YEAR, -1);

        String lastOfMonth = new SimpleDateFormat("dd-MM-yyyy").format(new Date(c.getTimeInMillis()));

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(firstOfMonth, lastOfMonth, category);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(3));
                }
            }
        } else {
            cursor = this.getIncomesByDateToDate(firstOfMonth, lastOfMonth, category);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(1));
                }
            }
        }


        return total;
    }

    //used for every category in a week in PieDistributionActivity
    public double getWeekTotalForCategory(int firstDayOfWeek, String category, boolean isExpense) {

        double total = 0;
        Cursor cursor;

        Calendar c = Calendar.getInstance();

        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int endDay = firstDayOfWeek;

        Date startDate, endDate;

        if (currentDay == endDay) {
            startDate = c.getTime();
            c.add(Calendar.DAY_OF_YEAR, 6);
            endDate = c.getTime();
        } else {
            while (currentDay != endDay) {
                c.add(Calendar.DATE, 1);
                currentDay = c.get(Calendar.DAY_OF_WEEK);
            }
            c.add(Calendar.DAY_OF_YEAR, -1);
            endDate = c.getTime();

            c.add(Calendar.DAY_OF_YEAR, -6);
            startDate = c.getTime();

        }

        c.setTime(startDate);

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String firstOfWeek = day + "-" + month + "-" + c.get(Calendar.YEAR);
        c.setTime(endDate);

        day = c.get(Calendar.DAY_OF_MONTH) + "";
        month = (c.get(Calendar.MONTH) + 1) + "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String lastOfWeek = day + "-" + month + "-" + c.get(Calendar.YEAR);

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(firstOfWeek, lastOfWeek, category);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(3));
                }
            }
        } else {
            cursor = this.getIncomesByDateToDate(firstOfWeek, lastOfWeek, category);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(1));
                }
            }
        }

        return total;
    }

    //used for custom date in PieDistributionActivity
    public double getTotalForCategoryCustomDate(String startDate, String endDate, String category, boolean isExpense) {

        Cursor cursor;
        double total;

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(startDate, endDate, category);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(3));
                }
            }
        } else {
            cursor = this.getIncomesByDateToDate(startDate, endDate, category);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(1));
                }
            }
        }

        return total;
    }

    //used for custom date in BarsAcitivity
    public double getTotalCustomDate(String startDate, String endDate, boolean isExpense, ArrayList<String> categoryFilters) {

        Cursor cursor;
        double total;

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(startDate, endDate);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilters.contains(cursor.getString(1)))
                        total += Double.parseDouble(cursor.getString(3));
                }
            }
        } else {
            cursor = this.getIncomesByDateToDate(startDate, endDate);

            total = 0;

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilters.contains(cursor.getString(2)))
                        total += Double.parseDouble(cursor.getString(1));
                }
            }
        }

        return total;
    }


    //used for every month in BarDistributionActivity
    public double getMonthTotal(int year, int month, boolean isExpense, ArrayList<String> categoryFilter) {

        Cursor cursor;

        double total = 0;

        month++;
        String m = month + "";
        if (month < 10) {
            m = "0" + month;
        }

        String firstOfMonth = "01" + "-" + m + "-" + year;
        String lastOfMonth = "31" + "-" + m + "-" + year;

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(firstOfMonth, lastOfMonth);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilter.contains(cursor.getString(1))) {
                        total += Double.parseDouble(cursor.getString(3));
                    }
                }
            }
        } else {
            cursor = this.getIncomesByDateToDate(firstOfMonth, lastOfMonth);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilter.contains(cursor.getString(2))) {
                        total += Double.parseDouble(cursor.getString(1));
                    }
                }
            }
        }


        return total;
    }

    //used for every week in BarDistributionActivity
    public double getWeekTotal(int year, int day, boolean isExpense, ArrayList<String> categoryFilter) {
        double total = 0;
        Cursor cursor;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_YEAR, day);
        c.set(Calendar.YEAR, year);

        int currentDay = day;
        int days[] = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
                Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY};
        int endDay = days[new SharedPrefsManager(context).getPrefsDayStart()];

        Date startDate, endDate;

        if (currentDay == endDay) {
            startDate = c.getTime();
            c.add(Calendar.DAY_OF_YEAR, 6);
            endDate = c.getTime();
        } else {
            while (currentDay != endDay) {
                c.add(Calendar.DATE, 1);
                currentDay = c.get(Calendar.DAY_OF_WEEK);
            }
            c.add(Calendar.DAY_OF_YEAR, -1);
            endDate = c.getTime();

            c.add(Calendar.DAY_OF_YEAR, -6);
            startDate = c.getTime();

        }

        c.setTime(startDate);

        String sDay = c.get(Calendar.DAY_OF_MONTH) + "";
        String sMonth = (c.get(Calendar.MONTH) + 1) + "";

        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            sDay = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            sMonth = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String firstOfWeek = sDay + "-" + sMonth + "-" + c.get(Calendar.YEAR);
        c.setTime(endDate);

        sDay = c.get(Calendar.DAY_OF_MONTH) + "";
        sMonth = (c.get(Calendar.MONTH) + 1) + "";
        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            sDay = "0" + c.get(Calendar.DAY_OF_MONTH);
        }
        if (c.get(Calendar.MONTH) + 1 < 10) {
            sMonth = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String lastOfWeek = sDay + "-" + sMonth + "-" + c.get(Calendar.YEAR);

        if (isExpense) {
            cursor = this.getExpensesByDateToDate(firstOfWeek, lastOfWeek);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilter.contains(cursor.getString(1))) {
                        total += Double.parseDouble(cursor.getString(3));
                    }
                }
            }
        } else {
            cursor = this.getIncomesByDateToDate(firstOfWeek, lastOfWeek);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    if (categoryFilter.contains(cursor.getString(2))) {
                        total += Double.parseDouble(cursor.getString(1));
                    }
                }
            }
        }

        return total;
    }

    public double getDailyTotal(boolean isExpense) {
        double total = 0;
        Cursor cursor;

        Calendar c = Calendar.getInstance();

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";

        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }

        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String currentDay = day + "-" + month + "-" + c.get(Calendar.YEAR);

        if (isExpense) {
            cursor = this.getExpensesByDate(currentDay);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(3));
                }
            }
        } else {
            cursor = this.getIncomesByDate(currentDay);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(1));
                }
            }
        }
        return total;

    }

    public double getDailyTotalForCategory(String category, boolean isExpense) {
        double total = 0;
        Cursor cursor;

        Calendar c = Calendar.getInstance();

        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        String month = (c.get(Calendar.MONTH) + 1) + "";

        if (c.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + c.get(Calendar.DAY_OF_MONTH);
        }

        if (c.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + (c.get(Calendar.MONTH) + 1);
        }

        String currentDay = day + "-" + month + "-" + c.get(Calendar.YEAR);

        if (isExpense) {
            cursor = this.getExpensesByDate(currentDay, category);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(3));
                }
            }
        } else {
            cursor = this.getIncomesByDate(currentDay, category);

            if (cursor.getCount() != 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    total += Double.parseDouble(cursor.getString(1));
                }
            }
        }
        return total;
    }

    public RecurrentTransaction getRecurrent(int id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_RECURRENT
                        + " WHERE " + KEY_ID + "='" + id + "';",
                null);
        c.moveToFirst();
        return new RecurrentTransaction(c);
    }

}
