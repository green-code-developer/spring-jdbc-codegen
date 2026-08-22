package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNormalPk1Repository;

/**
 * Table: normal_pk1
 */
@Repository
public class NormalPk1Repository extends BaseNormalPk1Repository {
    public NormalPk1Repository(RepositoryHelper helper) {
        super(helper);
    }
}