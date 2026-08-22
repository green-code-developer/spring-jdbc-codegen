package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOnlyPk1Repository;

/**
 * Table: only_pk1
 */
@Repository
public class OnlyPk1Repository extends BaseOnlyPk1Repository {
    public OnlyPk1Repository(RepositoryHelper helper) {
        super(helper);
    }
}