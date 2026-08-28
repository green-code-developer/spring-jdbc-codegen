package jp.green_code.spring_jdbc_codegen.test_app.entity.base;


/**
 * Table: mapper_no_pk
 */
public abstract class BaseMapperNoPkEntity {

    /** rename_target */
    protected String renamedNoPkName;

    /** other_col */
    protected String otherCol;

    public String getRenamedNoPkName() {
        return renamedNoPkName;
    }

    public void setRenamedNoPkName(String renamedNoPkName) {
        this.renamedNoPkName = renamedNoPkName;
    }

    public String getOtherCol() {
        return otherCol;
    }

    public void setOtherCol(String otherCol) {
        this.otherCol = otherCol;
    }
}