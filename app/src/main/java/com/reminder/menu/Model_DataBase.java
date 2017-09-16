package com.reminder.menu;


/**
 * Created by admin on 19-01-2017.
 */

public class Model_DataBase {

    String Name,Holder_Names,Policy_No,Start_Date,Maturity_Date,Premium_Interval,Before_notify,InvestMent_Plan,Sum_Assured,Premium_Date,Last_Premium_Date;
    int Premium_Amount;
    int No_Installment;
    int Total_Premium_Amount;
    long total_days;
    int Id;
    String Main_Holder;

    public String getMain_Holder() {
        return Main_Holder;
    }

    public void setMain_Holder(String main_Holder) {
        Main_Holder = main_Holder;
    }


    public String getLast_Premium_Date() {
        return Last_Premium_Date;
    }

    public void setLast_Premium_Date(String last_Premium_Date) {
        Last_Premium_Date = last_Premium_Date;
    }

    public String getPremium_Interval() {
        return Premium_Interval;
    }

    public void setPremium_Interval(String premium_Interval) {
        Premium_Interval = premium_Interval;
    }

    public String getBefore_notify() {
        return Before_notify;
    }

    public void setBefore_notify(String before_notify) {
        Before_notify = before_notify;
    }

    public String getPremium_Date() {
        return Premium_Date;
    }

    public void setPremium_Date(String premium_Date) {
        Premium_Date = premium_Date;
    }

    public int getNo_Installment() {
        return No_Installment;
    }

    public void setNo_Installment(int no_Installment) {
        No_Installment = no_Installment;
    }

    public int getTotal_Premium_Amount() {
        return Total_Premium_Amount;
    }

    public void setTotal_Premium_Amount(int total_Premium_Amount) {
        Total_Premium_Amount = total_Premium_Amount;
    }

    public long getTotal_days() {
        return total_days;
    }

    public void setTotal_days(long total_days) {
        this.total_days = total_days;
    }

    public String getHolder_Names() {
        return Holder_Names;
    }

    public void setHolder_Names(String holder_Names) {
        Holder_Names = holder_Names;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getInvestMent_Plan() {
        return InvestMent_Plan;
    }

    public void setInvestMent_Plan(String investMent_Plan) {
        InvestMent_Plan = investMent_Plan;
    }

    public String getMaturity_Date() {
        return Maturity_Date;
    }

    public void setMaturity_Date(String maturity_Date) {
        Maturity_Date = maturity_Date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPayment_Mode() {
        return Premium_Interval;
    }

    public void setPayment_Mode(String payment_Mode) {
        Premium_Interval = payment_Mode;
    }

    public String getPolicy_No() {
        return Policy_No;
    }

    public void setPolicy_No(String policy_No) {
        Policy_No = policy_No;
    }

    public int getPremium_Amount() {
        return Premium_Amount;
    }

    public void setPremium_Amount(int premium_Amount) {
        Premium_Amount = premium_Amount;
    }

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getSum_Assured() {
        return Sum_Assured;
    }

    public void setSum_Assured(String sum_Assured) {
        Sum_Assured = sum_Assured;
    }

    public String getTerm() {
        return Before_notify;
    }

    public void setTerm(String term) {
        Before_notify = term;
    }


    public Model_DataBase(int id, String name, String holder,String main_holder, String investment_plan, String policy, String sum, String start_Date, String maturity_date, String premium_interval, int amount, String premium_date, int no_installment, int tt_premium_amoount, String before_notify, String last_Premium_Date, long td) {

        this.setLast_Premium_Date(last_Premium_Date);
        this.setPremium_Date(premium_date);
        this.setNo_Installment(no_installment);
        this.setTotal_Premium_Amount(tt_premium_amoount);
        this.setId(id);
        this.setName(name);
        this.setHolder_Names(holder);
        this.setInvestMent_Plan(investment_plan);
        this.setMaturity_Date(maturity_date);
        this.setPayment_Mode(premium_interval);
        this.setPolicy_No(policy);
        this.setPremium_Amount(amount);
        this.setStart_Date(start_Date);
        this.setSum_Assured(sum);
        this.setMain_Holder(main_holder);
        this.setTerm(before_notify);
        this.setTotal_days(td);

    }

}
