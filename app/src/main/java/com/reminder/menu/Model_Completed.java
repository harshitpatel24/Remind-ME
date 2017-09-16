package com.reminder.menu;

/**
 * Created by admin on 26-03-2017.
 */

public class Model_Completed {

    String Name,Policy_No,Investment_Plan;
    int Id;

    public Model_Completed(int id, String name, String policy_No, String investment_Plan) {
        Name = name;
        Policy_No = policy_No;
        Investment_Plan = investment_Plan;
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPolicy_No() {
        return Policy_No;
    }

    public void setPolicy_No(String policy_No) {
        Policy_No = policy_No;
    }

    public String getInvestment_Plan() {
        return Investment_Plan;
    }

    public void setInvestment_Plan(String investment_Plan) {
        Investment_Plan = investment_Plan;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
