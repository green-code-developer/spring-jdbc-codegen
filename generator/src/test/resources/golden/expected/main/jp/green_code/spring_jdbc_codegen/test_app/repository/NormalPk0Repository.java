package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNormalPk0Repository;

/**
 * Table: normal_pk0
 */
@Repository
public class NormalPk0Repository extends BaseNormalPk0Repository {
    public NormalPk0Repository(RepositoryHelper helper) {
        super(helper);
    }
}