package jp.green_code.spring_jdbc_codegen.test_app.entity.base;

import org.jspecify.annotations.Nullable;

/**
 * Table: mapper_no_pk
 */
public abstract class BaseMapperNoPkEntity {

    /** rename_target */
    protected @Nullable String renamedNoPkName;

    /** other_col */
    protected @Nullable String otherCol;

    public @Nullable String getRenamedNoPkName() {
        return renamedNoPkName;
    }

    public void setRenamedNoPkName(@Nullable String renamedNoPkName) {
        this.renamedNoPkName = renamedNoPkName;
    }

    public @Nullable String getOtherCol() {
        return otherCol;
    }

    public void setOtherCol(@Nullable String otherCol) {
        this.otherCol = otherCol;
    }
}